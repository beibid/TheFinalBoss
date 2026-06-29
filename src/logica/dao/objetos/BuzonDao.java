package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Buzon;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.BuzonDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuzonDao implements BuzonDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(BuzonDao.class.getName());

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Agrega un buzon a la base de datos.
     * @param buzon el buzon a agregar
     * @return el numero de filas afectadas
     * @throws MensajeriaExcepcion si ocurre un error al agregar o de conexion
     */
    @Override
    public int agregarBuzon(Buzon buzon) throws MensajeriaExcepcion {
        String consultaBuzon = "INSERT INTO buzon (rolMensaje, idMensaje, idUsuario) VALUES (?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexionBaseDeDatos.prepareStatement(consultaBuzon);
            insercion.setString(1, buzon.getRolMensaje().toString());
            insercion.setInt(2, buzon.getIdMensaje());
            insercion.setInt(3, buzon.getIdUsuario());
            filasAfectadas = insercion.executeUpdate();
            LOGGER.info("Buzon insertado correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error en base de datos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error inesperado, no fue posible agregar buzon", excepcionSql);
        } finally {
            try {
                if (insercion != null) {
                    insercion.close();
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
}