import org.junit.jupiter.api.Test;
import logica.dao.objetos.UsuarioDao;
import logica.dominio.Usuario;
import logica.dominio.enums.Estado;
import org.junit.jupiter.api.Assertions;
import logica.dao.excepciones.UsuariosExcepcion;

public class UsuarioPrueba {
    @Test
    public void insertarTest() throws UsuariosExcepcion{
        Usuario nuevoUsuario= new Usuario("juan", "Bonilla", "Ramirez", "ESOTILIN1", Estado.Activo);
        UsuarioDao usuario = new UsuarioDao();

        int resultado = usuario.insertarUsuario(nuevoUsuario);
        Assertions.assertEquals(1, resultado,  "Usuario ingresado con exito dentro de la base de datos");


    }

}