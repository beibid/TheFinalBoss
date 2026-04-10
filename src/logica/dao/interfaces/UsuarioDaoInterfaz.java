package logica.dao.interfaces;
import logica.dto.Usuario;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;


public interface UsuarioDaoInterfaz {
    int insertarUsuario(Usuario usuario)throws InserccionBaseDeDatosExcepcion;
}
