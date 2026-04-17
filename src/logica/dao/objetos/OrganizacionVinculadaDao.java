package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.OrganizacionVinculada;
import logica.dao.interfaces.OrganizacionVinculadaDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OrganizacionVinculadaDao implements OrganizacionVinculadaDaoInterfaz{
    private static final Logger LOGGER = Logger.getLogger(OrganizacionVinculadaDao.class.getName());
    @Override

    public void insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion {
        String consultaOrganizacion = "insert into organizacion_vinculada (nombre, direccion) values (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaOrganizacion);
            insertarEnBaseDeDatos.setString(1, organizacionVinculada.getNombre());
            insertarEnBaseDeDatos.setString(2, organizacionVinculada.getDireccion());
            insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Organización vinculada insertada correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar la organización vinculada", e);
            throw new UsuariosExcepcion("Error al insertar la organizacion",e);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null){
                    insertarEnBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
    }
    public void modificarOrganizacionVinculada(int idOrganizacion, OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion {
        String consulta = "UPDATE organizacion_vinculada SET nombre = ?, direccion = ? WHERE idOrganizacion = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, organizacionVinculada.getNombre());
            actualizacion.setString(2, organizacionVinculada.getDireccion());
            actualizacion.setInt(3, idOrganizacion);
            actualizacion.executeUpdate();
            LOGGER.info("OrganizacionVinculada modificada correctamente: " + idOrganizacion);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al modificar organizacion vinculada", e);
            throw new UsuariosExcepcion("Error al modificar organizacion vinculada", e);
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
    }
}