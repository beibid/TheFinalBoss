package logica.dao.interfaces;


import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Practicante;



public interface PracticanteDaoInterfaz {
    void insertarPracticante(Practicante practicante) throws UsuariosExcepcion;
    void modificarPracticante(String matricula, Practicante practicante) throws UsuariosExcepcion;
    void inactivarPracticante(String matricula) throws UsuariosExcepcion;

}
