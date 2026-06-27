package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.PracticanteSeccion;

public interface PracticanteSeccionDaoInterfaz {
    int agregarPracticanteSeccion(PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion;
    int modificarPracticanteSeccion(String matricula, String noSeccion, int idPeriodo, PracticanteSeccion practicanteSeccion) throws UsuariosExcepcion;
}