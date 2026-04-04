package acceso.bd;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConectarBaseDeDatos {

    private static String enlace;
    private static String usuarioIngresado;
    private static String contrasena;

    static {
        try (InputStream input = ConectarBaseDeDatos.class
                .getClassLoader()
                .getResourceAsStream("basededatos.properties")) {

            if (input == null) {
                throw new RuntimeException("No se encontró ");
            }

            Properties props = new Properties();
            props.load(input);

            enlace        = props.getProperty("db.url");
            usuarioIngresado = props.getProperty("db.user");
            contrasena = props.getProperty("db.password");

        } catch (IOException e) {
            throw new RuntimeException("Error cargando ", e);
        }
    }

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(enlace, usuarioIngresado, contrasena);
            System.out.println("Conexion a base de datos realizada");
        } catch (SQLException e) {
            System.out.println("Conexion fallida");
            e.printStackTrace();
        }
        return conexion;
    }
}