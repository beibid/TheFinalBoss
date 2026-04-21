import logica.dao.excepciones.UsuariosExcepcion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.DocumentacionPracticanteDao;
import logica.dominio.DocumentacionPracticante;
import logica.dominio.enums.EstadoRevision;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentacionPracticantePrueba {

    @Test
    public void pruebaAgregarDocumentacionExitoso() throws UsuariosExcepcion {
        DocumentacionPracticante documentacionPracticante = new DocumentacionPracticante("/archivos/doc50.pdf", EstadoRevision.Pendiente);
        DocumentacionPracticanteDao documentacionPracticanteDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionPracticanteDao.agregarDocumentacion(documentacionPracticante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarDocumentacionAlternaEstadoRevisado() throws UsuariosExcepcion {
        DocumentacionPracticante documentacionPracticante = new DocumentacionPracticante("/archivos/doc51.pdf", EstadoRevision.Revisado);
        DocumentacionPracticanteDao documentacionPracticanteDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionPracticanteDao.agregarDocumentacion(documentacionPracticante);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarDocumentacionExcepcionRutaNula() {
        DocumentacionPracticante documentacionPracticante = new DocumentacionPracticante(null, EstadoRevision.Pendiente);
        DocumentacionPracticanteDao documentacionPracticanteDao = new DocumentacionPracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> documentacionPracticanteDao.agregarDocumentacion(documentacionPracticante));
    }

    @Test
    public void pruebaAgregarDocumentacionExcepcionEstadoNulo() {
        DocumentacionPracticante documentacionPracticante = new DocumentacionPracticante("/archivos/doc52.pdf", null);
        DocumentacionPracticanteDao documentacionPracticanteDao = new DocumentacionPracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> documentacionPracticanteDao.agregarDocumentacion(documentacionPracticante));
    }
}