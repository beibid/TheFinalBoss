import logica.dao.excepciones.MensajeriaExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Reporte;
import logica.dominio.enums.TipoReporte;
import logica.dominio.enums.EstadoDeCalificacion;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReportePrueba {

    @Test
    public void pruebaAgregarReporteExitoso() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte 50",
                "Actividades 50", "S20013461", "/archivos/rep50.pdf", "rep50.pdf");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteAlternoTipoDiferente() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Parcial, "Reporte 51",
                "Actividades 51", "S20013461", "/archivos/rep51.pdf", "rep51.pdf");
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
                "Actividades 53", "S20013461", "/archivos/rep53.pdf", "rep53.pdf");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }
}