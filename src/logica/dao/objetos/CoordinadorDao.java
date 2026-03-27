package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.Coordinador;
import logica.dao.excepciones.DaoExcepcion;
import logica.dao.interfaces.CoordinadorDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CoordinadorDao implements CoordinadorDaoInterfaz {
    @Override
    public void insertarCoordinador (Coordinador coordinador) throws DaoExcepcion {
        String queryCoordinador = "insert into Coordinador (numPersonalCoordinador) values (?)";
        try {
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryCoordinador); {
                insertarEnBaseDeDatos.setString(1, coordinador.getNumeroDePersonalCoordinador());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new DaoExcepcion("Error al insertar coordinador", e);
        }
    }
}
