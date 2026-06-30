package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.PeriodoUniversitarioDaoInterfaz;
import logica.dominio.PeriodoUniversitario;
import logica.dominio.enums.EstadoPeriodo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeriodoUniversitarioDao implements PeriodoUniversitarioDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(PeriodoUniversitarioDao.class.getName());

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Inserta un nuevo periodo universitario en la base de datos.
     * @param periodoUniversitario el periodo a insertar
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al insertar o de conexion
     */
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
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
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
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    /**
     * Verifica si existe al menos un periodo universitario abierto.
     * @return true si hay un periodo abierto, false en caso contrario
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
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
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al verificar periodo abierto", excepcionSql);
        } finally {
            try {
                if (consultaPreparada != null){
                    consultaPreparada.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return hayPeriodoAbierto;
    }

    /**
     * Obtiene la lista de periodos universitarios con estado abierto.
     * @return lista de periodos abiertos
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
    public List<PeriodoUniversitario> obtenerPeriodosAbiertos() throws UsuariosExcepcion {
        String consulta = "SELECT idPeriodo, nombre, fechaInicio, estado FROM periodo_universitario WHERE estado = 'Abierto'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaPreparada = null;
        List<PeriodoUniversitario> periodos = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaPreparada.executeQuery();
            while (resultado.next()) {
                PeriodoUniversitario periodo = new PeriodoUniversitario();
                periodo.setIdPeriodo(resultado.getInt("idPeriodo"));
                periodo.setNombre(resultado.getString("nombre"));
                periodo.setFechaInicio(resultado.getDate("fechaInicio"));
                periodo.setEstado(EstadoPeriodo.valueOf(resultado.getString("estado")));
                periodos.add(periodo);
            }
            LOGGER.info("Periodos abiertos obtenidos correctamente");
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener periodos abiertos", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener periodos abiertos", excepcionSql);
        } finally {
            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return periodos;
    }

    /**
     * Cierra un periodo universitario actualizando su estado y fecha de fin.
     * @param idPeriodo el ID del periodo a cerrar
     * @return el numero de filas afectadas
     * @throws UsuariosExcepcion si ocurre un error al cerrar o de conexion
     */
    public int cerrarPeriodo(int idPeriodo) throws UsuariosExcepcion {
        String consulta = "UPDATE periodo_universitario SET estado = 'Cerrado', fechaFin = CURDATE() WHERE idPeriodo = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setInt(1, idPeriodo);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Periodo cerrado correctamente: " + idPeriodo);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al cerrar el periodo", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al cerrar el periodo", excepcionSql);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public boolean verificarPeriodoActivoPorPracticante(String matricula) throws UsuariosExcepcion {
        String consulta = "SELECT COUNT(*) FROM practicante p " +
                "INNER JOIN seccion s ON p.idSeccion = s.idSeccion " +
                "INNER JOIN periodo_universitario pu ON s.idPeriodo = pu.idPeriodo " +
                "WHERE p.matricula = ? AND pu.estado = 'Abierto'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement sentencia = null;
        boolean periodoActivo = false;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexionBaseDeDatos.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                periodoActivo = resultado.getInt(1) > 0;
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al verificar periodo del practicante", excepcionSql);
            throw new UsuariosExcepcion("Error al verificar periodo del practicante", excepcionSql);
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return periodoActivo;
    }
}