import logica.dominio.UsuarioSesion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.UsuarioDao;
import logica.dominio.Usuario;
import logica.dominio.enums.Estado;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UsuarioPrueba {

    @Test
    public void pruebaInsertarUsuarioExitoso() throws UsuariosExcepcion {
        Usuario usuario = new Usuario("Ameth", "Polanco Hernandez", "contrasena12345", "ameth.polanco@uv.mx", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        int resultado = usuarioDao.insertarUsuario(usuario);
        assertTrue(resultado > 0);
    }

    @Test
    public void pruebaInsertarUsuarioAlternoNombreCompuesto() throws UsuariosExcepcion {
        Usuario usuario = new Usuario("Ana Karen", "Ramirez Solis", "anakaren98765", "anakaren.ramirez@uv.mx", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        int resultado = usuarioDao.insertarUsuario(usuario);
        assertTrue(resultado > 0);
    }

    @Test
    public void pruebaInsertarUsuarioAlternoApellidoConAcento() throws UsuariosExcepcion {
        Usuario usuario = new Usuario("Luis", "Hernández Téllez", "luis123456", "luis.hernandez@uv.mx", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        int resultado = usuarioDao.insertarUsuario(usuario);
        assertTrue(resultado > 0);
    }

    @Test
    public void pruebaInsertarUsuarioExcepcionNombreNulo() {
        Usuario usuario = new Usuario(null, "Polanco Hernandez", "contrasena12345", "nulo@uv.mx", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        assertThrows(UsuariosExcepcion.class, () -> usuarioDao.insertarUsuario(usuario));
    }

    @Test
    public void pruebaInsertarUsuarioAlternoNombreVacio() {
        Usuario usuario = new Usuario("", "Polanco Hernandez", "contrasena12345", "vacio@uv.mx", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        assertThrows(UsuariosExcepcion.class, () -> usuarioDao.insertarUsuario(usuario));
    }

    @Test
    public void pruebaInsertarUsuarioExcepcionContraseniaNula() {
        Usuario usuario = new Usuario("Carlos", "Mendez Leal", null, "carlos.mendez@uv.mx", Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        assertThrows(UsuariosExcepcion.class, () -> usuarioDao.insertarUsuario(usuario));
    }

    @Test
    public void pruebaInsertarUsuarioExcepcionCorreoNulo() {
        Usuario usuario = new Usuario("Sofia", "Torres Vidal", "sofia123456", null, Estado.Activo);
        UsuarioDao usuarioDao = new UsuarioDao();
        assertThrows(UsuariosExcepcion.class, () -> usuarioDao.insertarUsuario(usuario));
    }

    @Test
    public void pruebaBuscarUsuarioExitoso() throws UsuariosExcepcion {
        UsuarioDao usuarioDao = new UsuarioDao();
        UsuarioSesion sesion = usuarioDao.buscarUsuario("daviLa@uv.mx", "davidS240132");
        assertNotNull(sesion);
    }

    @Test
    public void pruebaBuscarUsuarioAlternoCredencialesIncorrectas() throws UsuariosExcepcion {
        UsuarioDao usuarioDao = new UsuarioDao();
        UsuarioSesion sesion = usuarioDao.buscarUsuario("daviLa@uv.mx", "contrasenaIncorrecta");
        assertNull(sesion);
    }

    @Test
    public void pruebaBuscarUsuarioAlternoCorreoNoExistente() throws UsuariosExcepcion {
        UsuarioDao usuarioDao = new UsuarioDao();
        UsuarioSesion sesion = usuarioDao.buscarUsuario("noexiste@uv.mx", "cualquierClave");
        assertNull(sesion);
    }
}