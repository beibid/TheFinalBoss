package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvaluacionReportePrueba extends ApplicationTest {

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

    private void iniciarSesionYAbrirEvaluacion() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_PROFESOR);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_PROFESOR);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
        clickOn("Calificar reporte");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaPanelErrorOcultoAlIniciar() {
        iniciarSesionYAbrirEvaluacion();
        assertFalse(lookup("#panelError").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaPanelExitoOcultoAlIniciar() {
        iniciarSesionYAbrirEvaluacion();
        assertFalse(lookup("#panelExito").<VBox>query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaTablaVaciaAlIniciar() {
        iniciarSesionYAbrirEvaluacion();
        assertTrue(lookup("#tablaReportes").<TableView>query().getItems().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaEvaluarSinReporteSeleccionadoMuestraError() {
        iniciarSesionYAbrirEvaluacion();
        clickOn("Evaluar");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#etiquetaTituloError").<Label>query().getText().contains("seleccionado"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarPracticanteCargaReportes() {
        iniciarSesionYAbrirEvaluacion();
        clickOn("#comboBoxPracticante");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#tablaReportes").<TableView>query().getItems().size() > 0);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCalificacionFueraDeRangoMuestraError() {
        iniciarSesionYAbrirEvaluacion();
        clickOn("#comboBoxPracticante");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_LARGA);
        clickOn(lookup("#tablaReportes").<TableView>query().lookup(".table-row-cell"));
        sleep(PAUSA_CORTA);
        clickOn("#campoCalificacion");
        write("150");
        clickOn("#campoObservaciones");
        write("Observacion de prueba");
        clickOn("Evaluar");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#etiquetaTituloError").<Label>query().getText().contains("invalida"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCalificacionNoNumericaMuestraError() {
        iniciarSesionYAbrirEvaluacion();
        clickOn("#comboBoxPracticante");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_LARGA);
        clickOn(lookup("#tablaReportes").<TableView>query().lookup(".table-row-cell"));
        sleep(PAUSA_CORTA);
        clickOn("#campoCalificacion");
        write("abc");
        clickOn("#campoObservaciones");
        write("Observacion de prueba");
        clickOn("Evaluar");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#etiquetaTituloError").<Label>query().getText().contains("invalida"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarLimpiaFormulario() {
        iniciarSesionYAbrirEvaluacion();
        clickOn("#campoCalificacion");
        write("90");
        clickOn("#campoObservaciones");
        write("Buen trabajo");
        clickOn("Cancelar");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#campoCalificacion").<javafx.scene.control.TextField>query().getText().isEmpty());
        sleep(PAUSA_FINAL);
    }
}