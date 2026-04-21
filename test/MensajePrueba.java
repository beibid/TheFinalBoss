import logica.dao.excepciones.MensajeriaExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.MensajeDao;
import logica.dominio.Mensaje;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MensajePrueba {

    @Test
    public void pruebaAgregarMensajeExitoso() throws MensajeriaExcepcion {
        Mensaje mensaje = new Mensaje("Mensaje de prueba 50");
        MensajeDao mensajeDao = new MensajeDao();
        int filasAfectadas = mensajeDao.agregarMensaje(mensaje);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarMensajeAlternoContenidoLargo() throws MensajeriaExcepcion {
        Mensaje mensaje = new Mensaje("Este es un mensaje de prueba con contenido muy largo para verificar que el sistema lo maneja correctamente sin truncar");
        MensajeDao mensajeDao = new MensajeDao();
        int filasAfectadas = mensajeDao.agregarMensaje(mensaje);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarMensajeExcepcionContenidoNulo() {
        Mensaje mensaje = new Mensaje(null);
        MensajeDao mensajeDao = new MensajeDao();
        assertThrows(MensajeriaExcepcion.class, () -> mensajeDao.agregarMensaje(mensaje));
    }

    @Test
    public void pruebaAgregarMensajeExcepcionContenidoExcedeLimite() {
        Mensaje mensaje = new Mensaje("a".repeat(501));
        MensajeDao mensajeDao = new MensajeDao();
        assertThrows(MensajeriaExcepcion.class, () -> mensajeDao.agregarMensaje(mensaje));
    }
}