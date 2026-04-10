package logica.dao.interfaces;
import logica.dto.Practicante;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface PracticanteDaoInterfaz {
    void insertarPracticante(Practicante practicante) throws InserccionBaseDeDatosExcepcion;

}
