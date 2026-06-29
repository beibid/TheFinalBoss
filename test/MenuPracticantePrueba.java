
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuPracticantePrueba extends ApplicationTest {

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

    private void iniciarSesionComoPracticante() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("Iniciar sesión");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaLoginExitosoMuestraBienvenida() {
        iniciarSesionComoPracticante();
        Label etiquetaBienvenida = lookup("#lblBienvenida").query();
        assertTrue(etiquetaBienvenida.getText().contains("Bienvenido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraNombrePracticante() {
        iniciarSesionComoPracticante();
        Label etiquetaNombre = lookup("#lblNombre").query();
        assertTrue(etiquetaNombre.getText().equals("Diego"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraRolPracticante() {
        iniciarSesionComoPracticante();
        Label etiquetaRol = lookup("#lblRol").query();
        assertTrue(etiquetaRol.getText().contains("PRACTICANTE"));
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
    public void pruebaBotonGenerarReporteMensualAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Generar reporte Mensual");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonGenerarReporteParcialAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Generar reporte Parcial");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonSubirReporteAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Subir reporte");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonMisReportesAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Mis reportes");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonSubirDocumentacionAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Subir documentación");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonSeleccionarProyectosAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Seleccionar proyectos");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxPrimerPrioridad").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonEnviarMensajeAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Enviar mensaje");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonVerBuzonAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Ver buzón");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonRealizarAutoevaluacionAbreVentana() {
        iniciarSesionComoPracticante();
        clickOn("Realizar Autoevaluacion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#panelError").tryQuery().isPresent()
                || lookup("#panelExito").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }
}