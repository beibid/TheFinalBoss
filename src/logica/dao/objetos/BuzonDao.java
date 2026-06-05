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
    private static final Logger LOGGER = Logger.getLogger(BuzonDao.class.getName());

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
            LOGGER.log(Level.SEVERE, "Error al insertar buzon", excepcionSql);
            throw new MensajeriaExcepcion("Error al agregar buzon", excepcionSql);
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