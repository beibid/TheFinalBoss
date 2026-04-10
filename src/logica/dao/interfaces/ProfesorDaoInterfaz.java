package logica.dao.interfaces;


import logica.dominio.Profesor;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;


public interface ProfesorDaoInterfaz {
    void insertarProfesor(Profesor profesor) throws InserccionBaseDeDatosExcepcion;
}
