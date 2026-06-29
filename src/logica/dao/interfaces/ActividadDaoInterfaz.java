package logica.dao.interfaces;

import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.Actividad;

import java.util.List;

public interface ActividadDaoInterfaz {
    int registrarActividad(Actividad actividad) throws MensajeriaExcepcion;
    List<Actividad> obtenerActividadesPorPracticante(String matricula) throws MensajeriaExcepcion;
    List<Actividad> obtenerActividadesPorPracticanteYMes(String matricula, int mes, int anio) throws MensajeriaExcepcion;
    int obtenerHorasTotalesPorPracticante(String matricula) throws MensajeriaExcepcion;
}