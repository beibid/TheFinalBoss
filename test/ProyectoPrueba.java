import logica.dao.excepciones.MensajeriaExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.enums.EstadoProyecto;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProyectoPrueba {


    @Test
    public void pruebaAgregarProyectoExitoso() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(50, "Proyecto 50", "Descripcion 50",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 50", "Finanzas",
                "Calle 50", 1, "S20013461", "C005", Date.valueOf("2026-04-20"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarProyectoAlternoDuplicado() {
        Proyecto proyecto = new Proyecto(6, "Proyecto 31", "Descripcion 31",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 31", "Finanzas",
                "Calle 31", 1, "S20013461", "C005", Date.valueOf("2026-04-10"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaAgregarProyectoExcepcionNombreNulo() {
        Proyecto proyecto = new Proyecto(51, null, "Descripcion 51",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 51", "Finanzas",
                "Calle 51", 1, "S20013461", "C005", Date.valueOf("2026-04-20"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaAgregarProyectoExcepcionFechaNula() {
        Proyecto proyecto = new Proyecto(52, "Proyecto 52", "Descripcion 52",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 52", "Finanzas",
                "Calle 52", 1, "S20013461", "C005", null, 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.agregarProyecto(proyecto));
    }

    @Test
    public void pruebaModificarProyectoExitoso() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(50, "Proyecto 50 Modificado", "Descripcion modificada",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 50", "Finanzas",
                "Calle 50", 1, "S20013461", "C005", Date.valueOf("2026-04-20"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(50, proyecto);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoAlternoNoExistente() throws MensajeriaExcepcion {
        Proyecto proyecto = new Proyecto(9999, "Proyecto No Existente", "Descripcion",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 31", "Finanzas",
                "Calle 31", 1, "S20013461", "C005", Date.valueOf("2026-04-20"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        int filasAfectadas = proyectoDao.modificarProyecto(9999, proyecto);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarProyectoExcepcionNombreNulo() {
        Proyecto proyecto = new Proyecto(50, null, "Descripcion modificada",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 50", "Finanzas",
                "Calle 50", 1, "S20013461", "C005", Date.valueOf("2026-04-20"), 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.modificarProyecto(50, proyecto));
    }

    @Test
    public void pruebaModificarProyectoExcepcionFechaNula() {
        Proyecto proyecto = new Proyecto(50, "Proyecto 50 Modificado", "Descripcion modificada",
                "Elena Vargas", EstadoProyecto.Disponible, "Empresa 50", "Finanzas",
                "Calle 50", 1, "S20013461", "C005", null, 5);
        ProyectoDao proyectoDao = new ProyectoDao();
        assertThrows(MensajeriaExcepcion.class, () -> proyectoDao.modificarProyecto(50, proyecto));
    }
}