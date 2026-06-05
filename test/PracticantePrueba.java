import org.junit.jupiter.api.Test;
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;
import logica.dao.excepciones.UsuariosExcepcion;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PracticantePrueba {

    @Test
    public void pruebaInsertarPracticanteExitoso() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S20050000", "Ninguna", Genero.Masculino, "Miguel", "Castro", "pass50", "migue@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoDuplicado() {
        Practicante practicante = new Practicante("S24013287", "Ninguna", Genero.Masculino, "Miguel", "Castro", "pass31", "migue2@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante(null, "Ninguna", Genero.Masculino, "Miguel", "Castro", "pass31", "migue3@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S20050001", "Ninguna", Genero.Masculino, null, "Castro", "pass31", "migue4@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInactivarPracticanteExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S24013287");
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
    public void pruebaInactivarPracticanteExcepcionMatriculaVacia() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.inactivarPracticante(""));
    }

    @Test
    public void pruebaModificarPracticanteExitoso() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S24897654", "Nahuatl", Genero.Femenino, "Lucy", "Fernandez", "pass50nuevo", "lucc@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S24897654", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoNoExistente() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S99999999", "Ninguna", Genero.Masculino, "Miguel", "Castro", "pass31", "migue@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S99999999", practicante);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S24897654", "Ninguna", Genero.Femenino, null, "Fernandez", "pass31", "lucc@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante("S24897654", practicante));
    }

    @Test
    public void pruebaModificarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante("S24897654", "Ninguna", Genero.Femenino, "Lucy", "Fernandez", "pass31", "lucc@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante(null, practicante));
    }

    @Test
    public void pruebaModificarPracticanteExcepcionMatriculaVacia() {
        Practicante practicante = new Practicante("S24897654", "Ninguna", Genero.Femenino, "Lucy", "Fernandez", "pass31", "lucc@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante("", practicante));
    }

    @Test
    public void pruebaObtenerPracticantesActivosExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesActivosListaNoVacia() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
        assertTrue(practicantes.size() > 0);
    }

    @Test
    public void pruebaAsignarProyectoExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.asignarProyecto("S24897654", 1);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAsignarProyectoAlternoPracticanteNoExistente() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.asignarProyecto("S9999999", 1));
    }

    @Test
    public void pruebaAsignarProyectoAlternoProyectoNoExistente() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.asignarProyecto("S24897654", 99));
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(3);
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorAlternoNoExistente() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(9999);
        assertEquals(0, practicantes.size());
    }
}