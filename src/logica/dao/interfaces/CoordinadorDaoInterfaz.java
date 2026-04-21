package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Coordinador;

public interface CoordinadorDaoInterfaz {
    int insertarCoordinador(Coordinador coordinador) throws UsuariosExcepcion;
    int inactivarCoordinador(String numPersonalCoordinador) throws UsuariosExcepcion;
    int modificarCoordinador(String numPersonalCoordinador, Coordinador coordinador) throws UsuariosExcepcion;
}