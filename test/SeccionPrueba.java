

import logica.dao.objetos.SeccionDao;
import logica.dominio.Seccion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class SeccionPrueba {
    @Test
    public void insertarSeccion(){
        Seccion nuevaSeccion = new Seccion("3", "febrero-julio 2026");
        SeccionDao seccion = new SeccionDao();

        assertDoesNotThrow(() -> seccion.agregarSeccion(nuevaSeccion));
    }
}
