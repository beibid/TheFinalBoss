package logica.dao.excepciones;

public class UsuariosExcepcion extends Exception {
    public UsuariosExcepcion(String mensaje) {
        super(mensaje);
    }

    public UsuariosExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}