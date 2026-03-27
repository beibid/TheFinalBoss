package logica.dao.interfaces;
import logica.Usuario;
import logica.dao.excepciones.DaoExcepcion;


public interface UsuarioDaoInterfaz {
    int insertarUsuario(Usuario usuario)throws DaoExcepcion;
}
