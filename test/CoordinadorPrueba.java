

import org.junit.jupiter.api.Test;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import logica.dominio.enums.Estado;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CoordinadorPrueba {
    @Test
    public void insertarCoordinador(){
        Coordinador nuevoCoordinador = new Coordinador("22331083", "Lolo", "Espino", "Nose", "EAHDSJAD", Estado.Activo);
        CoordinadorDao coordinador = new CoordinadorDao();

        assertDoesNotThrow(() -> coordinador.insertarCoordinador(nuevoCoordinador));
    }

    @Test
    public void inactivarCoordinador(){
        CoordinadorDao cordinador = new CoordinadorDao();

        assertDoesNotThrow(() -> cordinador.inactivarCoordinador("C001"));
    }

    @Test
    public void modificarCoordinador(){
        Coordinador coordinadorModificado = new Coordinador("C001", "Maria", "Flores", "Martinez", "abcd", Estado.Inactivo);
        CoordinadorDao coordinador = new CoordinadorDao();

        assertDoesNotThrow(() -> coordinador.modificarCoordinador("C001", coordinadorModificado));
    }
}
