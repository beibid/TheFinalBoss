package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Profesor;
import logica.dao.interfaces.ProfesorDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProfesorDao implements ProfesorDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(ProfesorDao.class.getName());
    @Override
    public void insertarProfesor(Profesor profesor) throws UsuariosExcepcion {
        String consultaUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        String consultaProfesor = "insert into Profesor (numPersonalProfesor, turno, idUsuario) values (?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionProfesor = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, profesor.getNombre());
            insercionUsuario.setString(2, profesor.getApellidoPaterno());
            insercionUsuario.setString(3, profesor.getApellidoMaterno());
            insercionUsuario.setString(4, profesor.getContrasena());
            insercionUsuario.setString(5, profesor.getEstado().toString());
            insercionUsuario.executeUpdate();
            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionProfesor = conexionBaseDeDatos.prepareStatement(consultaProfesor);
            insercionProfesor.setString(1, profesor.getNumeroDePersonalProfesor());
            insercionProfesor.setString(2, profesor.getTurno().toString());
            insercionProfesor.setInt(3, idUsuarioGenerado);
            insercionProfesor.executeUpdate();
            LOGGER.info("Profesor insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar profesor", e);
            throw new UsuariosExcepcion("Error al insertar profesor",e);
        } finally {
            try {
                if (insercionProfesor != null){
                    insercionProfesor.close();
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
