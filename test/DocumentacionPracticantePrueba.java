

import org.junit.jupiter.api.Test;
import logica.dao.objetos.DocumentacionPracticanteDao;
import logica.dominio.DocumentacionPracticante;
import logica.dominio.enums.EstadoRevision;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class DocumentacionPracticantePrueba {
    @Test
    public void insertarDocumentacionPracticante (){
        DocumentacionPracticante nuevaDocumenacionPracticante = new DocumentacionPracticante("ruta/usuario/archivo", EstadoRevision.Pendiente);
        DocumentacionPracticanteDao documentacionPracticante = new DocumentacionPracticanteDao();

        assertDoesNotThrow(() -> documentacionPracticante.agregarDocumentacion(nuevaDocumenacionPracticante));
    }
}
