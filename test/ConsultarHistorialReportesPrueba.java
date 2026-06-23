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

public class ConsultarHistorialReportesPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String CORREO_PROFESOR = "ricardo.fuentes@uv.mx";
    private static final String CONTRASENA_PROFESOR = "ricardoP001";
    private static final String NOMBRE_PRACTICANTE = "Diego Hernandez Lopez - S21013001";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    private void iniciarSesionYAbrirHistorial() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_PROFESOR);
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_PROFESOR);
        sleep(PAUSA_CORTA);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
        clickOn("Ver historial");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaPanelErrorOcultoAlIniciar() {
        iniciarSesionYAbrirHistorial();
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaTablaVaciaAlIniciar() {
        iniciarSesionYAbrirHistorial();
        TableView tablaReportes = lookup("#tablaReportes").query();
        assertTrue(tablaReportes.getItems().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarPracticanteCargaReportes() {
        iniciarSesionYAbrirHistorial();
        clickOn("#comboBoxPracticante");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_LARGA);
        TableView tablaReportes = lookup("#tablaReportes").query();
        assertTrue(tablaReportes.getItems().size() > 0);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarPracticanteOcultaPanelError() {
        iniciarSesionYAbrirHistorial();
        clickOn("#comboBoxPracticante");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }
}