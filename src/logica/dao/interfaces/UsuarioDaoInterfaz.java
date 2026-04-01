package logica.dao.interfaces;
import logica.dominio.Usuario;
import logica.dao.excepciones.DaoExcepcion;


public interface UsuarioDaoInterfaz {
    int insertarUsuario(Usuario usuario)throws DaoExcepcion;
}
