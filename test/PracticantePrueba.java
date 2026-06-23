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
        Practicante practicante = new Practicante("S24013282", "Ninguna", Genero.Masculino, "Ameth", "Polanco Hernandez", "amethS24013282", "ameth.polanco@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoGeneroFemenino() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S24013290", "Ninguna", Genero.Femenino, "Valeria", "Mendez Cruz", "valeriaS24013290", "valeria.mendez@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoConLenguaIndigena() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S23010045", "Nahuatl", Genero.Masculino, "Diego", "Xochitl Ramirez", "diegoS23010045", "diego.xochitl@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoGeneracion22() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S22019874", "Ninguna", Genero.Femenino, "Fernanda", "Gutierrez Leal", "fernandaS22019874", "fernanda.gutierrez@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteAlternoGeneracion21() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S21007654", "Totonaco", Genero.Masculino, "Kevin", "Bernal Salinas", "kevinS21007654", "kevin.bernal@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.insertarPracticante(practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInsertarPracticanteDuplicadoLanzaExcepcion() {
        Practicante practicante = new Practicante("S24013282", "Ninguna", Genero.Masculino, "Ameth", "Polanco Hernandez", "amethS24013282", "ameth2@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante(null, "Ninguna", Genero.Masculino, "Luis", "Torres Vega", "luispass", "luis.torres@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S24099001", "Ninguna", Genero.Femenino, null, "Reyes Dominguez", "pass24099001", "reyes@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionContraseniaNula() {
        Practicante practicante = new Practicante("S24099002", "Ninguna", Genero.Masculino, "Ivan", "Perez Molina", null, "ivan.perez@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInsertarPracticanteExcepcionCorreoNulo() {
        Practicante practicante = new Practicante("S24099003", "Ninguna", Genero.Femenino, "Sofia", "Luna Carrillo", "sofiaS24099003", null, Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.insertarPracticante(practicante));
    }

    @Test
    public void pruebaInactivarPracticanteExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S24013282");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaInactivarPracticanteAlternoSegundaMatricula() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.inactivarPracticante("S24013290");
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
        Practicante practicante = new Practicante("S21007654", "Zapoteco", Genero.Masculino, "Kevin", "Bernal Salinas", "kevinNuevoS21007654", "kevin.nuevo@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S21007654", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoCambioLengua() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S22019874", "Nahuatl", Genero.Femenino, "Fernanda", "Gutierrez Leal", "fernandaS22019874", "fernanda.nuevo@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S22019874", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoCambioCorreo() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S23010045", "Nahuatl", Genero.Masculino, "Diego", "Xochitl Ramirez", "diegoS23010045", "diego.nuevo@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S23010045", practicante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteAlternoMatriculaNoExistente() throws UsuariosExcepcion {
        Practicante practicante = new Practicante("S99999999", "Ninguna", Genero.Masculino, "Fantasma", "Inexistente", "pass99999999", "fantasma@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.modificarPracticante("S99999999", practicante);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void pruebaModificarPracticanteExcepcionMatriculaNula() {
        Practicante practicante = new Practicante("S21007654", "Ninguna", Genero.Masculino, "Kevin", "Bernal Salinas", "kevinS21007654", "kevin.bernal@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante(null, practicante));
    }

    @Test
    public void pruebaModificarPracticanteExcepcionMatriculaVacia() {
        Practicante practicante = new Practicante("S21007654", "Ninguna", Genero.Masculino, "Kevin", "Bernal Salinas", "kevinS21007654", "kevin.bernal@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante("", practicante));
    }

    @Test
    public void pruebaModificarPracticanteExcepcionNombreNulo() {
        Practicante practicante = new Practicante("S21007654", "Ninguna", Genero.Masculino, null, "Bernal Salinas", "kevinS21007654", "kevin.bernal@uv.mx", Estado.Activo, 3);
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.modificarPracticante("S21007654", practicante));
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
        int filasAfectadas = practicanteDao.asignarProyecto("S21007654", 1);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAsignarProyectoAlternoOtroPracticante() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        int filasAfectadas = practicanteDao.asignarProyecto("S22019874", 2);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAsignarProyectoAlternoProyectoNoExistente() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.asignarProyecto("S21007654", 9999));
    }

    @Test
    public void pruebaAsignarProyectoAlternoPracticanteNoExistente() {
        PracticanteDao practicanteDao = new PracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> practicanteDao.asignarProyecto("S99999999", 1));
    }
    @Test
    public void pruebaObtenerPracticantesPorProfesorExitoso() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(3);
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorAlternoOtroProfesor() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(4);
        assertNotNull(practicantes);
    }

    @Test
    public void pruebaObtenerPracticantesPorProfesorNoExistente() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(9999);
        assertEquals(0, practicantes.size());
    }
}