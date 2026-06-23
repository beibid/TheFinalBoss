package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuCoordinadorPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String CORREO_COORDINADOR = "laura.gutierrez@uv.mx";
    private static final String CONTRASENA_COORDINADOR = "lauraC001";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml")
        );
        Scene escena = new Scene(cargador.load());
        escenario.setScene(escena);
        escenario.show();
    }

    private void iniciarSesionComoCoordinador() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_COORDINADOR);
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_COORDINADOR);
        sleep(PAUSA_CORTA);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaLoginExitosoMuestraBienvenida() {
        iniciarSesionComoCoordinador();
        Label etiquetaBienvenida = lookup("#lblBienvenida").query();
        assertTrue(etiquetaBienvenida.getText().contains("Bienvenido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraNombreCoordinador() {
        iniciarSesionComoCoordinador();
        Label etiquetaNombre = lookup("#lblNombre").query();
        assertTrue(etiquetaNombre.getText().equals("Laura"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaMenuMuestraRolCoordinador() {
        iniciarSesionComoCoordinador();
        Label etiquetaRol = lookup("#lblRol").query();
        assertTrue(etiquetaRol.getText().contains("COORDINADOR"));
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
    public void pruebaBotonRegistrarPracticanteAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonRegistrarPracticante");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#campoTextoNombres").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonInactivarPracticanteAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonInactivarPracticante");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxPracticantes").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonModificarPracticanteAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonModificarPracticante");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxPracticantes").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonRegistrarProfesorAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonRegistrarProfesor");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#campoTextoNombres").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonInactivarProfesorAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonInactivarProfesor");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxProfesores").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonModificarProfesorAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonModificarProfesor");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxProfesores").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonRegistrarProyectoAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonRegistrarProyecto");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#campoTextoNombreProyecto").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonInactivarProyectoAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonInactivarProyecto");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxProyectos").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonModificarProyectoAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonModificarProyecto");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxProyectos").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonRegistrarOrganizacionAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonRegistrarOrganizacion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#campoTextoNombre").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonInactivarOrganizacionAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonInactivarOrganizacion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxOrganizacion").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonModificarOrganizacionAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonModificarOrganizacion");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#comboBoxOrganizacion").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBotonRegistrarPeriodoAbreVentana() {
        iniciarSesionComoCoordinador();
        clickOn("#botonRegistrarPeriodo");
        sleep(PAUSA_LARGA);
        assertTrue(lookup("#campoTextoNombre").tryQuery().isPresent());
        sleep(PAUSA_FINAL);
    }

}