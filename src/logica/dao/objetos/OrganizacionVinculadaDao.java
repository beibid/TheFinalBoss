package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.OrganizacionVinculada;
import logica.dao.interfaces.OrganizacionVinculadaDaoInterfaz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrganizacionVinculadaDao implements OrganizacionVinculadaDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(OrganizacionVinculadaDao.class.getName());

    @Override
    public int insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion {
        String consultaOrganizacion = "insert into organizacion_vinculada (nombre, direccion) values (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaOrganizacion);
            insertarEnBaseDeDatos.setString(1, organizacionVinculada.getNombre());
            insertarEnBaseDeDatos.setString(2, organizacionVinculada.getDireccion());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Organización vinculada insertada correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar la organización vinculada", e);
            throw new UsuariosExcepcion("Error al insertar la organizacion", e);
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

    public int modificarOrganizacionVinculada(int idOrganizacion, OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion {
        if (organizacionVinculada.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre de la organizacion no puede ser nulo");
        }
        if (organizacionVinculada.getDireccion() == null) {
            throw new UsuariosExcepcion("La direccion de la organizacion no puede ser nula");
        }
        String consulta = "UPDATE organizacion_vinculada SET nombre = ?, direccion = ? WHERE idOrganizacion = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, organizacionVinculada.getNombre());
            actualizacion.setString(2, organizacionVinculada.getDireccion());
            actualizacion.setInt(3, idOrganizacion);
            filasAfectadas = actualizacion.executeUpdate();
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
        return filasAfectadas;
    }

    public List<OrganizacionVinculada> obtenerOrganizacionesActivas() throws UsuariosExcepcion {
        String consulta = "SELECT idOrganizacion, nombre, direccion FROM organizacion_vinculada";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaOrganizaciones = null;
        List<OrganizacionVinculada> organizaciones = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaOrganizaciones = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaOrganizaciones.executeQuery();
            while (resultado.next()) {
                OrganizacionVinculada organizacion = new OrganizacionVinculada();
                organizacion.setIdOrganizacion(resultado.getInt("idOrganizacion"));
                organizacion.setNombre(resultado.getString("nombre"));
                organizacion.setDireccion(resultado.getString("direccion"));
                organizaciones.add(organizacion);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener organizaciones", e);
            throw new UsuariosExcepcion("Error al obtener organizaciones", e);
        } finally {
            try {
                if (consultaOrganizaciones != null) consultaOrganizaciones.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return organizaciones;
    }
}