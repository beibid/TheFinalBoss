package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.PracticanteSeccion;
import logica.dao.interfaces.PracticanteSeccionDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticanteSeccionDao implements PracticanteSeccionDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(PracticanteSeccionDao.class.getName());
    private static final int ID_PERIODO_MINIMO_VALIDO = 1;
    private static final int ERROR_DUPLICADO_MYSQL = 1062;

    @Override
    public int agregarPracticanteSeccion(PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        if (practicanteSeccion.getMatricula() == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        if (practicanteSeccion.getNoSeccion() == null) {
            throw new UsuariosExcepcion("El numero de seccion no puede ser nulo");
        }
        if (practicanteSeccion.getIdPeriodo() < ID_PERIODO_MINIMO_VALIDO) {
            throw new UsuariosExcepcion("El periodo no puede ser nulo");
        }
        String consultaPracticanteSeccion = "INSERT INTO practicante_seccion (matricula, noSeccion, idPeriodo) VALUES (?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexionBaseDeDatos.prepareStatement(consultaPracticanteSeccion);
            insercion.setString(1, practicanteSeccion.getMatricula());
            insercion.setString(2, practicanteSeccion.getNoSeccion());
            insercion.setInt(3, practicanteSeccion.getIdPeriodo());
            filasAfectadas = insercion.executeUpdate();
            LOGGER.info("PracticanteSeccion insertada correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar practicante_seccion", excepcionSql);
            if (excepcionSql.getMessage() != null && excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            if (excepcionSql.getErrorCode() == ERROR_DUPLICADO_MYSQL) {
                throw new RegistroDuplicadoExcepcion("El practicante ya esta asignado a una seccion en este periodo");
            }
            throw new UsuariosExcepcion("Error al agregar practicante seccion", excepcionSql);
        } finally {
            try {
                if (insercion != null) {
                    insercion.close();
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

    public int modificarPracticanteSeccion(String matricula, String noSeccion, int idPeriodo, PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion {
        if (matricula == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        if (noSeccion == null) {
            throw new UsuariosExcepcion("El numero de seccion no puede ser nulo");
        }
        if (idPeriodo < ID_PERIODO_MINIMO_VALIDO) {
            throw new UsuariosExcepcion("El periodo no puede ser nulo");
        }
        String consulta = "UPDATE practicante_seccion SET matricula = ?, noSeccion = ?, idPeriodo = ? WHERE matricula = ? AND noSeccion = ? AND idPeriodo = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, practicanteSeccion.getMatricula());
            actualizacion.setString(2, practicanteSeccion.getNoSeccion());
            actualizacion.setInt(3, practicanteSeccion.getIdPeriodo());
            actualizacion.setString(4, matricula);
            actualizacion.setString(5, noSeccion);
            actualizacion.setInt(6, idPeriodo);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("PracticanteSeccion modificada correctamente: " + matricula + " - " + noSeccion);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar practicante_seccion", excepcionSql);
            if (excepcionSql.getMessage() != null && excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al modificar practicante seccion", excepcionSql);
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
}