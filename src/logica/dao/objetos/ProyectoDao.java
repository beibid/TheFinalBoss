package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Proyecto;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.ProyectoDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProyectoDao implements ProyectoDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(ProyectoDao.class.getName());

    @Override
    public int agregarProyecto(Proyecto proyecto) throws MensajeriaExcepcion {
        String consultaProyecto = "INSERT INTO proyecto (nombreProyecto, descripcion, responsableDelProyecto, estado, nombreEmpresa, sectorEmpresa, direccionEmpresa, idOrganizacion, matricula, numPersonalCoordinador, fechaRegistro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaProyecto);
            insertarEnBaseDeDatos.setString(1, proyecto.getNombreProyecto());
            insertarEnBaseDeDatos.setString(2, proyecto.getDescripcion());
            insertarEnBaseDeDatos.setString(3, proyecto.getResponsableDelProyecto());
            insertarEnBaseDeDatos.setString(4, proyecto.getEstado().toString().replace("_", " "));
            insertarEnBaseDeDatos.setString(5, proyecto.getNombreEmpresa());
            insertarEnBaseDeDatos.setString(6, proyecto.getSectorEmpresa());
            insertarEnBaseDeDatos.setString(7, proyecto.getDireccionEmpresa());
            insertarEnBaseDeDatos.setInt(8, proyecto.getIdOrganizacion());
            insertarEnBaseDeDatos.setString(9, proyecto.getMatricula());
            insertarEnBaseDeDatos.setString(10, proyecto.getNumPersonalCoordinador());
            insertarEnBaseDeDatos.setDate(11, proyecto.getFechaRegistro());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Proyecto insertado correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar proyecto", e);
            throw new MensajeriaExcepcion("Error al agregar el proyecto", e);
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

    public int modificarProyecto(int idProyecto, Proyecto proyecto) throws MensajeriaExcepcion {
        if (proyecto.getNombreProyecto() == null) {
            throw new MensajeriaExcepcion("El nombre del proyecto no puede ser nulo");
        }
        if (proyecto.getFechaRegistro() == null) {
            throw new MensajeriaExcepcion("La fecha de registro no puede ser nula");
        }
        String consulta = "UPDATE proyecto SET nombreProyecto = ?, descripcion = ?, responsableDelProyecto = ?, estado = ?, nombreEmpresa = ?, sectorEmpresa = ?, direccionEmpresa = ?, idOrganizacion = ?, matricula = ?, numPersonalCoordinador = ?, fechaRegistro = ? WHERE idProyecto = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, proyecto.getNombreProyecto());
            actualizacion.setString(2, proyecto.getDescripcion());
            actualizacion.setString(3, proyecto.getResponsableDelProyecto());
            actualizacion.setString(4, proyecto.getEstado().toString().replace("_", " "));
            actualizacion.setString(5, proyecto.getNombreEmpresa());
            actualizacion.setString(6, proyecto.getSectorEmpresa());
            actualizacion.setString(7, proyecto.getDireccionEmpresa());
            actualizacion.setInt(8, proyecto.getIdOrganizacion());
            actualizacion.setString(9, proyecto.getMatricula());
            actualizacion.setString(10, proyecto.getNumPersonalCoordinador());
            actualizacion.setDate(11, proyecto.getFechaRegistro());
            actualizacion.setInt(12, idProyecto);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Proyecto modificado correctamente: " + idProyecto);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al modificar proyecto", e);
            throw new MensajeriaExcepcion("Error al modificar proyecto");
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
}