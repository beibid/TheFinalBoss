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

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Inserta un nuevo proyecto en la base de datos.
     * @param proyecto el proyecto a insertar
     * @return el numero de filas afectadas
     * @throws MensajeriaExcepcion si ocurre un error al insertar o de conexion
     */
    @Override
    public int agregarProyecto(Proyecto proyecto) throws MensajeriaExcepcion {
        String consulta = "INSERT INTO proyecto (nombreProyecto, descripcion, responsableDelProyecto, estado, nombreEmpresa, sectorEmpresa, direccionEmpresa, idOrganizacion, numPersonalProfesor, numPersonalCoordinador, fechaRegistro, capacidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, proyecto.getNombreProyecto());
            sentencia.setString(2, proyecto.getDescripcion());
            sentencia.setString(3, proyecto.getResponsableDelProyecto());
            sentencia.setString(4, proyecto.getEstado().toString().replace("_", " "));
            sentencia.setString(5, proyecto.getNombreEmpresa());
            sentencia.setString(6, proyecto.getSectorEmpresa());
            sentencia.setString(7, proyecto.getDireccionEmpresa());
            sentencia.setInt(8, proyecto.getIdOrganizacion());
            sentencia.setString(9, proyecto.getNumPersonalProfesor());
            sentencia.setString(10, proyecto.getNumPersonalCoordinador());
            sentencia.setDate(11, proyecto.getFechaRegistro());
            sentencia.setInt(12, proyecto.getCapacidad());
            filasAfectadas = sentencia.executeUpdate();
            LOGGER.info("Proyecto insertado correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar proyecto", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al agregar el proyecto", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
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

    /**
     * Obtiene un proyecto por su identificador.
     * @param idProyecto el identificador del proyecto a buscar
     * @return el proyecto con todos sus datos, o null si no se encontro
     * @throws MensajeriaExcepcion si ocurre un error al obtener el proyecto o de conexion
     */
    public Proyecto obtenerProyectoPorId(int idProyecto) throws MensajeriaExcepcion {
        String consulta = "SELECT idProyecto, nombreProyecto, descripcion, responsableDelProyecto, " +
                "estado, nombreEmpresa, sectorEmpresa, direccionEmpresa, " +
                "numPersonalCoordinador, fechaRegistro, idOrganizacion, capacidad " +
                "FROM proyecto WHERE idProyecto = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        Proyecto proyecto = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setInt(1, idProyecto);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                proyecto = new Proyecto();
                proyecto.setIdProyecto(resultado.getInt("idProyecto"));
                proyecto.setNombreProyecto(resultado.getString("nombreProyecto"));
                proyecto.setDescripcion(resultado.getString("descripcion"));
                proyecto.setResponsableDelProyecto(resultado.getString("responsableDelProyecto"));
                proyecto.setEstado(EstadoProyecto.valueOf(
                        resultado.getString("estado").replace(" ", "_")));
                proyecto.setNombreEmpresa(resultado.getString("nombreEmpresa"));
                proyecto.setSectorEmpresa(resultado.getString("sectorEmpresa"));
                proyecto.setDireccionEmpresa(resultado.getString("direccionEmpresa"));
                proyecto.setNumPersonalCoordinador(resultado.getString("numPersonalCoordinador"));
                proyecto.setFechaRegistro(resultado.getDate("fechaRegistro"));
                proyecto.setIdOrganizacion(resultado.getInt("idOrganizacion"));
                proyecto.setCapacidad(resultado.getInt("capacidad"));
            }
            LOGGER.info("Proyecto cargado por id: " + idProyecto);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener proyecto por id", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener proyecto", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return proyecto;
    }

    /**
     * Modifica los datos de un proyecto existente en la base de datos.
     * @param idProyecto el identificador del proyecto a modificar
     * @param proyecto el proyecto con los nuevos datos
     * @return el numero de filas afectadas
     * @throws MensajeriaExcepcion si ocurre un error al modificar o de conexion
     */
    public int modificarProyecto(int idProyecto, Proyecto proyecto) throws MensajeriaExcepcion {
        if (proyecto.getNombreProyecto() == null) {
            throw new MensajeriaExcepcion("El nombre del proyecto no puede ser nulo");
        }
        if (proyecto.getFechaRegistro() == null) {
            throw new MensajeriaExcepcion("La fecha de registro no puede ser nula");
        }
        String consulta = "UPDATE proyecto SET nombreProyecto = ?, descripcion = ?, responsableDelProyecto = ?, estado = ?, nombreEmpresa = ?, sectorEmpresa = ?, direccionEmpresa = ?, idOrganizacion = ?, numPersonalProfesor = ?, numPersonalCoordinador = ?, fechaRegistro = ?, capacidad = ? WHERE idProyecto = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, proyecto.getNombreProyecto());
            sentencia.setString(2, proyecto.getDescripcion());
            sentencia.setString(3, proyecto.getResponsableDelProyecto());
            sentencia.setString(4, proyecto.getEstado().toString().replace("_", " "));
            sentencia.setString(5, proyecto.getNombreEmpresa());
            sentencia.setString(6, proyecto.getSectorEmpresa());
            sentencia.setString(7, proyecto.getDireccionEmpresa());
            sentencia.setInt(8, proyecto.getIdOrganizacion());
            sentencia.setString(9, proyecto.getNumPersonalProfesor());
            sentencia.setString(10, proyecto.getNumPersonalCoordinador());
            sentencia.setDate(11, proyecto.getFechaRegistro());
            sentencia.setInt(12, proyecto.getCapacidad());
            sentencia.setInt(13, idProyecto);
            filasAfectadas = sentencia.executeUpdate();
            LOGGER.info("Proyecto modificado correctamente: " + idProyecto);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar proyecto", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al modificar proyecto", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
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

    /**
     * Obtiene la lista de proyectos con estado Disponible.
     * @return lista de proyectos disponibles para asignar a practicantes
     * @throws MensajeriaExcepcion si ocurre un error al obtener los proyectos o de conexion
     */
    public List<Proyecto> obtenerProyectosDisponibles() throws MensajeriaExcepcion {
        String consulta = "SELECT p.idProyecto, p.nombreProyecto, p.estado, p.capacidad, " +
                "o.nombre AS nombreOrganizacion " +
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
                proyecto.setCapacidad(resultados.getInt("capacidad"));
                listaProyectos.add(proyecto);
            }
            LOGGER.info("Proyectos disponibles cargados: " + listaProyectos.size());
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener proyectos disponibles", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener proyectos disponibles", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return listaProyectos;
    }

    /**
     * Cambia el estado de un proyecto a Eliminado.
     * @param idProyecto el identificador del proyecto a inactivar
     * @return el numero de filas afectadas
     * @throws MensajeriaExcepcion si ocurre un error al inactivar o de conexion
     */
    public int inactivarProyecto(int idProyecto) throws MensajeriaExcepcion {
        String consulta = "UPDATE proyecto SET estado = ? WHERE idProyecto = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, EstadoProyecto.Eliminado.toString().replace("_", " "));
            sentencia.setInt(2, idProyecto);
            filasAfectadas = sentencia.executeUpdate();
            LOGGER.info("Proyecto inactivado correctamente: " + idProyecto);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al inactivar proyecto", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al inactivar proyecto", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Obtiene el proyecto asignado a un practicante.
     * @param matricula la matricula del practicante
     * @return el proyecto asignado al practicante, o null si no tiene proyecto asignado
     * @throws MensajeriaExcepcion si ocurre un error al obtener el proyecto o de conexion
     */
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
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return proyecto;
    }
}