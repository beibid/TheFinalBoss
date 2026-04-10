package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Proyecto;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.ProyectoDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProyectoDao implements ProyectoDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(ProyectoDao.class.getName());

    @Override
    public void agregarProyecto(Proyecto proyecto) throws InserccionBaseDeDatosExcepcion {
        String consultaProyecto = "INSERT INTO proyecto (nombreProyecto, descripcion, responsableDelProyecto, estado, nombreEmpresa, sectorEmpresa, direccionEmpresa, idOrganizacion, matricula, numPersonalCoordinador, fechaRegistro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conexionBaseDeDatos = ConexionBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaProyecto); {
                insertarEnBaseDeDatos.setString(1, proyecto.getNombreProyecto());
                insertarEnBaseDeDatos.setString(2, proyecto.getDescripcion());
                insertarEnBaseDeDatos.setString(3, proyecto.getResponsableDelProyecto());
                insertarEnBaseDeDatos.setString(4, proyecto.getEstado().toString().replace("_", " "));
                insertarEnBaseDeDatos.setString(5, proyecto.getNombreEmpresa());
                insertarEnBaseDeDatos.setString(6, proyecto.getSectorEmpresa());
                insertarEnBaseDeDatos.setString(7, proyecto.getDireccionEmpresa());
                insertarEnBaseDeDatos.setInt(8, proyecto.getIdOrganizacion());
                insertarEnBaseDeDatos.setString(9, proyecto.getMatricula());
                insertarEnBaseDeDatos.setString(10, proyecto.getNumPersonalCoordinador());
                insertarEnBaseDeDatos.setDate(11, proyecto.getFechaRegistro());
                insertarEnBaseDeDatos.executeUpdate();

                LOGGER.info("Proyecto insertado correctamente ");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar proyecto", e);
            throw new InserccionBaseDeDatosExcepcion("Error al agregar el proyecto");
        }
    }
}
