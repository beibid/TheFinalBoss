package logica.dao.excepciones;

public class DaoExcepcion extends Exception {

    public DaoExcepcion(String mensaje) {

        super(mensaje);
    }
    public DaoExcepcion(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }
}
