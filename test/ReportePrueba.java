import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Reporte;
import logica.dominio.enums.TipoReporte;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportePrueba {

    @Test
    public void pruebaAgregarReporteMensualExitoso() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte Junio 2026",
                "Actividades de desarrollo de modulo de login", "S24013282", "/reportes/rep_jun_S24013282.pdf", "rep_jun_S24013282.pdf");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteParcialExitoso() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Parcial, "Reporte Parcial 1",
                "Avance del primer modulo de gestion de usuarios", "S24013282", "/reportes/parcial1_S24013282.pdf", "parcial1_S24013282.pdf");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteAlternoOtraPracticante() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte Mayo 2026",
                "Actividades de diseno de base de datos", "S22019874", "/reportes/rep_may_S22019874.pdf", "rep_may_S22019874.pdf");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteAlternoDescripcionLarga() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte Julio 2026",
                "Durante este mes se realizaron actividades de implementacion del modulo de reportes, pruebas unitarias y documentacion del codigo fuente desarrollado durante la practica profesional",
                "S23010045", "/reportes/rep_jul_S23010045.pdf", "rep_jul_S23010045.pdf");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteExcepcionMatriculaNula() {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte sin matricula",
                "Actividades varias", null, "/reportes/rep.pdf", "rep.pdf");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }

    @Test
    public void pruebaAgregarReporteExcepcionTipoNulo() {
        Reporte reporte = new Reporte(null, "Reporte sin tipo",
                "Actividades varias", "S24013282", "/reportes/rep.pdf", "rep.pdf");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }

    @Test
    public void pruebaAgregarReporteExcepcionRutaNula() {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte sin ruta",
                "Actividades varias", "S24013282", null, "rep.pdf");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }

    @Test
    public void pruebaObtenerReportesPorPracticanteExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorPracticante("S24013282");
        assertNotNull(reportes);
    }

    @Test
    public void pruebaObtenerReportesPorPracticanteListaNoVacia() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorPracticante("S24013282");
        assertTrue(reportes.size() > 0);
    }

    @Test
    public void pruebaObtenerReportesPorPracticanteAlternoOtraMatricula() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorPracticante("S22019874");
        assertNotNull(reportes);
    }

    @Test
    public void pruebaObtenerReportesPorPracticanteNoExistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorPracticante("S99999999");
        assertEquals(0, reportes.size());
    }

    @Test
    public void pruebaObtenerReportesPorMatriculaExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorMatricula("S24013282");
        assertNotNull(reportes);
    }

    @Test
    public void pruebaObtenerReportesPorMatriculaAlternoGeneracion23() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorMatricula("S23010045");
        assertNotNull(reportes);
    }

    @Test
    public void pruebaObtenerReportesPorMatriculaNoExistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorMatricula("S99999999");
        assertEquals(0, reportes.size());
    }

    @Test
    public void pruebaEvaluarReporteExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.evaluarReporte(17, 9.0, "Buen trabajo, continua asi", "22114455");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaEvaluarReporteAlternoCalificacionMaxima() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.evaluarReporte(18, 10.0, "Excelente desempeno, actividades muy bien documentadas", "22114455");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaEvaluarReporteAlternoCalificacionMinima() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.evaluarReporte(19, 6.0, "Debe mejorar la redaccion y profundizar en los temas", "33224411");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaEvaluarReporteAlternoIdNoExistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.evaluarReporte(9999, 9.5, "Buen trabajo", "22114455");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaContarReportesEvaluadosMensualExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int total = reporteDao.contarReportesEvaluados("S24013282", "Mensual");
        assertTrue(total >= 0);
    }

    @Test
    public void pruebaContarReportesEvaluadosParcialExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int total = reporteDao.contarReportesEvaluados("S24013282", "Parcial");
        assertTrue(total >= 0);
    }

    @Test
    public void pruebaContarReportesEvaluadosAlternoOtraPracticante() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int total = reporteDao.contarReportesEvaluados("S22019874", "Mensual");
        assertTrue(total >= 0);
    }

    @Test
    public void pruebaContarReportesEvaluadosMatriculaNoExistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int total = reporteDao.contarReportesEvaluados("S99999999", "Mensual");
        assertEquals(0, total);
    }

    @Test
    public void pruebaExisteReporteMensualEnMesActualExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        boolean existe = reporteDao.existeReporteMensualEnMesActual("S24013282");
        assertNotNull(existe);
    }

    @Test
    public void pruebaExisteReporteMensualEnMesActualAlternoOtraPracticante() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        boolean existe = reporteDao.existeReporteMensualEnMesActual("S23010045");
        assertNotNull(existe);
    }

    @Test
    public void pruebaExisteReporteMensualEnMesActualMatriculaNoExistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        boolean existe = reporteDao.existeReporteMensualEnMesActual("S99999999");
        assertFalse(existe);
    }
}