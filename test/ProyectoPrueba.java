import logica.dao.excepciones.MensajeriaExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.enums.EstadoProyecto;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProyectoPrueba {

    @Test
    public void pruebaAgregarProyectoExitoso() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(0, "Sistema de nomina", "Sistema para gestionar nomina",
                "Elena Vargas", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", Date.valueOf("2026-06-05"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarProyectoAlternoDuplicado() {
        Proyecto proyecto = new Proyecto(1, "Sistema inventario", "sistema para inventario",
                "Horacio Peña", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", Date.valueOf("2026-06-05"), 10);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaAgregarProyectoExcepcionNombreNulo() {
        Proyecto proyecto = new Proyecto(0, null, "Descripcion",
                "Elena Vargas", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", Date.valueOf("2026-06-05"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaAgregarProyectoExcepcionFechaNula() {
        Proyecto proyecto = new Proyecto(0, "Sistema de nomina", "Descripcion",
                "Elena Vargas", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", null, 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaModificarProyectoExitoso() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(1, "Sistema inventario actualizado", "Descripcion actualizada",
                "Horacio Peña", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", Date.valueOf("2026-06-05"), 10);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(1, proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoAlternoNoExistente() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(9999, "Proyecto inexistente", "Descripcion",
                "Elena Vargas", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", Date.valueOf("2026-06-05"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(9999, proyecto);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoExcepcionNombreNulo() {
        Proyecto proyecto = new Proyecto(1, null, "Descripcion",
                "Horacio Peña", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", Date.valueOf("2026-06-05"), 10);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.modificarProyecto(1, proyecto));
    }

    @Test
    public void pruebaModificarProyectoExcepcionFechaNula() {
        Proyecto proyecto = new Proyecto(1, "Sistema inventario", "Descripcion",
                "Horacio Peña", EstadoProyecto.Disponible, "Liverpool", "Ventas",
                "Plaza Americas", 1, "22114455", "33445566", null, 10);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.modificarProyecto(1, proyecto));
    }

    @Test
    public void pruebaObtenerProyectosDisponiblesExitoso() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
        assertNotNull(proyectos);
    }

    @Test
    public void pruebaInactivarProyectoExitoso() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.inactivarProyecto(1);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProyectoAlternoNoExistente() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.inactivarProyecto(9999);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaObtenerProyectoPorPracticanteExitoso() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante("S24013287");
        assertNotNull(proyecto);
    }

    @Test
    public void pruebaObtenerProyectoPorPracticanteAlternoNoExistente() throws MensajeriaExcepcion {
        ProyectoDao proyectoDao = new ProyectoDao();
        Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante("S99999999");
        assertEquals(null, proyecto);
    }
}