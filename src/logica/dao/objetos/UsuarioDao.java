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

    private static final Logger LOGGER = Logger.getLogger(UsuarioDao.class.getName());

    @Override
    public int insertarUsuario(Usuario usuario) throws UsuariosExcepcion {
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
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al insertar el usuario", excepcionSQL);
            throw new UsuariosExcepcion("Error al insertar usuario", excepcionSQL);
        } finally {
            try {
                if (insercionBaseDeDatos != null) {
                    insercionBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
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
            }
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario", excepcionSQL);
            throw new UsuariosExcepcion("Error al buscar usuario", excepcionSQL);
        } finally {
            try {
                if (procedimiento != null) {
                    procedimiento.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return usuarioSesion;
    }
}