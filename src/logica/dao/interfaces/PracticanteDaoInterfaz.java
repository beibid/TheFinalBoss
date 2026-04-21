package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Practicante;

public interface PracticanteDaoInterfaz {
    int insertarPracticante(Practicante practicante) throws UsuariosExcepcion;
    int modificarPracticante(String matricula, Practicante practicante) throws UsuariosExcepcion;
    int inactivarPracticante(String matricula) throws UsuariosExcepcion;
}