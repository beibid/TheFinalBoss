package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Administrador;

public interface AdministradorDaoInterfaz {
    int insertarAdministrador(Administrador administrador) throws UsuariosExcepcion;
}