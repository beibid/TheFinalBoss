import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.enums.EstadoProyecto;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProyectoPrueba {

    @Test
    public void pruebaAgregarProyectoExitoso() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(0, "Sistema de control escolar", "Sistema para gestionar calificaciones",
                "Ing. Roberto Salinas", EstadoProyecto.Disponible, "Tecnologico de Xalapa", "Educacion",
                "Av. Murillo Vidal 998, Xalapa", 1, "12345", "29345", Date.valueOf("2026-08-01"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarProyectoAlternoSectorDiferente() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(0, "Sistema de nomina empresarial", "Automatizacion del pago a empleados",
                "Lic. Carmen Flores", EstadoProyecto.Disponible, "Chedraui Xalapa", "Comercio",
                "Blvd. Adolfo Ruiz Cortines 123, Xalapa", 1, "12345", "45678", Date.valueOf("2026-08-15"), 4);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarProyectoAlternoMayorCupo() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(0, "Aplicacion movil de transporte", "App para rastreo de camiones urbanos",
                "Ing. Hector Mendoza", EstadoProyecto.Disponible, "Transporte Urbano Xalapa", "Transporte",
                "Calle Enriquez 45, Xalapa", 1, "12345", "29345", Date.valueOf("2026-09-01"), 10);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarProyectoAlternoDiferenteOrganizacion() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(0, "Portal de atencion ciudadana", "Sistema web para reportes de servicios publicos",
                "Lic. Ana Dominguez", EstadoProyecto.Disponible, "Municipio de Xalapa", "Gobierno",
                "Enriquez 20, Centro, Xalapa", 2, "12345", "45678", Date.valueOf("2026-07-20"), 3);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarProyectoDuplicadoLanzaExcepcion() {
        Proyecto proyecto = new Proyecto(1, "Sistema inventario", "sistema para inventario",
                "Horacio Peña", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "12345", "29345", Date.valueOf("2026-06-05"), 10);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaAgregarProyectoExcepcionNombreNulo() {
        Proyecto proyecto = new Proyecto(0, null, "Descripcion valida",
                "Responsable Valido", EstadoProyecto.Disponible, "Empresa SA", "Servicios",
                "Direccion valida 123", 1, "12345", "29345", Date.valueOf("2026-08-01"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaAgregarProyectoExcepcionFechaNula() {
        Proyecto proyecto = new Proyecto(0, "Proyecto sin fecha", "Descripcion valida",
                "Responsable Valido", EstadoProyecto.Disponible, "Empresa SA", "Servicios",
                "Direccion valida 123", 1, "12345", "29345", null, 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaAgregarProyectoExcepcionDescripcionNula() {
        Proyecto proyecto = new Proyecto(0, "Proyecto sin descripcion", null,
                "Responsable Valido", EstadoProyecto.Disponible, "Empresa SA", "Servicios",
                "Direccion valida 123", 1, "12345", "29345", Date.valueOf("2026-08-01"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaModificarProyectoExitoso() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(1, "Sistema control escolar actualizado", "Sistema mejorado",
                "Ing. Roberto Salinas", EstadoProyecto.Disponible, "Tecnologico de Xalapa", "Educacion",
                "Av. Murillo Vidal 998, Xalapa", 1, "12345", "45678", Date.valueOf("2026-08-01"), 6);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(1, proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoAlternoCambioResponsable() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(1, "Sistema control escolar actualizado", "Sistema mejorado",
                "Lic. Ernesto Vazquez", EstadoProyecto.Disponible, "Tecnologico de Xalapa", "Educacion",
                "Av. Murillo Vidal 998, Xalapa", 1, "12345", "29345", Date.valueOf("2026-08-01"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(1, proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoAlternoCambioCupo() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(2, "RIUV actualizado", "Sistema de conexion actualizado",
                "Luz Fernanda", EstadoProyecto.Disponible, "FEI", "Tecnologia",
                "Av. Avila Camacho", 1, "12345", "29345", Date.valueOf("2026-08-15"), 8);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(2, proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoAlternoNoExistente() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(9999, "Proyecto fantasma", "Descripcion",
                "Nadie", EstadoProyecto.Disponible, "Empresa", "Sector",
                "Direccion", 1, "12345", "29345", Date.valueOf("2026-08-01"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(9999, proyecto);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoExcepcionNombreNulo() {
        Proyecto proyecto = new Proyecto(1, null, "Descripcion",
                "Responsable", EstadoProyecto.Disponible, "Empresa", "Sector",
                "Direccion 123", 1, "12345", "29345", Date.valueOf("2026-08-01"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.modificarProyecto(1, proyecto));
    }

    @Test
    public void pruebaModificarProyectoExcepcionFechaNula() {
        Proyecto proyecto = new Proyecto(1, "Proyecto sin fecha", "Descripcion",
                "Responsable", EstadoProyecto.Disponible, "Empresa", "Sector",
                "Direccion 123", 1, "12345", "29345", null, 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.modificarProyecto(1, proyecto));
    }

    @Test
    public void pruebaInactivarProyectoExitoso() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.inactivarProyecto(3);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProyectoAlternoSegundoRegistro() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.inactivarProyecto(4);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProyectoAlternoNoExistente() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.inactivarProyecto(9999);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaObtenerProyectosDisponiblesExitoso() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
        assertNotNull(proyectos);
    }

    @Test
    public void pruebaObtenerProyectosDisponiblesListaNoVacia() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
        assertTrue(proyectos.size() > 0);
    }

    @Test
    public void pruebaObtenerProyectosDisponiblesTodosDisponibles() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
        for (Proyecto proyecto : proyectos) {
            assertEquals(EstadoProyecto.Disponible, proyecto.getEstado());
        }
    }

    @Test
    public void pruebaObtenerProyectosDisponiblesTienenNombre() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
        for (Proyecto proyecto : proyectos) {
            assertNotNull(proyecto.getNombreProyecto());
        }
    }

    @Test
    public void pruebaObtenerProyectoPorPracticanteExitoso() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante("S21007654");
        assertNotNull(proyecto);
    }

    @Test
    public void pruebaObtenerProyectoPorPracticanteAlternoOtraMatricula() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante("S22019874");
        assertNotNull(proyecto);
    }

    @Test
    public void pruebaObtenerProyectoPorPracticanteAlternoMatriculaNoExistente() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante("S99999999");
        assertNull(proyecto);
    }

    @Test
    public void pruebaObtenerProyectoPorPracticanteTieneNombre() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante("S21007654");
        assertNotNull(proyecto.getNombreProyecto());
    }
}