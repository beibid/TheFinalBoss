import org.junit.jupiter.api.Test;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProfesorPrueba {

    @Test
    public void pruebaInsertarProfesorExitoso() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("P050", Turno.Matutino, "Elena", "Vargas", "pass50", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.insertarProfesor(profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarProfesorAlternoDuplicado() {
        Profesor profesor = new Profesor("P031", Turno.Matutino, "Elena", "Vargas",  "pass31", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionNumPersonalNulo() {
        Profesor profesor = new Profesor(null, Turno.Matutino, "Elena", "Vargas", "pass31", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionNombreNulo() {
        Profesor profesor = new Profesor("P051", Turno.Matutino, null, "Vargas",  "pass31", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaModificarProfesorExitoso() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("P050", Turno.Vespertino, "Elena", "Vargas", "pass50nuevo", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("P050", profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorAlternoNoExistente() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("P999", Turno.Vespertino, "Elena", "Vargas",  "pass31", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("P999", profesor);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorExcepcionNombreNulo() {
        Profesor profesor = new Profesor("P050", Turno.Vespertino, null, "Vargas", "pass31", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.modificarProfesor("P050", profesor));
    }

    @Test
    public void pruebaModificarProfesorExcepcionNumPersonalNulo() {
        Profesor profesor = new Profesor("P050", Turno.Vespertino, "Elena", "Vargas",  "pass31", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.modificarProfesor(null, profesor));
    }

    @Test
    public void pruebaInactivarProfesorExitoso() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("P050");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorAlternoNoExistente() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("P999");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorExcepcionNumPersonalNulo() {
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.inactivarProfesor(null));
    }

    @Test
    public void pruebaInactivarProfesorExcepcionNumPersonalVacio() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("");
        assertEquals(0, filasAfectadas);
    }
}