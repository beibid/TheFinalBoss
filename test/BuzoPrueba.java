

import org.junit.jupiter.api.Test;
import logica.dao.objetos.BuzonDao;
import logica.dominio.Buzon;
import logica.dominio.enums.RolMensaje;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BuzoPrueba {
    @Test
    public void insertarMensajeEnBuzon(){
        Buzon nuevaEntradaEnBuzon = new Buzon(RolMensaje.Remitente, 1,  3);
        BuzonDao buzon = new BuzonDao();

        assertDoesNotThrow(() -> buzon.agregarBuzon(nuevaEntradaEnBuzon));
    }
}
