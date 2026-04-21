package logica.dao.interfaces;

import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.Mensaje;

public interface MensajeDaoInterfaz {
    int agregarMensaje(Mensaje mensaje) throws MensajeriaExcepcion;
}