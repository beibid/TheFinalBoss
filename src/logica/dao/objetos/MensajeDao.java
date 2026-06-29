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

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(MensajeDao.class.getName());

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Agrega un mensaje a la base de datos.
     * @param mensaje el mensaje a agregar
     * @return el ID generado del mensaje insertado
     * @throws MensajeriaExcepcion si ocurre un error al agregar o de conexion
     */
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
            if (esErrorDeConexion(excepcionSql)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al agregar mensaje", excepcionSql);
        } finally {
            try {
                if (insercion != null){
                    insercion.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return idGenerado;
    }

    /**
     * Obtiene los mensajes recibidos de un usuario.
     * @param idUsuario el ID del usuario destinatario
     * @return lista de mensajes recibidos por el usuario
     * @throws MensajeriaExcepcion si ocurre un error al consultar o de conexion
     */
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
            if (esErrorDeConexion(excepcionSQL)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener mensajes recibidos", excepcionSQL);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSQL);
            }
        }
        return mensajes;
    }

    /**
     * Obtiene los mensajes enviados por un usuario.
     * @param idUsuario el ID del usuario remitente
     * @return lista de mensajes enviados por el usuario
     * @throws MensajeriaExcepcion si ocurre un error al consultar o de conexion
     */
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
            if (esErrorDeConexion(excepcionSQL)) {
                throw new MensajeriaExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new MensajeriaExcepcion("Error al obtener mensajes enviados", excepcionSQL);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSQL);
            }
        }
        return mensajes;
    }
}