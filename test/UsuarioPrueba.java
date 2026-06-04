import logica.dominio.UsuarioSesion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.UsuarioDao;
import logica.dominio.Usuario;
import logica.dominio.enums.Estado;
import logica.dao.excepciones.UsuariosExcepcion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioPrueba {
    @Test
    public void pruebaInsertarUsuarioExitosa() throws UsuariosExcepcion {
        Usuario nuevoUsuario = new Usuario("juan", "Bonilla", "Ramirez", "ESOTILIN1", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        int resultado = usuarioDao.insertarUsuario(nuevoUsuario);
        assertTrue(resultado > 0);
    }
    @Test
    public void pruebaInsertarUsuarioAlternoNombreVacio() throws UsuariosExcepcion {
        Usuario nuevoUsuario = new Usuario("", "Bonilla", "Ramirez", "ESOTILIN1", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        int resultado = usuarioDao.insertarUsuario(nuevoUsuario);
        assertTrue(resultado > 0);
    }

    @Test
    public void pruebaInsertarUsuarioExcepcionNombreNulo() {
        Usuario nuevoUsuario = new Usuario(null, "Bonilla", "Ramirez", "ESOTILIN1", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        assertThrows(UsuariosExcepcion.class, () -> usuarioDao.insertarUsuario(nuevoUsuario));
    }

    @Test
    public void pruebaInsertarUsuarioExcepcionContraseniaNula() {
        Usuario nuevoUsuario = new Usuario("juan", "Bonilla", "Ramirez", null, Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        assertThrows(UsuariosExcepcion.class, () -> usuarioDao.insertarUsuario(nuevoUsuario));
    }

    @Test
    public void pruebaBuscarUsuarioExitoso() throws UsuariosExcepcion{
        UsuarioDao usuarioDao = new UsuarioDao();
        UsuarioSesion usuarioSesion = usuarioDao.buscarUsuario("daviLa@uv.mx", "davidS240132");
        assertNotNull(usuarioSesion);
    }

}