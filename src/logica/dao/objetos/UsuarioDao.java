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
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryUsuario, Statement.RETURN_GENERATED_KEYS); {
                insertarEnBaseDeDatos.setString(1, usuario.getNombre());
                insertarEnBaseDeDatos.setString(2, usuario.getApellidoPaterno());
                insertarEnBaseDeDatos.setString(3, usuario.getApellidoMaterno());
                insertarEnBaseDeDatos.setString(4, usuario.getContrasena());
                insertarEnBaseDeDatos.setString(5, usuario.getEstado().toString());
                insertarEnBaseDeDatos.executeUpdate();

                ResultSet tomarLlave = insertarEnBaseDeDatos.getGeneratedKeys();
                if (tomarLlave.next()) return tomarLlave.getInt(1);



                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new InserccionUsuarioExcepcion("Error al insertar usuario", e);
        }
        return -1;
    }
}
