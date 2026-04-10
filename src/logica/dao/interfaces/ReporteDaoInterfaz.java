package logica.dao.interfaces;
import logica.dto.Reporte;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface ReporteDaoInterfaz {
    void agregarReporte(Reporte reporte) throws InserccionBaseDeDatosExcepcion;
}
