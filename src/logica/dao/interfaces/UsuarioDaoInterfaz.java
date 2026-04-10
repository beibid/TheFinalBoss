package logica.dao.interfaces;


import logica.dominio.Usuario;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;


public interface UsuarioDaoInterfaz {
    int insertarUsuario(Usuario usuario)throws InserccionBaseDeDatosExcepcion;
}
