package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubirDocumentacionPracticantePrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/SubirDocumentacionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    @Test
    public void pruebaPanelErrorOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaPanelExitoOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        VBox panelExito = lookup("#panelExito").query();
        assertFalse(panelExito.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaEtiquetaArchivoMuestraTextoInicial() {
        sleep(PAUSA_LARGA);
        Label etiquetaArchivo = lookup("#etiquetaArchivo").query();
        assertTrue(etiquetaArchivo.getText().equals("Ningún archivo seleccionado"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSubirSinArchivoMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("Subir documentación");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSubirSinArchivoMuestraMensajeCorrecto() {
        sleep(PAUSA_LARGA);
        clickOn("Subir documentación");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("requerido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarOcultaPanelError() {
        sleep(PAUSA_LARGA);
        clickOn("Subir documentación");
        sleep(PAUSA_LARGA);
        clickOn("Cancelar");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarRestableceLabelArchivo() {
        sleep(PAUSA_LARGA);
        clickOn("Cancelar");
        sleep(PAUSA_LARGA);
        Label etiquetaArchivo = lookup("#etiquetaArchivo").query();
        assertTrue(etiquetaArchivo.getText().equals("Ningún archivo seleccionado"));
        sleep(PAUSA_FINAL);
    }
}