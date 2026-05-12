package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Mensaje;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.MensajeDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MensajeDao implements MensajeDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(MensajeDao.class.getName());

    @Override
    public int agregarMensaje(Mensaje mensaje) throws MensajeriaExcepcion {
        String consultaMensaje = "INSERT INTO mensaje (contenido) VALUES (?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercion = null;
        int idGenerado = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexionBaseDeDatos.prepareStatement(consultaMensaje, Statement.RETURN_GENERATED_KEYS);
            insercion.setString(1, mensaje.getContenido());
            insercion.executeUpdate();
            ResultSet llave = insercion.getGeneratedKeys();
            if (llave.next()) {
                idGenerado = llave.getInt(1);
            }
            LOGGER.info("Mensaje insertado correctamente con id: " + idGenerado);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar mensaje", e);
            throw new MensajeriaExcepcion("Error al agregar mensaje", e);
        } finally {
            try {
                if (insercion != null) insercion.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return idGenerado;
    }
}