package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.PracticanteSeccion;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.PracticanteSeccionDaoInterfaz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticanteSeccionDao implements PracticanteSeccionDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(PracticanteSeccionDao.class.getName());

    @Override
    public void agregarPracticanteSeccion(PracticanteSeccion practicanteSeccion) throws InserccionBaseDeDatosExcepcion {
        String query = "INSERT INTO practicante_seccion (matricula, noSeccion) VALUES (?, ?)";
        try {
            Connection conexion = ConexionBaseDeDatos.conectar();
            PreparedStatement insercion = conexion.prepareStatement(query);
            insercion.setString(1, practicanteSeccion.getMatricula());
            insercion.setString(2, practicanteSeccion.getNoSeccion());
            insercion.executeUpdate();
            LOGGER.info("PracticanteSeccion insertada correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar practicante_seccion", e);
            throw new InserccionBaseDeDatosExcepcion("Error al agregar practicante seccion");
        }
    }
}
