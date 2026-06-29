package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.EntregaDocumentacion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.EntregaDocumentacionDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntregaDocumentacionDao implements EntregaDocumentacionDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(EntregaDocumentacionDao.class.getName());

    @Override
    public int agregarEntrega(EntregaDocumentacion entrega) throws UsuariosExcepcion {
        if (entrega.getFechaEntrega() == null) {
            throw new UsuariosExcepcion("La fecha de entrega no puede ser nula");
        }
        if (entrega.getEntregaMatricula() == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        String consulta = "INSERT INTO EntregaDocumentacion (fechaEntrega, entregaMatricula, entregaDocumentacion) VALUES (?, ?, ?)";
        Connection conexion = null;
        PreparedStatement insercion = null;
        int filasAfectadas = 0;
        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexion.prepareStatement(consulta);
            insercion.setDate(1, entrega.getFechaEntrega());
            insercion.setString(2, entrega.getEntregaMatricula());
            insercion.setInt(3, entrega.getEntregaDocumentacion());
            filasAfectadas = insercion.executeUpdate();
            LOGGER.info("EntregaDocumentacion insertada correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar entrega documentacion", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al agregar entrega documentacion");
        } finally {
            try {
                if (insercion != null) {
                    insercion.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }
}