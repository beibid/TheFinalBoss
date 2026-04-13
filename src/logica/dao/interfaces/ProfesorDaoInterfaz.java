package logica.dao.interfaces;


import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Profesor;


public interface ProfesorDaoInterfaz {
    void insertarProfesor(Profesor profesor) throws UsuariosExcepcion;
}
