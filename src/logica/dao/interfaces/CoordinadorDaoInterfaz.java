package logica.dao.interfaces;
import logica.dominio.Coordinador;
import logica.dao.excepciones.DaoExcepcion;

public interface CoordinadorDaoInterfaz {
    void insertarCoordinador(Coordinador coordinador) throws DaoExcepcion;
}
