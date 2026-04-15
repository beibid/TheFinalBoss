package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.EntregaDocumentacion;

public interface EntregaDocumentacionDaoInterfaz {
    void agregarEntrega(EntregaDocumentacion entrega) throws UsuariosExcepcion;
}