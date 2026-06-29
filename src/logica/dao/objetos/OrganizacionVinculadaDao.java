package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.OrganizacionVinculada;
import logica.dao.interfaces.OrganizacionVinculadaDaoInterfaz;
import logica.dominio.enums.EstadoOrganizacion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrganizacionVinculadaDao implements OrganizacionVinculadaDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(OrganizacionVinculadaDao.class.getName());

    @Override
    public int insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion {
        String consultaOrganizacion = "INSERT INTO organizacion_vinculada (nombre, direccion, telefono, correo, sector) VALUES (?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaOrganizacion);
            insertarEnBaseDeDatos.setString(1, organizacionVinculada.getNombre());
            insertarEnBaseDeDatos.setString(2, organizacionVinculada.getDireccion());
            insertarEnBaseDeDatos.setString(3, organizacionVinculada.getTelefono());
            insertarEnBaseDeDatos.setString(4, organizacionVinculada.getCorreo());
            insertarEnBaseDeDatos.setString(5, organizacionVinculada.getSector());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Organizacion vinculada insertada correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar la organizacion vinculada", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al insertar la organizacion", excepcionSql);
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

    public int modificarOrganizacionVinculada(int idOrganizacion, OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion {
        String consulta = "UPDATE organizacion_vinculada SET nombre = ?, direccion = ?, telefono = ?, correo = ?, sector = ? WHERE idOrganizacion = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, organizacionVinculada.getNombre());
            actualizacion.setString(2, organizacionVinculada.getDireccion());
            actualizacion.setString(3, organizacionVinculada.getTelefono());
            actualizacion.setString(4, organizacionVinculada.getCorreo());
            actualizacion.setString(5, organizacionVinculada.getSector());
            actualizacion.setInt(6, idOrganizacion);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("OrganizacionVinculada modificada correctamente: " + idOrganizacion);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar organizacion vinculada", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al modificar organizacion vinculada", excepcionSql);
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

    public List<OrganizacionVinculada> obtenerOrganizacionesActivas() throws UsuariosExcepcion {
        String consulta = "SELECT idOrganizacion, nombre, direccion, telefono, correo, sector FROM organizacion_vinculada";
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
                organizacion.setTelefono(resultado.getString("telefono"));
                organizacion.setCorreo(resultado.getString("correo"));
                organizacion.setSector(resultado.getString("sector"));
                organizaciones.add(organizacion);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener organizaciones", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener organizaciones", excepcionSql);
        } finally {
            try {
                if (consultaOrganizaciones != null) {
                    consultaOrganizaciones.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return organizaciones;
    }

    public int inactivarOrganizacionVinculada(int idOrganizacion) throws MensajeriaExcepcion {
        String consulta = "UPDATE organizacion_vinculada SET estado = ? WHERE idOrganizacion = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, EstadoOrganizacion.Inactiva.name());
            actualizacion.setInt(2, idOrganizacion);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Organizacion inactivada correctamente: " + idOrganizacion);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al inactivar la organizacion", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al inactivar la organizacion", excepcionSql);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }
}