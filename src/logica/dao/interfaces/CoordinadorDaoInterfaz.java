package logica.dao.interfaces;


import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Coordinador;


public interface CoordinadorDaoInterfaz {
    void insertarCoordinador(Coordinador coordinador) throws UsuariosExcepcion;
    void inactivarCoordinador(String numPersonalCoordinador) throws UsuariosExcepcion;
    void modificarCoordinador(String numPersonalCoordinador, Coordinador coordinador) throws UsuariosExcepcion;
}
