package logica.dao.interfaces;

import logica.dominio.Seccion;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface SeccionDaoInterfaz {
    void agregarSeccion(Seccion seccion) throws InserccionBaseDeDatosExcepcion;
}