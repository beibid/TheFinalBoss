import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.EntregaDocumentacion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.EntregaDocumentacionDao;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntregaDocumentacionPrueba {

    @Test
    public void pruebaAgregarEntregaDocumentacionExitoso() throws UsuariosExcepcion {
        EntregaDocumentacion entregaDocumentacion = new EntregaDocumentacion(Date.valueOf("2026-04-20"), "S20013461", 1);
        EntregaDocumentacionDao entregaDocumentacionDao = new EntregaDocumentacionDao();
        int filasAfectadas = entregaDocumentacionDao.agregarEntrega(entregaDocumentacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarEntregaDocumentacionAlternaOtraFecha() throws UsuariosExcepcion {
        EntregaDocumentacion entregaDocumentacion = new EntregaDocumentacion(Date.valueOf("2026-04-21"), "S20013461", 1);
        EntregaDocumentacionDao entregaDocumentacionDao = new EntregaDocumentacionDao();
        int filasAfectadas = entregaDocumentacionDao.agregarEntrega(entregaDocumentacion);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarEntregaDocumentacionExcepcionFechaNula() {
        EntregaDocumentacion entregaDocumentacion = new EntregaDocumentacion(null, "S20013461", 1);
        EntregaDocumentacionDao entregaDocumentacionDao = new EntregaDocumentacionDao();
        assertThrows(UsuariosExcepcion.class, () -> entregaDocumentacionDao.agregarEntrega(entregaDocumentacion));
    }

    @Test
    public void pruebaAgregarEntregaDocumentacionExcepcionMatriculaNula() {
        EntregaDocumentacion entregaDocumentacion = new EntregaDocumentacion(Date.valueOf("2026-04-20"), null, 1);
        EntregaDocumentacionDao entregaDocumentacionDao = new EntregaDocumentacionDao();
        assertThrows(UsuariosExcepcion.class, () -> entregaDocumentacionDao.agregarEntrega(entregaDocumentacion));
    }
}