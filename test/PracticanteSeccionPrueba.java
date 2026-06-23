import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteSeccionDao;
import logica.dao.objetos.SeccionDao;
import logica.dominio.PracticanteSeccion;
import logica.dominio.Seccion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PracticanteSeccionPrueba {

    @Test
    public void pruebaAgregarPracticanteSeccionExitoso() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        seccionDao.agregarSeccion(new Seccion("E100", "2026-2"));
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "E100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionAlternoOtraPracticante() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S22019874", "E100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionAlternoOtraSeccion() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        seccionDao.agregarSeccion(new Seccion("F100", "2026-2"));
        PracticanteSeccion ps = new PracticanteSeccion("S23010045", "F100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionDuplicadaLanzaExcepcion() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "E100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion ps = new PracticanteSeccion(null, "E100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionSeccionNula() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", null);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExitoso() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "E100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S24013282", "E100", ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionAlternoOtraPracticante() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S22019874", "E100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S22019874", "E100", ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionAlternoNoExistente() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S99999999", "Z999");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S99999999", "Z999", ps);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion ps = new PracticanteSeccion(null, "E100");
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.modificarPracticanteSeccion(null, "E100", ps));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionSeccionNula() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", null);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.modificarPracticanteSeccion("S24013282", null, ps));
    }
}
