package logica.dao.interfaces;

import logica.dominio.Proyecto;
import logica.dao.excepciones.MensajeriaExcepcion;

public interface ProyectoDaoInterfaz {
    int agregarProyecto(Proyecto proyecto) throws MensajeriaExcepcion;
    int modificarProyecto(int idProyecto, Proyecto proyecto) throws MensajeriaExcepcion;
}