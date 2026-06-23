package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrarPeriodoPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/RegistrarPeriodoVista.fxml")
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
    public void pruebaCamposVaciosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("Registrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaNombreVacioMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#selectorFechaInicio");
        sleep(PAUSA_CORTA);
        write("2026-12-01");
        sleep(PAUSA_CORTA);
        clickOn("Registrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaFechaVaciaMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Febrero-Agosto 2026");
        sleep(PAUSA_CORTA);
        clickOn("Registrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaNombreConCaracteresEspecialesMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Periodo@@@");
        sleep(PAUSA_CORTA);
        clickOn("#selectorFechaInicio");
        sleep(PAUSA_CORTA);
        write("2026-12-01");
        sleep(PAUSA_CORTA);
        clickOn("Registrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaFechaConFormatoIncorrectoMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Febrero-Agosto 2026");
        sleep(PAUSA_CORTA);
        clickOn("#selectorFechaInicio");
        sleep(PAUSA_CORTA);
        write("01-12-2026");
        sleep(PAUSA_CORTA);
        clickOn("Registrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalida"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaFechaAnteriorAHoyMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Febrero-Agosto 2026");
        sleep(PAUSA_CORTA);
        clickOn("#selectorFechaInicio");
        sleep(PAUSA_CORTA);
        write("2020-01-01");
        sleep(PAUSA_CORTA);
        clickOn("Registrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalida"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarSinConfirmarNoBorraCampos() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Febrero-Agosto 2026");
        sleep(PAUSA_CORTA);
        clickOn("Cancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        TextField campoNombre = lookup("#campoTextoNombre").query();
        assertFalse(campoNombre.getText().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarConfirmadoBorraCampos() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Febrero-Agosto 2026");
        sleep(PAUSA_CORTA);
        clickOn("#selectorFechaInicio");
        sleep(PAUSA_CORTA);
        write("2026-12-01");
        sleep(PAUSA_CORTA);
        clickOn("Cancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        TextField campoNombre = lookup("#campoTextoNombre").query();
        TextField campoFecha = lookup("#selectorFechaInicio").query();
        assertTrue(campoNombre.getText().isEmpty());
        assertTrue(campoFecha.getText().isEmpty());
        sleep(PAUSA_FINAL);
    }
}