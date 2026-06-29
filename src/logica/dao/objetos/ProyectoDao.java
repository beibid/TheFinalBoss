package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Proyecto;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.ProyectoDaoInterfaz;
import logica.dominio.enums.EstadoProyecto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProyectoDao implements ProyectoDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(ProyectoDao.class.getName());

    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    @Override
    public int agregarProyecto(Proyecto proyecto) throws MensajeriaExcepcion {
        String consultaProyecto = "INSERT INTO proyecto (nombreProyecto, descripcion, responsableDelProyecto, estado, nombreEmpresa, sectorEmpresa, direccionEmpresa, idOrganizacion, numPersonalProfesor, numPersonalCoordinador, fechaRegistro, capacidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            insertarEnBaseDeDatos.setString(9, proyecto.getNumPersonalProfesor());
            insertarEnBaseDeDatos.setString(10, proyecto.getNumPersonalCoordinador());
            insertarEnBaseDeDatos.setDate(11, proyecto.getFechaRegistro());
            insertarEnBaseDeDatos.setInt(12, proyecto.getCapacidad());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Proyecto insertado correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar proyecto", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al agregar el proyecto", excepcionSql);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null) insertarEnBaseDeDatos.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
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
        String consulta = "UPDATE proyecto SET nombreProyecto = ?, descripcion = ?, responsableDelProyecto = ?, estado = ?, nombreEmpresa = ?, sectorEmpresa = ?, direccionEmpresa = ?, idOrganizacion = ?, numPersonalProfesor = ?, numPersonalCoordinador = ?, fechaRegistro = ?, capacidad = ? WHERE idProyecto = ?";
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
            actualizacion.setString(9, proyecto.getNumPersonalProfesor());
            actualizacion.setString(10, proyecto.getNumPersonalCoordinador());
            actualizacion.setDate(11, proyecto.getFechaRegistro());
            actualizacion.setInt(12, proyecto.getCapacidad());
            actualizacion.setInt(13, idProyecto);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Proyecto modificado correctamente: " + idProyecto);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar proyecto", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al modificar proyecto");
        } finally {
            try {
                if (actualizacion != null) actualizacion.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public List<Proyecto> obtenerProyectosDisponibles() throws MensajeriaExcepcion {
        String consulta = "SELECT p.idProyecto, p.nombreProyecto, p.estado, o.nombre AS nombreOrganizacion " +
                "FROM proyecto p JOIN organizacion_vinculada o ON p.idOrganizacion = o.idOrganizacion " +
                "WHERE p.estado = 'Disponible'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        List<Proyecto> listaProyectos = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultados = sentencia.executeQuery();
            while (resultados.next()) {
                Proyecto proyecto = new Proyecto();
                proyecto.setIdProyecto(resultados.getInt("idProyecto"));
                proyecto.setNombreProyecto(resultados.getString("nombreProyecto"));
                proyecto.setEstado(EstadoProyecto.valueOf(
                        resultados.getString("estado").replace(" ", "_")));
                proyecto.setNombreEmpresa(resultados.getString("nombreOrganizacion"));
                listaProyectos.add(proyecto);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener proyectos disponibles", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener proyectos disponibles", excepcionSql);
        } finally {
            try {
                if (sentencia != null) sentencia.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return listaProyectos;
    }

    public int inactivarProyecto(int idProyecto) throws MensajeriaExcepcion {
        String consulta = "UPDATE proyecto SET estado = ? WHERE idProyecto = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, EstadoProyecto.Eliminado.toString().replace("_", " "));
            actualizacion.setInt(2, idProyecto);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Proyecto inactivado correctamente: " + idProyecto);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al inactivar proyecto", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al inactivar proyecto", excepcionSql);
        } finally {
            try {
                if (actualizacion != null) actualizacion.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public Proyecto obtenerProyectoPorPracticante(String matricula) throws MensajeriaExcepcion {
        String consulta = "SELECT pr.idProyecto, pr.nombreProyecto, pr.responsableDelProyecto, " +
                "o.nombre AS nombreOrganizacion " +
                "FROM practicante p " +
                "INNER JOIN proyecto pr ON p.idProyecto = pr.idProyecto " +
                "INNER JOIN organizacion_vinculada o ON pr.idOrganizacion = o.idOrganizacion " +
                "WHERE p.matricula = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        Proyecto proyecto = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                proyecto = new Proyecto();
                proyecto.setIdProyecto(resultado.getInt("idProyecto"));
                proyecto.setNombreProyecto(resultado.getString("nombreProyecto"));
                proyecto.setResponsableDelProyecto(resultado.getString("responsableDelProyecto"));
                proyecto.setNombreOrganizacion(resultado.getString("nombreOrganizacion"));
            }
            LOGGER.info("Proyecto cargado para practicante: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener proyecto del practicante", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener proyecto del practicante", excepcionSql);
        } finally {
            try {
                if (sentencia != null) sentencia.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return proyecto;
    }
}