package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Reporte;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.ReporteDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReporteDao implements ReporteDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(ReporteDao.class.getName());

    @Override
    public int agregarReporte(Reporte reporte) throws MensajeriaExcepcion {
        if (reporte.getFechaGeneracion() == null) {
            throw new MensajeriaExcepcion("La fecha de generacion no puede ser nula");
        }
        if (reporte.getMatriculaPracticante() == null) {
            throw new MensajeriaExcepcion("La matricula no puede ser nula");
        }
        String consultaReporte = "insert into reporte (tipoReporte, descripcion, fechaGeneracion, calificacion, observacionesProf, estado, matricula, numPersonalProfesor) values (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaReporte);
            insertarEnBaseDeDatos.setString(1, reporte.getTipoReporte().toString());
            insertarEnBaseDeDatos.setString(2, reporte.getDescripcion());
            insertarEnBaseDeDatos.setDate(3, reporte.getFechaGeneracion());
            insertarEnBaseDeDatos.setDouble(4, reporte.getCalificacion());
            insertarEnBaseDeDatos.setString(5, reporte.getObservaciones());
            insertarEnBaseDeDatos.setString(6, reporte.getEstadoDeCalificacion().toString());
            insertarEnBaseDeDatos.setString(7, reporte.getMatriculaPracticante());
            insertarEnBaseDeDatos.setString(8, reporte.getNumPersonalProfesor());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Reporte insertado correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar el reporte", e);
            throw new MensajeriaExcepcion("Error al agregar el reporte", e);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null) {
                    insertarEnBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }
}