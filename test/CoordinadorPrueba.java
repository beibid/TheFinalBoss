import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import logica.dominio.enums.Estado;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoordinadorPrueba {

    @Test
    public void pruebaInsertarCoordinadorExitoso() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("55001100", "Lucia", "Perez Sandoval", "luciaS55001100", "lucia.perez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorAlternoCorreoDiferente() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("55001101", "Roberto", "Fuentes Mora", "robertoS55001101", "roberto.fuentes@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorAlternoNombreConAcento() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("55001102", "Andrés", "Núñez Ávila", "andresS55001102", "andres.nunez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorAlternoApellidoCompuesto() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("55001103", "Elena", "de la Torre Vega", "elenaS55001103", "elena.delatorre@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorDuplicadoLanzaExcepcion() {
        Coordinador coordinador = new Coordinador("12345", "Juan Carlos", "Perez Arriega", "duppass", "dup@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(RegistroDuplicadoExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionNombreNulo() {
        Coordinador coordinador = new Coordinador("55001104", null, "Gutierrez Vega", "pass55001104", "nulo.nombre@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionContraseniaNula() {
        Coordinador coordinador = new Coordinador("55001105", "Ramon", "Estrada Poblano", null, "ramon.estrada@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionCorreoNulo() {
        Coordinador coordinador = new Coordinador("55001106", "Diana", "Lozano Reyes", "diana55001106", null, Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionNumPersonalNulo() {
        Coordinador coordinador = new Coordinador(null, "Diana", "Lozano Reyes", "diana55001107", "diana.nueva@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionApellidosNulos() {
        Coordinador coordinador = new Coordinador("55001108", "Sergio", null, "sergio55001108", "sergio.mendez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaModificarCoordinadorExitoso() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("11223344", "Verónica", "Jiménez Ávila", "veromod1", "vero.mod1@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("11223344", coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorAlternoCambioCorreo() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("99887766", "Carlos", "de la Cruz Herrera", "carlosmod2", "carlos.mod2@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("99887766", coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorAlternoCambioApellidos() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("11223344", "Verónica", "Jiménez Torres", "veromod3", "vero.mod3@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("11223344", coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorAlternoNoExistente() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("00000000", "Fantasma", "Inexistente", "pass00000000", "fantasma@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("00000000", coordinador);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorExcepcionNumPersonalNulo() {
        Coordinador coordinador = new Coordinador("11223344", "Verónica", "Jiménez Ávila", "veromod4", "vero.mod4@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.modificarCoordinador(null, coordinador));
    }

    @Test
    public void pruebaModificarCoordinadorExcepcionNombreNulo() {
        Coordinador coordinador = new Coordinador("11223344", null, "Jiménez Ávila", "veromod5", "vero.mod5@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.modificarCoordinador("11223344", coordinador));
    }

    @Test
    public void pruebaInactivarCoordinadorExitoso() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("11223344");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorAlternoSegundoRegistro() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("99887766");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorAlternoNoExistente() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("00000000");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorAlternoYaInactivo() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("87654321");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorExcepcionNumPersonalNulo() {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.inactivarCoordinador(null));
    }

    @Test
    public void pruebaObtenerCoordinadoresActivosExitoso() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
        assertNotNull(coordinadores);
    }

    @Test
    public void pruebaObtenerCoordinadoresActivosListaNoVacia() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
        assertTrue(coordinadores.size() > 0);
    }

    @Test
    public void pruebaObtenerCoordinadoresActivosTodosActivos() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
        for (Coordinador coordinador : coordinadores) {
            assertEquals(Estado.Activo, coordinador.getEstado());
        }
    }

    @Test
    public void pruebaObtenerCoordinadoresActivosTienenNombre() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
        for (Coordinador coordinador : coordinadores) {
            assertNotNull(coordinador.getNombre());
        }
    }

    @Test
    public void pruebaObtenerCoordinadoresActivosTienenNumPersonal() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
        for (Coordinador coordinador : coordinadores) {
            assertNotNull(coordinador.getNumeroDePersonalCoordinador());
        }
    }

    @Test
    public void pruebaExisteCoordinadorActivoRetornaEntero() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int resultado = coordinadorDao.existeCoordinadorActivo();
        assertTrue(resultado >= 0);
    }

    @Test
    public void pruebaExisteCoordinadorActivoHayAlMenosUno() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int resultado = coordinadorDao.existeCoordinadorActivo();
        assertTrue(resultado > 0);
    }
}