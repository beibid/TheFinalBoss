import org.junit.jupiter.api.Test;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import logica.dominio.enums.Estado;
import logica.dao.excepciones.UsuariosExcepcion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CoordinadorPrueba {

    @Test
    public void pruebaInsertarCoordinadorExitoso() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("C050", "Ricardo", "Fuentes", "Castillo", "pass50", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarCoordinadorAlternoDuplicado() {
        Coordinador coordinador = new Coordinador("C031", "Ricardo", "Fuentes", "Castillo", "pass31", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionNumPersonalNulo() {
        Coordinador coordinador = new Coordinador(null, "Ricardo", "Fuentes", "Castillo", "pass31", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaInsertarCoordinadorExcepcionNombreNulo() {
        Coordinador coordinador = new Coordinador("C051", null, "Fuentes", "Castillo", "pass31", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.insertarCoordinador(coordinador));
    }

    @Test
    public void pruebaModificarCoordinadorExitoso() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("C031", "Ricardo", "Fuentes", "Castillo", "pass31nuevo", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("C031", coordinador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorAlternoNoExistente() throws UsuariosExcepcion {
        Coordinador coordinador = new Coordinador("C999", "Ricardo", "Fuentes", "Castillo", "pass31", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.modificarCoordinador("C999", coordinador);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarCoordinadorExcepcionNombreNulo() {
        Coordinador coordinador = new Coordinador("C031", null, "Fuentes", "Castillo", "pass31", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.modificarCoordinador("C031", coordinador));
    }

    @Test
    public void pruebaModificarCoordinadorExcepcionNumPersonalNulo() {
        Coordinador coordinador = new Coordinador("C031", "Ricardo", "Fuentes", "Castillo", "pass31", Estado.Activo);
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.modificarCoordinador(null, coordinador));
    }

    @Test
    public void pruebaInactivarCoordinadorExitoso() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("C050");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorAlternoNoExistente() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("C999");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarCoordinadorExcepcionNumPersonalNulo() {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        assertThrows(UsuariosExcepcion.class, () -> coordinadorDao.inactivarCoordinador(null));
    }

    @Test
    public void pruebaInactivarCoordinadorExcepcionNumPersonalVacio() throws UsuariosExcepcion {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        int filasAfectadas = coordinadorDao.inactivarCoordinador("");
        assertEquals(0, filasAfectadas);
    }
}