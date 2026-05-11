package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.PreferenciaProyecto;

import java.util.List;

public interface PreferenciaProyectoDaoInterfaz {

    void guardarPreferencias(String matricula, List<Integer> idProyectosOrdenados) throws UsuariosExcepcion;

    List<PreferenciaProyecto> obtenerPreferencias(String matricula) throws UsuariosExcepcion;
}