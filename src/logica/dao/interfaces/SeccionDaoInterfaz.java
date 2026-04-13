package logica.dao.interfaces;


import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Seccion;


public interface SeccionDaoInterfaz {
    void agregarSeccion(Seccion seccion) throws UsuariosExcepcion;
}