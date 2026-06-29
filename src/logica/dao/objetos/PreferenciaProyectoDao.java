package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.PreferenciaProyectoDaoInterfaz;
import logica.dominio.PreferenciaProyecto;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreferenciaProyectoDao implements PreferenciaProyectoDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final Logger LOGGER = Logger.getLogger(PreferenciaProyectoDao.class.getName());

    /**
     * Verifica si la excepcion SQL es un error de conexion a la base de datos.
     * @param excepcion la excepcion SQL a verificar
     * @return true si es un error de conexion, false en caso contrario
     */
    private boolean esErrorDeConexion(SQLException excepcion) {
        return excepcion.getMessage() != null && excepcion.getMessage().contains(ERROR_CONEXION);
    }

    /**
     * Guarda las preferencias de proyecto de un practicante, eliminando las anteriores
     * y reinsertando las nuevas en el orden indicado dentro de una transaccion.
     * @param matricula la matricula del practicante
     * @param idProyectosOrdenados lista de IDs de proyectos en orden de prioridad
     * @throws UsuariosExcepcion si ocurre un error al guardar o de conexion
     */
    @Override
    public void guardarPreferencias(String matricula, List<Integer> idProyectosOrdenados) throws UsuariosExcepcion {
        Connection conexionBaseDeDatos = null;
        CallableStatement eliminar = null;
        CallableStatement insertar = null;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            conexionBaseDeDatos.setAutoCommit(false);
            eliminar = conexionBaseDeDatos.prepareCall("{CALL EliminarPreferencias(?)}");
            eliminar.setString(1, matricula);
            eliminar.execute();
            insertar = conexionBaseDeDatos.prepareCall("{CALL GuardarPreferencias(?, ?, ?)}");
            for (int i = 0; i < idProyectosOrdenados.size(); i++) {
                insertar.setString(1, matricula);
                insertar.setInt(2, idProyectosOrdenados.get(i));
                insertar.setInt(3, i + 1);
                insertar.addBatch();
            }
            insertar.executeBatch();
            conexionBaseDeDatos.commit();
            LOGGER.info("Preferencias guardadas correctamente para: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al guardar preferencias", excepcionSql);
            if (conexionBaseDeDatos != null) {
                try {
                    conexionBaseDeDatos.rollback();
                    LOGGER.warning("Rollback ejecutado para preferencias de: " + matricula);
                } catch (SQLException excepcionRollback) {
                    LOGGER.log(Level.SEVERE, "Error al ejecutar rollback", excepcionRollback);
                }
            }
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al guardar preferencias", excepcionSql);
        } finally {
            try {
                if (insertar != null) {
                    insertar.close();
                }
                if (eliminar != null){
                    eliminar.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.setAutoCommit(true);
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
    }

    /**
     * Obtiene la lista de preferencias de proyecto registradas para un practicante.
     * @param matricula la matricula del practicante
     * @return lista de preferencias ordenadas por prioridad
     * @throws UsuariosExcepcion si ocurre un error al consultar o de conexion
     */
    @Override
    public List<PreferenciaProyecto> obtenerPreferencias(String matricula) throws UsuariosExcepcion {
        Connection conexionBaseDeDatos = null;
        CallableStatement consultaPreferencias = null;
        List<PreferenciaProyecto> lista = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaPreferencias = conexionBaseDeDatos.prepareCall("{CALL ObtenerPreferencias(?)}");
            consultaPreferencias.setString(1, matricula);
            ResultSet resultado = consultaPreferencias.executeQuery();
            while (resultado.next()) {
                PreferenciaProyecto preferencia = new PreferenciaProyecto();
                preferencia.setMatricula(matricula);
                preferencia.setPrioridad(resultado.getInt("prioridad"));
                preferencia.setIdProyecto(resultado.getInt("idProyecto"));
                preferencia.setNombreProyecto(resultado.getString("nombreProyecto"));
                preferencia.setNombreEmpresa(resultado.getString("nombreEmpresa"));
                preferencia.setDescripcion(resultado.getString("descripcion"));
                preferencia.setEstado(resultado.getString("estado"));
                lista.add(preferencia);
            }
            LOGGER.info("Preferencias obtenidas para: " + matricula + " - total: " + lista.size());
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener preferencias", excepcionSql);
            if (esErrorDeConexion(excepcionSql)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener preferencias", excepcionSql);
        } finally {
            try {
                if (consultaPreferencias != null) {
                    consultaPreferencias.close();
                }
                if (conexionBaseDeDatos != null){
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return lista;
    }
}