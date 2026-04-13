package logica.dao.excepciones;

public class MensajeriaExcepcion extends Exception {

    public MensajeriaExcepcion(String mensaje) {

        super(mensaje);
    }
    public MensajeriaExcepcion(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }
}
