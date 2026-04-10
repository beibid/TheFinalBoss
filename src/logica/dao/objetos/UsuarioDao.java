package logica.dao.objetos;
import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Usuario;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.UsuarioDaoInterfaz;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDao implements UsuarioDaoInterfaz{
    private static final Logger LOGGER = Logger.getLogger(UsuarioDao.class.getName());
    @Override
    public int insertarUsuario (Usuario usuario) throws InserccionBaseDeDatosExcepcion {
        String queryUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        try {
            Connection conexionBaseDeDatos = ConexionBaseDeDatos.conectar();
            PreparedStatement insercionBaseDeDatos = conexionBaseDeDatos.prepareStatement(queryUsuario, Statement.RETURN_GENERATED_KEYS); {
                insercionBaseDeDatos.setString(1, usuario.getNombre());
                insercionBaseDeDatos.setString(2, usuario.getApellidoPaterno());
                insercionBaseDeDatos.setString(3, usuario.getApellidoMaterno());
                insercionBaseDeDatos.setString(4, usuario.getContrasena());
                insercionBaseDeDatos.setString(5, usuario.getEstado().toString());
                insercionBaseDeDatos.executeUpdate();

                ResultSet tomarLlave = insercionBaseDeDatos.getGeneratedKeys();
                if (tomarLlave.next()) {
                    return tomarLlave.getInt(1);
                }


                LOGGER.info("Usuario insertado correctamente");

            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar el usuario", e);
            throw new InserccionBaseDeDatosExcepcion("Error al insertar usuario");
        }
        return -1;
    }
}
