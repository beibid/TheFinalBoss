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
import java.sql.SQLException;

public class UsuarioDao implements UsuarioDaoInterfaz {

    private static final Logger LOGGER = Logger.getLogger(UsuarioDao.class.getName());

    @Override
    public int insertarUsuario(Usuario usuario) throws UsuariosExcepcion {
        String consultaUsuario = "INSERT INTO usuario (nombre, apellidos, contrasena, estado) VALUES (?, ?, ?, ?)";
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

    public UsuarioSesion buscarUsuario(String identificador, String contrasena) throws UsuariosExcepcion {
        String consultaBusqueda =
                "SELECT u.nombre, u.apellidos, u.estado, u.rol as tipo, " +
                        "    pr.matricula " +                                          // <-- nuevo
                        "FROM usuario u " +
                        "LEFT JOIN practicante pr ON pr.idUsuario = u.idUsuario " +   // <-- nuevo
                        "WHERE u.contrasena = ? " +
                        "AND ( " +
                        "    EXISTS (SELECT 1 FROM coordinador c WHERE c.idUsuario = u.idUsuario AND c.numPersonalCoordinador = ?) " +
                        "    OR EXISTS (SELECT 1 FROM profesor p WHERE p.idUsuario = u.idUsuario AND p.numPersonalProfesor = ?) " +
                        "    OR EXISTS (SELECT 1 FROM practicante pr WHERE pr.idUsuario = u.idUsuario AND pr.matricula = ?) " +
                        "    OR EXISTS (SELECT 1 FROM administrador a WHERE a.idUsuarioAdministrador = u.idUsuario AND a.numeroDePersonalAdministrador = ?) " +
                        ")";
        Connection conexionBaseDeDatos = null;
        PreparedStatement busquedaUsuario = null;
        UsuarioSesion usuarioSesion = null;

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            busquedaUsuario = conexionBaseDeDatos.prepareStatement(consultaBusqueda);
            busquedaUsuario.setString(1, contrasena);
            busquedaUsuario.setString(2, identificador);
            busquedaUsuario.setString(3, identificador);
            busquedaUsuario.setString(4, identificador);
            busquedaUsuario.setString(5, identificador);

            ResultSet resultado = busquedaUsuario.executeQuery();
            if (resultado.next()) {
                usuarioSesion = new UsuarioSesion();
                usuarioSesion.setNombre(resultado.getString("nombre"));
                usuarioSesion.setApellidos(resultado.getString("apellidos"));
                usuarioSesion.setRol(Rol.valueOf(resultado.getString("tipo")));
                usuarioSesion.setEstado(Estado.valueOf(resultado.getString("estado")));
                usuarioSesion.setMatricula(resultado.getString("matricula"));
            }
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario", excepcionSQL);
            throw new UsuariosExcepcion("Error al buscar usuario", excepcionSQL);
        } finally {
            try {
                if (busquedaUsuario != null) {
                    busquedaUsuario.close();
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