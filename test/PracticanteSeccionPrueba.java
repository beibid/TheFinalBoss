

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

    @Test
    public void cambiarPracticanteSeccion(){
        PracticanteSeccion practicanteSeccionModificada = new PracticanteSeccion("S20013460", "S05");
        PracticanteSeccionDao practicanteSeccion = new PracticanteSeccionDao();

        assertDoesNotThrow(() -> practicanteSeccion.modificarPracticanteSeccion("S20013460", "S04", practicanteSeccionModificada));
    }
}
