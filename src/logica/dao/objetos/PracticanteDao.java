package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.dominio.Practicante;
import logica.dao.excepciones.InserccionUsuarioExcepcion;
import logica.dao.interfaces.PracticanteDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PracticanteDao implements PracticanteDaoInterfaz{
    @Override
    public void insertarPracticante (Practicante practicante) throws InserccionUsuarioExcepcion {
        String queryPracticante = "insert into Practicante (matricula, genero, lenguaIndigena, idUsuario) values (?, ?, ?,?)";
        try {
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryPracticante); {
                insertarEnBaseDeDatos.setString(1, practicante.getMatricula());
                insertarEnBaseDeDatos.setString(2, practicante.getGenero().toString());
                insertarEnBaseDeDatos.setString(3, practicante.getLenguaIndigena());
                insertarEnBaseDeDatos.setInt(4, practicante.getIdUsuarioPracticante());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new InserccionUsuarioExcepcion("Error al insertar practicante", e);
        }
    }

}
