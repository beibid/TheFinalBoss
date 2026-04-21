import logica.dao.excepciones.MensajeriaExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.BuzonDao;
import logica.dominio.Buzon;
import logica.dominio.enums.RolMensaje;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuzoPrueba {

    @Test
    public void pruebaAgregarBuzonExitoso() throws MensajeriaExcepcion {
        Buzon buzon = new Buzon(RolMensaje.destinatario, 1, 1);
        BuzonDao buzonDao = new BuzonDao();
        int filasAfectadas = buzonDao.agregarBuzon(buzon);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarBuzonAlternoRolRemitente() throws MensajeriaExcepcion {
        Buzon buzon = new Buzon(RolMensaje.Remitente, 1, 1);
        BuzonDao buzonDao = new BuzonDao();
        int filasAfectadas = buzonDao.agregarBuzon(buzon);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarBuzonExcepcionIdMensajeInexistente() {
        Buzon buzon = new Buzon(RolMensaje.destinatario, 9999, 1);
        BuzonDao buzonDao = new BuzonDao();
        assertThrows(MensajeriaExcepcion.class, () -> buzonDao.agregarBuzon(buzon));
    }

    @Test
    public void pruebaAgregarBuzonExcepcionIdUsuarioInexistente() {
        Buzon buzon = new Buzon(RolMensaje.destinatario, 1, 9999);
        BuzonDao buzonDao = new BuzonDao();
        assertThrows(MensajeriaExcepcion.class, () -> buzonDao.agregarBuzon(buzon));
    }
}