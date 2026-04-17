

import org.junit.jupiter.api.Test;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.enums.EstadoProyecto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.sql.Date;


public class ProyectoPrueba {
    @Test
    public void insertarProyecto(){
        Proyecto nuevoProyecto = new Proyecto(1, "ASO", "Proyecto perro", "Juanito", EstadoProyecto.Disponible, "Oracle", "Negocio perro", "Su casa", 1, "S20013456", "22331083", Date.valueOf("2026-04-15"));
        ProyectoDao proyecto = new ProyectoDao();

        assertDoesNotThrow(() -> proyecto.agregarProyecto(nuevoProyecto));
    }
}
