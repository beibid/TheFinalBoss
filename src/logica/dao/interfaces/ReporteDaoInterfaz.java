package logica.dao.interfaces;


import logica.dominio.Reporte;
import logica.dao.excepciones.MensajeriaExcepcion;


public interface ReporteDaoInterfaz {
    void agregarReporte(Reporte reporte) throws MensajeriaExcepcion;
}
