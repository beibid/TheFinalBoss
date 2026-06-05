package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dominio.Coordinador;
import logica.dao.interfaces.CoordinadorDaoInterfaz;
import logica.dominio.enums.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinadorDao implements CoordinadorDaoInterfaz {

    private static final Logger LOGGER = Logger.getLogger(CoordinadorDao.class.getName());

    @Override
    public int insertarCoordinador(Coordinador coordinador) throws UsuariosExcepcion {
        String consultaUsuario = "INSERT INTO usuario (nombre, apellidos, contrasena, estado, rol, correo) VALUES (?, ?, ?, ?, 'Coordinador', ?)";
        String consultaCoordinador = "INSERT INTO coordinador (numPersonalCoordinador, idUsuario) VALUES (?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionCoordinador = null;
        int filasAfectadas = 0;

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, coordinador.getNombre());
            insercionUsuario.setString(2, coordinador.getApellidos());
            insercionUsuario.setString(3, coordinador.getContrasena());
            insercionUsuario.setString(4, coordinador.getEstado().toString());
            insercionUsuario.setString(5, coordinador.getCorreo());
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
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al insertar Coordinador", excepcionSQL);
            if (excepcionSQL.getMessage().contains("Duplicate entry")) {
                throw new RegistroDuplicadoExcepcion("El numero del personal ya existe", excepcionSQL);
            }
            throw new UsuariosExcepcion("Error al insertar coordinador", excepcionSQL);
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
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return filasAfectadas;
    }

    public int inactivarCoordinador(String numPersonalCoordinador) throws UsuariosExcepcion {
        if (numPersonalCoordinador == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        String consulta = "UPDATE usuario SET estado = ? WHERE idUsuario = (SELECT idUsuario FROM coordinador WHERE numPersonalCoordinador = ?)";
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
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al inactivar coordinador", excepcionSQL);
            throw new UsuariosExcepcion("Error al inactivar coordinador", excepcionSQL);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
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
        String consultaUsuario = "UPDATE usuario SET nombre = ?, apellidos = ?, correo = ?, contrasena = ?, estado = ? WHERE idUsuario = (SELECT idUsuario FROM coordinador WHERE numPersonalCoordinador = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consultaUsuario);
            actualizacion.setString(1, coordinador.getNombre());
            actualizacion.setString(2, coordinador.getApellidos());
            actualizacion.setString(3, coordinador.getCorreo());
            actualizacion.setString(4, coordinador.getContrasena());
            actualizacion.setString(5, coordinador.getEstado().toString());
            actualizacion.setString(6, numPersonalCoordinador);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Coordinador modificado correctamente: " + numPersonalCoordinador);
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al modificar coordinador", excepcionSQL);
            throw new UsuariosExcepcion("Error al modificar coordinador", excepcionSQL);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return filasAfectadas;
    }

    public List<Coordinador> obtenerCoordinadoresActivos() throws UsuariosExcepcion {
        String consulta = "SELECT u.nombre, u.apellidos, u.correo, u.estado, u.contrasena, p.numPersonalCoordinador " +
                "FROM usuario u " +
                "INNER JOIN coordinador p ON u.idUsuario = p.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaCoordinadores = null;
        List<Coordinador> coordinadores = new ArrayList<>();

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaCoordinadores = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaCoordinadores.executeQuery();
            while (resultado.next()) {
                Coordinador coordinador = new Coordinador();
                coordinador.setNombre(resultado.getString("nombre"));
                coordinador.setApellidos(resultado.getString("apellidos"));
                coordinador.setNumeroDePersonalCoordinador(resultado.getString("numPersonalCoordinador"));
                coordinador.setCorreo(resultado.getString("correo"));
                coordinador.setEstado(Estado.valueOf(resultado.getString("estado")));
                coordinador.setContrasena(resultado.getString("contrasena"));
                coordinadores.add(coordinador);
            }
        } catch (SQLException excepcionSQL) {
            LOGGER.log(Level.SEVERE, "Error al obtener coordinadores activos", excepcionSQL);
            throw new UsuariosExcepcion("Error al obtener coordinadores activos", excepcionSQL);
        } finally {
            try {
                if (consultaCoordinadores != null) {
                    consultaCoordinadores.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSQL);
            }
        }
        return coordinadores;
    }

    public int existeCoordinadorActivo() throws UsuariosExcepcion {
        String consulta = "SELECT COUNT(*) " +
                "FROM usuario u " +
                "INNER JOIN coordinador c ON u.idUsuario = c.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaCoordinadorActivo = null;
        int filasAfectadas = 0;

        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaCoordinadorActivo = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaCoordinadorActivo.executeQuery();

            if (resultado.next()) {
                filasAfectadas = resultado.getInt(1);
            }
        } catch (SQLException exceptionSql) {
            LOGGER.log(Level.SEVERE, "Error al verificar coordinadores activos", exceptionSql);
            throw new UsuariosExcepcion("Error al verificar coordinadores activos", exceptionSql);
        } finally {
            try {
                if (consultaCoordinadorActivo != null) {
                    consultaCoordinadorActivo.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            }catch (SQLException exceptionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", exceptionSql);
            }
        }
        return filasAfectadas;
    }
}