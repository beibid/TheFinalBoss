package logica.dao.objetos;
import acceso.bd.ConexionBaseDeDatos;
import logica.dto.Profesor;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
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
    public void insertarProfesor(Profesor profesor) throws InserccionBaseDeDatosExcepcion {
        String queryUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        String queryProfesor = "insert into Profesor (numPersonalProfesor, turno, idUsuario) values (?, ?, ?)";
        try {
            Connection conexionBaseDeDatos = ConexionBaseDeDatos.conectar();

            PreparedStatement insercionUsuario = conexionBaseDeDatos.prepareStatement(queryUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, profesor.getNombre());
            insercionUsuario.setString(2, profesor.getApellidoPaterno());
            insercionUsuario.setString(3, profesor.getApellidoMaterno());
            insercionUsuario.setString(4, profesor.getContrasena());
            insercionUsuario.setString(5, profesor.getEstado().toString());
            insercionUsuario.executeUpdate();

            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new InserccionBaseDeDatosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);

            PreparedStatement insercionProfesor = conexionBaseDeDatos.prepareStatement(queryProfesor);
            insercionProfesor.setString(1, profesor.getNumeroDePersonalProfesor());
            insercionProfesor.setString(2, profesor.getTurno().toString());
            insercionProfesor.setInt(3, idUsuarioGenerado);
            insercionProfesor.executeUpdate();

            LOGGER.info("Profesor insertado correctamente con ID de usuario: + idUsuarioGenerado");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar profesor", e);
            throw new InserccionBaseDeDatosExcepcion("Error al insertar profesor");
        }
    }
}
