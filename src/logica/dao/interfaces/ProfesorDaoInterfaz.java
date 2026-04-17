package logica.dao.interfaces;


import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Profesor;


public interface ProfesorDaoInterfaz {
    void insertarProfesor(Profesor profesor) throws UsuariosExcepcion;
    void inactivarProfesor(String numPersonalProfesor) throws UsuariosExcepcion;
    void modificarProfesor(String numPersonalProfesor, Profesor profesor) throws UsuariosExcepcion;
}