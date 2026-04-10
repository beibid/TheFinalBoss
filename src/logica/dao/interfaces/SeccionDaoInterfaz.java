package logica.dao.interfaces;

import logica.dto.Seccion;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface SeccionDaoInterfaz {
    void agregarSeccion(Seccion seccion) throws InserccionBaseDeDatosExcepcion;
}