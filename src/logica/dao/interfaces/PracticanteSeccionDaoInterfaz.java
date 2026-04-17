package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.PracticanteSeccion;

public interface PracticanteSeccionDaoInterfaz {
    void agregarPracticanteSeccion(PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion;
    void modificarPracticanteSeccion(String matricula, String noSeccion, PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion;
}