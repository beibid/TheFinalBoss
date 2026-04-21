import logica.dao.excepciones.UsuariosExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrganizacionVinculadaPrueba {

    @Test
    public void pruebaInsertarOrganizacionVinculadaExitoso() throws UsuariosExcepcion {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada("Empresa Prueba 50", "Calle 50");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacionVinculada);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarOrganizacionVinculadaAlterna() throws UsuariosExcepcion {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada("Empresa Prueba 51", "");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacionVinculada);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarOrganizacionVinculadaExcepcionNombreNulo() {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada(null, "Calle 50");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.insertarOrganizacionVinculada(organizacionVinculada));
    }

    @Test
    public void pruebaInsertarOrganizacionVinculadaExcepcionDireccionNula() {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada("Empresa Prueba 52", null);
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.insertarOrganizacionVinculada(organizacionVinculada));
    }

    @Test
    public void pruebaModificarOrganizacionVinculadaExitoso() throws UsuariosExcepcion {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada("Empresa Prueba 50 Modificada", "Calle 50 Nueva");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.modificarOrganizacionVinculada(1, organizacionVinculada);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarOrganizacionVinculadaAlternaNoExistente() throws UsuariosExcepcion {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada("Empresa No Existente", "Calle 999");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.modificarOrganizacionVinculada(9999, organizacionVinculada);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarOrganizacionVinculadaExcepcionNombreNulo() {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada(null, "Calle 50");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.modificarOrganizacionVinculada(1, organizacionVinculada));
    }

    @Test
    public void pruebaModificarOrganizacionVinculadaExcepcionDireccionNula() {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada("Empresa Prueba 50", null);
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.modificarOrganizacionVinculada(1, organizacionVinculada));
    }
}