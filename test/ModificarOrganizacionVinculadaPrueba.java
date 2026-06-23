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

public class ModificarOrganizacionVinculadaPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String NOMBRE_ORGANIZACION = "Softtek";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/ModificarOrganizacionVinculadaVista.fxml")
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
    public void pruebaSeleccionarOrganizacionMuestraFormulario() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
        sleep(PAUSA_LARGA);
        VBox panelFormulario = lookup("#panelFormulario").query();
        assertTrue(panelFormulario.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaFormularioSeRellenaConDatosDeLaBD() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
        sleep(PAUSA_LARGA);
        TextField campoNombre = lookup("#campoNombre").query();
        assertFalse(campoNombre.getText().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCampoNombreVacioMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
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
    public void pruebaTelefonoInvalidoMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
        sleep(PAUSA_LARGA);
        clickOn("#campoTelefono");
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.DELETE);
        sleep(PAUSA_CORTA);
        write("123");
        sleep(PAUSA_CORTA);
        clickOn("#botonGuardar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCorreoInvalidoMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
        sleep(PAUSA_LARGA);
        clickOn("#campoCorreo");
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.DELETE);
        sleep(PAUSA_CORTA);
        write("correoSinArroba.com");
        sleep(PAUSA_CORTA);
        clickOn("#botonGuardar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSectorConNumerosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
        sleep(PAUSA_LARGA);
        clickOn("#campoSector");
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        sleep(PAUSA_CORTA);
        type(javafx.scene.input.KeyCode.DELETE);
        sleep(PAUSA_CORTA);
        write("Sector123");
        sleep(PAUSA_CORTA);
        clickOn("#botonGuardar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarConfirmadoOcultaFormulario() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
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
        clickOn("#comboBoxOrganizacion");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_ORGANIZACION);
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