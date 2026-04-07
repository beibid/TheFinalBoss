package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.dominio.Usuario;
import logica.dao.excepciones.InserccionUsuarioExcepcion;
import logica.dao.interfaces.UsuarioDaoInterfaz;

import java.sql.*;

public class UsuarioDao implements UsuarioDaoInterfaz{
    @Override
    public int insertarUsuario (Usuario usuario) throws InserccionUsuarioExcepcion {
        String queryUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        try {
            Connection conexionBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insercionBaseDeDatos = conexionBaseDeDatos.prepareStatement(queryUsuario, Statement.RETURN_GENERATED_KEYS); {
                insercionBaseDeDatos.setString(1, usuario.getNombre());
                insercionBaseDeDatos.setString(2, usuario.getApellidoPaterno());
                insercionBaseDeDatos.setString(3, usuario.getApellidoMaterno());
                insercionBaseDeDatos.setString(4, usuario.getContrasena());
                insercionBaseDeDatos.setString(5, usuario.getEstado().toString());
                insercionBaseDeDatos.executeUpdate();

                ResultSet tomarLlave = insercionBaseDeDatos.getGeneratedKeys();
                if (tomarLlave.next()) return tomarLlave.getInt(1);



                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new InserccionUsuarioExcepcion("Error al insertar usuario");
        }
        return -1;
    }
}
