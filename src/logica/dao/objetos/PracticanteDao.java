package logica.dao.objetos;
import acceso.bd.ConexionBaseDeDatos;
import logica.dto.Practicante;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.PracticanteDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticanteDao implements PracticanteDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(PracticanteDao.class.getName());
    @Override
    public void insertarPracticante(Practicante practicante) throws InserccionBaseDeDatosExcepcion {
        String queryUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        String queryPracticante = "insert into Practicante (matricula, lenguaIndigena, genero, idUsuario) values (?, ?, ?, ?)";
        try {
            Connection conexionBaseDeDatos = ConexionBaseDeDatos.conectar();

            PreparedStatement insercionUsuario = conexionBaseDeDatos.prepareStatement(queryUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, practicante.getNombre());
            insercionUsuario.setString(2, practicante.getApellidoPaterno());
            insercionUsuario.setString(3, practicante.getApellidoMaterno());
            insercionUsuario.setString(4, practicante.getContrasena());
            insercionUsuario.setString(5, practicante.getEstado().toString());
            insercionUsuario.executeUpdate();

            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new InserccionBaseDeDatosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);

            PreparedStatement insercionPracticante = conexionBaseDeDatos.prepareStatement(queryPracticante);
            insercionPracticante.setString(1, practicante.getMatricula());
            insercionPracticante.setString(2, practicante.getLenguaIndigena());
            insercionPracticante.setString(3, practicante.getGenero().toString());
            insercionPracticante.setInt(4, idUsuarioGenerado);
            insercionPracticante.executeUpdate();

            LOGGER.info("Practicante insertado correctamente con ID de usuario: + idUsuarioGenerado");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar practicante", e);
            throw new InserccionBaseDeDatosExcepcion("Error al insertar practicante");
        }
    }
}