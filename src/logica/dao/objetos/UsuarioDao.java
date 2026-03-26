package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.Usuario;
import logica.dao.interfaces.UsuarioDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class UsuarioDao implements UsuarioDaoInterfaz{
    @Override
    public void insertarUsuario (Usuario usuario) {
        String queryUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryUsuario); {
                insertarEnBaseDeDatos.setString(1, usuario.getNombre());
                insertarEnBaseDeDatos.setString(2, usuario.getApellidoPaterno());
                insertarEnBaseDeDatos.setString(3, usuario.getApellidoMaterno());
                insertarEnBaseDeDatos.setString(4, usuario.getContrasena());
                insertarEnBaseDeDatos.setString(5, usuario.getEstado().toString());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            System.out.println("No fue posible añadir los datos");
            e.printStackTrace();
        }
    }
}
