package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.interfaces.AutoevaluacionPracticanteDaoInterfaz;
import logica.dominio.AutoevaluacionPracticante;
import logica.dao.excepciones.MensajeriaExcepcion;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoevaluacionPracticanteDao implements AutoevaluacionPracticanteDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final String MENSAJE_EXITO = "Autoevaluación registrada correctamente";
    private static final Logger LOGGER = Logger.getLogger(AutoevaluacionPracticanteDao.class.getName());

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Registra la autoevaluacion de un practicante mediante un procedimiento almacenado.
     * @param autoevaluacion la autoevaluacion con las respuestas del practicante
     * @return 1 si el registro fue exitoso, 0 en caso contrario
     * @throws MensajeriaExcepcion si ocurre un error al registrar o de conexion
     */
    @Override
    public int registrarAutoevaluacion(AutoevaluacionPracticante autoevaluacion) throws MensajeriaExcepcion {
        String consultaProcedimiento = "{CALL RegistrarAutoevaluacion(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conexionBaseDeDatos = null;
        CallableStatement registro = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            registro = conexionBaseDeDatos.prepareCall(consultaProcedimiento);
            registro.setString(1, autoevaluacion.getMatriculaPracticante());
            registro.setInt(2, autoevaluacion.getIdProyecto());
            registro.setInt(3, autoevaluacion.getRespuesta1());
            registro.setInt(4, autoevaluacion.getRespuesta2());
            registro.setInt(5, autoevaluacion.getRespuesta3());
            registro.setInt(6, autoevaluacion.getRespuesta4());
            registro.setInt(7, autoevaluacion.getRespuesta5());
            registro.setInt(8, autoevaluacion.getRespuesta6());
            registro.setInt(9, autoevaluacion.getRespuesta7());
            registro.setInt(10, autoevaluacion.getRespuesta8());
            registro.setInt(11, autoevaluacion.getRespuesta9());
            registro.setInt(12, autoevaluacion.getRespuesta10());
            registro.registerOutParameter(13, Types.VARCHAR);
            registro.execute();
            String mensaje = registro.getString(13);
            LOGGER.info(mensaje);
            if (mensaje != null && mensaje.equals(MENSAJE_EXITO)) {
                filasAfectadas = 1;
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error en base de datos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion(
                        "No se pudo conectar al servidor. Verifique que la base de datos este encendida"
                );
            }
            throw new MensajeriaExcepcion(
                    "Error inesperado, no fue posible registrar la autoevaluacion", excepcionSql
            );
        } finally {
            try {
                if (registro != null) {
                    registro.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Obtiene la informacion necesaria para mostrar en la vista de autoevaluacion
     * mediante un procedimiento almacenado.
     * @param matricula la matricula del practicante
     * @return objeto con la informacion del proyecto asignado al practicante,
     *         o null si no se encontro informacion
     * @throws MensajeriaExcepcion si ocurre un error al obtener la informacion o de conexion
     */
    @Override
    public AutoevaluacionPracticante obtenerInfoAutoevaluacion(String matricula) throws MensajeriaExcepcion {
        String consultaProcedimiento = "{CALL ObtenerInfoAutoevaluacion(?, ?)}";
        Connection conexionBaseDeDatos = null;
        CallableStatement sentencia = null;
        AutoevaluacionPracticante informacionParaAutoevaluacion = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareCall(consultaProcedimiento);
            sentencia.setString(1, matricula);
            sentencia.registerOutParameter(2, Types.VARCHAR);
            sentencia.execute();
            ResultSet resultado = sentencia.getResultSet();
            if (resultado != null && resultado.next()) {
                informacionParaAutoevaluacion = new AutoevaluacionPracticante(
                        matricula,
                        resultado.getInt("idProyecto"),
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                );
                informacionParaAutoevaluacion.setNombreProyecto(resultado.getString("nombreProyecto"));
                informacionParaAutoevaluacion.setResponsableDelProyecto(resultado.getString("responsableDelProyecto"));
                informacionParaAutoevaluacion.setNombreOrganizacion(resultado.getString("nombreOrganizacion"));
            }
            LOGGER.info(sentencia.getString(2));
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error en base de datos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion(
                        "No se pudo conectar al servidor. Verifique que la base de datos este encendida"
                );
            }
            throw new MensajeriaExcepcion(
                    "Error inesperado, no fue posible obtener informacion", excepcionSql
            );
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return informacionParaAutoevaluacion;
    }

    /**
     * Obtiene la autoevaluacion registrada de un practicante.
     * @param matricula la matricula del practicante
     * @return la autoevaluacion con todas sus respuestas si existe,
     *         null si el practicante aun no ha entregado su autoevaluacion
     * @throws MensajeriaExcepcion si ocurre un error al obtener la autoevaluacion o de conexion
     */
    public AutoevaluacionPracticante obtenerAutoevaluacion(String matricula) throws MensajeriaExcepcion {
        String consulta = "SELECT idAutoevaluacion, idProyecto, respuesta1, respuesta2, respuesta3, " +
                "respuesta4, respuesta5, respuesta6, respuesta7, respuesta8, respuesta9, respuesta10 " +
                "FROM autoevaluacion WHERE matricula = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        AutoevaluacionPracticante autoevaluacion = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                autoevaluacion = new AutoevaluacionPracticante(
                        matricula,
                        resultado.getInt("idProyecto"),
                        resultado.getInt("respuesta1"),
                        resultado.getInt("respuesta2"),
                        resultado.getInt("respuesta3"),
                        resultado.getInt("respuesta4"),
                        resultado.getInt("respuesta5"),
                        resultado.getInt("respuesta6"),
                        resultado.getInt("respuesta7"),
                        resultado.getInt("respuesta8"),
                        resultado.getInt("respuesta9"),
                        resultado.getInt("respuesta10")
                );
                autoevaluacion.setIdAutoevaluacion(resultado.getInt("idAutoevaluacion"));
            }
            LOGGER.info("Autoevaluación obtenida para: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error en base de datos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion(
                        "No se pudo conectar al servidor. Verifique que la base de datos este encendida"
                );
            }
            throw new MensajeriaExcepcion(
                    "Error inesperado, no fue posible obtener la autoevaluacion", excepcionSql
            );
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return autoevaluacion;
    }
}