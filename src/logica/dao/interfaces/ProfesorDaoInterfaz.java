package logica.dao.interfaces;


import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Profesor;


public interface ProfesorDaoInterfaz {
    int insertarProfesor(Profesor profesor) throws UsuariosExcepcion;
    int inactivarProfesor(String numPersonalProfesor) throws UsuariosExcepcion;
    int modificarProfesor(String numPersonalProfesor, Profesor profesor) throws UsuariosExcepcion;
}