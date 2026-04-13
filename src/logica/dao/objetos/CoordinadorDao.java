package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Coordinador;
import logica.dao.interfaces.CoordinadorDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CoordinadorDao implements CoordinadorDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(CoordinadorDao.class.getName());
    @Override

    public void insertarCoordinador(Coordinador coordinador) throws UsuariosExcepcion {
        String consultaUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        String consultaCoordinador = "insert into Coordinador (numPersonalCoordinador, idUsuario) values (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionCoordinador = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, coordinador.getNombre());
            insercionUsuario.setString(2, coordinador.getApellidoPaterno());
            insercionUsuario.setString(3, coordinador.getApellidoMaterno());
            insercionUsuario.setString(4, coordinador.getContrasena());
            insercionUsuario.setString(5, coordinador.getEstado().toString());
            insercionUsuario.executeUpdate();
            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionCoordinador = conexionBaseDeDatos.prepareStatement(consultaCoordinador);
            insercionCoordinador.setString(1, coordinador.getNumeroDePersonalCoordinador());
            insercionCoordinador.setInt(2, idUsuarioGenerado);
            insercionCoordinador.executeUpdate();
            LOGGER.info("Coordinador insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar Coordinador", e);
            throw new UsuariosExcepcion("Error al insertar coordinador",e);
        } finally {
            try {
                if (insercionCoordinador != null){
                    insercionCoordinador.close();
                }
                if (insercionUsuario != null){
                    insercionUsuario.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
    }
}
