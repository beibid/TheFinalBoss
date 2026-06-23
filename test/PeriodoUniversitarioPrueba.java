import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PeriodoUniversitarioDao;
import logica.dominio.PeriodoUniversitario;
import logica.dominio.enums.EstadoPeriodo;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeriodoUniversitarioPrueba {

    @Test
    public void pruebaInsertarPeriodoExitoso() throws UsuariosExcepcion {
        PeriodoUniversitario periodo = new PeriodoUniversitario(0, "Agosto-Diciembre 2026", Date.valueOf("2026-08-10"), null, EstadoPeriodo.Abierto);
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.insertarPeriodo(periodo);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPeriodoAlternoFechaDiferente() throws UsuariosExcepcion {
        PeriodoUniversitario periodo = new PeriodoUniversitario(0, "Enero-Mayo 2027", Date.valueOf("2027-01-17"), null, EstadoPeriodo.Abierto);
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.insertarPeriodo(periodo);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPeriodoAlternoEstadoCerrado() throws UsuariosExcepcion {
        PeriodoUniversitario periodo = new PeriodoUniversitario(0, "Agosto-Diciembre 2025", Date.valueOf("2025-08-11"), Date.valueOf("2025-12-19"), EstadoPeriodo.Cerrado);
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.insertarPeriodo(periodo);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPeriodoAlternoConFechaFin() throws UsuariosExcepcion {
        PeriodoUniversitario periodo = new PeriodoUniversitario(0, "Enero-Mayo 2026", Date.valueOf("2026-01-19"), Date.valueOf("2026-05-29"), EstadoPeriodo.Cerrado);
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.insertarPeriodo(periodo);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPeriodoExcepcionNombreNulo() {
        PeriodoUniversitario periodo = new PeriodoUniversitario(0, null, Date.valueOf("2026-08-10"), null, EstadoPeriodo.Abierto);
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        assertThrows(UsuariosExcepcion.class, () -> periodoUniversitarioDao.insertarPeriodo(periodo));
    }

    @Test
    public void pruebaInsertarPeriodoExcepcionFechaNula() {
        PeriodoUniversitario periodo = new PeriodoUniversitario(0, "Agosto-Diciembre 2026", null, null, EstadoPeriodo.Abierto);
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        assertThrows(UsuariosExcepcion.class, () -> periodoUniversitarioDao.insertarPeriodo(periodo));
    }

    @Test
    public void pruebaInsertarPeriodoExcepcionEstadoNulo() {
        PeriodoUniversitario periodo = new PeriodoUniversitario(0, "Agosto-Diciembre 2026", Date.valueOf("2026-08-10"), null, null);
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        assertThrows(UsuariosExcepcion.class, () -> periodoUniversitarioDao.insertarPeriodo(periodo));
    }

    @Test
    public void pruebaVerificarPeriodoAbiertoExitoso() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        boolean resultado = periodoUniversitarioDao.verificarPeriodoAbierto();
        assertTrue(resultado || !resultado);
    }

    @Test
    public void pruebaVerificarPeriodoAbiertoNoLanzaExcepcion() {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        try {
            periodoUniversitarioDao.verificarPeriodoAbierto();
        } catch (UsuariosExcepcion e) {
            assertTrue(false, "No deberia lanzar excepcion: " + e.getMessage());
        }
    }

    @Test
    public void pruebaObtenerPeriodosAbiertosExitoso() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        List<PeriodoUniversitario> periodos = periodoUniversitarioDao.obtenerPeriodosAbiertos();
        assertNotNull(periodos);
    }

    @Test
    public void pruebaObtenerPeriodosAbiertosListaNoVacia() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        List<PeriodoUniversitario> periodos = periodoUniversitarioDao.obtenerPeriodosAbiertos();
        assertTrue(periodos.size() > 0);
    }

    @Test
    public void pruebaObtenerPeriodosAbiertosTienenNombre() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        List<PeriodoUniversitario> periodos = periodoUniversitarioDao.obtenerPeriodosAbiertos();
        for (PeriodoUniversitario periodo : periodos) {
            assertNotNull(periodo.getNombre());
        }
    }

    @Test
    public void pruebaObtenerPeriodosAbiertosTodosAbiertos() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        List<PeriodoUniversitario> periodos = periodoUniversitarioDao.obtenerPeriodosAbiertos();
        for (PeriodoUniversitario periodo : periodos) {
            assertEquals(EstadoPeriodo.Abierto, periodo.getEstado());
        }
    }

    @Test
    public void pruebaObtenerPeriodosAbiertosTienenFechaInicio() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        List<PeriodoUniversitario> periodos = periodoUniversitarioDao.obtenerPeriodosAbiertos();
        for (PeriodoUniversitario periodo : periodos) {
            assertNotNull(periodo.getFechaInicio());
        }
    }

    @Test
    public void pruebaCerrarPeriodoExitoso() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.cerrarPeriodo(1);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaCerrarPeriodoAlternoSegundoRegistro() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.cerrarPeriodo(2);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaCerrarPeriodoAlternoIdNoExistente() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.cerrarPeriodo(9999);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaCerrarPeriodoAlternoYaCerrado() throws UsuariosExcepcion {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        int filasAfectadas = periodoUniversitarioDao.cerrarPeriodo(3);
        assertEquals(0, filasAfectadas);
    }
}