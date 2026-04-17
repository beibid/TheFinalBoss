

import org.junit.jupiter.api.Test;
import logica.dao.objetos.MensajeDao;
import logica.dominio.Mensaje;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class MensajePrueba {
    @Test
    public void insertarMensaje(){
        Mensaje nuevoMensaje = new Mensaje("Hola puto");
        MensajeDao mensaje = new MensajeDao();

        assertDoesNotThrow(() -> mensaje.agregarMensaje(nuevoMensaje));
    }
}
