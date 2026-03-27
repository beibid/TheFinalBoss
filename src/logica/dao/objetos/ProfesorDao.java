package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.Profesor;
import logica.dao.excepciones.DaoExcepcion;
import logica.dao.interfaces.ProfesorDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProfesorDao implements ProfesorDaoInterfaz{
    @Override
    public void insertarProfesor (Profesor profesor) throws DaoExcepcion {
        String queryProfesor = "insert into Profesor (numPersonalProfesor, turno) values (?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryProfesor); {
                insertarEnBaseDeDatos.setString(1, profesor.getNumeroDePersonalProfesor());
                insertarEnBaseDeDatos.setString(2, profesor.getTurno().toString());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new DaoExcepcion("Error al insertar profesor", e);
        }
    }
}
