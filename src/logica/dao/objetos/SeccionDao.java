package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dto.Seccion;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.SeccionDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeccionDao implements SeccionDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(SeccionDao.class.getName());
    @Override
    public void agregarSeccion(Seccion seccion) throws InserccionBaseDeDatosExcepcion {
        String query = "INSERT INTO seccion (noSeccion, periodo) VALUES (?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConexionBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(query); {
                insertarEnBaseDeDatos.setString(1, seccion.getNoSeccion());
                insertarEnBaseDeDatos.setString(2, seccion.getPeriodo());
                insertarEnBaseDeDatos.executeUpdate();

                LOGGER.info("Seccion insertada correctamente ");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar la seccion", e);
            throw new InserccionBaseDeDatosExcepcion("Error al agregar la sección");
        }
    }
}
