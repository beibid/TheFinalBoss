

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

    @Test
    public void inactivarProfesor(){
        ProfesorDao professor = new ProfesorDao();

        assertDoesNotThrow(()-> professor.inactivarProfesor("P001"));
    }

    @Test
    public void modificarProfesor(){
        Profesor profesorModificado = new Profesor("P001", Turno.Mixto, "Carlos", "Ramirez", "Torres", "5678", Estado.Inactivo);
        ProfesorDao profesor = new ProfesorDao();

        assertDoesNotThrow(() -> profesor.modificarProfesor("P001", profesorModificado));
    }
}
