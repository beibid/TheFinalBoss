import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.AdministradorDao;
import logica.dominio.Administrador;
import logica.dominio.enums.Estado;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdministradorPrueba {


    @Test
    public void pruebaInsertarAdministradorExitoso() throws UsuariosExcepcion {
        Administrador administrador = new Administrador("10293847", "Hector", "Sandoval Reyes", "hector10293847", "hector.sandoval@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        int filasAfectadas = administradorDao.insertarAdministrador(administrador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarAdministradorAlternoNombreCompuesto() throws UsuariosExcepcion {
        Administrador administrador = new Administrador("56473829", "Ana Maria", "Flores Gutierrez", "anamaria56473829", "anamaria.flores@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        int filasAfectadas = administradorDao.insertarAdministrador(administrador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarAdministradorAlternoApellidoConAcento() throws UsuariosExcepcion {
        Administrador administrador = new Administrador("11223344", "Verónica", "Jiménez Ávila", "veronica11223344", "veronica.jimenez@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        int filasAfectadas = administradorDao.insertarAdministrador(administrador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarAdministradorAlternoEstadoInactivo() throws UsuariosExcepcion {
        Administrador administrador = new Administrador("99887766", "Carlos", "de la Cruz Herrera", "carlos99887766", "carlos.delacruz@uv.mx", Estado.Inactivo);
        AdministradorDao administradorDao = new AdministradorDao();
        int filasAfectadas = administradorDao.insertarAdministrador(administrador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarAdministradorAlternoNumPersonalDistinto() throws UsuariosExcepcion {
        Administrador administrador = new Administrador("12345678", "Roberto", "Perez Salinas", "roberto12345678", "roberto.perez@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        int filasAfectadas = administradorDao.insertarAdministrador(administrador);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarAdministradorDuplicadoLanzaExcepcion() {
        Administrador administrador = new Administrador("10293847", "Hector", "Sandoval Reyes", "hector10293847", "hector.sandoval2@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        assertThrows(UsuariosExcepcion.class, () -> administradorDao.insertarAdministrador(administrador));
    }

    @Test
    public void pruebaInsertarAdministradorExcepcionNombreNulo() {
        Administrador administrador = new Administrador("55667788", null, "Gutierrez Vega", "pass55667788", "nombre.nulo@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        assertThrows(UsuariosExcepcion.class, () -> administradorDao.insertarAdministrador(administrador));
    }

    @Test
    public void pruebaInsertarAdministradorExcepcionApellidosNulos() {
        Administrador administrador = new Administrador("44556677", "Ramon", null, "ramon44556677", "ramon.estrada@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        assertThrows(UsuariosExcepcion.class, () -> administradorDao.insertarAdministrador(administrador));
    }

    @Test
    public void pruebaInsertarAdministradorExcepcionContraseniaNula() {
        Administrador administrador = new Administrador("33445566", "Diana", "Lozano Reyes", null, "diana.lozano@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        assertThrows(UsuariosExcepcion.class, () -> administradorDao.insertarAdministrador(administrador));
    }

    @Test
    public void pruebaInsertarAdministradorExcepcionCorreoNulo() {
        Administrador administrador = new Administrador("22334455", "Sergio", "Mendez Torres", "sergio22334455", null, Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        assertThrows(UsuariosExcepcion.class, () -> administradorDao.insertarAdministrador(administrador));
    }

    @Test
    public void pruebaInsertarAdministradorExcepcionNumPersonalNulo() {
        Administrador administrador = new Administrador(null, "Miriam", "Ortega Vidal", "miriam11223344", "miriam.ortega@uv.mx", Estado.Activo);
        AdministradorDao administradorDao = new AdministradorDao();
        assertThrows(UsuariosExcepcion.class, () -> administradorDao.insertarAdministrador(administrador));
    }
}