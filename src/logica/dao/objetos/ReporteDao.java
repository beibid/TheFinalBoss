package logica.dao.objetos;
import acceso.bd.ConexionBaseDeDatos;
import logica.dto.Reporte;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.ReporteDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReporteDao implements ReporteDaoInterfaz{
    private static final Logger LOGGER = Logger.getLogger(ReporteDao.class.getName());
    @Override
    public void agregarReporte (Reporte reporte) throws InserccionBaseDeDatosExcepcion {
        String queryPracticante = "insert into reporte (tipoReporte, descripcion, fechaGeneracion, calificacion, observacionesProf, estado, matricula, numPersonalProfesor) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConexionBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(queryPracticante); {
                insertarEnBaseDeDatos.setString(1, reporte.getTipoReporte().toString());
                insertarEnBaseDeDatos.setString(2, reporte.getDescripcion());
                insertarEnBaseDeDatos.setDate(3, reporte.getFechaGeneracion());
                insertarEnBaseDeDatos.setDouble(4, reporte.getCalificacion());
                insertarEnBaseDeDatos.setString(5, reporte.getObservaciones());
                insertarEnBaseDeDatos.setString(6, reporte.getEstadoDeCalificacion().toString());
                insertarEnBaseDeDatos.setString(7, reporte.getMatriculaPracticante());
                insertarEnBaseDeDatos.setString(8, reporte.getNumPersonalProfesor());
                insertarEnBaseDeDatos.executeUpdate();

                LOGGER.info("Reporte insertado correctamente ");

            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar el reporte", e);
            throw new InserccionBaseDeDatosExcepcion("Error al agregar el reporte");
        }
    }
}
