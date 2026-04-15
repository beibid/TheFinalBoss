package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.DocumentacionPracticante;

public interface DocumentacionPracticanteDaoInterfaz {
    void agregarDocumentacion(DocumentacionPracticante documentacion) throws UsuariosExcepcion;
}