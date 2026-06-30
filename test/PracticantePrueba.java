import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PracticantePrueba {

    @Test
    public void pruebaInsertarPracticanteExitoso() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S25001001", "Ninguna", Genero.Masculino, "Carlos", "Mora Fuentes", "carlosS25001001", "carlos.mora@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoGeneroFemenino() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S25001002", "Ninguna", Genero.Femenino, "Laura", "Solis Vera", "lauraS25001002", "laura.solis@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoConLenguaIndigena() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S25001003", "Nahuatl", Genero.Masculino, "Emilio", "Cruz Tepetl", "emilioS25001003", "emilio.cruz@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoGeneroFemenino2() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S25001004", "Ninguna", Genero.Femenino, "Alex", "Rios Blanco", "alexS25001004", "alex.rios@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoProfesorDistinto() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S25001005", "Totonaco", Genero.Masculino, "Rodrigo", "Leal Pedraza", "rodrigoS25001005", "rodrigo.leal@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteDuplicadoLanzaExcepcion() {
        Practicante practicante = new Practicante("S24013287", "Ninguna", Genero.Masculino, "David", "Espinoza Morales", "daviddup", "daviddup@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante(null, "Ninguna", Genero.Masculino, "Luis", "Torres Vega", "luispassnuevo", "luisnuevo@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S25001006", "Ninguna", Genero.Femenino, null, "Reyes Dominguez", "pass25001006", "reyes25@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionContraseniaNula() {
        Practicante practicante = new Practicante("S25001007", "Ninguna", Genero.Masculino, "Ivan", "Perez Molina", null, "ivan25@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionCorreoNulo() {
        Practicante practicante = new Practicante("S25001008", "Ninguna", Genero.Femenino, "Sofia", "Luna Carrillo", "sofiaS25001008", null, Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInactivarPracticanteExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S012452");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarPracticanteAlternoSegundaMatricula() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S0252944");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarPracticanteAlternoMatriculaNoExistente() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S99999999");
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaInactivarPracticanteExcepcionMatriculaNula() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.inactivarPracticante(null));
    }

    @Test
    public void pruebaInactivarPracticanteExcepcionMatriculaVacia() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.inactivarPracticante(""));
    }

    @Test
    public void pruebaModificarPracticanteExitoso() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S24026789", "Ninguna", Genero.Masculino, "Valentin", "Benavidez Martinez", "valenmod1", "valenmod1@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S24026789", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoCambioLengua() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S23016547", "Zapoteco", Genero.Masculino, "Valentina", "Espinoza", "valinmod2", "valinmod2@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S23016547", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoCambioCorreo() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S23035678", "Ninguna", Genero.Masculino, "Martin", "Murrieta", "tintinmod3", "tintinmod3@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S23035678", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoMatriculaNoExistente() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S99999999", "Ninguna", Genero.Masculino, "Fantasma", "Inexistente", "pass99999999", "fantasma@uv.mx", Estado.Activo, 6);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S99999999", practicante);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante("S24026789", "Ninguna", Genero.Masculino, "Valentin", "Benavidez Martinez", "valenmod4", "valenmod4@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante(null, practicante));
    }

    @Test
    public void pruebaModificarPracticanteExcepcionMatriculaVacia() {
        Practicante practicante = new Practicante("S24026789", "Ninguna", Genero.Masculino, "Valentin", "Benavidez Martinez", "valenmod5", "valenmod5@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante("", practicante));
    }

    @Test
    public void pruebaModificarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S24026789", "Ninguna", Genero.Masculino, null, "Benavidez Martinez", "valenmod6", "valenmod6@uv.mx", Estado.Activo, 7);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante("S24026789", practicante));
    }

    @Test
    public void pruebaObtenerPracticantesActivosExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesActivosListaNoVacia() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
        assertTrue(practicantes.size() > 0);
    }

    @Test
    public void pruebaObtenerPracticantesActivosTodosActivos() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
        for (Practicante practicante : practicantes) {
            assertEquals(Estado.Activo, practicante.getEstado());
        }
    }

    @Test
    public void pruebaObtenerPracticantesActivosTienenMatricula() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
        for (Practicante practicante : practicantes) {
            assertNotNull(practicante.getMatricula());
        }
    }

    @Test
    public void pruebaObtenerPracticantesActivosTienenNombre() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
        for (Practicante practicante : practicantes) {
            assertNotNull(practicante.getNombre());
        }
    }

    @Test
    public void pruebaAsignarProyectoExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.asignarProyecto("S23010045", 1);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAsignarProyectoAlternoOtroPracticante() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.asignarProyecto("S0425523", 5);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAsignarProyectoAlternoProyectoNoExistente() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.asignarProyecto("S23010045", 9999));
    }

    @Test
    public void pruebaAsignarProyectoAlternoPracticanteNoExistente() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.asignarProyecto("S99999999", 1));
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(6);
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorAlternoOtroProfesor() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(7);
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorNoExistente() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(9999);
        assertEquals(0, practicantes.size());
    }
}