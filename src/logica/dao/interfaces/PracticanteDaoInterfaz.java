package logica.dao.interfaces;
import logica.Practicante;
import logica.dao.excepciones.InserccionUsuarioExcepcion;

public interface PracticanteDaoInterfaz {
    void insertarPracticante(Practicante practicante) throws InserccionUsuarioExcepcion;

}
