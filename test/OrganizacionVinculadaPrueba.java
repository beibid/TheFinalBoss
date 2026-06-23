import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;
import logica.dominio.enums.EstadoOrganizacion;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrganizacionVinculadaPrueba {

    @Test
    public void pruebaInsertarOrganizacionExitoso() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Grupo Bimbo Xalapa");
        organizacion.setDireccion("Av. Lazaro Cardenas 456, Xalapa");
        organizacion.setTelefono("2281904321");
        organizacion.setCorreo("contacto@bimboxalapa.com");
        organizacion.setSector("Alimenticio");
        organizacion.setEstadoOrganizacion(EstadoOrganizacion.Activa);
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarOrganizacionAlternoSectorTecnologia() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Softtek Veracruz");
        organizacion.setDireccion("Av. Tecnologico 200, Xalapa");
        organizacion.setTelefono("2281567890");
        organizacion.setCorreo("contacto@softtek.com");
        organizacion.setSector("Tecnologia");
        organizacion.setEstadoOrganizacion(EstadoOrganizacion.Activa);
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarOrganizacionAlternoSectorConstruccion() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Cemex Veracruz");
        organizacion.setDireccion("Blvd. Adolfo Ruiz Cortines 789");
        organizacion.setTelefono("2291873456");
        organizacion.setCorreo("ventas@cemexver.com");
        organizacion.setSector("Construccion");
        organizacion.setEstadoOrganizacion(EstadoOrganizacion.Activa);
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarOrganizacionAlternoSectorGobierno() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Municipio de Xalapa");
        organizacion.setDireccion("Enriquez 20, Centro, Xalapa");
        organizacion.setTelefono("2288412300");
        organizacion.setCorreo("contacto@xalapa.gob.mx");
        organizacion.setSector("Gobierno");
        organizacion.setEstadoOrganizacion(EstadoOrganizacion.Activa);
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarOrganizacionAlternoEstadoInactiva() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Empresa Temporal SA");
        organizacion.setDireccion("Calle Zaragoza 10, Xalapa");
        organizacion.setTelefono("2289001234");
        organizacion.setCorreo("info@temporal.com");
        organizacion.setSector("Servicios");
        organizacion.setEstadoOrganizacion(EstadoOrganizacion.Inactiva);
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarOrganizacionExcepcionNombreNulo() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre(null);
        organizacion.setDireccion("Av. Principal 100");
        organizacion.setTelefono("2281234567");
        organizacion.setCorreo("info@empresa.com");
        organizacion.setSector("Tecnologia");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion));
    }

    @Test
    public void pruebaInsertarOrganizacionExcepcionCorreoNulo() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Empresa Sin Correo");
        organizacion.setDireccion("Calle Falsa 123");
        organizacion.setTelefono("2281234567");
        organizacion.setCorreo(null);
        organizacion.setSector("Servicios");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion));
    }

    @Test
    public void pruebaInsertarOrganizacionExcepcionDireccionNula() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Empresa Sin Direccion");
        organizacion.setDireccion(null);
        organizacion.setTelefono("2281234567");
        organizacion.setCorreo("info@sindirec.com");
        organizacion.setSector("Servicios");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.insertarOrganizacionVinculada(organizacion));
    }

    @Test
    public void pruebaModificarOrganizacionExitoso() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Softtek Veracruz Actualizado");
        organizacion.setDireccion("Av. Tecnologico 300, Xalapa");
        organizacion.setTelefono("2281567891");
        organizacion.setCorreo("nuevo@softtek.com");
        organizacion.setSector("Tecnologia");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.modificarOrganizacionVinculada(1, organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarOrganizacionAlternoCambioSector() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Cemex Veracruz");
        organizacion.setDireccion("Blvd. Adolfo Ruiz Cortines 789");
        organizacion.setTelefono("2291873456");
        organizacion.setCorreo("ventas@cemexver.com");
        organizacion.setSector("Materiales");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.modificarOrganizacionVinculada(2, organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarOrganizacionAlternoCambioDireccion() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Grupo Bimbo Xalapa");
        organizacion.setDireccion("Calle Nueva 999, Xalapa");
        organizacion.setTelefono("2281904321");
        organizacion.setCorreo("contacto@bimboxalapa.com");
        organizacion.setSector("Alimenticio");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.modificarOrganizacionVinculada(3, organizacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarOrganizacionAlternoIdNoExistente() throws UsuariosExcepcion {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Empresa Fantasma");
        organizacion.setDireccion("Calle Inexistente 999");
        organizacion.setTelefono("2289999999");
        organizacion.setCorreo("nadie@inexistente.com");
        organizacion.setSector("Ninguno");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.modificarOrganizacionVinculada(9999, organizacion);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarOrganizacionExcepcionNombreNulo() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre(null);
        organizacion.setDireccion("Av. Tecnologico 200");
        organizacion.setTelefono("2281567890");
        organizacion.setCorreo("contacto@softtek.com");
        organizacion.setSector("Tecnologia");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.modificarOrganizacionVinculada(1, organizacion));
    }

    @Test
    public void pruebaModificarOrganizacionExcepcionCorreoNulo() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Empresa Valida");
        organizacion.setDireccion("Av. Principal 100");
        organizacion.setTelefono("2281234567");
        organizacion.setCorreo(null);
        organizacion.setSector("Servicios");
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        assertThrows(UsuariosExcepcion.class, () -> organizacionVinculadaDao.modificarOrganizacionVinculada(1, organizacion));
    }

    @Test
    public void pruebaObtenerOrganizacionesActivasExitoso() throws UsuariosExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        List<OrganizacionVinculada> organizaciones = organizacionVinculadaDao.obtenerOrganizacionesActivas();
        assertNotNull(organizaciones);
    }

    @Test
    public void pruebaObtenerOrganizacionesActivasListaNoVacia() throws UsuariosExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        List<OrganizacionVinculada> organizaciones = organizacionVinculadaDao.obtenerOrganizacionesActivas();
        assertTrue(organizaciones.size() > 0);
    }

    @Test
    public void pruebaObtenerOrganizacionesActivasTienenNombre() throws UsuariosExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        List<OrganizacionVinculada> organizaciones = organizacionVinculadaDao.obtenerOrganizacionesActivas();
        for (OrganizacionVinculada org : organizaciones) {
            assertNotNull(org.getNombre());
        }
    }

    @Test
    public void pruebaObtenerOrganizacionesActivasTienenCorreo() throws UsuariosExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        List<OrganizacionVinculada> organizaciones = organizacionVinculadaDao.obtenerOrganizacionesActivas();
        for (OrganizacionVinculada org : organizaciones) {
            assertNotNull(org.getCorreo());
        }
    }

    @Test
    public void pruebaObtenerOrganizacionesActivasTodosActivos() throws UsuariosExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        List<OrganizacionVinculada> organizaciones = organizacionVinculadaDao.obtenerOrganizacionesActivas();
        for (OrganizacionVinculada org : organizaciones) {
            assertEquals(EstadoOrganizacion.Activa, org.getEstadoOrganizacion());
        }
    }

    @Test
    public void pruebaInactivarOrganizacionExitoso() throws MensajeriaExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.inactivarOrganizacionVinculada(1);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarOrganizacionAlternoSegundoRegistro() throws MensajeriaExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.inactivarOrganizacionVinculada(2);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarOrganizacionAlternoTercerRegistro() throws MensajeriaExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.inactivarOrganizacionVinculada(3);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarOrganizacionAlternoIdNoExistente() throws MensajeriaExcepcion {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        int filasAfectadas = organizacionVinculadaDao.inactivarOrganizacionVinculada(9999);
        assertEquals(0, filasAfectadas);
    }
}