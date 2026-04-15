package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.DocumentacionPracticante;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.DocumentacionPracticanteDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentacionPracticanteDao implements DocumentacionPracticanteDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(DocumentacionPracticanteDao.class.getName());

    @Override
    public void agregarDocumentacion(DocumentacionPracticante documentacion) throws UsuariosExcepcion {
        String consulta = "INSERT INTO DocumentacionPracticante (rutaDeArchivo, estadoRevision) VALUES (?, ?)";
        Connection conexion = null;
        PreparedStatement insercion = null;
        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexion.prepareStatement(consulta);
            insercion.setString(1, documentacion.getRutaDeArchivo());
            insercion.setString(2, documentacion.getEstadoRevision().toString());
            insercion.executeUpdate();
            LOGGER.info("DocumentacionPracticante insertada correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar documentacion", e);
            throw new UsuariosExcepcion("Error al agregar documentacion");
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