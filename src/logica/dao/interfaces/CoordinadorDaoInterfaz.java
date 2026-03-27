package logica.dao.interfaces;
import logica.Coordinador;
import logica.dao.excepciones.InserccionUsuarioExcepcion;

public interface CoordinadorDaoInterfaz {
    void insertarCoordinador(Coordinador coordinador) throws InserccionUsuarioExcepcion;
}
