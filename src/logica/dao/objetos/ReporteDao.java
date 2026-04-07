package logica.dao.objetos;
import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Reporte;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.ReporteDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReporteDao implements ReporteDaoInterfaz{
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

                System.out.println("Los datos han sido añadidos correctamente");

            }
        } catch (SQLException e) {
            throw new InserccionBaseDeDatosExcepcion("Error al agregar el reporte");
        }
    }
}
