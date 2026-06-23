import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.DocumentacionPracticanteDao;
import logica.dominio.DocumentacionPracticante;
import logica.dominio.enums.EstadoRevision;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentacionPracticantePrueba {

    @Test
    public void pruebaAgregarDocumentacionPendienteExitoso() throws UsuariosExcepcion {
        DocumentacionPracticante doc = new DocumentacionPracticante("/uploads/oficio_S24013282.pdf", EstadoRevision.Pendiente, "");
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.agregarDocumentacion(doc);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarDocumentacionAlternoEstadoAprobado() throws UsuariosExcepcion {
        DocumentacionPracticante doc = new DocumentacionPracticante("/uploads/horario_S22019874.pdf", EstadoRevision.Aprobado, "");
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.agregarDocumentacion(doc);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarDocumentacionAlternoEstadoRechazado() throws UsuariosExcepcion {
        DocumentacionPracticante doc = new DocumentacionPracticante("/uploads/seguro_S23010045.pdf", EstadoRevision.Rechazado, "Firma ilegible, favor de resubir");
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.agregarDocumentacion(doc);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarDocumentacionAlternoRutaDiferente() throws UsuariosExcepcion {
        DocumentacionPracticante doc = new DocumentacionPracticante("/uploads/cronograma_S21007654.pdf", EstadoRevision.Pendiente, "");
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.agregarDocumentacion(doc);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarDocumentacionExcepcionRutaNula() {
        DocumentacionPracticante doc = new DocumentacionPracticante(null, EstadoRevision.Pendiente, "");
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> documentacionDao.agregarDocumentacion(doc));
    }

    @Test
    public void pruebaAgregarDocumentacionExcepcionEstadoNulo() {
        DocumentacionPracticante doc = new DocumentacionPracticante("/uploads/doc.pdf", null, "");
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        assertThrows(UsuariosExcepcion.class, () -> documentacionDao.agregarDocumentacion(doc));
    }

    @Test
    public void pruebaObtenerDocumentosPendientesExitoso() throws UsuariosExcepcion {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        List<DocumentacionPracticante> documentos = documentacionDao.obtenerDocumentosPendientes("S24013282");
        assertNotNull(documentos);
    }

    @Test
    public void pruebaObtenerDocumentosPendientesAlternoOtraPracticante() throws UsuariosExcepcion {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        List<DocumentacionPracticante> documentos = documentacionDao.obtenerDocumentosPendientes("S22019874");
        assertNotNull(documentos);
    }

    @Test
    public void pruebaObtenerDocumentosPendientesMatriculaNoExistente() throws UsuariosExcepcion {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        List<DocumentacionPracticante> documentos = documentacionDao.obtenerDocumentosPendientes("S99999999");
        assertEquals(0, documentos.size());
    }

    @Test
    public void pruebaValidarDocumentoAprobadoExitoso() throws UsuariosExcepcion {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.validarDocumento(1, EstadoRevision.Aprobado, "");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaValidarDocumentoAlternoRechazado() throws UsuariosExcepcion {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.validarDocumento(2, EstadoRevision.Rechazado, "El documento esta incompleto, falta la firma del jefe de departamento");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaValidarDocumentoAlternoOtroDocumento() throws UsuariosExcepcion {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.validarDocumento(3, EstadoRevision.Aprobado, "");
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaValidarDocumentoAlternoIdNoExistente() throws UsuariosExcepcion {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        int filasAfectadas = documentacionDao.validarDocumento(9999, EstadoRevision.Aprobado, "");
        assertEquals(0, filasAfectadas);
    }
}