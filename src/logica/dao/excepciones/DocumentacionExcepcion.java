package logica.dao.excepciones;

public class DocumentacionExcepcion extends RuntimeException {
    public DocumentacionExcepcion(String message) {
        super(message);
    }
    public DocumentacionExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
