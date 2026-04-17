

import logica.dominio.EntregaDocumentacion;
import org.junit.jupiter.api.Test;
import logica.dao.objetos.EntregaDocumentacionDao;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class EntregaDocumentacionPrueba {
    @Test
    public void registrarEntregaDocumentacion(){
        EntregaDocumentacion nuevaEntrega = new EntregaDocumentacion(Date.valueOf("2026-04-17"), "S20013456", 1);
        EntregaDocumentacionDao entregaDocumentacion = new EntregaDocumentacionDao();

        assertDoesNotThrow(() -> entregaDocumentacion.agregarEntrega(nuevaEntrega));
    }
}
