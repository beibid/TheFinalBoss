

import logica.dao.objetos.SeccionDao;
import logica.dominio.Seccion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;


public class SeccionPrueba {
    @Test
    public void insertarSeccion(){
        Seccion nuevaSeccion = new Seccion("3", "febrero-julio 2026");
        SeccionDao seccion = new SeccionDao();

        assertDoesNotThrow(() -> seccion.agregarSeccion(nuevaSeccion));
    }

    @Test
    public void modificarSeccion(){
        Seccion seccionModificada = new Seccion("S04", "2025-2026");
        SeccionDao seccion = new SeccionDao();

        assertDoesNotThrow(() -> seccion.modificarSeccion("S04", seccionModificada));
    }
}
