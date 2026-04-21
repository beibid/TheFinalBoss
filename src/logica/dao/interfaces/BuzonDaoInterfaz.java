package logica.dao.interfaces;

import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.Buzon;

public interface BuzonDaoInterfaz {
    int agregarBuzon(Buzon buzon) throws MensajeriaExcepcion;
}