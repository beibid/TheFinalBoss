package logica.dao.objetos;

import acceso.bd.ConectarBaseDeDatos;
import logica.dominio.Proyecto;
import logica.dao.excepciones.InserccionUsuarioExcepcion;
import logica.dao.interfaces.ProyectoDaoInterfaz;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProyectoDao implements ProyectoDaoInterfaz {
    @Override
    public void agregarProyecto(Proyecto proyecto) throws InserccionUsuarioExcepcion {
        String query = "INSERT INTO proyecto (nombreProyecto, descripcion, responsableDelProyecto, estado, nombreEmpresa, sectorEmpresa, direccionEmpresa, idOrganizacion, matricula, numPersonalCoordinador, fechaRegistro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conectarConBaseDeDatos = ConectarBaseDeDatos.conectar();
            PreparedStatement insertarEnBaseDeDatos = conectarConBaseDeDatos.prepareStatement(query); {
                insertarEnBaseDeDatos.setString(1, proyecto.getNombreProyecto());
                insertarEnBaseDeDatos.setString(2, proyecto.getDescripcion());
                insertarEnBaseDeDatos.setString(3, proyecto.getResponsableDelProyecto());
                insertarEnBaseDeDatos.setString(4, proyecto.getEstado().toString());
                insertarEnBaseDeDatos.setString(5, proyecto.getNombreEmpresa());
                insertarEnBaseDeDatos.setString(6, proyecto.getSectorEmpresa());
                insertarEnBaseDeDatos.setString(7, proyecto.getDireccionEmpresa());
                insertarEnBaseDeDatos.setInt(8, proyecto.getIdOrganizacion());
                insertarEnBaseDeDatos.setString(9, proyecto.getMatricula());
                insertarEnBaseDeDatos.setString(10, proyecto.getNumPersonalCoordinador());
                insertarEnBaseDeDatos.setDate(11, proyecto.getFechaRegistro());
                insertarEnBaseDeDatos.executeUpdate();

                System.out.println("Los datos han sido añadidos correctamente");
            }
        } catch (SQLException e) {
            throw new InserccionUsuarioExcepcion("Error al agregar el proyecto");
        }
    }
}
