package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Practicante;
import logica.dao.interfaces.PracticanteDaoInterfaz;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticanteDao implements PracticanteDaoInterfaz {

    private static final String ERROR_CONEXION = "No se pudo conectar";
    private static final String ERROR_DUPLICADO = "Duplicate entry";
    private static final Logger LOGGER = Logger.getLogger(PracticanteDao.class.getName());

    @Override
    public int insertarPracticante(Practicante practicante) throws UsuariosExcepcion {
        String consultaUsuario = "INSERT INTO usuario (nombre, apellidos, contrasena, estado, rol, correo) VALUES (?, ?, ?, ?, 'Practicante', ?)";
        String consultaPracticante = "INSERT INTO practicante (matricula, lenguaIndigena, genero, idUsuario, idProfesor) VALUES (?, ?, ?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionPracticante = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, practicante.getNombre());
            insercionUsuario.setString(2, practicante.getApellidos());
            insercionUsuario.setString(3, practicante.getContrasena());
            insercionUsuario.setString(4, practicante.getEstado().toString());
            insercionUsuario.setString(5, practicante.getCorreo());
            insercionUsuario.executeUpdate();
            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionPracticante = conexionBaseDeDatos.prepareStatement(consultaPracticante);
            insercionPracticante.setString(1, practicante.getMatricula());
            insercionPracticante.setString(2, practicante.getLenguaIndigena());
            insercionPracticante.setString(3, practicante.getGenero().toString());
            insercionPracticante.setInt(4, idUsuarioGenerado);
            insercionPracticante.setInt(5, practicante.getNumeroPersonalProfesor());
            filasAfectadas = insercionPracticante.executeUpdate();
            LOGGER.info("Practicante insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar practicante", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            } else if (excepcionSql.getMessage().contains(ERROR_DUPLICADO)) {
                throw new RegistroDuplicadoExcepcion("La matricula ingresada ya existe", excepcionSql);
            }
            throw new UsuariosExcepcion("Error al insertar practicante", excepcionSql);
        } finally {
            try {
                if (insercionPracticante != null) {
                    insercionPracticante.close();
                }
                if (insercionUsuario != null) {
                    insercionUsuario.close();
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

    public int inactivarPracticante(String matricula) throws UsuariosExcepcion {
        if (matricula == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        if (matricula.isEmpty()) {
            throw new UsuariosExcepcion("La matricula no debe de estar vacia");
        }
        String consulta = "UPDATE usuario SET estado = ? WHERE idUsuario = (SELECT idUsuario FROM practicante WHERE matricula = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, Estado.Inactivo.toString());
            actualizacion.setString(2, matricula);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Practicante inactivado correctamente: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al inactivar practicante", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al inactivar practicante", excepcionSql);
        } finally {
            try {
                if (actualizacion != null) {
                    actualizacion.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSQL) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSQL);
            }
        }
        return filasAfectadas;
    }

    public int modificarPracticante(String matricula, Practicante practicante) throws UsuariosExcepcion {
        if (matricula == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        if (matricula.isEmpty()) {
            throw new UsuariosExcepcion("La matricula no pueda estar vacia");
        }
        if (practicante.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre del practicante no puede ser nulo");
        }
        String consultaUsuario = "UPDATE usuario SET nombre = ?, apellidos = ?, correo = ?, contrasena = ?, estado = ? WHERE idUsuario = (SELECT idUsuario FROM practicante WHERE matricula = ?)";
        String consultaPracticante = "UPDATE practicante SET lenguaIndigena = ?, genero = ? WHERE matricula = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacionUsuario = null;
        PreparedStatement actualizacionPracticante = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario);
            actualizacionUsuario.setString(1, practicante.getNombre());
            actualizacionUsuario.setString(2, practicante.getApellidos());
            actualizacionUsuario.setString(3, practicante.getCorreo());
            actualizacionUsuario.setString(4, practicante.getContrasena());
            actualizacionUsuario.setString(5, practicante.getEstado().toString());
            actualizacionUsuario.setString(6, matricula);
            actualizacionUsuario.executeUpdate();
            actualizacionPracticante = conexionBaseDeDatos.prepareStatement(consultaPracticante);
            actualizacionPracticante.setString(1, practicante.getLenguaIndigena());
            actualizacionPracticante.setString(2, practicante.getGenero().toString());
            actualizacionPracticante.setString(3, matricula);
            filasAfectadas = actualizacionPracticante.executeUpdate();
            LOGGER.info("Practicante modificado correctamente: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al modificar practicante", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al modificar practicante", excepcionSql);
        } finally {
            try {
                if (actualizacionPracticante != null) {
                    actualizacionPracticante.close();
                }
                if (actualizacionUsuario != null) {
                    actualizacionUsuario.close();
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

    public List<Practicante> obtenerPracticantesActivos() throws UsuariosExcepcion {
        String consulta = "SELECT u.idUsuario, u.nombre, u.apellidos, u.correo, u.estado, u.contrasena, " +
                "p.matricula, p.lenguaIndigena, p.genero " +
                "FROM usuario u " +
                "INNER JOIN practicante p ON u.idUsuario = p.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaPracticantes = null;
        List<Practicante> practicantes = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaPracticantes = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaPracticantes.executeQuery();
            while (resultado.next()) {
                Practicante practicante = new Practicante();
                practicante.setIdUsuario(resultado.getInt("idUsuario"));
                practicante.setNombre(resultado.getString("nombre"));
                practicante.setApellidos(resultado.getString("apellidos"));
                practicante.setCorreo(resultado.getString("correo"));
                practicante.setEstado(Estado.valueOf(resultado.getString("estado")));
                practicante.setContrasena(resultado.getString("contrasena"));
                practicante.setMatricula(resultado.getString("matricula"));
                practicante.setLenguaIndigena(resultado.getString("lenguaIndigena"));
                practicante.setGenero(Genero.valueOf(resultado.getString("genero")));
                practicantes.add(practicante);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener practicantes activos", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener practicantes activos", excepcionSql);
        } finally {
            try {
                if (consultaPracticantes != null) {
                    consultaPracticantes.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return practicantes;
    }

    public int asignarProyecto(String matricula, int idProyecto) throws UsuariosExcepcion {
        Connection conexionBaseDeDatos = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            CallableStatement procedimientoAsignaProyecto = conexionBaseDeDatos.prepareCall("{CALL AsignarProyecto(?, ?)}");
            procedimientoAsignaProyecto.setString(1, matricula);
            procedimientoAsignaProyecto.setInt(2, idProyecto);
            procedimientoAsignaProyecto.execute();
            filasAfectadas = 1;
            LOGGER.info("Proyecto asignado correctamente al practicante: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al asignar proyecto", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion(excepcionSql.getMessage(), excepcionSql);
        } finally {
            try {
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return filasAfectadas;
    }

    public List<Practicante> obtenerPracticantesPorProfesor(int idProfesor) throws UsuariosExcepcion {
        String consulta = "SELECT u.idUsuario, u.nombre, u.apellidos, u.correo, u.estado, u.contrasena, " +
                "p.matricula, p.lenguaIndigena, p.genero " +
                "FROM usuario u " +
                "INNER JOIN practicante p ON u.idUsuario = p.idUsuario " +
                "WHERE p.idProfesor = ? AND u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaPracticantes = null;
        List<Practicante> practicantes = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaPracticantes = conexionBaseDeDatos.prepareStatement(consulta);
            consultaPracticantes.setInt(1, idProfesor);
            ResultSet resultado = consultaPracticantes.executeQuery();
            while (resultado.next()) {
                Practicante practicante = new Practicante();
                practicante.setIdUsuario(resultado.getInt("idUsuario"));
                practicante.setNombre(resultado.getString("nombre"));
                practicante.setApellidos(resultado.getString("apellidos"));
                practicante.setCorreo(resultado.getString("correo"));
                practicante.setEstado(Estado.valueOf(resultado.getString("estado")));
                practicante.setContrasena(resultado.getString("contrasena"));
                practicante.setMatricula(resultado.getString("matricula"));
                practicante.setLenguaIndigena(resultado.getString("lenguaIndigena"));
                practicante.setGenero(Genero.valueOf(resultado.getString("genero")));
                practicantes.add(practicante);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener practicantes por profesor", excepcionSql);
            if (excepcionSql.getMessage().contains(ERROR_CONEXION)) {
                throw new UsuariosExcepcion("No se pudo conectar al servidor. Verifique que la base de datos este encendida");
            }
            throw new UsuariosExcepcion("Error al obtener practicantes por profesor", excepcionSql);
        } finally {
            try {
                if (consultaPracticantes != null) {
                    consultaPracticantes.close();
                }
                if (conexionBaseDeDatos != null) {
                    conexionBaseDeDatos.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexion", excepcionSql);
            }
        }
        return practicantes;
    }
}