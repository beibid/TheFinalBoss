package logica.dao.interfaces;

import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dto.PracticanteSeccion;

public interface PracticanteSeccionDaoInterfaz {
    void agregarPracticanteSeccion(PracticanteSeccion practicanteSeccion) throws InserccionBaseDeDatosExcepcion;
}