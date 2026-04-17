

import org.junit.jupiter.api.Test;
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class PracticantePrueba {
    @Test
    public void insertarPracticante() throws UsuariosExcepcion{
        Practicante nuevoPracticante = new Practicante("S2243356", "", Genero.Masculino, "Edgardo", "Martinez", "Fernandez", "ESOTUTUTU", Estado.Activo);
        PracticanteDao practicante = new PracticanteDao();

        assertDoesNotThrow(() -> practicante.insertarPracticante(nuevoPracticante)) ;
    }
}
