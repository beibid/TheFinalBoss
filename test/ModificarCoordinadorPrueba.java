package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModificarCoordinadorPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String NOMBRE_COORDINADOR = "Laura Gutierrez Vega";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/ModificarCoordinadorVista.fxml")
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
    public void pruebaPanelFormularioOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        assertFalse(panelFormulario.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarCoordinadorMuestraFormulario() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        assertTrue(panelFormulario.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaFormularioSeRellenaConDatosDeLaBD() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        TextField campoNombre = lookup("#campoNombre").query();
        assertFalse(campoNombre.getText().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCampoNombreVacioMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        clickOn("#campoNombre");
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
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCampoApellidosVacioMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        clickOn("#campoApellidos");
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
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCampoCorreoVacioMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        clickOn("#campoCorreo");
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
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarConfirmadoOcultaFormulario() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        assertFalse(panelFormulario.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarSinConfirmarMantieneFormulario() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        assertTrue(panelFormulario.isVisible());
        sleep(PAUSA_FINAL);
    }
}