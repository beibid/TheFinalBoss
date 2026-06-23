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

public class EnviarMensajePrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String CORREO_PROFESOR = "ricardo.fuentes@uv.mx";
    private static final String CONTRASENA_PROFESOR = "ricardoP001";
    private static final String NOMBRE_DESTINATARIO = "[Practicante] Diego Hernandez Lopez";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    private void iniciarSesionYAbrirEnviarMensaje() {
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
        clickOn("Enviar mensaje");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaPanelErrorOcultoAlIniciar() {
        iniciarSesionYAbrirEnviarMensaje();
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaPanelExitoOcultoAlIniciar() {
        iniciarSesionYAbrirEnviarMensaje();
        VBox panelExito = lookup("#panelExito").query();
        assertFalse(panelExito.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaEnviarSinDestinatarioMuestraError() {
        iniciarSesionYAbrirEnviarMensaje();
        clickOn("Enviar");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("destinatario"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaEnviarSinMensajeMuestraError() {
        iniciarSesionYAbrirEnviarMensaje();
        clickOn("#comboBoxDestinatario");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_DESTINATARIO);
        sleep(PAUSA_CORTA);
        clickOn("Enviar");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("vacío"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaEnviarMensajeMuyLargoMuestraError() {
        iniciarSesionYAbrirEnviarMensaje();
        clickOn("#comboBoxDestinatario");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_DESTINATARIO);
        sleep(PAUSA_CORTA);
        clickOn("#areaTextoMensaje");
        sleep(PAUSA_CORTA);
        String mensajeLargo = "a".repeat(501);
        write(mensajeLargo);
        sleep(PAUSA_CORTA);
        clickOn("Enviar");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("largo"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarSinConfirmarNoBorraContenido() {
        iniciarSesionYAbrirEnviarMensaje();
        clickOn("#areaTextoMensaje");
        sleep(PAUSA_CORTA);
        write("Hola practicante");
        sleep(PAUSA_CORTA);
        clickOn("Cancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        javafx.scene.control.TextArea areaTexto = lookup("#areaTextoMensaje").query();
        assertFalse(areaTexto.getText().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarConfirmadoBorraContenido() {
        iniciarSesionYAbrirEnviarMensaje();
        clickOn("#areaTextoMensaje");
        sleep(PAUSA_CORTA);
        write("Hola practicante");
        sleep(PAUSA_CORTA);
        clickOn("Cancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        javafx.scene.control.TextArea areaTexto = lookup("#areaTextoMensaje").query();
        assertTrue(areaTexto.getText().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaEnviarMensajeExitosoMuestraExito() {
        iniciarSesionYAbrirEnviarMensaje();
        clickOn("#comboBoxDestinatario");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_DESTINATARIO);
        sleep(PAUSA_CORTA);
        clickOn("#areaTextoMensaje");
        sleep(PAUSA_CORTA);
        write("Mensaje de prueba automatizado");
        sleep(PAUSA_CORTA);
        clickOn("Enviar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        VBox panelExito = lookup("#panelExito").query();
        assertTrue(panelExito.isVisible());
        sleep(PAUSA_FINAL);
    }
}