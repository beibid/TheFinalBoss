import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ActividadDao;
import logica.dominio.Actividad;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActividadPrueba {

    @Test
    public void pruebaRegistrarActividadExitoso() throws MensajeriaExcepcion {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Analisis de requisitos del sistema");
        actividad.setDescripcion("Levantamiento de requisitos funcionales con el tutor de la organizacion");
        actividad.setFechaInicio(Date.valueOf("2026-02-03"));
        actividad.setFechaFin(Date.valueOf("2026-02-28"));
        actividad.setHorasActividad(60);
        actividad.setMatriculaPracticante("S24013282");
        ActividadDao actividadDao = new ActividadDao();
        int filasAfectadas = actividadDao.registrarActividad(actividad);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaRegistrarActividadAlternoOtraPracticante() throws MensajeriaExcepcion {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Diseno de base de datos");
        actividad.setDescripcion("Modelado entidad relacion del modulo de reportes");
        actividad.setFechaInicio(Date.valueOf("2026-03-02"));
        actividad.setFechaFin(Date.valueOf("2026-03-31"));
        actividad.setHorasActividad(70);
        actividad.setMatriculaPracticante("S22019874");
        ActividadDao actividadDao = new ActividadDao();
        int filasAfectadas = actividadDao.registrarActividad(actividad);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaRegistrarActividadAlternoHorasMinimas() throws MensajeriaExcepcion {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Reunion de seguimiento semanal");
        actividad.setDescripcion("Reunion con tutor para revisar avances del sprint");
        actividad.setFechaInicio(Date.valueOf("2026-04-07"));
        actividad.setFechaFin(Date.valueOf("2026-04-07"));
        actividad.setHorasActividad(2);
        actividad.setMatriculaPracticante("S23010045");
        ActividadDao actividadDao = new ActividadDao();
        int filasAfectadas = actividadDao.registrarActividad(actividad);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaRegistrarActividadAlternoPeriodoMasLargo() throws MensajeriaExcepcion {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Desarrollo del modulo de autenticacion");
        actividad.setDescripcion("Implementacion de login con SHA-256 y manejo de sesiones de usuario");
        actividad.setFechaInicio(Date.valueOf("2026-04-01"));
        actividad.setFechaFin(Date.valueOf("2026-04-30"));
        actividad.setHorasActividad(80);
        actividad.setMatriculaPracticante("S24013282");
        ActividadDao actividadDao = new ActividadDao();
        int filasAfectadas = actividadDao.registrarActividad(actividad);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaRegistrarActividadAlternoHorasMaximas() throws MensajeriaExcepcion {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Pruebas de integracion y documentacion final");
        actividad.setDescripcion("Ejecucion de pruebas unitarias e integracion de modulos del sistema");
        actividad.setFechaInicio(Date.valueOf("2026-06-01"));
        actividad.setFechaFin(Date.valueOf("2026-06-30"));
        actividad.setHorasActividad(90);
        actividad.setMatriculaPracticante("S21007654");
        ActividadDao actividadDao = new ActividadDao();
        int filasAfectadas = actividadDao.registrarActividad(actividad);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaRegistrarActividadExcepcionMatriculaNula() {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Actividad sin matricula");
        actividad.setDescripcion("Descripcion de prueba");
        actividad.setFechaInicio(Date.valueOf("2026-02-03"));
        actividad.setFechaFin(Date.valueOf("2026-02-28"));
        actividad.setHorasActividad(40);
        actividad.setMatriculaPracticante(null);
        ActividadDao actividadDao = new ActividadDao();
        assertThrows(MensajeriaExcepcion.class, () -> actividadDao.registrarActividad(actividad));
    }

    @Test
    public void pruebaRegistrarActividadExcepcionTituloNulo() {
        Actividad actividad = new Actividad();
        actividad.setTitulo(null);
        actividad.setDescripcion("Descripcion de prueba");
        actividad.setFechaInicio(Date.valueOf("2026-02-03"));
        actividad.setFechaFin(Date.valueOf("2026-02-28"));
        actividad.setHorasActividad(40);
        actividad.setMatriculaPracticante("S24013282");
        ActividadDao actividadDao = new ActividadDao();
        assertThrows(MensajeriaExcepcion.class, () -> actividadDao.registrarActividad(actividad));
    }

    @Test
    public void pruebaRegistrarActividadExcepcionMatriculaNoExistente() {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Actividad matricula invalida");
        actividad.setDescripcion("Descripcion de prueba");
        actividad.setFechaInicio(Date.valueOf("2026-02-03"));
        actividad.setFechaFin(Date.valueOf("2026-02-28"));
        actividad.setHorasActividad(40);
        actividad.setMatriculaPracticante("S99999999");
        ActividadDao actividadDao = new ActividadDao();
        assertThrows(MensajeriaExcepcion.class, () -> actividadDao.registrarActividad(actividad));
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteExitoso() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante("S24013282");
        assertNotNull(actividades);
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteListaNoVacia() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante("S24013282");
        assertTrue(actividades.size() > 0);
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteAlternoOtraPracticante() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante("S22019874");
        assertNotNull(actividades);
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteTienenTitulo() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante("S24013282");
        for (Actividad actividad : actividades) {
            assertNotNull(actividad.getTitulo());
        }
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteNoExistente() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante("S99999999");
        assertEquals(0, actividades.size());
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteYMesExitoso() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticanteYMes("S24013282", 2, 2026);
        assertNotNull(actividades);
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteYMesAlternoMesMarzo() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticanteYMes("S24013282", 4, 2026);
        assertNotNull(actividades);
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteYMesAlternoOtraPracticante() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticanteYMes("S22019874", 3, 2026);
        assertNotNull(actividades);
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteYMesSinActividades() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticanteYMes("S24013282", 1, 2025);
        assertEquals(0, actividades.size());
    }

    @Test
    public void pruebaObtenerActividadesPorPracticanteYMesMatriculaNoExistente() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticanteYMes("S99999999", 2, 2026);
        assertEquals(0, actividades.size());
    }

    @Test
    public void pruebaObtenerHorasTotalesPorPracticanteExitoso() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante("S24013282");
        assertTrue(horasTotales >= 0);
    }

    @Test
    public void pruebaObtenerHorasTotalesPorPracticanteAlternoOtraPracticante() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante("S22019874");
        assertTrue(horasTotales >= 0);
    }

    @Test
    public void pruebaObtenerHorasTotalesPorPracticanteAlternoGeneracion21() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante("S21007654");
        assertTrue(horasTotales >= 0);
    }

    @Test
    public void pruebaObtenerHorasTotalesPorPracticanteNoExistente() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante("S99999999");
        assertEquals(0, horasTotales);
    }

    @Test
    public void pruebaObtenerHorasTotalesPorPracticanteMenorQueLimite() throws MensajeriaExcepcion {
        ActividadDao actividadDao = new ActividadDao();
        int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante("S23010045");
        assertTrue(horasTotales <= 420);
    }
}