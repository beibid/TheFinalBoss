package logica.dao.interfaces;


import logica.dominio.AutoevaluacionPracticante;
import logica.dao.excepciones.MensajeriaExcepcion;


public interface AutoevaluacionPracticanteDaoInterfaz {
    int registrarAutoevaluacion(AutoevaluacionPracticante autoevaluacion) throws MensajeriaExcepcion;
    AutoevaluacionPracticante obtenerInfoAutoevaluacion(String matricula) throws MensajeriaExcepcion;
}
