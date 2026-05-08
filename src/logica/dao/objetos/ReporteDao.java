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
        if (reporte.getMatriculaPracticante() == null) {
            throw new MensajeriaExcepcion("La matricula no puede ser nula");
        }
        if (reporte.getTipoReporte() == null) {
            throw new MensajeriaExcepcion("El tipo de reporte no puede ser nulo");
        }

        String consultaReporte = "INSERT INTO Reporte (tipoReporte, descripcion, matricula, archivoAdjunto, nombreArchivo) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaReporte);
            insertarEnBaseDeDatos.setString(1, reporte.getTipoReporte().toString());
            insertarEnBaseDeDatos.setString(2, reporte.getDescripcion());
            insertarEnBaseDeDatos.setString(3, reporte.getMatriculaPracticante());
            insertarEnBaseDeDatos.setString(4, reporte.getArchivoAdjunto());
            insertarEnBaseDeDatos.setString(5, reporte.getNombreArchivo());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Reporte insertado correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar el reporte", e);
            throw new MensajeriaExcepcion("Error al agregar el reporte", e);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null) insertarEnBaseDeDatos.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }
}