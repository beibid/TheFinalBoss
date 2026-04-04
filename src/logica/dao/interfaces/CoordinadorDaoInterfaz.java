package logica.dao.interfaces;
import logica.dominio.Coordinador;
import logica.dao.excepciones.InserccionUsuarioExcepcion;

public interface CoordinadorDaoInterfaz {
    void insertarCoordinador(Coordinador coordinador) throws InserccionUsuarioExcepcion;
}
