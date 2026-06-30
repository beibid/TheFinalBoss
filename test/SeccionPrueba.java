import logica.dao.excepciones.RegistroDuplicadoExcepcion;
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
    public void pruebaAgregarSeccionExitoso() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        Seccion seccion = new Seccion("NRC-TEST1", 1, "29345");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionAlternoOtroProfesor() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        Seccion seccion = new Seccion("NRC-TEST2", 1, "45678");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionAlternoMismoNRCPeriodoDiferente() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        Seccion seccion = new Seccion("NRC-TEST3", 1, "29345");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionDuplicadaEnMismoPeriodoLanzaExcepcion() {
        Seccion seccion = new Seccion("1248", 1, "29345");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(RegistroDuplicadoExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionNoSeccionNulo() {
        Seccion seccion = new Seccion(null, 1, "29345");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionProfesorNulo() {
        Seccion seccion = new Seccion("NRC-TEST4", 1, null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionProfesorNoExistente() {
        Seccion seccion = new Seccion("NRC-TEST5", 1, "PROF-999");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaModificarSeccionExitoso() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("1248", 1, "45678");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("1248", seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoCambioProfesor() throws UsuariosExcepcion {

        Seccion seccion = new Seccion("1369", 1, "29345");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("1369", seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoNoExistente() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("NRC-99999", 1, "29345");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("NRC-99999", seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionNoSeccionNuloRetornaCero() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("1248", 1, "29345");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion(null, seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionExcepcionProfesorNulo() {
        Seccion seccion = new Seccion("1248", 1, null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.modificarSeccion("1248", seccion));
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
    public void pruebaObtenerSeccionesTienenProfesor() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSecciones();
        for (Seccion seccion : secciones) {
            assertNotNull(seccion.getNumPersonalProfesor());
        }
    }

    @Test
    public void pruebaObtenerSeccionesPorProfesorExitoso() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSeccionesPorProfesor("29345");
        assertNotNull(secciones);
    }

    @Test
    public void pruebaObtenerSeccionesPorProfesorAlternoOtroProfesor() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSeccionesPorProfesor("45678");
        assertNotNull(secciones);
    }

    @Test
    public void pruebaObtenerSeccionesPorProfesorNoExistente() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSeccionesPorProfesor("PROF-999");
        assertEquals(0, secciones.size());
    }
}