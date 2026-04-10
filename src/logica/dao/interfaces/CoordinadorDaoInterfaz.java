package logica.dao.interfaces;
import logica.dto.Coordinador;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface CoordinadorDaoInterfaz {
    void insertarCoordinador(Coordinador coordinador) throws InserccionBaseDeDatosExcepcion;
}
