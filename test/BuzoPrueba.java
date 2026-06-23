import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.BuzonDao;
import logica.dominio.Buzon;
import logica.dominio.enums.RolMensaje;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuzoPrueba {

    @Test
    public void pruebaAgregarBuzonRolDestinatarioExitoso() throws MensajeriaExcepcion {
        Buzon buzon = new Buzon(RolMensaje.destinatario, 1, 1);
        BuzonDao buzonDao = new BuzonDao();
        int filasAfectadas = buzonDao.agregarBuzon(buzon);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarBuzonAlternoRolRemitente() throws MensajeriaExcepcion {
        Buzon buzon = new Buzon(RolMensaje.Remitente, 1, 2);
        BuzonDao buzonDao = new BuzonDao();
        int filasAfectadas = buzonDao.agregarBuzon(buzon);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarBuzonAlternoOtroMensaje() throws MensajeriaExcepcion {
        Buzon buzon = new Buzon(RolMensaje.destinatario, 2, 3);
        BuzonDao buzonDao = new BuzonDao();
        int filasAfectadas = buzonDao.agregarBuzon(buzon);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarBuzonAlternoOtroUsuario() throws MensajeriaExcepcion {
        Buzon buzon = new Buzon(RolMensaje.Remitente, 2, 4);
        BuzonDao buzonDao = new BuzonDao();
        int filasAfectadas = buzonDao.agregarBuzon(buzon);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarBuzonExcepcionIdMensajeNoExistente() {
        Buzon buzon = new Buzon(RolMensaje.destinatario, 9999, 1);
        BuzonDao buzonDao = new BuzonDao();
        assertThrows(MensajeriaExcepcion.class, () -> buzonDao.agregarBuzon(buzon));
    }

    @Test
    public void pruebaAgregarBuzonExcepcionIdUsuarioNoExistente() {
        Buzon buzon = new Buzon(RolMensaje.destinatario, 1, 9999);
        BuzonDao buzonDao = new BuzonDao();
        assertThrows(MensajeriaExcepcion.class, () -> buzonDao.agregarBuzon(buzon));
    }

    @Test
    public void pruebaAgregarBuzonExcepcionAmbosIdsNoExistentes() {
        Buzon buzon = new Buzon(RolMensaje.Remitente, 9998, 9999);
        BuzonDao buzonDao = new BuzonDao();
        assertThrows(MensajeriaExcepcion.class, () -> buzonDao.agregarBuzon(buzon));
    }
}