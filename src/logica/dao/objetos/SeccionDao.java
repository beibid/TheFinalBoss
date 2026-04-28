package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
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

    @Override
    public int agregarSeccion(Seccion seccion) throws UsuariosExcepcion {
        String consultaSeccion = "INSERT INTO seccion (noSeccion, periodo) VALUES (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaSeccion);
            insertarEnBaseDeDatos.setString(1, seccion.getNoSeccion());
            insertarEnBaseDeDatos.setString(2, seccion.getPeriodo());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Seccion insertada correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar la seccion", e);
            throw new UsuariosExcepcion("Error al agregar la sección", e);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null) {
                    insertarEnBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public int modificarSeccion(String noSeccion, Seccion seccion) throws UsuariosExcepcion {
        if (seccion.getPeriodo() == null) {
            throw new UsuariosExcepcion("El periodo no puede ser nulo");
        }
        String consultaSeccion = "UPDATE seccion SET periodo = ? WHERE noSeccion = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consultaSeccion);
            actualizacion.setString(1, seccion.getPeriodo());
            actualizacion.setString(2, noSeccion);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Seccion modificada correctamente: " + noSeccion);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al modificar la seccion", e);
            throw new UsuariosExcepcion("Error al modificar la sección", e);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public List<Seccion> obtenerSecciones() throws UsuariosExcepcion {
        String consulta = "SELECT noSeccion, periodo FROM Seccion";
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
                seccion.setPeriodo(resultado.getString("periodo"));
                secciones.add(seccion);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener secciones", e);
            throw new UsuariosExcepcion("Error al obtener secciones", e);
        } finally {
            try {
                if (consultaSecciones != null) consultaSecciones.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return secciones;
    }

}