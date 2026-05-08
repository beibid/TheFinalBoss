package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.DocumentacionPracticante;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.DocumentacionPracticanteDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentacionPracticanteDao implements DocumentacionPracticanteDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(DocumentacionPracticanteDao.class.getName());

    @Override
    public int agregarDocumentacion(DocumentacionPracticante documentacion) throws UsuariosExcepcion {
        if (documentacion.getEstadoRevision() == null) {
            throw new UsuariosExcepcion("El estado de revision no puede ser nulo");
        }
        if (documentacion.getRutaDeArchivo() == null) {
            throw new UsuariosExcepcion("La ruta del archivo no puede ser nula");
        }
        String consulta = "INSERT INTO DocumentacionPracticante (rutaDeArchivo, estadoRevision) VALUES (?, ?)";
        Connection conexion = null;
        PreparedStatement insercion = null;
        int idGenerado = -1;

        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexion.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            insercion.setString(1, documentacion.getRutaDeArchivo());
            insercion.setString(2, documentacion.getEstadoRevision().toString());
            insercion.executeUpdate();

            ResultSet tomarLlave = insercion.getGeneratedKeys();
            if (tomarLlave.next()) {
                idGenerado = tomarLlave.getInt(1);
                LOGGER.info("DocumentacionPracticante insertada correctamente con ID: " + idGenerado);
            }
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al insertar documentacion", excepcionSQL);
            throw new UsuariosExcepcion("Error al agregar documentacion");
        } finally {
            try {
                if (insercion != null) {
                    insercion.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return idGenerado;
    }
}