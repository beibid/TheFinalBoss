package logica.dao.interfaces;


import logica.dominio.Proyecto;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;


public interface ProyectoDaoInterfaz {
    void agregarProyecto(Proyecto proyecto) throws InserccionBaseDeDatosExcepcion;
}
