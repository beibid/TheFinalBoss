package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Practicante;
import logica.dao.interfaces.PracticanteDaoInterfaz;
import logica.dominio.enums.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticanteDao implements PracticanteDaoInterfaz {
    private static final Logger LOGGER = Logger.getLogger(PracticanteDao.class.getName());

    @Override
    public int insertarPracticante(Practicante practicante) throws UsuariosExcepcion {
        String consultaUsuario = "insert into Usuario (nombre, apellidos, contrasena, estado) values (?, ?, ?, ?)";
        String consultaPracticante = "insert into Practicante (matricula, lenguaIndigena, genero, idUsuario) values (?, ?, ?, ?)";
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
            filasAfectadas = insercionPracticante.executeUpdate();
            LOGGER.info("Practicante insertado correctamente con ID de usuario: " + idUsuarioGenerado);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar practicante", e);
            if (e.getMessage().contains("Duplicate entry")){
                throw new RegistroDuplicadoExcepcion("La matricula ingresada ya existe", e);
            }
            throw new UsuariosExcepcion("Error al insertar practicante", e);
        } finally {
            try {
                if (insercionPracticante != null) insercionPracticante.close();
                if (insercionUsuario != null) insercionUsuario.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }

    public int inactivarPracticante(String matricula) throws UsuariosExcepcion {
        if (matricula == null) {
            throw new UsuariosExcepcion("El numero de personal no puede ser nulo");
        }
        String consulta = "UPDATE Usuario SET estado = ? WHERE idUsuario = (SELECT idUsuario FROM Practicante WHERE matricula = ?)";
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al inactivar practicante", e);
            throw new UsuariosExcepcion("Error al inactivar practicante", e);
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

    public int modificarPracticante(String matricula, Practicante practicante) throws UsuariosExcepcion {
        if (matricula == null) {
            throw new UsuariosExcepcion("La matricula no puede ser nula");
        }
        if (practicante.getNombre() == null) {
            throw new UsuariosExcepcion("El nombre del practicante no puede ser nulo");
        }
        String consultaUsuario = "UPDATE Usuario SET nombre = ?, apellidos = ?, contrasena = ?, estado = ? WHERE idUsuario = (SELECT idUsuario FROM Practicante WHERE matricula = ?)";
        String consultaPracticante = "UPDATE Practicante SET lenguaIndigena = ?, genero = ? WHERE matricula = ?";
        Connection conexionBaseDeDatos = null;
        PreparedStatement actualizacionUsuario = null;
        PreparedStatement actualizacionPracticante = null;
        int filasAfectadas = 0;
        try {
            conexionBaseDeDatos = ConexionBaseDeDatos.getInstance().conectar();
            actualizacionUsuario = conexionBaseDeDatos.prepareStatement(consultaUsuario);
            actualizacionUsuario.setString(1, practicante.getNombre());
            actualizacionUsuario.setString(2, practicante.getApellidos());
            actualizacionUsuario.setString(3, practicante.getContrasena());
            actualizacionUsuario.setString(4, practicante.getEstado().toString());
            actualizacionUsuario.setString(5, matricula);
            actualizacionUsuario.executeUpdate();
            actualizacionPracticante = conexionBaseDeDatos.prepareStatement(consultaPracticante);
            actualizacionPracticante.setString(1, practicante.getLenguaIndigena());
            actualizacionPracticante.setString(2, practicante.getGenero().toString());
            actualizacionPracticante.setString(3, matricula);
            filasAfectadas = actualizacionPracticante.executeUpdate();
            LOGGER.info("Practicante modificado correctamente: " + matricula);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al modificar practicante", e);
            throw new UsuariosExcepcion("Error al modificar practicante", e);
        } finally {
            try {
                if (actualizacionPracticante != null) actualizacionPracticante.close();
                if (actualizacionUsuario != null) actualizacionUsuario.close();
                if (conexionBaseDeDatos != null) conexionBaseDeDatos.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
        return filasAfectadas;
    }
}