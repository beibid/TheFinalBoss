import logica.dao.excepciones.UsuariosExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.PracticanteSeccionDao;
import logica.dominio.PracticanteSeccion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PracticanteSeccionPrueba {

    @Test
    public void pruebaAgregarPracticanteSeccionExitoso() throws UsuariosExcepcion {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion("S20050000", "S050");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(practicanteSeccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionAlternaDuplicada() {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion("S20013461", "S05");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(practicanteSeccion));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion(null, "S050");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(practicanteSeccion));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionSeccionNula() {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion("S20050000", null);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(practicanteSeccion));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExitoso() throws UsuariosExcepcion {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion("S20050000", "S050");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S20050000", "S050", practicanteSeccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionAlternaNoExistente() throws UsuariosExcepcion {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion("S99999999", "S999");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S99999999", "S999", practicanteSeccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion(null, "S050");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.modificarPracticanteSeccion(null, "S050", practicanteSeccion));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionSeccionNula() {
        PracticanteSeccion practicanteSeccion = new PracticanteSeccion("S20050000", null);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.modificarPracticanteSeccion("S20050000", null, practicanteSeccion));
    }
}