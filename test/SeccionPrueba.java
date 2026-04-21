import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.SeccionDao;
import logica.dominio.Seccion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SeccionPrueba {

    @Test
    public void pruebaAgregarSeccionExitoso() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("S060", "2026-2");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionAlternaDuplicada() {
        Seccion seccion = new Seccion("S031", "2026-2");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionNoSeccionNulo() {
        Seccion seccion = new Seccion(null, "2026-2");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionPeriodoNulo() {
        Seccion seccion = new Seccion("S061", null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaModificarSeccionExitoso() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("S060", "2026-3");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("S060", seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternaNoExistente() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("S999", "2026-3");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("S999", seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoNoSeccionNulo() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("S060", "2026-3");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion(null, seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionExcepcionPeriodoNulo() {
        Seccion seccion = new Seccion("S060", null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.modificarSeccion("S060", seccion));
    }
}