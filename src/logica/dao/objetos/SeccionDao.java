package logica.dao.objetos;


import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Seccion;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.interfaces.SeccionDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SeccionDao implements SeccionDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(SeccionDao.class.getName());
    @Override
    public void agregarSeccion(Seccion seccion) throws UsuariosExcepcion {
        String consultaSeccion = "INSERT INTO seccion (noSeccion, periodo) VALUES (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertarEnBaseDeDatos = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertarEnBaseDeDatos = conexionBaseDeDatos.prepareStatement(consultaSeccion);
            insertarEnBaseDeDatos.setString(1, seccion.getNoSeccion());
            insertarEnBaseDeDatos.setString(2, seccion.getPeriodo());
            insertarEnBaseDeDatos.executeUpdate();
            LOGGER.info("Seccion insertada correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar la seccion", e);
            throw new UsuariosExcepcion("Error al agregar la sección",e);
        } finally {
            try {
                if (insertarEnBaseDeDatos != null){
                    insertarEnBaseDeDatos.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
    }
}
