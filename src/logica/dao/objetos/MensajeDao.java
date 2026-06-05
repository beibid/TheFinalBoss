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
import logica.dominio.MensajeVista;
import java.util.ArrayList;
import java.util.List;
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
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar mensaje", excepcionSql);
            throw new MensajeriaExcepcion("Error al agregar mensaje", excepcionSql);
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
        return idGenerado;
    }

    public List<MensajeVista> obtenerMensajesRecibidos(int idUsuario) throws MensajeriaExcepcion {
        String consulta = "SELECT m.idMensaje, m.contenido, m.fechaEnvio, u.nombre, u.apellidos " +
                "FROM mensaje m " +
                "JOIN buzon bDestinatario ON m.idMensaje = bDestinatario.idMensaje " +
                "JOIN buzon bRemitente ON m.idMensaje = bRemitente.idMensaje " +
                "JOIN usuario u ON bRemitente.idUsuario = u.idUsuario " +
                "WHERE bDestinatario.idUsuario = ? " +
                "AND bDestinatario.rolMensaje = 'Destinatario' " +
                "AND bRemitente.rolMensaje = 'Remitente'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        List<MensajeVista> mensajes = new ArrayList<>();

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setInt(1, idUsuario);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                MensajeVista mensaje = new MensajeVista();
                mensaje.setIdMensaje(resultado.getInt("idMensaje"));
                mensaje.setContenido(resultado.getString("contenido"));
                mensaje.setFechaEnvio(resultado.getDate("fechaEnvio"));
                mensaje.setNombreUsuario(resultado.getString("nombre") + " " + resultado.getString("apellidos"));
                mensajes.add(mensaje);
            }
            LOGGER.info("Mensajes recibidos obtenidos correctamente");
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al obtener mensajes recibidos", excepcionSQL);
            throw new MensajeriaExcepcion("Error al obtener mensajes recibidos", excepcionSQL);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return mensajes;
    }

    public List<MensajeVista> obtenerMensajesEnviados(int idUsuario) throws MensajeriaExcepcion {
        String consulta = "SELECT m.idMensaje, m.contenido, m.fechaEnvio, u.nombre, u.apellidos " +
                "FROM mensaje m " +
                "JOIN buzon bRemitente ON m.idMensaje = bRemitente.idMensaje " +
                "JOIN buzon bDestinatario ON m.idMensaje = bDestinatario.idMensaje " +
                "JOIN usuario u ON bDestinatario.idUsuario = u.idUsuario " +
                "WHERE bRemitente.idUsuario = ? " +
                "AND bRemitente.rolMensaje = 'Remitente' " +
                "AND bDestinatario.rolMensaje = 'Destinatario'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        List<MensajeVista> mensajes = new ArrayList<>();

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setInt(1, idUsuario);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                MensajeVista mensaje = new MensajeVista();
                mensaje.setIdMensaje(resultado.getInt("idMensaje"));
                mensaje.setContenido(resultado.getString("contenido"));
                mensaje.setFechaEnvio(resultado.getDate("fechaEnvio"));
                mensaje.setNombreUsuario(resultado.getString("nombre") + " " + resultado.getString("apellidos"));
                mensajes.add(mensaje);
            }
            LOGGER.info("Mensajes enviados obtenidos correctamente");
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al obtener mensajes enviados", excepcionSQL);
            throw new MensajeriaExcepcion("Error al obtener mensajes enviados", excepcionSQL);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return mensajes;
    }
}