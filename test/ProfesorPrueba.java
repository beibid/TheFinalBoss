

import logica.dominio.Practicante;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import logica.dao.excepciones.UsuariosExcepcion;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class ProfesorPrueba {

    @Test
    public void pruebaInsertarProfesorExitoso() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("P050", Turno.Matutino, "Elena", "Vargas", "pass50", "elena@uv.mx",Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.insertarProfesor(profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarProfesorAlternoDuplicado() {
        Profesor profesor = new Profesor("P031", Turno.Matutino, "Elena", "Vargas",  "pass31", "elena@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionNumPersonalNulo() {
        Profesor profesor = new Profesor(null, Turno.Matutino, "Elena", "Vargas", "pass31", "elena@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionNombreNulo() {
        Profesor profesor = new Profesor("P051", Turno.Matutino, null, "Vargas",  "pass31","elena@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaModificarProfesorExitoso() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("P050", Turno.Vespertino, "Elena", "Vargas", "pass50nuevo", "elenaNuevo@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("P050", profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorAlternoNoExistente() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("P999", Turno.Vespertino, "Elena", "Vargas",  "pass31", "elenaNuevo@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("P999", profesor);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorExcepcionNombreNulo() {
        Profesor profesor = new Profesor("P050", Turno.Vespertino, null, "Vargas", "pass31", "elenaNuevo@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.modificarProfesor("P050", profesor));
    }

    @Test
    public void pruebaModificarProfesorExcepcionNumPersonalNulo() {
        Profesor profesor = new Profesor("P050", Turno.Vespertino, "Elena", "Vargas",  "pass31", "elenaNuevo@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.modificarProfesor(null, profesor));
    }

    @Test
    public void pruebaInactivarProfesorExitoso() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("P050");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorAlternoNoExistente() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("P999");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorExcepcionNumPersonalNulo() {
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.inactivarProfesor(null));
    }

    @Test
    public void pruebaInactivarProfesorExcepcionNumPersonalVacio() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaObtenerProfesoresActivosExitoso() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
        assertNotNull(profesores);
    }

    @Test
    public void pruebaObtenerProfesoresActivosNoVacia() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
        assertEquals(true, profesores.size() > 0);
    }

    @Test
    public void pruebaObtenerPracticantePorProfesorExitoso() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Practicante> practicantes = profesorDao.obtenerPracticantesPorProfesor("22342");
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantePorProfesorAlternoNoExistente() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Practicante> practicantes = profesorDao.obtenerPracticantesPorProfesor("9999");
        assertEquals(0, practicantes.size());

    }


}