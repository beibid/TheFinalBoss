package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Reporte;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.ReporteDaoInterfaz;
import logica.dominio.enums.TipoReporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        String consultaReporte = "INSERT INTO reporte (tipoReporte, descripcion, matricula, archivoAdjunto, nombreArchivo) " +
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
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al insertar el reporte", excepcionSQL);
            throw new MensajeriaExcepcion("Error al agregar el reporte", excepcionSQL);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null) {
                    insertarEnBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return filasAfectadas;
    }

    public List<Reporte> obtenerReportesPorPracticante(String matricula) throws MensajeriaExcepcion {
        String consulta = "SELECT idReporte, tipoReporte, descripcion, fechaGeneracion, estado " +
                "FROM reporte WHERE matricula = ? AND estado = 'Pendiente'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaReportes = null;
        List<Reporte> reportes = new ArrayList<>();

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaReportes = conexionBaseDeDatos.prepareStatement(consulta);
            consultaReportes.setString(1, matricula);
            ResultSet resultado = consultaReportes.executeQuery();
            while (resultado.next()) {
                Reporte reporte = new Reporte(
                        TipoReporte.valueOf(resultado.getString("tipoReporte")),
                        resultado.getString("descripcion"),
                        matricula, null, null
                );
                reporte.setIdReporte(resultado.getInt("idReporte"));
                reporte.setFechaGeneracion(resultado.getDate("fechaGeneracion"));
                reportes.add(reporte);
            }
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al obtener reportes", excepcionSQL);
            throw new MensajeriaExcepcion("Error al obtener reportes", excepcionSQL);
        } finally {
            try {
                if (consultaReportes != null) {
                    consultaReportes.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return reportes;
    }

    public int evaluarReporte(int idReporte, double calificacion, String observaciones, String numPersonalProfesor) throws MensajeriaExcepcion {
        String consulta = "UPDATE reporte SET calificacion = ?, observacionesProf = ?, " +
                "numPersonalProfesor = ?, estado = 'Evaluado' WHERE idReporte = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setDouble(1, calificacion);
            actualizacion.setString(2, observaciones);
            actualizacion.setString(3, numPersonalProfesor);
            actualizacion.setInt(4, idReporte);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Reporte evaluado correctamente: " + idReporte);
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al evaluar reporte", excepcionSQL);
            throw new MensajeriaExcepcion("Error al evaluar reporte", excepcionSQL);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return filasAfectadas;
    }
}