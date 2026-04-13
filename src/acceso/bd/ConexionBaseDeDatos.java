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
    private static ConexionBaseDeDatos instancia;
    private String enlace;
    private String usuarioIngresado;
    private String contrasena;
    private static final Logger LOGGER = Logger.getLogger(ConexionBaseDeDatos.class.getName());

    private ConexionBaseDeDatos() {
        cargarConfiguracionLogging();
        cargarConfiguracion();
    }

    public static  ConexionBaseDeDatos getInstance() {
        if (instancia == null) {
            instancia = new ConexionBaseDeDatos();
        }
        return instancia;
    }

    private void cargarConfiguracion() {
        try (InputStream input = getClass()
                .getResourceAsStream("propiedades/basededatos.properties")) {
            if (input == null) {
                LOGGER.severe("No se encontró el archivo basededatos.properties");
                return;
            }
            Properties propiedadesBaseDatos = new Properties();
            propiedadesBaseDatos.load(input);
            enlace = propiedadesBaseDatos.getProperty("db.url");
            usuarioIngresado = propiedadesBaseDatos.getProperty("db.user");
            contrasena = propiedadesBaseDatos.getProperty("db.password");
            LOGGER.info("Configuración de base de datos cargada correctamente");
            LOGGER.config("URL de conexión: " + enlace);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error cargando configuración de base de datos", e);
        }
    }

    private void cargarConfiguracionLogging() {
        try (InputStream input = getClass()
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

    public Connection conectar() {
        Connection conexion = null;
        long tiempoInicio = System.currentTimeMillis();
        try {
            LOGGER.fine("Intentando establecer conexión a la base de datos...");
            conexion = DriverManager.getConnection(enlace, usuarioIngresado, contrasena);
            long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
            LOGGER.log(Level.INFO, "Conexión a base de datos realizada exitosamente en {0} ms", tiempoTotal);
        } catch (SQLException e) {
            long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
            LOGGER.log(Level.SEVERE, String.format("Conexión fallida después de %d ms - URL: %s",
                    tiempoTotal, enlace), e);
        }
        return conexion;
    }
}