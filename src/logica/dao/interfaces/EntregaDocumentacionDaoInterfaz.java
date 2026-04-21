package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.EntregaDocumentacion;

public interface EntregaDocumentacionDaoInterfaz {
    int agregarEntrega(EntregaDocumentacion entrega) throws UsuariosExcepcion;
}