package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.PeriodoUniversitario;

public interface PeriodoUniversitarioDaoInterfaz {
    int insertarPeriodo(PeriodoUniversitario periodoUniversitario) throws UsuariosExcepcion;
    boolean verificarPeriodoAbierto() throws UsuariosExcepcion;
}