package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Coordinador;
import logica.dao.interfaces.CoordinadorDaoInterfaz;
import logica.dominio.enums.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinadorDao implements CoordinadorDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(CoordinadorDao.class.getName());

    @Override
    public int insertarCoordinador(Coordinador coordinador) throws UsuariosExcepcion {
        String consultaUsuario = "insert into Usuario (nombre, apellidoPaterno, apellidoMaterno, contrasena, estado) values (?, ?, ?, ?, ?)";
        String consultaCoordinador = "insert into Coordinador (numPersonalCoordinador, idUsuario) values (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionCoordinador = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, coordinador.getNombre());
            insercionUsuario.setString(2, coordinador.getApellidoPaterno());
            insercionUsuario.setString(3, coordinador.getApellidoMaterno());
            insercionUsuario.setString(4, coordinador.getContrasena());
            insercionUsuario.setString(5, coordinador.getEstado().toString());
            insercionUsuario.executeUpdate();
            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionCoordinador = conexionBaseDeDatos.prepareStatement(consultaCoordinador);
            insercionCoordinador.setString(1, coordinador.getNumeroDePersonalCoordinador());
            insercionCoordinador.setInt(2, idUsuarioGenerado);
            filasAfectadas = insercionCoordinador.executeUpdate();
            LOGGER.info("Coordinador insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar Coordinador", e);
            throw new UsuariosExcepcion("Error al insertar coordinador", e);
        } finally {
            try {
                if (insercionCoordinador != null) {
                    insercionCoordinador.close();
                }
                if (insercionUsuario != null) {
                    insercionUsuario.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public int inactivarCoordinador(String numPersonalCoordinador) throws UsuariosExcepcion {
        if (numPersonalCoordinador == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        String consulta = "UPDATE Usuario SET estado = ? WHERE idUsuario = (SELECT idUsuario FROM Coordinador WHERE numPersonalCoordinador = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, Estado.Inactivo.toString());
            actualizacion.setString(2, numPersonalCoordinador);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Coordinador inactivado correctamente: " + numPersonalCoordinador);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al inactivar coordinador", e);
            throw new UsuariosExcepcion("Error al inactivar coordinador", e);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public int modificarCoordinador(String numPersonalCoordinador, Coordinador coordinador) throws UsuariosExcepcion {
        if (numPersonalCoordinador == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        if (coordinador.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre del coordinador no puede ser nulo");
        }
        String consultaUsuario = "UPDATE Usuario SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, contrasena = ?, estado = ? WHERE idUsuario = (SELECT idUsuario FROM Coordinador WHERE numPersonalCoordinador = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consultaUsuario);
            actualizacion.setString(1, coordinador.getNombre());
            actualizacion.setString(2, coordinador.getApellidoPaterno());
            actualizacion.setString(3, coordinador.getApellidoMaterno());
            actualizacion.setString(4, coordinador.getContrasena());
            actualizacion.setString(5, coordinador.getEstado().toString());
            actualizacion.setString(6, numPersonalCoordinador);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Coordinador modificado correctamente: " + numPersonalCoordinador);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al modificar coordinador", e);
            throw new UsuariosExcepcion("Error al modificar coordinador", e);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }
}