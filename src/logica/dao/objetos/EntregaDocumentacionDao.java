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
    private static final Logger LOGGER = Logger.getLogger(EntregaDocumentacionDao.class.getName());

    @Override
    public void agregarEntrega(EntregaDocumentacion entrega) throws UsuariosExcepcion {
        String consulta = "INSERT INTO EntregaDocumentacion (fechaEntrega, entregaMatricula, entregaDocumentacion) VALUES (?, ?, ?)";
        Connection conexion = null;
        PreparedStatement insercion = null;
        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexion.prepareStatement(consulta);
            insercion.setDate(1, entrega.getFechaEntrega());
            insercion.setString(2, entrega.getEntregaMatricula());
            insercion.setInt(3, entrega.getEntregaDocumentacion());
            insercion.executeUpdate();
            LOGGER.info("EntregaDocumentacion insertada correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar entrega documentacion", e);
            throw new UsuariosExcepcion("Error al agregar entrega documentacion");
        } finally {
            try {
                if (insercion != null) {
                    insercion.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
    }
}