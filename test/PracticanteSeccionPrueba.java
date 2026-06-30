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
        PracticanteSeccion ps = new PracticanteSeccion("S22019874", "1248", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionAlternoOtraPracticante() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S23010045", "1248", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionAlternoOtraSeccion() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S21007654", "1369", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarPracticanteSeccionDuplicadaLanzaExcepcion() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "1248", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(RegistroDuplicadoExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaAgregarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion ps = new PracticanteSeccion(null, "1248", 1);
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
    public void pruebaAgregarPracticanteSeccionExcepcionMatriculaInexistente() {
        PracticanteSeccion ps = new PracticanteSeccion("S99999999", "1248", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteSeccionDao.agregarPracticanteSeccion(ps));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExitoso() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", "1369", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S24013282", "1248", 1, ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionAlternoOtraPracticante() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S012452", "1369", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S012452", "1248", 1, ps);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionAlternoNoExistente() throws UsuariosExcepcion {
        PracticanteSeccion ps = new PracticanteSeccion("S99999999", "1248", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        int filasAfectadas = practicanteSeccionDao.modificarPracticanteSeccion("S99999999", "1248", 1, ps);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionMatriculaNula() {
        PracticanteSeccion ps = new PracticanteSeccion(null, "1248", 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class,
                () -> practicanteSeccionDao.modificarPracticanteSeccion(null, "1248", 1, ps));
    }

    @Test
    public void pruebaModificarPracticanteSeccionExcepcionSeccionNula() {
        PracticanteSeccion ps = new PracticanteSeccion("S24013282", null, 1);
        PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();
        assertThrows(UsuariosExcepcion.class,
                () -> practicanteSeccionDao.modificarPracticanteSeccion("S24013282", null, 1, ps));
    }
}