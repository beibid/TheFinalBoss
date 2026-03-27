package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.Practicante;
import logica.dao.interfaces.PracticanteDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PracticanteDao implements PracticanteDaoInterfaz{
    @Override
    public void insertarPracticante (Practicante practicante) {
        String queryPracticante = "insert into Practicante (matricula, genero, lenguaIndigena) values (?, ?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryPracticante); {
                insertarEnBaseDeDatos.setString(1, practicante.getMatricula());
                insertarEnBaseDeDatos.setString(2, practicante.getGenero().toString());
                insertarEnBaseDeDatos.setString(3, practicante.getLenguaIndigena());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            System.out.println("No fue posible añadir los datos");
            e.printStackTrace();
        }
    }

}
