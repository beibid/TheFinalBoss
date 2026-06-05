import logica.dao.excepciones.MensajeriaExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Reporte;
import logica.dominio.enums.TipoReporte;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReportePrueba {

    @Test
    public void pruebaAgregarReporteExitoso() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte 50",
                "Actividades 50", "S24013287", "/archivos/rep50.pdf", "rep50.pdf");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteAlternoTipoDiferente() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Parcial, "Reporte 51",
                "Actividades 51", "S24013287", "/archivos/rep51.pdf", "rep51.pdf");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteExcepcionMatriculaNula() {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte 52",
                "Actividades 52", null, "/archivos/rep52.pdf", "rep52.pdf");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }

    @Test
    public void pruebaAgregarReporteExcepcionTipoNulo() {
        Reporte reporte = new Reporte(null, "Reporte 53",
                "Actividades 53", "S24013287", "/archivos/rep53.pdf", "rep53.pdf");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }

    @Test
    public void pruebaObtenerReportesPorPracticanteExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorPracticante("S24013287");
        assertNotNull(reportes);
    }

    @Test
    public void pruebaObtenerReportesPorPracticanteAlternoMatriculaInexistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorPracticante("S99999999");
        assertEquals(0, reportes.size());
    }

    @Test
    public void pruebaObtenerReportesPorMatriculaExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorMatricula("S24013287");
        assertNotNull(reportes);
    }

    @Test
    public void pruebaObtenerReportesPorMatriculaAlternoMatriculaInexistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        List<Reporte> reportes = reporteDao.obtenerReportesPorMatricula("S99999999");
        assertEquals(0, reportes.size());
    }

    @Test
    public void pruebaEvaluarReporteAlternoIdInexistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.evaluarReporte(9999, 9.5, "Buen trabajo", "22114455");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaContarReportesEvaluadosExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int total = reporteDao.contarReportesEvaluados("S24013287", "Mensual");
        assertTrue(total >= 0);
    }

    @Test
    public void pruebaContarReportesEvaluadosAlternoMatriculaInexistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        int total = reporteDao.contarReportesEvaluados("S99999999", "Mensual");
        assertEquals(0, total);
    }

    @Test
    public void pruebaExisteReporteMensualEnMesActualExitoso() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        boolean existe = reporteDao.existeReporteMensualEnMesActual("S24013287");
        assertNotNull(existe);
    }

    @Test
    public void pruebaExisteReporteMensualEnMesActualAlternoMatriculaInexistente() throws MensajeriaExcepcion {
        ReporteDao reporteDao = new ReporteDao();
        boolean existe = reporteDao.existeReporteMensualEnMesActual("S99999999");
        assertFalse(existe);
    }
}