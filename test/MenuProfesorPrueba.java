package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuProfesorPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String CORREO_PROFESOR = "ricardo.fuentes@uv.mx";
    private static final String CONTRASENA_PROFESOR = "ricardoP001";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    private void iniciarSesionComoProfesor() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_PROFESOR);
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_PROFESOR);
        sleep(PAUSA_CORTA);
        clickOn("Iniciar sesión");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaLoginExitosoMuestraBienvenida() {
        iniciarSesionComoProfesor();
        Label etiquetaBienvenida = lookup("#lblBienvenida").query();
        assertTrue(etiquetaBienvenida.getText().contains("Bienvenido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraNombreProfesor() {
        iniciarSesionComoProfesor();
        Label etiquetaNombre = lookup("#lblNombre").query();
        assertTrue(etiquetaNombre.getText().equals("Ricardo"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraRolProfesor() {
        iniciarSesionComoProfesor();
        Label etiquetaRol = lookup("#lblRol").query();
        assertTrue(etiquetaRol.getText().contains("PROFESOR"));
        sleep(PAUSA_FINAL);
    }
    @Test
    public void pruebaLoginCamposVaciosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaLoginCredencialesInvalidasMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write("correo@invalido.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write("contrasenaErronea");
        sleep(PAUSA_CORTA);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").query().isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonCalificarReporteAbreVentana() {
        iniciarSesionComoProfesor();
        clickOn("Calificar reporte");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonVerHistorialAbreVentana() {
        iniciarSesionComoProfesor();
        clickOn("Ver historial");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonValidarDocumentacionAbreVentana() {
        iniciarSesionComoProfesor();
        clickOn("Validar documentacion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonEnviarMensajeAbreVentana() {
        iniciarSesionComoProfesor();
        clickOn("Enviar mensaje");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonVerBuzonAbreVentana() {
        iniciarSesionComoProfesor();
        clickOn("Ver buzón");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }
}