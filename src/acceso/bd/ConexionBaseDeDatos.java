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
    private static final int ERROR_HOST_NO_ENCONTRADO = 1042;
    private static final int ERROR_ACCESO_DENEGADO = 1045;
    private static final int ERROR_BD_NO_ENCONTRADA = 1049;

    private ConexionBaseDeDatos() {
        cargarConfiguracionLogging();
        cargarConfiguracion();
    }

    public static ConexionBaseDeDatos getInstance() {
        if (instancia == null) {
            instancia = new ConexionBaseDeDatos();
        }
        return instancia;
    }

    private void cargarConfiguracion() {
        try (InputStream input = getClass()
                .getResourceAsStream("propiedades/basededatos.properties")) {
            if (input == null) {
                LOGGER.severe("No se encontro el archivo basededatos.properties");
                return;
            }
            Properties propiedadesBaseDatos = new Properties();
            propiedadesBaseDatos.load(input);
            enlace = propiedadesBaseDatos.getProperty("db.url");
            usuarioIngresado = propiedadesBaseDatos.getProperty("db.user");
            contrasena = propiedadesBaseDatos.getProperty("db.password");
            LOGGER.info("Configuracion de base de datos cargada correctamente");
            LOGGER.config("URL de conexion: " + enlace);
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error cargando configuracion de base de datos", exception);
        }
    }

    private void cargarConfiguracionLogging() {
        try (InputStream input = getClass()
                .getResourceAsStream("propiedades/logging.properties")) {
            if (input != null) {
                LogManager.getLogManager().readConfiguration(input);
                System.out.println("Configuracion de logging cargada desde archivo");
            } else {
                System.out.println("Usando configuracion de logging por defecto");
            }
        } catch (IOException exception) {
            System.err.println("Error cargando configuracion de logging: " + exception.getMessage());
            System.err.println("Usando configuracion por defecto");
        }
    }

    public Connection conectar() throws SQLException {
        Connection conexion = null;
        long tiempoInicio = System.currentTimeMillis();
        try {
            LOGGER.fine("Intentando establecer conexion a la base de datos...");
            conexion = DriverManager.getConnection(enlace, usuarioIngresado, contrasena);
            long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
            LOGGER.log(Level.INFO, "Conexion a base de datos realizada exitosamente en {0} ms", tiempoTotal);
        } catch (SQLException excepcionSql) {
            long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
            LOGGER.log(Level.SEVERE, String.format("Conexion fallida despues de %d ms - URL: %s",
                    tiempoTotal, enlace), excepcionSql);
            if (excepcionSql.getErrorCode() == ERROR_HOST_NO_ENCONTRADO) {
                throw new SQLException("No se pudo encontrar el servidor de base de datos", excepcionSql);
            } else if (excepcionSql.getErrorCode() == ERROR_ACCESO_DENEGADO) {
                throw new SQLException("Acceso denegado a la base de datos", excepcionSql);
            } else if (excepcionSql.getErrorCode() == ERROR_BD_NO_ENCONTRADA) {
                throw new SQLException("La base de datos no existe", excepcionSql);
            } else {
                throw new SQLException("No se pudo conectar a la base de datos", excepcionSql);
            }
        }
        return conexion;
    }
}