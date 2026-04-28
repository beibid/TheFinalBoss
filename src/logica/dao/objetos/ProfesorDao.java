package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Profesor;
import logica.dao.interfaces.ProfesorDaoInterfaz;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfesorDao implements ProfesorDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(ProfesorDao.class.getName());

    @Override
    public int insertarProfesor(Profesor profesor) throws UsuariosExcepcion {
        String consultaUsuario = "insert into Usuario (nombre, apellidos, contrasena, estado) values (?, ?, ?, ?)";
        String consultaProfesor = "insert into Profesor (numPersonalProfesor, turno, idUsuario) values (?, ?, ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement insercionUsuario = null;
        PreparedStatement insercionProfesor = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            insercionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);
            insercionUsuario.setString(1, profesor.getNombre());
            insercionUsuario.setString(2, profesor.getApellidos());
            insercionUsuario.setString(3, profesor.getContrasena());
            insercionUsuario.setString(4, profesor.getEstado().toString());
            insercionUsuario.executeUpdate();
            ResultSet tomarLlave = insercionUsuario.getGeneratedKeys();
            if (!tomarLlave.next()) {
                throw new UsuariosExcepcion("No se obtuvo el ID del usuario insertado");
            }
            int idUsuarioGenerado = tomarLlave.getInt(1);
            insercionProfesor = conexionBaseDeDatos.prepareStatement(consultaProfesor);
            insercionProfesor.setString(1, profesor.getNumeroDePersonalProfesor());
            insercionProfesor.setString(2, profesor.getTurno().toString());
            insercionProfesor.setInt(3, idUsuarioGenerado);
            filasAfectadas = insercionProfesor.executeUpdate();
            LOGGER.info("Profesor insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar profesor", e);
            throw new UsuariosExcepcion("Error al insertar profesor", e);
        } finally {
            try {
                if (insercionProfesor != null) insercionProfesor.close();
                if (insercionUsuario != null) insercionUsuario.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public int inactivarProfesor(String numPersonalProfesor) throws UsuariosExcepcion {
        if (numPersonalProfesor == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        String consulta = "UPDATE Usuario SET estado = ? WHERE idUsuario = (SELECT idUsuario FROM Profesor WHERE numPersonalProfesor = ?)";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacion = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacion = conexionBaseDeDatos.prepareStatement(consulta);
            actualizacion.setString(1, Estado.Inactivo.toString());
            actualizacion.setString(2, numPersonalProfesor);
            filasAfectadas = actualizacion.executeUpdate();
            LOGGER.info("Profesor inactivado correctamente: " + numPersonalProfesor);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al inactivar profesor", e);
            throw new UsuariosExcepcion("Error al inactivar profesor", e);
        } finally {
            try {
                if (actualizacion != null) actualizacion.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public int modificarProfesor(String numPersonalProfesor, Profesor profesor) throws UsuariosExcepcion {
        if (profesor.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre del profesor no puede ser nulo");
        }
        if (numPersonalProfesor == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        String consultaUsuario = "UPDATE Usuario SET nombre = ?, apellidos = ?, contrasena = ?, estado = ? WHERE idUsuario = (SELECT idUsuario FROM Profesor WHERE numPersonalProfesor = ?)";
        String consultaProfesor = "UPDATE Profesor SET turno = ? WHERE numPersonalProfesor = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacionUsuario = null;
        PreparedStatement actualizacionProfesor = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario);
            actualizacionUsuario.setString(1, profesor.getNombre());
            actualizacionUsuario.setString(2, profesor.getApellidos());
            actualizacionUsuario.setString(3, profesor.getContrasena());
            actualizacionUsuario.setString(4, profesor.getEstado().toString());
            actualizacionUsuario.setString(5, numPersonalProfesor);
            actualizacionUsuario.executeUpdate();
            actualizacionProfesor = conexionBaseDeDatos.prepareStatement(consultaProfesor);
            actualizacionProfesor.setString(1, profesor.getTurno().toString());
            actualizacionProfesor.setString(2, numPersonalProfesor);
            filasAfectadas = actualizacionProfesor.executeUpdate();
            LOGGER.info("Profesor modificado correctamente: " + numPersonalProfesor);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al modificar profesor", e);
            throw new UsuariosExcepcion("Error al modificar profesor", e);
        } finally {
            try {
                if (actualizacionProfesor != null) actualizacionProfesor.close();
                if (actualizacionUsuario != null) actualizacionUsuario.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public List<Profesor> obtenerProfesoresActivos() throws UsuariosExcepcion {
        String consulta = "SELECT u.nombre, u.apellidos, p.numPersonalProfesor, p.turno " +
                "FROM Usuario u " +
                "INNER JOIN Profesor p ON u.idUsuario = p.idUsuario " +
                "WHERE u.estado = 'Activo'";
        Connection conexionBaseDeDatos = null;
        PreparedStatement consultaProfesores = null;
        List<Profesor> profesores = new ArrayList<>();
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            consultaProfesores = conexionBaseDeDatos.prepareStatement(consulta);
            ResultSet resultado = consultaProfesores.executeQuery();
            while (resultado.next()) {
                Profesor profesor = new Profesor();
                profesor.setNombre(resultado.getString("nombre"));
                profesor.setApellidos(resultado.getString("apellidos"));
                profesor.setNumeroDePersonalProfesor(resultado.getString("numPersonalProfesor"));
                profesor.setTurno(Turno.valueOf(resultado.getString("turno")));
                profesores.add(profesor);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener profesores activos", e);
            throw new UsuariosExcepcion("Error al obtener profesores activos", e);
        } finally {
            try {
                if (consultaProfesores != null) consultaProfesores.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return profesores;
    }
}