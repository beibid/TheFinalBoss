package logica.dao.interfaces;
import logica.dominio.Coordinador;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface CoordinadorDaoInterfaz {
    void insertarCoordinador(Coordinador coordinador) throws InserccionBaseDeDatosExcepcion;
}
