package logica.dao.interfaces;

import logica.dto.Proyecto;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface ProyectoDaoInterfaz {
    void agregarProyecto(Proyecto proyecto) throws InserccionBaseDeDatosExcepcion;
}
