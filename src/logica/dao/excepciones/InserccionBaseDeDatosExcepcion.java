package logica.dao.excepciones;

public class InserccionBaseDeDatosExcepcion extends Exception {

    public InserccionBaseDeDatosExcepcion(String mensaje) {

        super(mensaje);
    }
    public InserccionBaseDeDatosExcepcion(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }
}
