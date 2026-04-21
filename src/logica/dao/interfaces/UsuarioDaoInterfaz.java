package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.Usuario;

public interface UsuarioDaoInterfaz {
    int insertarUsuario(Usuario usuario) throws UsuariosExcepcion;
}