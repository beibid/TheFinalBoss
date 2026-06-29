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
        Seccion seccion = new Seccion("NRC-11111", 1, "PROF-001");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionAlternoOtroProfesor() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        Seccion seccion = new Seccion("NRC-22222", 1, "PROF-002");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionAlternoMismoNRCPeriodoDiferente() throws UsuariosExcepcion, RegistroDuplicadoExcepcion {
        Seccion seccion = new Seccion("NRC-33333", 1, "PROF-001");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.agregarSeccion(seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarSeccionDuplicadaEnMismoPeriodoLanzaExcepcion() {
        Seccion seccion = new Seccion("NRC-11111", 1, "PROF-001");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(RegistroDuplicadoExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionNoSeccionNulo() {
        Seccion seccion = new Seccion(null, 1, "PROF-001");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionProfesorNulo() {
        Seccion seccion = new Seccion("NRC-44444", 1, null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaAgregarSeccionExcepcionProfesorNoExistente() {
        Seccion seccion = new Seccion("NRC-55555", 1, "PROF-999");
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.agregarSeccion(seccion));
    }

    @Test
    public void pruebaModificarSeccionExitoso() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("NRC-11111", 1, "PROF-002");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("NRC-11111", seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoCambioProfesor() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("NRC-22222", 1, "PROF-001");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("NRC-22222", seccion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionAlternoNoExistente() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("NRC-99999", 1, "PROF-001");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion("NRC-99999", seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionNoSeccionNuloRetornaCero() throws UsuariosExcepcion {
        Seccion seccion = new Seccion("NRC-11111", 1, "PROF-001");
        SeccionDao seccionDao = new SeccionDao();
        int filasAfectadas = seccionDao.modificarSeccion(null, seccion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarSeccionExcepcionProfesorNulo() {
        Seccion seccion = new Seccion("NRC-11111", 1, null);
        SeccionDao seccionDao = new SeccionDao();
        assertThrows(UsuariosExcepcion.class, () -> seccionDao.modificarSeccion("NRC-11111", seccion));
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
        List<Seccion> secciones = seccionDao.obtenerSeccionesPorProfesor("PROF-001");
        assertNotNull(secciones);
    }

    @Test
    public void pruebaObtenerSeccionesPorProfesorAlternoOtroProfesor() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSeccionesPorProfesor("PROF-002");
        assertNotNull(secciones);
    }

    @Test
    public void pruebaObtenerSeccionesPorProfesorNoExistente() throws UsuariosExcepcion {
        SeccionDao seccionDao = new SeccionDao();
        List<Seccion> secciones = seccionDao.obtenerSeccionesPorProfesor("PROF-999");
        assertEquals(0, secciones.size());
    }
}