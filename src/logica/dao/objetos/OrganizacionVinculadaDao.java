package logica.dao.objetos;
import acceso.bd.ConectarBaseDeDatos;
import logica.OrganizacionVinculada;
import logica.dao.excepciones.DaoExcepcion;
import logica.dao.interfaces.OrganizacionVinculadaDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrganizacionVinculadaDao implements OrganizacionVinculadaDaoInterfaz{
    @Override
    public void insertarOrganizacionVinculada (OrganizacionVinculada organizacionVinculada) throws DaoExcepcion {
        String queryPracticante = "insert into organizacion_vinculada (nombre, direccion) values (?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryPracticante); {
                insertarEnBaseDeDatos.setString(1, organizacionVinculada.getNombre());
                insertarEnBaseDeDatos.setString(2, organizacionVinculada.getDireccion());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new DaoExcepcion("Error al insertar la organizacion", e);
        }
    }
}
