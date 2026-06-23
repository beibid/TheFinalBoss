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

public class GenerarAutoevaluacionPracticantePrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String CORREO_PRACTICANTE = "diego.hernandez@uv.mx";
    private static final String CONTRASENA_PRACTICANTE = "diegoS21013001";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    private void iniciarSesionYAbrirAutoevaluacion() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_PRACTICANTE);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_PRACTICANTE);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
        clickOn("Realizar Autoevaluacion");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaPanelErrorOcultoAlIniciar() {
        iniciarSesionYAbrirAutoevaluacion();
        assertFalse(lookup("#panelError").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaPanelExitoOcultoAlIniciar() {
        iniciarSesionYAbrirAutoevaluacion();
        assertFalse(lookup("#panelExito").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMatriculaMuestraCorrectamente() {
        iniciarSesionYAbrirAutoevaluacion();
        Label etiquetaMatricula = lookup("#etiquetaMatricula").query();
        assertTrue(etiquetaMatricula.getText().equals("S21013001"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaGuardarSinResponderMuestraError() {
        iniciarSesionYAbrirAutoevaluacion();
        clickOn("#botonGuardar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaGenerarPDFSinReportesCompletosRequeridosMuestraError() {
        iniciarSesionYAbrirAutoevaluacion();
        clickOn("#botonGenerarPDF");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("incompletos")
                || etiquetaTitulo.getText().contains("incompletas"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarSinConfirmarNoBorraRespuestas() {
        iniciarSesionYAbrirAutoevaluacion();
        clickOn("1. Mi participación en la Organización Vinculada fue productiva.");
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        assertFalse(lookup("#panelError").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarConfirmadoOcultaPaneles() {
        iniciarSesionYAbrirAutoevaluacion();
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        assertFalse(lookup("#panelError").<VBox>query().isVisible());
        assertFalse(lookup("#panelExito").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }
}