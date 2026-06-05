package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.PracticanteSeccion;
import logica.dao.interfaces.PracticanteSeccionDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticanteSeccionDao implements PracticanteSeccionDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(PracticanteSeccionDao.class.getName());

    @Override
    public int agregarPracticanteSeccion(PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion {
        if (practicanteSeccion.getMatricula() == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        if (practicanteSeccion.getNoSeccion() == null) {
            throw new UsuariosExcepcion("El numero de seccion no puede ser nulo");
        }
        String consultaPracticanteSeccion = "INSERT INTO practicante_seccion (matricula, noSeccion) VALUES (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexionBaseDeDatos.prepareStatement(consultaPracticanteSeccion);
            insercion.setString(1, practicanteSeccion.getMatricula());
            insercion.setString(2, practicanteSeccion.getNoSeccion());
            filasAfectadas = insercion.executeUpdate();
            LOGGER.info("PracticanteSeccion insertada correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar practicante_seccion", excepcionSql);
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
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public int modificarPracticanteSeccion(String matricula, String noSeccion, PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion {
        if (matricula == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        if (noSeccion == null) {
            throw new UsuariosExcepcion("El numero de seccion no puede ser nulo");
        }
        String consulta = "UPDATE practicante_seccion SET matricula = ?, noSeccion = ? WHERE matricula = ? AND noSeccion = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, practicanteSeccion.getMatricula());
            actualizacion.setString(2, practicanteSeccion.getNoSeccion());
            actualizacion.setString(3, matricula);
            actualizacion.setString(4, noSeccion);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("PracticanteSeccion modificada correctamente: " + matricula + " - " + noSeccion);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar practicante_seccion", excepcionSql);
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
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }
}