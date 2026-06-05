package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.PeriodoUniversitarioDaoInterfaz;
import logica.dominio.PeriodoUniversitario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeriodoUniversitarioDao implements PeriodoUniversitarioDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(PeriodoUniversitarioDao.class.getName());

    @Override
    public int insertarPeriodo(PeriodoUniversitario periodoUniversitario) throws UsuariosExcepcion {
        String consulta = "INSERT INTO periodo_universitario (nombre, fechaInicio, estado) VALUES (?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insertar = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insertar = conexionBaseDeDatos.prepareStatement(consulta);
            insertar.setString(1, periodoUniversitario.getNombre());
            insertar.setDate(2, periodoUniversitario.getFechaInicio());
            insertar.setString(3, periodoUniversitario.getEstado().name());
            filasAfectadas = insertar.executeUpdate();
            LOGGER.info("Periodo universitario insertado correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar el periodo universitario", excepcionSql);
            throw new UsuariosExcepcion("Error al insertar el periodo universitario", excepcionSql);
        } finally {
            try {
                if (insertar != null) {
                    insertar.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    @Override
    public boolean verificarPeriodoAbierto() throws UsuariosExcepcion {
        String consulta = "SELECT COUNT(*) FROM periodo_universitario WHERE estado = 'Abierto'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaPreparada = null;
        boolean hayPeriodoAbierto = false;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaPreparada.executeQuery();
            if (resultado.next()) {
                hayPeriodoAbierto = resultado.getInt(1) > 0;
            }
            LOGGER.info("Verificacion de periodo abierto completada");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al verificar periodo abierto", excepcionSql);
            throw new UsuariosExcepcion("Error al verificar periodo abierto", excepcionSql);
        } finally {
            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return hayPeriodoAbierto;
    }
}