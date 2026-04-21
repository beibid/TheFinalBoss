import org.junit.jupiter.api.Test;
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PracticantePrueba {

    @Test
    public void pruebaInsertarPracticanteExitoso() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S20050000", "Ninguna", Genero.Masculino, "Miguel", "Castro", "Lara", "pass50", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoDuplicado() {
        Practicante practicante = new Practicante("S20031000", "Ninguna", Genero.Masculino, "Miguel", "Castro", "Lara", "pass31", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante(null, "Ninguna", Genero.Masculino, "Miguel", "Castro", "Lara", "pass31", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S20050001", "Ninguna", Genero.Masculino, null, "Castro", "Lara", "pass31", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInactivarPracticanteExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S20050000");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarPracticanteAlternoNoExistente() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S99999999");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarPracticanteExcepcionMatriculaNula() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.inactivarPracticante(null));
    }

    @Test
    public void pruebaInactivarPracticanteExcepcionMatriculaVacia() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteExitoso() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S20050000", "Nahuatl", Genero.Masculino, "Miguel", "Castro", "Lara", "pass50nuevo", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S20050000", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoNoExistente() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S99999999", "Ninguna", Genero.Masculino, "Miguel", "Castro", "Lara", "pass31", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S99999999", practicante);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S20050000", "Ninguna", Genero.Masculino, null, "Castro", "Lara", "pass31", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante("S20050000", practicante));
    }

    @Test
    public void pruebaModificarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante("S20050000", "Ninguna", Genero.Masculino, "Miguel", "Castro", "Lara", "pass31", Estado.Activo);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante(null, practicante));
    }
}