package logica.dao.objetos;
import acceso.bd.ConexionBaseDeDatos;
import logica.dto.OrganizacionVinculada;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.OrganizacionVinculadaDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrganizacionVinculadaDao implements OrganizacionVinculadaDaoInterfaz{
    private static final Logger LOGGER = Logger.getLogger(OrganizacionVinculadaDao.class.getName());
    @Override
    public void insertarOrganizacionVinculada (OrganizacionVinculada organizacionVinculada) throws InserccionBaseDeDatosExcepcion {
        String queryPracticante = "insert into organizacion_vinculada (nombre, direccion) values (?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConexionBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryPracticante); {
                insertarEnBaseDeDatos.setString(1, organizacionVinculada.getNombre());
                insertarEnBaseDeDatos.setString(2, organizacionVinculada.getDireccion());
                insertarEnBaseDeDatos.executeUpdate();

                LOGGER.info("Organización vinculada insertada correctamente");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar la organización vinculada", e);
            throw new InserccionBaseDeDatosExcepcion("Error al insertar la organizacion");
        }
    }
}
