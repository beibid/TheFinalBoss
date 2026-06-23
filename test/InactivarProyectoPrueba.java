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

public class InactivarProyectoPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String NOMBRE_PROYECTO = "Sistema de nomina";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/InactivarProyectoVista.fxml")
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
    public void pruebaSeleccionarProyectoMuestraDatos() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROYECTO);
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertTrue(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarProyectoMuestraNombreCorrecto() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROYECTO);
        sleep(PAUSA_LARGA);
        Label etiquetaNombreProyecto = lookup("#etiquetaNombreProyecto").query();
        assertTrue(etiquetaNombreProyecto.getText().equals("Sistema de nomina"));
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
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROYECTO);
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
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROYECTO);
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertTrue(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaInactivarConSeleccionPideConfirmacion() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROYECTO);
        sleep(PAUSA_LARGA);
        clickOn("#botonInactivar");
        sleep(PAUSA_LARGA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        VBox panelDatos = lookup("#panelDatos").query();
        assertTrue(panelDatos.isVisible());
        sleep(PAUSA_FINAL);
    }
}