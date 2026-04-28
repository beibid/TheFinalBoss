package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Usuario;
import logica.dominio.UsuarioSesion;
import logica.dominio.enums.Estado;
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
        String consultaUsuario = "insert into Usuario (nombre, apellidos, contrasena, estado) values (?, ?, ?, ?)";
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
                if (insercionBaseDeDatos != null) insercionBaseDeDatos.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return idGenerado;
    }

    public UsuarioSesion buscarUsuario(String identificador, String contrasena) throws UsuariosExcepcion {
        String consultaBusqueda =
                "SELECT u.nombre, u.apellidos, u.estado, 'Profesor' as tipo " +
                        "FROM Usuario u INNER JOIN Profesor p ON u.idUsuario = p.idUsuario " +
                        "WHERE p.numPersonalProfesor = ? AND u.contrasena = ? " +
                        "UNION " +
                        "SELECT u.nombre, u.apellidos, u.estado, 'Coordinador' as tipo " +
                        "FROM Usuario u INNER JOIN Coordinador c ON u.idUsuario = c.idUsuario " +
                        "WHERE c.numPersonalCoordinador = ? AND u.contrasena = ? " +
                        "UNION " +
                        "SELECT u.nombre, u.apellidos, u.estado, 'Practicante' as tipo " +
                        "FROM Usuario u INNER JOIN Practicante pr ON u.idUsuario = pr.idUsuario " +
                        "WHERE pr.matricula = ? AND u.contrasena = ? " +
                        "UNION " +
                        "SELECT u.nombre, u.apellidos, u.estado, 'Administrador' as tipo " +
                        "FROM Usuario u INNER JOIN administrador a ON u.idUsuario = a.idUsuarioAdministrador " +
                        "WHERE a.numeroDePersonalAdministrador = ? AND u.contrasena = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement busquedaUsuario = null;
        UsuarioSesion usuarioSesion = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            busquedaUsuario = conexionBaseDeDatos.prepareStatement(consultaBusqueda);
            busquedaUsuario.setString(1, identificador);
            busquedaUsuario.setString(2, contrasena);
            busquedaUsuario.setString(3, identificador);
            busquedaUsuario.setString(4, contrasena);
            busquedaUsuario.setString(5, identificador);
            busquedaUsuario.setString(6, contrasena);
            busquedaUsuario.setString(7, identificador);
            busquedaUsuario.setString(8, contrasena);
            ResultSet resultado = busquedaUsuario.executeQuery();
            if (resultado.next()) {
                usuarioSesion = new UsuarioSesion();
                usuarioSesion.setNombre(resultado.getString("nombre"));
                usuarioSesion.setApellidos(resultado.getString("apellidos"));
                usuarioSesion.setTipo(resultado.getString("tipo"));
                usuarioSesion.setEstado(Estado.valueOf(resultado.getString("estado")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario", e);
            throw new UsuariosExcepcion("Error al buscar usuario", e);
        } finally {
            try {
                if (busquedaUsuario != null) busquedaUsuario.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return usuarioSesion;
    }
}