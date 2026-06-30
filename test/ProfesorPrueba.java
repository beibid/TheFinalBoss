import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Practicante;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfesorPrueba {

    @Test
    public void pruebaInsertarProfesorExitoso() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("PRF-T001", Turno.Matutino, "Ricardo", "Fuentes Morales", "ricardoT001", "prof.test1@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.insertarProfesor(profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarProfesorAlternoTurnoVespertino() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("PRF-T002", Turno.Vespertino, "Patricia", "Campos Ochoa", "patriciaT002", "prof.test2@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.insertarProfesor(profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarProfesorAlternoNombreCompuesto() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("PRF-T003", Turno.Matutino, "Jose Luis", "Mendez Castillo", "joseluisT003", "prof.test3@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.insertarProfesor(profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarProfesorAlternoApellidoConDe() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("PRF-T004", Turno.Vespertino, "Alicia", "de la Rosa Vargas", "aliciaT004", "prof.test4@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.insertarProfesor(profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarProfesorDuplicadoLanzaExcepcion() {
        Profesor profesor = new Profesor("29345", Turno.Matutino, "Grecia", "Ramirez Polanco", "greciadup", "grecia.dup@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionNombreNulo() {
        Profesor profesor = new Profesor("PRF-T005", Turno.Matutino, null, "Fuentes Morales", "passT005", "prof.test5@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionNumPersonalNulo() {
        Profesor profesor = new Profesor(null, Turno.Matutino, "Beatriz", "Hernandez Puga", "beatrizT006", "prof.test6@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionContraseniaNula() {
        Profesor profesor = new Profesor("PRF-T007", Turno.Vespertino, "Ignacio", "Solis Bravo", null, "prof.test7@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaInsertarProfesorExcepcionCorreoNulo() {
        Profesor profesor = new Profesor("PRF-T008", Turno.Matutino, "Norma", "Aguilar Perez", "normaT008", null, Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.insertarProfesor(profesor));
    }

    @Test
    public void pruebaModificarProfesorExitoso() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("22114455", Turno.Vespertino, "Ricardo", "Fuentes Morales", "ricardoNuevo22114455", "ricardo.fuentes2@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("22114455", profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorAlternoCambioTurno() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("33224411", Turno.Matutino, "Patricia", "Campos Ochoa", "patricia33224411", "patricia.campos@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("33224411", profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorAlternoCambioApellidos() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("22114455", Turno.Matutino, "Ricardo", "Fuentes Gutierrez", "ricardoNuevo22114455", "ricardo.fuentes2@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("22114455", profesor);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorAlternoNoExistente() throws UsuariosExcepcion {
        Profesor profesor = new Profesor("00000000", Turno.Matutino, "Inexistente", "Prueba", "pass00000000", "inexistente@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.modificarProfesor("00000000", profesor);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarProfesorExcepcionNumPersonalNulo() {
        Profesor profesor = new Profesor("22114455", Turno.Matutino, "Ricardo", "Fuentes Morales", "ricardo22114455", "ricardo.fuentes2@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.modificarProfesor(null, profesor));
    }

    @Test
    public void pruebaModificarProfesorExcepcionNombreNulo() {
        Profesor profesor = new Profesor("22114455", Turno.Matutino, null, "Fuentes Morales", "ricardo22114455", "ricardo.fuentes2@uv.mx", Estado.Activo);
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.modificarProfesor("22114455", profesor));
    }

    @Test
    public void pruebaInactivarProfesorExitoso() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("22114455");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorAlternoSegundoRegistro() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("33224411");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorAlternoNoExistente() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("00000000");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorAlternoNumPersonalVacio() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int filasAfectadas = profesorDao.inactivarProfesor("");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarProfesorExcepcionNumPersonalNulo() {
        ProfesorDao profesorDao = new ProfesorDao();
        assertThrows(UsuariosExcepcion.class, () -> profesorDao.inactivarProfesor(null));
    }

    @Test
    public void pruebaObtenerProfesoresActivosExitoso() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
        assertNotNull(profesores);
    }

    @Test
    public void pruebaObtenerProfesoresActivosListaNoVacia() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
        assertTrue(profesores.size() > 0);
    }

    @Test
    public void pruebaObtenerProfesoresActivosTodosActivos() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
        for (Profesor profesor : profesores) {
            assertEquals(Estado.Activo, profesor.getEstado());
        }
    }

    @Test
    public void pruebaObtenerProfesoresActivosTienenNumPersonal() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
        for (Profesor profesor : profesores) {
            assertNotNull(profesor.getNumeroDePersonalProfesor());
        }
    }

    @Test
    public void pruebaObtenerProfesoresActivosTienenTurno() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
        for (Profesor profesor : profesores) {
            assertNotNull(profesor.getTurno());
        }
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorExitoso() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Practicante> practicantes = profesorDao.obtenerPracticantesPorProfesor("29345");
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorAlternoOtroProfesor() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Practicante> practicantes = profesorDao.obtenerPracticantesPorProfesor("45678");
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorNoExistente() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        List<Practicante> practicantes = profesorDao.obtenerPracticantesPorProfesor("00000000");
        assertEquals(0, practicantes.size());
    }

    @Test
    public void pruebaExisteProfesorActivoRetornaEntero() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int resultado = profesorDao.existeProfesorActivo();
        assertTrue(resultado >= 0);
    }

    @Test
    public void pruebaExisteProfesorActivoHayAlMenosUno() throws UsuariosExcepcion {
        ProfesorDao profesorDao = new ProfesorDao();
        int resultado = profesorDao.existeProfesorActivo();
        assertTrue(resultado > 0);
    }
}