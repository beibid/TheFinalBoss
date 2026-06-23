package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DescargarDocumentacionPrueba extends ApplicationTest {

    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/DescargarDocumentacionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    @Test
    public void pruebaTablaDocumentosCargaAlIniciar() {
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#tablaDocumentos").<TableView>query().getItems().size() > 0);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaTablaDocumentosTieneDosDocumentos() {
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#tablaDocumentos").<TableView>query().getItems().size() == 2);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaPanelErrorOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        assertFalse(lookup("#panelError").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaPanelExitoOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        assertFalse(lookup("#panelExito").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaTablaContieneDocumentoPRAIS() {
        sleep(PAUSA_LARGA);
        TableView<String[]> tabla = lookup("#tablaDocumentos").query();
        boolean encontrado = false;
        for (String[] fila : tabla.getItems()) {
            if (fila[0].contains("PRAIS")) {
                encontrado = true;
            }
        }
        assertTrue(encontrado);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaTablaContieneDocumentoF1() {
        sleep(PAUSA_LARGA);
        TableView<String[]> tabla = lookup("#tablaDocumentos").query();
        boolean encontrado = false;
        for (String[] fila : tabla.getItems()) {
            if (fila[0].contains("F1")) {
                encontrado = true;
            }
        }
        assertTrue(encontrado);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaArchivosExistenEnRecursos() {
        sleep(PAUSA_LARGA);
        TableView<String[]> tabla = lookup("#tablaDocumentos").query();
        for (String[] fila : tabla.getItems()) {
            boolean existe = getClass().getResourceAsStream(fila[1]) != null;
            assertTrue(existe);
        }
        sleep(PAUSA_FINAL);
    }
}