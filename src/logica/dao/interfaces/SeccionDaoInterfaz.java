package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Seccion;

public interface SeccionDaoInterfaz {
    int agregarSeccion(Seccion seccion) throws UsuariosExcepcion;
    int modificarSeccion(String noSeccion, Seccion seccion) throws UsuariosExcepcion;
}