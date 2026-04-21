package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Usuario;
import logica.dao.interfaces.UsuarioDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;

public class UsuarioDao implements UsuarioDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(UsuarioDao.class.getName());

    @Override
    public int insertarUsuario(Usuario usuario) throws UsuariosExcepcion {
        String consultaUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionBaseDeDatos = null;
        int idGenerado = -1;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionBaseDeDatos.setString(1, usuario.getNombre());
            insercionBaseDeDatos.setString(2, usuario.getApellidoPaterno());
            insercionBaseDeDatos.setString(3, usuario.getApellidoMaterno());
            insercionBaseDeDatos.setString(4, usuario.getContrasena());
            insercionBaseDeDatos.setString(5, usuario.getEstado().toString());
            insercionBaseDeDatos.executeUpdate();
            ResultSet tomarLlave = insercionBaseDeDatos.getGeneratedKeys();
            if (tomarLlave.next()) {
                idGenerado = tomarLlave.getInt(1);
                LOGGER.info("Usuario insertado correctamente con ID: " + idGenerado);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar el usuario", e);
            throw new UsuariosExcepcion("Error al insertar usuario", e);
        } finally {
            try {
                if (insercionBaseDeDatos != null) {
                    insercionBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return idGenerado;
    }
}