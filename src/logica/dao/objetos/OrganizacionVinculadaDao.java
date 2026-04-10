package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.OrganizacionVinculada;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.OrganizacionVinculadaDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class OrganizacionVinculadaDao implements OrganizacionVinculadaDaoInterfaz{

    @Override

    public void insertarOrganizacionVinculada (OrganizacionVinculada organizacionVinculada) throws InserccionBaseDeDatosExcepcion {
        String consultaOrganizacion = "insert into organizacion_vinculada (nombre, direccion) values (?, ?)";
        try {
            Connection conexionBaseDeDatos = ConexionBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaOrganizacion); {
                insertarEnBaseDeDatos.setString(1, organizacionVinculada.getNombre());
                insertarEnBaseDeDatos.setString(2, organizacionVinculada.getDireccion());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new InserccionBaseDeDatosExcepcion("Error al insertar la organizacion");
        }
    }
}
