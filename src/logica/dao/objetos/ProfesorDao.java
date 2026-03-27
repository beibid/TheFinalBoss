package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.Profesor;
import logica.dao.excepciones.InserccionUsuarioExcepcion;
import logica.dao.interfaces.ProfesorDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProfesorDao implements ProfesorDaoInterfaz{
    @Override
    public void insertarProfesor (Profesor profesor) throws InserccionUsuarioExcepcion {
        String queryProfesor = "insert into Profesor (numPersonalProfesor, turno) values (?, ?)";
        try {
            Connection conexionBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insercionBaseDeDatos = conexionBaseDeDatos.prepareStatement(queryProfesor); {
                insercionBaseDeDatos.setString(1, profesor.getNumeroDePersonalProfesor());
                insercionBaseDeDatos.setString(2, profesor.getTurno().toString());
                insercionBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new InserccionUsuarioExcepcion("Error al insertar profesor", e);
        }
    }
}
