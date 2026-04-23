package logica.dao.excepciones;

public class RegistroDuplicadoExcepcion extends RuntimeException {
    public RegistroDuplicadoExcepcion(String mensaje) {
        super(mensaje);
    }

    public RegistroDuplicadoExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
