package logica.dao.interfaces;

import logica.dominio.Proyecto;
import logica.dao.excepciones.InserccionUsuarioExcepcion;

public interface ProyectoDaoInterfaz {
    void agregarProyecto(Proyecto proyecto) throws InserccionUsuarioExcepcion;
}
