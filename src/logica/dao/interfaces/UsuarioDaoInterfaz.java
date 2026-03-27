package logica.dao.interfaces;
import logica.Usuario;
import logica.dao.excepciones.InserccionUsuarioExcepcion;


public interface UsuarioDaoInterfaz {
    int insertarUsuario(Usuario usuario)throws InserccionUsuarioExcepcion;
}
