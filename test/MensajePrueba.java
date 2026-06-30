import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.MensajeDao;
import logica.dominio.Mensaje;
import logica.dominio.MensajeVista;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MensajePrueba {

    @Test
    public void pruebaAgregarMensajeExitoso() throws MensajeriaExcepcion {
        Mensaje mensaje = new Mensaje("Hola, te aviso que tu reporte del mes de junio ya fue revisado.");
        MensajeDao mensajeDao = new MensajeDao();
        int filasAfectadas = mensajeDao.agregarMensaje(mensaje);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarMensajeAlternoContenidoCorto() throws MensajeriaExcepcion {
        Mensaje mensaje = new Mensaje("Por favor revisa tu reporte.");
        MensajeDao mensajeDao = new MensajeDao();
        int filasAfectadas = mensajeDao.agregarMensaje(mensaje);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarMensajeAlternoContenidoLargo() throws MensajeriaExcepcion {
        Mensaje mensaje = new Mensaje("Estimado practicante, me complace informarte que tu reporte mensual correspondiente al mes de junio del año 2026 ha sido revisado satisfactoriamente por el profesor responsable de tu experiencia educativa.");
        MensajeDao mensajeDao = new MensajeDao();
        int filasAfectadas = mensajeDao.agregarMensaje(mensaje);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarMensajeAlternoConSignosPuntuacion() throws MensajeriaExcepcion {
        Mensaje mensaje = new Mensaje("Recuerda: debes entregar tu reporte antes del viernes 28 de junio. ¡Es importante!");
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

    @Test
    public void pruebaObtenerMensajesRecibidosExitoso() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesRecibidos(1);
        assertNotNull(mensajes);
    }

    @Test
    public void pruebaObtenerMensajesRecibidosAlternoOtroUsuario() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesRecibidos(3);
        assertNotNull(mensajes);
    }

    @Test
    public void pruebaObtenerMensajesRecibidosAlternoListaNoVacia() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesRecibidos(1);
        assertEquals(1, mensajes.size());
    }

    @Test
    public void pruebaObtenerMensajesRecibidosAlternoIdNoExistente() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesRecibidos(9999);
        assertEquals(0, mensajes.size());
    }

    @Test
    public void pruebaObtenerMensajesEnviadosExitoso() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesEnviados(2);
        assertNotNull(mensajes);
    }

    @Test
    public void pruebaObtenerMensajesEnviadosAlternoOtroUsuario() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesEnviados(4);
        assertNotNull(mensajes);
    }

    @Test
    public void pruebaObtenerMensajesEnviadosAlternoListaNoVacia() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesEnviados(2);
        assertEquals(1, mensajes.size());
    }

    @Test
    public void pruebaObtenerMensajesEnviadosAlternoIdNoExistente() throws MensajeriaExcepcion {
        MensajeDao mensajeDao = new MensajeDao();
        List<MensajeVista> mensajes = mensajeDao.obtenerMensajesEnviados(9999);
        assertEquals(0, mensajes.size());
    }
}