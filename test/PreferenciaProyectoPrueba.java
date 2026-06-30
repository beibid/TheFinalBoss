import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PreferenciaProyectoDao;
import logica.dominio.PreferenciaProyecto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PreferenciaProyectoPrueba {

    @Test
    public void pruebaGuardarPreferenciasExitoso() {
        List<Integer> idProyectos = Arrays.asList(1, 2, 3);
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertDoesNotThrow(() -> preferenciaProyectoDao.guardarPreferencias("S24013282", idProyectos));
    }

    @Test
    public void pruebaGuardarPreferenciasAlternoOtraPracticante() {
        List<Integer> idProyectos = Arrays.asList(1, 2, 3);
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertDoesNotThrow(() -> preferenciaProyectoDao.guardarPreferencias("S22019874", idProyectos));
    }

    @Test
    public void pruebaGuardarPreferenciasAlternoUnaPreferencia() {
        List<Integer> idProyectos = Arrays.asList(2);
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertDoesNotThrow(() -> preferenciaProyectoDao.guardarPreferencias("S23010045", idProyectos));
    }

    @Test
    public void pruebaGuardarPreferenciasAlternoDosPreferencias() {
        List<Integer> idProyectos = Arrays.asList(1, 3);
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertDoesNotThrow(() -> preferenciaProyectoDao.guardarPreferencias("S21007654", idProyectos));
    }

    @Test
    public void pruebaGuardarPreferenciasAlternoListaVacia() {
        List<Integer> idProyectos = new ArrayList<>();
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertDoesNotThrow(() -> preferenciaProyectoDao.guardarPreferencias("S24013290", idProyectos));
    }

    @Test
    public void pruebaGuardarPreferenciasAlternoProyectosDistintos() {
        List<Integer> idProyectos = Arrays.asList(2, 5);
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertDoesNotThrow(() -> preferenciaProyectoDao.guardarPreferencias("S24013282", idProyectos));
    }

    @Test
    public void pruebaGuardarPreferenciasExcepcionMatriculaNula() {
        List<Integer> idProyectos = Arrays.asList(1, 2, 3);
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertThrows(UsuariosExcepcion.class,
                () -> preferenciaProyectoDao.guardarPreferencias(null, idProyectos));
    }

    @Test
    public void pruebaGuardarPreferenciasExcepcionListaNula() {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertThrows(UsuariosExcepcion.class,
                () -> preferenciaProyectoDao.guardarPreferencias("S24013282", null));
    }

    @Test
    public void pruebaObtenerPreferenciasExitoso() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S22019874");
        assertNotNull(preferencias);
    }

    @Test
    public void pruebaObtenerPreferenciasAlternoOtraPracticante() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S21007654");
        assertNotNull(preferencias);
    }

    @Test
    public void pruebaObtenerPreferenciasAlternoListaNoVacia() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S22019874");
        assertTrue(preferencias.size() > 0);
    }

    @Test
    public void pruebaObtenerPreferenciasCantidadCorrecta() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S22019874");
        assertEquals(3, preferencias.size());
    }

    @Test
    public void pruebaObtenerPreferenciasTienenMatricula() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S22019874");
        for (PreferenciaProyecto preferencia : preferencias) {
            assertNotNull(preferencia.getMatricula());
        }
    }

    @Test
    public void pruebaObtenerPreferenciasTienenPrioridad() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S22019874");
        for (PreferenciaProyecto preferencia : preferencias) {
            assertTrue(preferencia.getPrioridad() > 0);
        }
    }

    @Test
    public void pruebaObtenerPreferenciasMatriculaCorrecta() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S21007654");
        for (PreferenciaProyecto preferencia : preferencias) {
            assertEquals("S21007654", preferencia.getMatricula());
        }
    }

    @Test
    public void pruebaObtenerPreferenciasAlternoUnaPreferencia() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S23010045");
        assertEquals(1, preferencias.size());
    }

    @Test
    public void pruebaObtenerPreferenciasAlternoSinPreferencias() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S24013290");
        assertEquals(0, preferencias.size());
    }

    @Test
    public void pruebaObtenerPreferenciasAlternoMatriculaInexistente() throws UsuariosExcepcion {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        List<PreferenciaProyecto> preferencias = preferenciaProyectoDao.obtenerPreferencias("S99999999");
        assertEquals(0, preferencias.size());
    }

    @Test
    public void pruebaObtenerPreferenciasExcepcionMatriculaNula() {
        PreferenciaProyectoDao preferenciaProyectoDao = new PreferenciaProyectoDao();
        assertThrows(UsuariosExcepcion.class,
                () -> preferenciaProyectoDao.obtenerPreferencias(null));
    }
}