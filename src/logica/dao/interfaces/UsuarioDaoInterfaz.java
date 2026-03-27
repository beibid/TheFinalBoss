package logica.dao.interfaces;
import logica.Usuario;
import logica.dao.excepciones.DaoExcepcion;


public interface UsuarioDaoInterfaz {
    void insertarUsuario(Usuario usuario)throws DaoExcepcion;
}
