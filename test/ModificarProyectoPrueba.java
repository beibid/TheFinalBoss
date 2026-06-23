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

public class ModificarProyectoPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 500;
    private static final int PAUSA_LARGA = 1000;

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/ModificarProyectoVista.fxml")
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
    }

    @Test
    public void pruebaPanelExitoOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        VBox panelExito = lookup("#panelExito").query();
        assertFalse(panelExito.isVisible());
    }

    @Test
    public void pruebaPanelFormularioOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        assertFalse(panelFormulario.isVisible());
    }

    @Test
    public void pruebaCamposVaciosAlGuardarMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.DOWN);
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.ENTER);
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        if (panelFormulario.isVisible()) {
            clickOn("#campoNombreProyecto");
            sleep(PAUSA_CORTA);
            type(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
            sleep(PAUSA_CORTA);
            type(javafx.scene.input.KeyCode.DELETE);
            sleep(PAUSA_CORTA);
            clickOn("#botonGuardar");
            sleep(PAUSA_CORTA);
            clickOn("Sí");
            sleep(PAUSA_LARGA);
            VBox panelError = lookup("#panelError").query();
            assertTrue(panelError.isVisible());
        }
    }

    @Test
    public void pruebaFechaInvalidaMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.DOWN);
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.ENTER);
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        if (panelFormulario.isVisible()) {
            clickOn("#campoFechaRegistro");
            sleep(PAUSA_CORTA);
            type(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
            sleep(PAUSA_CORTA);
            type(javafx.scene.input.KeyCode.DELETE);
            sleep(PAUSA_CORTA);
            write("fecha-invalida");
            sleep(PAUSA_CORTA);
            clickOn("#botonGuardar");
            sleep(PAUSA_CORTA);
            clickOn("Sí");
            sleep(PAUSA_LARGA);
            VBox panelError = lookup("#panelError").query();
            assertTrue(panelError.isVisible());
        }
    }

    @Test
    public void pruebaCancelarOcultaFormulario() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.DOWN);
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.ENTER);
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        if (panelFormulario.isVisible()) {
            clickOn("#botonCancelar");
            sleep(PAUSA_CORTA);
            clickOn("Sí");
            sleep(PAUSA_LARGA);
            assertFalse(panelFormulario.isVisible());
        }
    }

    @Test
    public void pruebaCancelarSinConfirmarNoOcultaFormulario() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.DOWN);
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.ENTER);
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        if (panelFormulario.isVisible()) {
            clickOn("#botonCancelar");
            sleep(PAUSA_CORTA);
            clickOn("No");
            sleep(PAUSA_LARGA);
            assertTrue(panelFormulario.isVisible());
        }
    }
}