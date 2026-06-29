package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dominio.Coordinador;
import logica.dao.interfaces.CoordinadorDaoInterfaz;
import logica.dominio.enums.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinadorDao implements CoordinadorDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(CoordinadorDao.class.getName());

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Inserta un nuevo coordinador en la base de datos.
     * @param coordinador el coordinador a insertar
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al insertar o de conexion
     * @throws RegistroDuplicadoExcepcion si el numero de personal ya existe
     */
    @Override
    public int insertarCoordinador(Coordinador coordinador) throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        String consultaUsuario = "INSERT INTO usuario (nombre, apellidos, contrasena, estado, rol, correo) VALUES (?, ?, ?, ?, 'Coordinador', ?)";
        String consultaCoordinador = "INSERT INTO coordinador (numPersonalCoordinador, idUsuario) VALUES (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionCoordinador = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, coordinador.getNombre());
            insercionUsuario.setString(2, coordinador.getApellidos());
            insercionUsuario.setString(3, coordinador.getContrasena());
            insercionUsuario.setString(4, coordinador.getEstado().toString());
            insercionUsuario.setString(5, coordinador.getCorreo());
            insercionUsuario.executeUpdate();
            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionCoordinador = conexionBaseDeDatos.prepareStatement(consultaCoordinador);
            insercionCoordinador.setString(1, coordinador.getNumeroDePersonalCoordinador());
            insercionCoordinador.setInt(2, idUsuarioGenerado);
            filasAfectadas = insercionCoordinador.executeUpdate();
            LOGGER.info("Coordinador insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar coordinador", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            if (excepcionSql.getMessage() != null && excepcionSql.getMessage().contains("Duplicate entry")) {
                throw new RegistroDuplicadoExcepcion("El numero del personal ya existe", excepcionSql);
            }
            throw new UsuariosExcepcion("Error al insertar coordinador", excepcionSql);
        } finally {
            try {
                if (insercionCoordinador != null) {
                    insercionCoordinador.close();
                }
                if (insercionUsuario != null){
                    insercionUsuario.close();
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

    /**
     * Inactiva un coordinador en la base de datos.
     * @param numPersonalCoordinador el numero de personal del coordinador a inactivar
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al inactivar o de conexion
     */
    public int inactivarCoordinador(String numPersonalCoordinador) throws UsuariosExcepcion {
        if (numPersonalCoordinador == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        String consulta = "UPDATE usuario SET estado = ? WHERE idUsuario = (SELECT idUsuario FROM coordinador WHERE numPersonalCoordinador = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, Estado.Inactivo.toString());
            actualizacion.setString(2, numPersonalCoordinador);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Coordinador inactivado correctamente: " + numPersonalCoordinador);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al inactivar coordinador", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al inactivar coordinador", excepcionSql);
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

    /**
     * Modifica los datos de un coordinador en la base de datos.
     * @param numPersonalCoordinador el numero de personal del coordinador a modificar
     * @param coordinador el coordinador con los nuevos datos
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al modificar o de conexion
     */
    public int modificarCoordinador(String numPersonalCoordinador, Coordinador coordinador) throws UsuariosExcepcion {
        if (numPersonalCoordinador == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        if (coordinador.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre del coordinador no puede ser nulo");
        }
        String consultaUsuario = "UPDATE usuario SET nombre = ?, apellidos = ?, correo = ?, contrasena = ?, estado = ? WHERE idUsuario = (SELECT idUsuario FROM coordinador WHERE numPersonalCoordinador = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consultaUsuario);
            actualizacion.setString(1, coordinador.getNombre());
            actualizacion.setString(2, coordinador.getApellidos());
            actualizacion.setString(3, coordinador.getCorreo());
            actualizacion.setString(4, coordinador.getContrasena());
            actualizacion.setString(5, coordinador.getEstado().toString());
            actualizacion.setString(6, numPersonalCoordinador);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Coordinador modificado correctamente: " + numPersonalCoordinador);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar coordinador", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al modificar coordinador", excepcionSql);
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

    /**
     * Obtiene la lista de coordinadores activos en el sistema.
     * @return lista de coordinadores con estado activo
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
    public List<Coordinador> obtenerCoordinadoresActivos() throws UsuariosExcepcion {
        String consulta = "SELECT u.nombre, u.apellidos, u.correo, u.estado, u.contrasena, p.numPersonalCoordinador " +
                "FROM usuario u " +
                "INNER JOIN coordinador p ON u.idUsuario = p.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaCoordinadores = null;
        List<Coordinador> coordinadores = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaCoordinadores = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaCoordinadores.executeQuery();
            while (resultado.next()) {
                Coordinador coordinador = new Coordinador();
                coordinador.setNombre(resultado.getString("nombre"));
                coordinador.setApellidos(resultado.getString("apellidos"));
                coordinador.setNumeroDePersonalCoordinador(resultado.getString("numPersonalCoordinador"));
                coordinador.setCorreo(resultado.getString("correo"));
                coordinador.setEstado(Estado.valueOf(resultado.getString("estado")));
                coordinador.setContrasena(resultado.getString("contrasena"));
                coordinadores.add(coordinador);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener coordinadores activos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener coordinadores activos", excepcionSql);
        } finally {
            try {
                if (consultaCoordinadores != null){
                    consultaCoordinadores.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return coordinadores;
    }

    /**
     * Verifica si existe al menos un coordinador activo en el sistema.
     * @return el numero de coordinadores activos
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
    public int existeCoordinadorActivo() throws UsuariosExcepcion {
        String consulta = "SELECT COUNT(*) " +
                "FROM usuario u " +
                "INNER JOIN coordinador c ON u.idUsuario = c.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaCoordinadorActivo = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaCoordinadorActivo = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaCoordinadorActivo.executeQuery();
            if (resultado.next()) {
                filasAfectadas = resultado.getInt(1);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al verificar coordinadores activos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al verificar coordinadores activos", excepcionSql);
        } finally {
            try {
                if (consultaCoordinadorActivo != null){
                    consultaCoordinadorActivo.close();
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