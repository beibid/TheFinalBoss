

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

    @Test
    public void inactivarPracticante() {
        PracticanteDao practicante = new PracticanteDao();
        assertDoesNotThrow(() -> practicante.inactivarPracticante("S20013458"));


    }

    @Test
    public void modificarPracticante(){
        Practicante practicanteModificado = new Practicante("S20013460", "Ninguna", Genero.Femenino, "Laura", "Rojas", "Medina", "pass6", Estado.Activo);
        PracticanteDao practicante = new PracticanteDao();

        assertDoesNotThrow(() -> practicante.modificarPracticante("S20013460", practicanteModificado));

    }
}
