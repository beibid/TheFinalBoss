package logica.dao.interfaces;

import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Seccion;
import java.util.List;

public interface SeccionDaoInterfaz {
    int agregarSeccion(Seccion seccion) throws UsuariosExcepcion, RegistroDuplicadoExcepcion;
    int modificarSeccion(String noSeccion, Seccion seccion) throws UsuariosExcepcion;
    List<Seccion> obtenerSecciones() throws UsuariosExcepcion;
    List<Seccion> obtenerSeccionesPorProfesor(String numPersonalProfesor) throws UsuariosExcepcion;
}