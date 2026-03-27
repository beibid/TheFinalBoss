package logica.dao.interfaces;
import logica.Profesor;
import logica.dao.excepciones.InserccionUsuarioExcepcion;

public interface ProfesorDaoInterfaz {
    void insertarProfesor(Profesor profesor) throws InserccionUsuarioExcepcion;
}
