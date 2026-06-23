import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.SeccionDao;
import logica.dominio.Seccion;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeccionPrueba {

    @Test
    public void pruebaAgregarSeccionExitoso() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("A010", "2026-2");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionAlternoPeriodoDiferente() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("B010", "2026-1");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionAlternoLetraDiferente() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("C010", "2026-2");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionDuplicadaLanzaExcepcion() {
        Seccion seccion = new Seccion("A010", "2026-2");
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
        Seccion seccion = new Seccion("D010", null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaModificarSeccionExitoso() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("A010", "2027-1");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("A010", seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoCambioPeriodo() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("B010", "2027-2");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("B010", seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoNoExistente() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("Z999", "2026-2");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("Z999", seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoNoSeccionNuloRetornaCero() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("A010", "2027-1");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion(null, seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionExcepcionPeriodoNulo() {
        Seccion seccion = new Seccion("A010", null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.modificarSeccion("A010", seccion));
    }

    @Test
    public void pruebaObtenerSeccionesExitoso() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSecciones();
        assertNotNull(secciones);
    }

    @Test
    public void pruebaObtenerSeccionesListaNoVacia() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSecciones();
        assertTrue(secciones.size() > 0);
    }

    @Test
    public void pruebaObtenerSeccionesTienenNoSeccion() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSecciones();
        for (Seccion seccion : secciones) {
            assertNotNull(seccion.getNoSeccion());
        }
    }

    @Test
    public void pruebaObtenerSeccionesTienenPeriodo() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSecciones();
        for (Seccion seccion : secciones) {
            assertNotNull(seccion.getPeriodo());
        }
    }
}