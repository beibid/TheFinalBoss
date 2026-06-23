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
        Coordinador coordinador = new Coordinador("12345678", "Laura", "Gutierrez Vega", "laura12345678", "laura.gutierrez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorAlternoCorreoDiferente() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("87654321", "Marco", "Ruiz Pedraza", "marco87654321", "marco.ruiz@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorAlternoNombreConAcento() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("11223344", "Verónica", "Jiménez Ávila", "veronica11223344", "veronica.jimenez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorAlternoApellidoCompuesto() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("99887766", "Carlos", "de la Cruz Herrera", "carlos99887766", "carlos.delacruz@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorDuplicadoLanzaExcepcion() {
        Coordinador coordinador = new Coordinador("12345678", "Laura", "Gutierrez Vega", "laura12345678", "laura.gutierrez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionNombreNulo() {
        Coordinador coordinador = new Coordinador("55667788", null, "Gutierrez Vega", "pass55667788", "nombre.nulo@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionContraseniaNula() {
        Coordinador coordinador = new Coordinador("44556677", "Ramon", "Estrada Poblano", null, "ramon.estrada@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionCorreoNulo() {
        Coordinador coordinador = new Coordinador("33445566", "Diana", "Lozano Reyes", "diana33445566", null, Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionNumPersonalNulo() {
        Coordinador coordinador = new Coordinador(null, "Diana", "Lozano Reyes", "diana33445566", "diana.lozano@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionApellidosNulos() {
        Coordinador coordinador = new Coordinador("22334455", "Sergio", null, "sergio22334455", "sergio.mendez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaModificarCoordinadorExitoso() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("12345678", "Laura", "Gutierrez Vega", "laura12345678nuevo", "laura.nuevo@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("12345678", coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorAlternoCambioCorreo() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("87654321", "Marco", "Ruiz Pedraza", "marco87654321", "marco.nuevo@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("87654321", coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorAlternoCambioApellidos() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("12345678", "Laura", "Gutierrez Torres", "laura12345678nuevo", "laura.nuevo@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("12345678", coordinador);
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
        Coordinador coordinador = new Coordinador("12345678", "Laura", "Gutierrez Vega", "laura12345678", "laura.gutierrez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.modificarCoordinador(null, coordinador));
    }

    @Test
    public void pruebaModificarCoordinadorExcepcionNombreNulo() {
        Coordinador coordinador = new Coordinador("12345678", null, "Gutierrez Vega", "laura12345678", "laura.gutierrez@uv.mx", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.modificarCoordinador("12345678", coordinador));
    }

    @Test
    public void pruebaInactivarCoordinadorExitoso() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("12345678");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorAlternoSegundoRegistro() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("87654321");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorAlternoNoExistente() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("00000000");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorAlternoNumPersonalVacio() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("");
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