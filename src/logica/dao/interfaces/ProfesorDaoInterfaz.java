package logica.dao.interfaces;
import logica.dto.Profesor;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface ProfesorDaoInterfaz {
    void insertarProfesor(Profesor profesor) throws InserccionBaseDeDatosExcepcion;
}
