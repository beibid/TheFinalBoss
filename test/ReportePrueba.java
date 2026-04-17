

import org.junit.jupiter.api.Test;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Reporte;
import logica.dominio.enums.TipoReporte;
import logica.dominio.enums.EstadoDeCalificacion;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.sql.Date;


public class ReportePrueba {
    @Test
    public void insertarReporte(){
        Reporte nuevoReporte = new Reporte(TipoReporte.Mensual, "Reporte de tipo mensual", Date.valueOf("2026-04-15"), 9.1, "Sin observaciones", EstadoDeCalificacion.Evaluado, "S20013456", "2354445" );
        ReporteDao reporte = new ReporteDao();

        assertDoesNotThrow(() -> reporte.agregarReporte(nuevoReporte));
    }
}
