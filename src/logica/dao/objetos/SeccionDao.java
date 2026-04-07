package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.Seccion;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.interfaces.SeccionDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SeccionDao implements SeccionDaoInterfaz {
    @Override
    public void agregarSeccion(Seccion seccion) throws InserccionBaseDeDatosExcepcion {
        String query = "INSERT INTO seccion (noSeccion, periodo) VALUES (?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConexionBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(query); {
                insertarEnBaseDeDatos.setString(1, seccion.getNoSeccion());
                insertarEnBaseDeDatos.setString(2, seccion.getPeriodo());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");
            }
        } catch (SQLException e) {
            throw new InserccionBaseDeDatosExcepcion("Error al agregar la sección");
        }
    }
}
