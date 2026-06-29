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

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Agrega un nuevo reporte en la base de datos.
     * @param reporte el reporte a insertar
     * @return el numero de filas afectadas
     * @throws MensajeriaExcepcion si ocurre un error al insertar o de conexion
     */
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
                if (insertarEnBaseDeDatos != null) {
                    insertarEnBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Obtiene los reportes pendientes de un practicante segun su matricula.
     * @param matricula la matricula del practicante
     * @return lista de reportes pendientes
     * @throws MensajeriaExcepcion si ocurre un error al consultar o de conexion
     */
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
                if (consultaReportes != null) {
                    consultaReportes.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return reportes;
    }

    /**
     * Registra la evaluacion de un reporte por parte del profesor.
     * @param idReporte el ID del reporte a evaluar
     * @param calificacion la calificacion asignada
     * @param observaciones las observaciones del profesor
     * @param numPersonalProfesor el numero de personal del profesor evaluador
     * @return el numero de filas afectadas
     * @throws MensajeriaExcepcion si ocurre un error al evaluar o de conexion
     */
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
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Obtiene todos los reportes de un practicante ordenados por fecha de generacion.
     * @param matricula la matricula del practicante
     * @return lista de reportes del practicante
     * @throws MensajeriaExcepcion si ocurre un error al consultar o de conexion
     */
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
                if (consultaReportes != null) {
                    consultaReportes.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return reportes;
    }

    /**
     * Cuenta los reportes evaluados de un tipo especifico para un practicante.
     * @param matricula la matricula del practicante
     * @param tipoReporte el tipo de reporte a contar
     * @return el total de reportes evaluados del tipo indicado
     * @throws MensajeriaExcepcion si ocurre un error al consultar o de conexion
     */
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
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return totalReportes;
    }

    /**
     * Verifica si ya existe un reporte mensual registrado en el mes actual para un practicante.
     * @param matricula la matricula del practicante
     * @return true si ya existe un reporte mensual en el mes actual, false en caso contrario
     * @throws MensajeriaExcepcion si ocurre un error al consultar o de conexion
     */
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
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
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