package logica.dao.interfaces;

import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.Buzon;

public interface BuzonDaoInterfaz {
    void agregarBuzon(Buzon buzon) throws MensajeriaExcepcion;
}