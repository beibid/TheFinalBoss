package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.dominio.Coordinador;
import logica.dao.excepciones.InserccionUsuarioExcepcion;
import logica.dao.interfaces.CoordinadorDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CoordinadorDao implements CoordinadorDaoInterfaz {
    @Override
    public void insertarCoordinador (Coordinador coordinador) throws InserccionUsuarioExcepcion {
        String queryCoordinador = "insert into Coordinador (numPersonalCoordinador) values (?)";
        try {
            Connection conexionBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insercionBaseDeDatos = conexionBaseDeDatos.prepareStatement(queryCoordinador); {
                insercionBaseDeDatos.setString(1, coordinador.getNumeroDePersonalCoordinador());
                insercionBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new InserccionUsuarioExcepcion("Error al insertar coordinador");
        }
    }
}
