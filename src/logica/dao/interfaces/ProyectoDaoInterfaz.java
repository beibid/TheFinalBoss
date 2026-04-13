package logica.dao.interfaces;


import logica.dominio.Proyecto;
import logica.dao.excepciones.MensajeriaExcepcion;


public interface ProyectoDaoInterfaz {
    void agregarProyecto(Proyecto proyecto) throws MensajeriaExcepcion;
}
