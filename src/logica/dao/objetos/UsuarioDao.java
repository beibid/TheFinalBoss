package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Usuario;
import logica.dominio.UsuarioSesion;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Rol;
import logica.dao.interfaces.UsuarioDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class UsuarioDao implements UsuarioDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(UsuarioDao.class.getName());

    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    @Override
    public int insertarUsuario(Usuario usuario) throws UsuariosExcepcion {
        if (usuario.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre del usuario no puede ser nulo");
        }
        if (usuario.getNombre().isEmpty()) {
            throw new UsuariosExcepcion("El nombre del usuario no puede estar vacio");
        }
        if (usuario.getApellidos() == null) {
            throw new UsuariosExcepcion("Los apellidos del usuario no pueden ser nulos");
        }
        if (usuario.getContrasena() == null) {
            throw new UsuariosExcepcion("La contrasena del usuario no puede ser nula");
        }
        String consultaUsuario = "INSERT INTO usuario (nombre, apellidos, contrasena, estado, correo) VALUES (?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionBaseDeDatos = null;
        int idGenerado = -1;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionBaseDeDatos.setString(1, usuario.getNombre());
            insercionBaseDeDatos.setString(2, usuario.getApellidos());
            insercionBaseDeDatos.setString(3, usuario.getContrasena());
            insercionBaseDeDatos.setString(4, usuario.getEstado().toString());
            insercionBaseDeDatos.setString(5, usuario.getCorreo());
            insercionBaseDeDatos.executeUpdate();
            ResultSet tomarLlave = insercionBaseDeDatos.getGeneratedKeys();
            if (tomarLlave.next()) {
                idGenerado = tomarLlave.getInt(1);
                LOGGER.info("Usuario insertado correctamente con ID: " + idGenerado);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar el usuario", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al insertar usuario", excepcionSql);
        } finally {
            try {
                if (insercionBaseDeDatos != null) insercionBaseDeDatos.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return idGenerado;
    }

    public UsuarioSesion buscarUsuario(String correo, String contrasena) throws UsuariosExcepcion {
        String llamadaProcedimiento = "{CALL sp_buscar_usuario_login(?, ?)}";
        Connection conexionBaseDeDatos = null;
        CallableStatement procedimiento = null;
        UsuarioSesion usuarioSesion = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            procedimiento = conexionBaseDeDatos.prepareCall(llamadaProcedimiento);
            procedimiento.setString(1, correo);
            procedimiento.setString(2, contrasena);
            ResultSet resultado = procedimiento.executeQuery();
            if (resultado.next()) {
                usuarioSesion = new UsuarioSesion();
                usuarioSesion.setNombre(resultado.getString("nombre"));
                usuarioSesion.setApellidos(resultado.getString("apellidos"));
                usuarioSesion.setRol(Rol.valueOf(resultado.getString("rol")));
                usuarioSesion.setEstado(Estado.valueOf(resultado.getString("estado")));
                usuarioSesion.setMatricula(resultado.getString("matricula"));
                usuarioSesion.setIdentificador(resultado.getString("identificador"));
                usuarioSesion.setIdUsuario(resultado.getInt("idUsuario"));
                usuarioSesion.setDebeCambiarContrasena(resultado.getBoolean("debeCambiarContrasena"));
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al buscar usuario", excepcionSql);
        } finally {
            try {
                if (procedimiento != null) procedimiento.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return usuarioSesion;
    }

    public int actualizarContrasena(int idUsuario, String contrasenaCifrada) throws UsuariosExcepcion {
        String consulta = "UPDATE usuario SET contrasena = ?, debeCambiarContrasena = 0 WHERE idUsuario = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, contrasenaCifrada);
            actualizacion.setInt(2, idUsuario);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Contrasena actualizada para usuario: " + idUsuario);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al actualizar contrasena", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al actualizar la contrasena", excepcionSql);
        } finally {
            try {
                if (actualizacion != null) actualizacion.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }
}