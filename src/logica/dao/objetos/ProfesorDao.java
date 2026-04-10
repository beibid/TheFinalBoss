package logica.dao.objetos;
import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Profesor;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.ProfesorDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfesorDao implements ProfesorDaoInterfaz {
    @Override
    public void insertarProfesor(Profesor profesor) throws InserccionBaseDeDatosExcepcion {
        String consultaUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        String consultaProfesor = "insert into Profeso (numPersonalProfesor, turno, idUsuario) values (?, ?, ?)";
        try {
            Connection conexionBaseDeDatos = ConexionBaseDeDatos.conectar();

            PreparedStatement insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
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

            PreparedStatement insercionProfesor = conexionBaseDeDatos.prepareStatement(consultaProfesor);
            insercionProfesor.setString(1, profesor.getNumeroDePersonalProfesor());
            insercionProfesor.setString(2, profesor.getTurno().toString());
            insercionProfesor.setInt(3, idUsuarioGenerado);
            insercionProfesor.executeUpdate();

            System.out.println("Los datos han sido añadidos correctamente");

        } catch (SQLException e) {
            throw new InserccionBaseDeDatosExcepcion("Error al insertar profesor");
        }
    }
}
