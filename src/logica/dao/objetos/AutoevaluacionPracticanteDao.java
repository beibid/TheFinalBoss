package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dao.interfaces.AutoevaluacionPracticanteDaoInterfaz;
import logica.dominio.AutoevaluacionPracticante;
import logica.dao.excepciones.MensajeriaExcepcion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AutoevaluacionPracticanteDao implements AutoevaluacionPracticanteDaoInterfaz {


    private static final Logger LOGGER = Logger.getLogger(AutoevaluacionPracticanteDao.class.getName());

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
        } catch (SQLException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar la autoevaluación", excepcion);
            throw new MensajeriaExcepcion("Error al registrar la autoevaluación", excepcion);
        } finally {
            try {
                if (registro != null) {
                    registro.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcion) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcion);
            }
        }
        return filasAfectadas;
    }

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
                informacionParaAutoevaluacion = new AutoevaluacionPracticante(matricula, resultado.getInt("idProyecto"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                informacionParaAutoevaluacion.setNombreProyecto(resultado.getString("nombreProyecto"));
                informacionParaAutoevaluacion.setResponsableDelProyecto(resultado.getString("responsableDelProyecto"));
                informacionParaAutoevaluacion.setNombreOrganizacion(resultado.getString("nombreOrganizacion"));
            }
            LOGGER.info(sentencia.getString(2));
        } catch (SQLException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al obtener info de autoevaluación", excepcion);
            throw new MensajeriaExcepcion("Error al obtener info de autoevaluación", excepcion);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcion) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcion);
            }
        }
        return informacionParaAutoevaluacion;
    }
}
