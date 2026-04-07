package acceso.bd;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConexionBaseDeDatos {

    private static String ENLACE;
    private static String USUARIO_INGRESADO;
    private static String CONTRASENA;

    static {
        try (InputStream input = ConexionBaseDeDatos.class
                .getClassLoader()
                .getResourceAsStream("basededatos.properties")) {

            if (input == null) {
                throw new RuntimeException("No se encontró ");
            }

            Properties propiedadesBaseDatos = new Properties();
            propiedadesBaseDatos.load(input);

            ENLACE = propiedadesBaseDatos.getProperty("db.url");
            USUARIO_INGRESADO = propiedadesBaseDatos.getProperty("db.user");
            CONTRASENA = propiedadesBaseDatos.getProperty("db.password");

        } catch (IOException e) {
            throw new RuntimeException("Error cargando ", e);
        }
    }

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(ENLACE, USUARIO_INGRESADO, CONTRASENA);
            System.out.println("Conexion a base de datos realizada");
        } catch (SQLException e) {
            System.out.println("Conexion fallida");
            e.printStackTrace();
        }
        return conexion;
    }
}