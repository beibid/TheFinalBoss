package logica.dao.interfaces;

import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.Mensaje;

public interface MensajeDaoInterfaz {
    void agregarMensaje(Mensaje mensaje) throws MensajeriaExcepcion;
}
