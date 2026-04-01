package logica.dao.interfaces;
import logica.dominio.Profesor;
import logica.dao.excepciones.DaoExcepcion;

public interface ProfesorDaoInterfaz {
    void insertarProfesor(Profesor profesor) throws DaoExcepcion;
}
