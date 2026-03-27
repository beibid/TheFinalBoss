package logica.dao.interfaces;
import logica.Practicante;
import logica.dao.excepciones.DaoExcepcion;

public interface PracticanteDaoInterfaz {
    void insertarPracticante(Practicante practicante) throws DaoExcepcion;

}
