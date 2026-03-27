package logica.dao.excepciones;

public class InserccionUsuarioExcepcion extends Exception {

    public InserccionUsuarioExcepcion(String mensaje) {

        super(mensaje);
    }
    public InserccionUsuarioExcepcion(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }
}
