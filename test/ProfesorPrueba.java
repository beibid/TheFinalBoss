

import org.junit.jupiter.api.Test;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class ProfesorPrueba {
    @Test
    public void insertarProfesor() throws UsuariosExcepcion{
        Profesor nuevoProfesor = new Profesor("456786", Turno.Matutino, "Daniel", "Fernandez", "Dominguez", "ESOTILIN233", Estado.Activo);
        ProfesorDao profesor = new ProfesorDao();

        assertDoesNotThrow(() -> profesor.insertarProfesor(nuevoProfesor));
    }
}
