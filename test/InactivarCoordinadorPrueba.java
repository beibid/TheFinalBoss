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

public class InactivarCoordinadorPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String NOMBRE_COORDINADOR = "Laura Gutierrez Vega";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/InactivarCoordinadorVista.fxml")
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
    public void pruebaPanelDatosOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertFalse(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarCoordinadorMuestraDatos() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertTrue(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarCoordinadorMuestraNombreCorrecto() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        Label etiquetaNombre = lookup("#etiquetaNombre").query();
        assertTrue(etiquetaNombre.getText().equals("Laura"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaInactivarSinSeleccionMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#botonInactivar");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarSinSeleccionNoMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarConSeleccionOcultaDatos() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertFalse(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarSinConfirmarMantieneDatos() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxCoordinadores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_COORDINADOR);
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertTrue(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }
}