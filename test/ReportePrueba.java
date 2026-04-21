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
                Date.valueOf("2026-04-20"), 8.0, "Progreso adecuado",
                EstadoDeCalificacion.Evaluado, "S20013461", "P005");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteAlternoCalificacionCero() throws MensajeriaExcepcion {
        Reporte reporte = new Reporte(TipoReporte.Parcial, "Reporte 51",
                Date.valueOf("2026-04-20"), 0.0, "Sin calificacion",
                EstadoDeCalificacion.Pendiente, "S20013461", "P005");
        ReporteDao reporteDao = new ReporteDao();
        int filasAfectadas = reporteDao.agregarReporte(reporte);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarReporteExcepcionMatriculaNula() {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte 52",
                Date.valueOf("2026-04-20"), 8.0, "Progreso adecuado",
                EstadoDeCalificacion.Evaluado, null, "P005");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }

    @Test
    public void pruebaAgregarReporteExcepcionFechaNula() {
        Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte 53",
                null, 8.0, "Progreso adecuado",
                EstadoDeCalificacion.Evaluado, "S20013461", "P005");
        ReporteDao reporteDao = new ReporteDao();
        assertThrows(MensajeriaExcepcion.class, () -> reporteDao.agregarReporte(reporte));
    }
}