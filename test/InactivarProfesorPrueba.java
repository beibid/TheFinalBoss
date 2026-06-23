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

public class InactivarProfesorPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String NOMBRE_PROFESOR = "Ricardo Fuentes Morales";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/InactivarProfesorVista.fxml")
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
    public void pruebaSeleccionarProfesorMuestraDatos() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProfesores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROFESOR);
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertTrue(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarProfesorMuestraNombreCorrecto() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProfesores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROFESOR);
        sleep(PAUSA_LARGA);
        Label etiquetaNombre = lookup("#etiquetaNombre").query();
        assertTrue(etiquetaNombre.getText().equals("Ricardo"));
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
    public void pruebaCancelarConSeleccionOcultaDatos() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProfesores");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROFESOR);
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertFalse(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }
}