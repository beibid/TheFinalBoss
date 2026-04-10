package acceso.bd;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class ConexionBaseDeDatos {

    private static final String ENLACE;
    private static final String USUARIO_INGRESADO;
    private static final String CONTRASENA;
    private static final Logger LOGGER = Logger.getLogger(ConexionBaseDeDatos.class.getName());

    static {

        cargarConfiguracionLogging();

        try (InputStream input = ConexionBaseDeDatos.class
                .getResourceAsStream("propiedades/basededatos.properties")) {

            if (input == null) {
                LOGGER.severe("No se encontró el archivo basededatos.properties");
                throw new RuntimeException("No se encontró ");
            }

            Properties propiedadesBaseDatos = new Properties();
            propiedadesBaseDatos.load(input);

            ENLACE = propiedadesBaseDatos.getProperty("db.url");
            USUARIO_INGRESADO = propiedadesBaseDatos.getProperty("db.user");
            CONTRASENA = propiedadesBaseDatos.getProperty("db.password");
            LOGGER.info("Configuración de base de datos cargada correctamente");
            LOGGER.config("URL de conexión: " + ENLACE);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error cargando configuración de base de datos", e);
            throw new RuntimeException("Error cargando ", e);
        }
    }
    private static void cargarConfiguracionLogging() {
        try (InputStream input = ConexionBaseDeDatos.class
                .getResourceAsStream("propiedades/logging.properties")) {

            if (input != null) {
                LogManager.getLogManager().readConfiguration(input);
                System.out.println("Configuración de logging cargada desde archivo");
            } else {
                System.out.println("Usando configuración de logging por defecto");
            }
        } catch (IOException e) {
            System.err.println("Error cargando configuración de logging: " + e.getMessage());
            System.err.println("Usando configuración por defecto");
        }
    }

    public static Connection conectar() {
        Connection conexion = null;
        long tiempoInicio = System.currentTimeMillis();
        try {
            LOGGER.fine("Intentando establecer conexión a la base de datos...");
            conexion = DriverManager.getConnection(ENLACE, USUARIO_INGRESADO, CONTRASENA);
            long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
            LOGGER.log(Level.INFO, "Conexión a base de datos realizada exitosamente en {0} ms", tiempoTotal);
        } catch (SQLException e) {
            long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
            LOGGER.log(Level.SEVERE, String.format("Conexión fallida después de %d ms - URL: %s", tiempoTotal, ENLACE),e);
        }
        return conexion;
    }
}