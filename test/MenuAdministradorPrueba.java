package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuAdministradorPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String CORREO_ADMINISTRADOR = "carlos.mendoza@uv.mx";
    private static final String CONTRASENA_ADMINISTRADOR = "carlosA001";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    private void iniciarSesionComoAdministrador() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_ADMINISTRADOR);
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_ADMINISTRADOR);
        sleep(PAUSA_CORTA);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaLoginExitosoMuestraBienvenida() {
        iniciarSesionComoAdministrador();
        Label etiquetaBienvenida = lookup("#lblBienvenida").query();
        assertTrue(etiquetaBienvenida.getText().contains("Bienvenido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraNombreAdministrador() {
        iniciarSesionComoAdministrador();
        Label etiquetaNombre = lookup("#lblNombre").query();
        assertTrue(etiquetaNombre.getText().equals("Carlos"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraRolAdministrador() {
        iniciarSesionComoAdministrador();
        Label etiquetaRol = lookup("#lblRol").query();
        assertTrue(etiquetaRol.getText().contains("ADMINISTRADOR"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonRegistrarCoordinadorAbreVentana() {
        iniciarSesionComoAdministrador();
        clickOn("Registrar");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#campoTextoNombres").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonInactivarCoordinadorAbreVentana() {
        iniciarSesionComoAdministrador();
        clickOn("Inactivar");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxCoordinadores").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonModificarCoordinadorAbreVentana() {
        iniciarSesionComoAdministrador();
        clickOn("Modificar");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxCoordinadores").tryQuery().isPresent());
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
    public void pruebaLoginCamposVaciosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").query().isVisible());
        sleep(PAUSA_FINAL);
    }
}