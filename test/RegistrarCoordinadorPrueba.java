package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegistrarCoordinadorPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 500;
    private static final int PAUSA_LARGA = 1000;

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/RegistrarCoordinadorVista.fxml")
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
    public void pruebaTodosLosCamposVaciosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
    }

    @Test
    public void pruebaNombreVacioMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoApellidos");
        sleep(PAUSA_CORTA);
        write("Perez Lopez");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("juan@uv.mx");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoNumeroPersonal");
        sleep(PAUSA_CORTA);
        write("P001");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
    }

    @Test
    public void pruebaNombreConNumerosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombres");
        sleep(PAUSA_CORTA);
        write("Juan123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoApellidos");
        sleep(PAUSA_CORTA);
        write("Perez Lopez");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("juan@uv.mx");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoNumeroPersonal");
        sleep(PAUSA_CORTA);
        write("P001");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaApellidosConNumerosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombres");
        sleep(PAUSA_CORTA);
        write("Juan");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoApellidos");
        sleep(PAUSA_CORTA);
        write("Perez123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("juan@uv.mx");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoNumeroPersonal");
        sleep(PAUSA_CORTA);
        write("P001");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalidos"));
    }

    @Test
    public void pruebaCorreoSinArrobaMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombres");
        sleep(PAUSA_CORTA);
        write("Juan");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoApellidos");
        sleep(PAUSA_CORTA);
        write("Perez Lopez");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("juansinArroba.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoNumeroPersonal");
        sleep(PAUSA_CORTA);
        write("P001");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaNumeroPersonalConCaracteresEspecialesMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombres");
        sleep(PAUSA_CORTA);
        write("Juan");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoApellidos");
        sleep(PAUSA_CORTA);
        write("Perez Lopez");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("juan@uv.mx");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoNumeroPersonal");
        sleep(PAUSA_CORTA);
        write("P001@@");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaCancelarSinConfirmarNoBorraCampos() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombres");
        sleep(PAUSA_CORTA);
        write("Juan");
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelarRegistro");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        TextField campoTexto = lookup("#campoTextoNombres").query();
        assertFalse(campoTexto.getText().isEmpty());
    }

    @Test
    public void pruebaCancelarConfirmadoBorraCampos() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombres");
        sleep(PAUSA_CORTA);
        write("Juan");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoApellidos");
        sleep(PAUSA_CORTA);
        write("Perez Lopez");
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelarRegistro");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        TextField campoNombre = lookup("#campoTextoNombres").query();
        TextField campoApellidos = lookup("#campoTextoApellidos").query();
        assertTrue(campoNombre.getText().isEmpty());
        assertTrue(campoApellidos.getText().isEmpty());
    }
}