import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteSeccionDao;
import logica.dominio.PracticanteSeccion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PracticanteSeccionPrueba {

    @Test
    public void pruebaAgregarPracticanteSeccionExitoso() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "NRC-11111", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionAlternoOtraPracticante() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S22019874", "NRC-11111", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionAlternoOtraSeccion() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S23010045", "NRC-22222", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionDuplicadaLanzaExcepcion() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "NRC-11111", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(RegistroDuplicadoExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion ps = new PracticanteSeccion(null, "NRC-11111", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionSeccionNula() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", null, 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionMatriculaNoExistente() {
        PracticanteSeccion ps = new PracticanteSeccion("S99999999", "NRC-11111", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExitoso() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "NRC-22222", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S24013282", "NRC-11111", 1, ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionAlternoOtraPracticante() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S22019874", "NRC-22222", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S22019874", "NRC-11111", 1, ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionAlternoNoExistente() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S99999999", "NRC-99999", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S99999999", "NRC-99999", 1, ps);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion ps = new PracticanteSeccion(null, "NRC-11111", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.modificarPracticanteSeccion(null, "NRC-11111", 1, ps));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionSeccionNula() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", null, 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.modificarPracticanteSeccion("S24013282", null, 1, ps));
    }
}