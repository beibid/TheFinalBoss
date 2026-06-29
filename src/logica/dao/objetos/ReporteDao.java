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

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(ReporteDao.class.getName());

    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

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
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar el reporte", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al agregar el reporte", excepcionSql);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null) insertarEnBaseDeDatos.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
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
                        resultado.getString("actividades"),
                        matricula, null, null
                );
                reporte.setIdReporte(resultado.getInt("idReporte"));
                reporte.setFechaGeneracion(resultado.getDate("fechaGeneracion"));
                reportes.add(reporte);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener reportes", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener reportes", excepcionSql);
        } finally {
            try {
                if (consultaReportes != null) consultaReportes.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
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
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al evaluar reporte", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al evaluar reporte", excepcionSql);
        } finally {
            try {
                if (actualizacion != null) actualizacion.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public List<Reporte> obtenerReportesPorMatricula(String matricula) throws MensajeriaExcepcion {
        String consulta = "SELECT idReporte, tipoReporte, descripcion, fechaGeneracion, calificacion, " +
                "observacionesProf, estado FROM reporte WHERE matricula = ? ORDER BY fechaGeneracion DESC";
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
                        null,
                        matricula, null, null
                );
                reporte.setIdReporte(resultado.getInt("idReporte"));
                reporte.setFechaGeneracion(resultado.getDate("fechaGeneracion"));
                reporte.setCalificacion(resultado.getDouble("calificacion"));
                reporte.setObservaciones(resultado.getString("observacionesProf"));
                reportes.add(reporte);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener reportes por matricula", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener reportes por matricula", excepcionSql);
        } finally {
            try {
                if (consultaReportes != null) consultaReportes.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return reportes;
    }

    public int contarReportesEvaluados(String matricula, String tipoReporte) throws MensajeriaExcepcion {
        String consulta = "SELECT COUNT(*) FROM reporte " +
                "WHERE matricula = ? AND tipoReporte = ? AND estado = 'Evaluado'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        int totalReportes = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            sentencia.setString(2, tipoReporte);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                totalReportes = resultado.getInt(1);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al contar reportes evaluados", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al contar reportes evaluados", excepcionSql);
        } finally {
            try {
                if (sentencia != null) sentencia.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return totalReportes;
    }

    public boolean existeReporteMensualEnMesActual(String matricula) throws MensajeriaExcepcion {
        String consulta = "SELECT COUNT(*) FROM reporte " +
                "WHERE matricula = ? AND tipoReporte = 'Mensual' " +
                "AND MONTH(fechaGeneracion) = MONTH(NOW()) " +
                "AND YEAR(fechaGeneracion) = YEAR(NOW())";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        boolean existeReporte = false;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                existeReporte = resultado.getInt(1) > 0;
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al verificar reporte mensual", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al verificar reporte mensual", excepcionSql);
        } finally {
            try {
                if (sentencia != null) sentencia.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return existeReporte;
    }
    public boolean existeReporteMensualEnMes(String matricula, int mes, int anio) throws MensajeriaExcepcion {
        String consulta = "SELECT COUNT(*) FROM reporte " +
                "WHERE matricula = ? AND tipoReporte = 'Mensual' " +
                "AND MONTH(fechaGeneracion) = ? " +
                "AND YEAR(fechaGeneracion) = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        boolean existeReporte = false;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            sentencia.setInt(2, mes);
            sentencia.setInt(3, anio);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                existeReporte = resultado.getInt(1) > 0;
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al verificar reporte mensual por mes", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al verificar reporte mensual", excepcionSql);
        } finally {
            try {
                if (sentencia != null) sentencia.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return existeReporte;
    }
}