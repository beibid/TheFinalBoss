package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Profesor;
import logica.dominio.Practicante;
import logica.dao.interfaces.ProfesorDaoInterfaz;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfesorDao implements ProfesorDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(ProfesorDao.class.getName());

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Inserta un nuevo profesor en la base de datos.
     * @param profesor el profesor a insertar
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al insertar o de conexion
     */
    @Override
    public int insertarProfesor(Profesor profesor) throws UsuariosExcepcion {
        String consultaUsuario = "INSERT INTO usuario (nombre, apellidos, contrasena, estado, rol, correo) VALUES (?, ?, ?, ?, 'Profesor', ?)";
        String consultaProfesor = "INSERT INTO profesor (numPersonalProfesor, turno, idUsuario) VALUES (?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionProfesor = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, profesor.getNombre());
            insercionUsuario.setString(2, profesor.getApellidos());
            insercionUsuario.setString(3, profesor.getContrasena());
            insercionUsuario.setString(4, profesor.getEstado().toString());
            insercionUsuario.setString(5, profesor.getCorreo());
            insercionUsuario.executeUpdate();
            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionProfesor = conexionBaseDeDatos.prepareStatement(consultaProfesor);
            insercionProfesor.setString(1, profesor.getNumeroDePersonalProfesor());
            insercionProfesor.setString(2, profesor.getTurno().toString());
            insercionProfesor.setInt(3, idUsuarioGenerado);
            filasAfectadas = insercionProfesor.executeUpdate();
            LOGGER.info("Profesor insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar profesor", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al insertar profesor", excepcionSql);
        } finally {
            try {
                if (insercionProfesor != null){
                    insercionProfesor.close();
                }
                if (insercionUsuario != null){
                    insercionUsuario.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Inactiva un profesor en la base de datos.
     * @param numPersonalProfesor el numero de personal del profesor a inactivar
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al inactivar o de conexion
     */
    public int inactivarProfesor(String numPersonalProfesor) throws UsuariosExcepcion {
        if (numPersonalProfesor == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        String consulta = "UPDATE usuario SET estado = ? WHERE idUsuario = (SELECT idUsuario FROM profesor WHERE numPersonalProfesor = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, Estado.Inactivo.toString());
            actualizacion.setString(2, numPersonalProfesor);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Profesor inactivado correctamente: " + numPersonalProfesor);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al inactivar profesor", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al inactivar profesor", excepcionSql);
        } finally {
            try {
                if (actualizacion != null){
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Modifica los datos de un profesor en la base de datos.
     * @param numPersonalProfesor el numero de personal del profesor a modificar
     * @param profesor el profesor con los nuevos datos
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al modificar o de conexion
     */
    public int modificarProfesor(String numPersonalProfesor, Profesor profesor) throws UsuariosExcepcion {
        if (numPersonalProfesor == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        if (profesor.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre del profesor no puede ser nulo");
        }
        String consultaUsuario = "UPDATE usuario SET nombre = ?, apellidos = ?, correo = ?, contrasena = ?, estado = ? WHERE idUsuario = (SELECT idUsuario FROM profesor WHERE numPersonalProfesor = ?)";
        String consultaProfesor = "UPDATE profesor SET turno = ? WHERE numPersonalProfesor = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacionUsuario = null;
        PreparedStatement actualizacionProfesor = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario);
            actualizacionUsuario.setString(1, profesor.getNombre());
            actualizacionUsuario.setString(2, profesor.getApellidos());
            actualizacionUsuario.setString(3, profesor.getCorreo());
            actualizacionUsuario.setString(4, profesor.getContrasena());
            actualizacionUsuario.setString(5, profesor.getEstado().toString());
            actualizacionUsuario.setString(6, numPersonalProfesor);
            actualizacionUsuario.executeUpdate();
            actualizacionProfesor = conexionBaseDeDatos.prepareStatement(consultaProfesor);
            actualizacionProfesor.setString(1, profesor.getTurno().toString());
            actualizacionProfesor.setString(2, numPersonalProfesor);
            filasAfectadas = actualizacionProfesor.executeUpdate();
            LOGGER.info("Profesor modificado correctamente: " + numPersonalProfesor);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar profesor", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al modificar profesor", excepcionSql);
        } finally {
            try {
                if (actualizacionProfesor != null){
                    actualizacionProfesor.close();
                }
                if (actualizacionUsuario != null){
                    actualizacionUsuario.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Obtiene la lista de profesores con estado activo en el sistema.
     * @return lista de profesores activos
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
    public List<Profesor> obtenerProfesoresActivos() throws UsuariosExcepcion {
        String consulta = "SELECT u.idUsuario, u.nombre, u.apellidos, u.estado, p.numPersonalProfesor, p.turno " +
                "FROM usuario u " +
                "INNER JOIN profesor p ON u.idUsuario = p.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaProfesores = null;
        List<Profesor> profesores = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaProfesores = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaProfesores.executeQuery();
            while (resultado.next()) {
                Profesor profesor = new Profesor();
                profesor.setIdUsuario(resultado.getInt("idUsuario"));
                profesor.setNombre(resultado.getString("nombre"));
                profesor.setApellidos(resultado.getString("apellidos"));
                profesor.setNumeroDePersonalProfesor(resultado.getString("numPersonalProfesor"));
                profesor.setTurno(Turno.valueOf(resultado.getString("turno")));
                profesor.setEstado(Estado.valueOf(resultado.getString("estado")));
                profesores.add(profesor);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener profesores activos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener profesores activos", excepcionSql);
        } finally {
            try {
                if (consultaProfesores != null){
                    consultaProfesores.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return profesores;
    }

    /**
     * Obtiene la lista de practicantes asignados al proyecto de un profesor.
     * @param numPersonalProfesor el numero de personal del profesor
     * @return lista de practicantes del profesor
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
    public List<Practicante> obtenerPracticantesPorProfesor(String numPersonalProfesor) throws UsuariosExcepcion {
        String consulta = "SELECT u.idUsuario, u.nombre, u.apellidos, p.matricula " +
                "FROM practicante p " +
                "INNER JOIN proyecto pr ON p.idProyecto = pr.idProyecto " +
                "INNER JOIN usuario u ON p.idUsuario = u.idUsuario " +
                "WHERE pr.numPersonalProfesor = ?";
        Connection conexion = null;
        PreparedStatement consultaProfesor = null;
        List<Practicante> practicantes = new ArrayList<>();
        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            consultaProfesor = conexion.prepareStatement(consulta);
            consultaProfesor.setString(1, numPersonalProfesor);
            ResultSet resultado = consultaProfesor.executeQuery();
            while (resultado.next()) {
                Practicante practicante = new Practicante();
                practicante.setIdUsuario(resultado.getInt("idUsuario"));
                practicante.setNombre(resultado.getString("nombre"));
                practicante.setApellidos(resultado.getString("apellidos"));
                practicante.setMatricula(resultado.getString("matricula"));
                practicantes.add(practicante);
            }
            LOGGER.info("Practicantes obtenidos para profesor: " + numPersonalProfesor);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener practicantes", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener practicantes", excepcionSql);
        } finally {
            try {
                if (consultaProfesor != null){
                    consultaProfesor.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexion", excepcionSql);
            }
        }
        return practicantes;
    }

    /**
     * Retorna el conteo de profesores con estado activo en el sistema.
     * @return numero de profesores activos
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
    public int existeProfesorActivo() throws UsuariosExcepcion {
        String consulta = "SELECT COUNT(*) " +
                "FROM usuario u " +
                "INNER JOIN profesor p ON u.idUsuario = p.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaProfesorActivo = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaProfesorActivo = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaProfesorActivo.executeQuery();
            if (resultado.next()) {
                filasAfectadas = resultado.getInt(1);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al verificar profesores activos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al verificar profesores activos", excepcionSql);
        } finally {
            try {
                if (consultaProfesorActivo != null) {
                    consultaProfesorActivo.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }
}