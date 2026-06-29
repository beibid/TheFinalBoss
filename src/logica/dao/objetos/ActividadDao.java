package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.ActividadDaoInterfaz;
import logica.dominio.Actividad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActividadDao implements ActividadDaoInterfaz {

    private static final Logger LOGGER = Logger.getLogger(ActividadDao.class.getName());

    @Override
    public int registrarActividad(Actividad actividad) throws MensajeriaExcepcion {
        if (actividad.getMatriculaPracticante() == null) {
            throw new MensajeriaExcepcion("La matricula no puede ser nula");
        }
        if (actividad.getTitulo() == null) {
            throw new MensajeriaExcepcion("El titulo de la actividad no puede ser nulo");
        }
        String consulta = "INSERT INTO actividad (titulo, descripcion, fechaInicio, fechaFin, horasActividad, matricula) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consulta);
            insertarEnBaseDeDatos.setString(1, actividad.getTitulo());
            insertarEnBaseDeDatos.setString(2, actividad.getDescripcion());
            insertarEnBaseDeDatos.setDate(3, actividad.getFechaInicio());
            insertarEnBaseDeDatos.setDate(4, actividad.getFechaFin());
            insertarEnBaseDeDatos.setInt(5, actividad.getHorasActividad());
            insertarEnBaseDeDatos.setString(6, actividad.getMatriculaPracticante());
            filasAfectadas = insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Actividad registrada correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al registrar la actividad", excepcionSql);
            throw new MensajeriaExcepcion("Error al registrar la actividad", excepcionSql);
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

    @Override
    public List<Actividad> obtenerActividadesPorPracticante(String matricula) throws MensajeriaExcepcion {
        String consulta = "SELECT idActividad, titulo, descripcion, fechaInicio, fechaFin, horasActividad " +
                "FROM actividad WHERE matricula = ? ORDER BY fechaInicio DESC";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaActividades = null;
        List<Actividad> actividades = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaActividades = conexionBaseDeDatos.prepareStatement(consulta);
            consultaActividades.setString(1, matricula);
            ResultSet resultado = consultaActividades.executeQuery();
            while (resultado.next()) {
                Actividad actividad = new Actividad();
                actividad.setIdActividad(resultado.getInt("idActividad"));
                actividad.setTitulo(resultado.getString("titulo"));
                actividad.setDescripcion(resultado.getString("descripcion"));
                actividad.setFechaInicio(resultado.getDate("fechaInicio"));
                actividad.setFechaFin(resultado.getDate("fechaFin"));
                actividad.setHorasActividad(resultado.getInt("horasActividad"));
                actividad.setMatriculaPracticante(matricula);
                actividades.add(actividad);
            }
            LOGGER.info("Actividades obtenidas correctamente para la matricula: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener actividades por practicante", excepcionSql);
            throw new MensajeriaExcepcion("Error al obtener actividades por practicante", excepcionSql);
        } finally {
            try {
                if (consultaActividades != null) {
                    consultaActividades.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return actividades;
    }

    @Override
    public List<Actividad> obtenerActividadesPorPracticanteYMes(String matricula, int mes, int anio) throws MensajeriaExcepcion {
        String consulta = "SELECT idActividad, titulo, descripcion, fechaInicio, fechaFin, horasActividad " +
                "FROM actividad WHERE matricula = ? " +
                "AND MONTH(fechaInicio) = ? AND YEAR(fechaInicio) = ? " +
                "ORDER BY fechaInicio ASC";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        List<Actividad> actividades = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            sentencia.setInt(2, mes);
            sentencia.setInt(3, anio);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Actividad actividad = new Actividad();
                actividad.setIdActividad(resultado.getInt("idActividad"));
                actividad.setTitulo(resultado.getString("titulo"));
                actividad.setDescripcion(resultado.getString("descripcion"));
                actividad.setFechaInicio(resultado.getDate("fechaInicio"));
                actividad.setFechaFin(resultado.getDate("fechaFin"));
                actividad.setHorasActividad(resultado.getInt("horasActividad"));
                actividad.setMatriculaPracticante(matricula);
                actividades.add(actividad);
            }
            LOGGER.info("Actividades del mes " + mes + "/" + anio + " obtenidas para: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener actividades por mes", excepcionSql);
            throw new MensajeriaExcepcion("Error al obtener actividades por mes", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return actividades;
    }

    @Override
    public int obtenerHorasTotalesPorPracticante(String matricula) throws MensajeriaExcepcion {
        String consulta = "SELECT COALESCE(SUM(horasActividad), 0) FROM actividad WHERE matricula = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        int horasTotales = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                horasTotales = resultado.getInt(1);
            }
            LOGGER.info("Horas totales obtenidas correctamente para la matricula: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener horas totales", excepcionSql);
            throw new MensajeriaExcepcion("Error al obtener horas totales", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return horasTotales;
    }
}