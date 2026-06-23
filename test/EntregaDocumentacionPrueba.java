import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.EntregaDocumentacionDao;
import logica.dominio.EntregaDocumentacion;
import java.sql.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntregaDocumentacionPrueba {

    @Test
    public void pruebaAgregarEntregaExitoso() throws UsuariosExcepcion {
        EntregaDocumentacion entrega = new EntregaDocumentacion(Date.valueOf("2026-06-10"), "S24013282", 1);
        EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();
        int filasAfectadas = entregaDao.agregarEntrega(entrega);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarEntregaAlternoOtraPracticante() throws UsuariosExcepcion {
        EntregaDocumentacion entrega = new EntregaDocumentacion(Date.valueOf("2026-06-12"), "S22019874", 2);
        EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();
        int filasAfectadas = entregaDao.agregarEntrega(entrega);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarEntregaAlternoFechaDiferente() throws UsuariosExcepcion {
        EntregaDocumentacion entrega = new EntregaDocumentacion(Date.valueOf("2026-07-01"), "S23010045", 1);
        EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();
        int filasAfectadas = entregaDao.agregarEntrega(entrega);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarEntregaAlternoDocumentoDiferente() throws UsuariosExcepcion {
        EntregaDocumentacion entrega = new EntregaDocumentacion(Date.valueOf("2026-06-15"), "S21007654", 3);
        EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();
        int filasAfectadas = entregaDao.agregarEntrega(entrega);
        assertEquals(1, filasAfectadas);
    }

    @Test
    public void pruebaAgregarEntregaExcepcionFechaNula() {
        EntregaDocumentacion entrega = new EntregaDocumentacion(null, "S24013282", 1);
        EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();
        assertThrows(UsuariosExcepcion.class, () -> entregaDao.agregarEntrega(entrega));
    }

    @Test
    public void pruebaAgregarEntregaExcepcionMatriculaNula() {
        EntregaDocumentacion entrega = new EntregaDocumentacion(Date.valueOf("2026-06-10"), null, 1);
        EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();
        assertThrows(UsuariosExcepcion.class, () -> entregaDao.agregarEntrega(entrega));
    }

    @Test
    public void pruebaAgregarEntregaExcepcionIdDocumentoInexistente() {
        EntregaDocumentacion entrega = new EntregaDocumentacion(Date.valueOf("2026-06-10"), "S24013282", 9999);
        EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();
        assertThrows(UsuariosExcepcion.class, () -> entregaDao.agregarEntrega(entrega));
    }
}