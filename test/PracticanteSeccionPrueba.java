

import org.junit.jupiter.api.Test;
import logica.dao.objetos.PracticanteSeccionDao;
import logica.dominio.PracticanteSeccion;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class PracticanteSeccionPrueba {
    @Test
    public void insertarPracticanteSeccion(){
        PracticanteSeccion nuevoPracticanteSeccion = new PracticanteSeccion("S20013456", "3");
        PracticanteSeccionDao practicanteSeccion = new PracticanteSeccionDao();

        assertDoesNotThrow(() -> practicanteSeccion.agregarPracticanteSeccion(nuevoPracticanteSeccion));
    }
}
