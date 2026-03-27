package logica.dao.interfaces;
import logica.Profesor;
import logica.dao.excepciones.DaoExcepcion;

public interface ProfesorDaoInterfaz {
    void insertarProfesor(Profesor profesor) throws DaoExcepcion;
}
