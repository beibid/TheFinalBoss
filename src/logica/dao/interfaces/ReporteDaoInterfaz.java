package logica.dao.interfaces;
import logica.dominio.Reporte;
import logica.dao.excepciones.InserccionUsuarioExcepcion;

public interface ReporteDaoInterfaz {
    void agregarReporte(Reporte reporte) throws InserccionUsuarioExcepcion;
}
