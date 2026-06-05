package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.AdministradorDaoInterfaz;
import logica.dominio.Administrador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministradorDao implements AdministradorDaoInterfaz {

    private static final Logger LOGGER = Logger.getLogger(AdministradorDao.class.getName());

    @Override
    public int insertarAdministrador(Administrador administrador) throws UsuariosExcepcion {
        String consultaUsuario = "INSERT INTO usuario (nombre, apellidos, contrasena, estado, rol, correo) VALUES (?, ?, ?, ?, 'Administrador', ?)";
        String consultaAdministrador = "INSERT INTO administrador (numeroDePersonalAdministrador, idUsuarioAdministrador) VALUES (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionAdministrador = null;
        int filasAfectadas = 0;

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, administrador.getNombre());
            insercionUsuario.setString(2, administrador.getApellidos());
            insercionUsuario.setString(3, administrador.getContrasena());
            insercionUsuario.setString(4, administrador.getEstado().toString());
            insercionUsuario.setString(5, administrador.getCorreo());
            insercionUsuario.executeUpdate();

            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }

            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionAdministrador = conexionBaseDeDatos.prepareStatement(consultaAdministrador);
            insercionAdministrador.setString(1, administrador.getNumeroDePersonalAdministrador());
            insercionAdministrador.setInt(2, idUsuarioGenerado);
            filasAfectadas = insercionAdministrador.executeUpdate();
            LOGGER.info("Administrador insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar Administrador", excepcionSql);
            throw new UsuariosExcepcion("Error al insertar administrador", excepcionSql);
        } finally {
            try {
                if (insercionAdministrador != null) {
                    insercionAdministrador.close();
                }
                if (insercionUsuario != null) {
                    insercionUsuario.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }
}