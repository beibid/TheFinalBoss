package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Seccion;
import logica.dao.interfaces.SeccionDaoInterfaz;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeccionDao implements SeccionDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(SeccionDao.class.getName());
    private static final int ID_PERIODO_MINIMO_VALIDO = 1;
    private static final int ERROR_DUPLICADO_MYSQL = 1062;

    @Override
    public int agregarSeccion(Seccion seccion) throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        if (seccion.getNoSeccion() == null) {
            throw new UsuariosExcepcion("El numero de seccion no puede ser nulo");
        }
        String consultaSeccion = "INSERT INTO seccion (noSeccion, idPeriodo, numPersonalProfesor) VALUES (?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaSeccion);
            insertarEnBaseDeDatos.setString(1, seccion.getNoSeccion());
            insertarEnBaseDeDatos.setInt(2, seccion.getIdPeriodo());
            insertarEnBaseDeDatos.setString(3, seccion.getNumPersonalProfesor());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Seccion insertada correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar la seccion", excepcionSql);
            if (excepcionSql.getErrorCode() == ERROR_DUPLICADO_MYSQL) {
                throw new RegistroDuplicadoExcepcion("El NRC " + seccion.getNoSeccion() + " ya existe en ese periodo");
            }
            throw new UsuariosExcepcion("Error al agregar la seccion", excepcionSql);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null) {
                    insertarEnBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public int modificarSeccion(String noSeccion, Seccion seccion) throws UsuariosExcepcion {
        if (seccion.getIdPeriodo() < ID_PERIODO_MINIMO_VALIDO) {
            throw new UsuariosExcepcion("El periodo no puede ser nulo");
        }
        String consultaSeccion = "UPDATE seccion SET idPeriodo = ?, numPersonalProfesor = ? WHERE noSeccion = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consultaSeccion);
            actualizacion.setInt(1, seccion.getIdPeriodo());
            actualizacion.setString(2, seccion.getNumPersonalProfesor());
            actualizacion.setString(3, noSeccion);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Seccion modificada correctamente: " + noSeccion);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar la seccion", excepcionSql);
            throw new UsuariosExcepcion("Error al modificar la seccion", excepcionSql);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public List<Seccion> obtenerSecciones() throws UsuariosExcepcion {
        String consulta = "SELECT s.noSeccion, s.idPeriodo, s.numPersonalProfesor, p.nombre AS nombrePeriodo " +
                "FROM seccion s " +
                "INNER JOIN periodo_universitario p ON p.idPeriodo = s.idPeriodo " +
                "WHERE p.estado = 'Abierto'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaSecciones = null;
        List<Seccion> secciones = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaSecciones = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaSecciones.executeQuery();
            while (resultado.next()) {
                Seccion seccion = new Seccion();
                seccion.setNoSeccion(resultado.getString("noSeccion"));
                seccion.setIdPeriodo(resultado.getInt("idPeriodo"));
                seccion.setNumPersonalProfesor(resultado.getString("numPersonalProfesor"));
                seccion.setNombrePeriodo(resultado.getString("nombrePeriodo"));
                secciones.add(seccion);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener secciones", excepcionSql);
            throw new UsuariosExcepcion("Error al obtener secciones", excepcionSql);
        } finally {
            try {
                if (consultaSecciones != null) {
                    consultaSecciones.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return secciones;
    }

    public List<Seccion> obtenerSeccionesPorProfesor(String numPersonalProfesor) throws UsuariosExcepcion {
        String consulta = "SELECT s.noSeccion, s.idPeriodo, s.numPersonalProfesor, p.nombre AS nombrePeriodo " +
                "FROM seccion s " +
                "INNER JOIN periodo_universitario p ON p.idPeriodo = s.idPeriodo " +
                "WHERE s.numPersonalProfesor = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaSecciones = null;
        List<Seccion> secciones = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaSecciones = conexionBaseDeDatos.prepareStatement(consulta);
            consultaSecciones.setString(1, numPersonalProfesor);
            ResultSet resultado = consultaSecciones.executeQuery();
            while (resultado.next()) {
                Seccion seccion = new Seccion();
                seccion.setNoSeccion(resultado.getString("noSeccion"));
                seccion.setIdPeriodo(resultado.getInt("idPeriodo"));
                seccion.setNumPersonalProfesor(resultado.getString("numPersonalProfesor"));
                seccion.setNombrePeriodo(resultado.getString("nombrePeriodo"));
                secciones.add(seccion);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener secciones por profesor", excepcionSql);
            throw new UsuariosExcepcion("Error al obtener secciones del profesor", excepcionSql);
        } finally {
            try {
                if (consultaSecciones != null) {
                    consultaSecciones.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return secciones;
    }
}