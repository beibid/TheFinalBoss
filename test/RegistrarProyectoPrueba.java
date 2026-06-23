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

public class RegistrarProyectoPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 500;
    private static final int PAUSA_LARGA = 1000;

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/RegistrarProyectoVista.fxml")
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
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
    }

    @Test
    public void pruebaTodosLosCamposVaciosMuestraMensajeCorrecto() {
        sleep(PAUSA_LARGA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("vacíos"));
    }

    @Test
    public void pruebaSoloNombreProyectoVacioMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoDescripcion");
        sleep(PAUSA_CORTA);
        write("Sistema para gestionar inventario");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoResponsable");
        sleep(PAUSA_CORTA);
        write("Juan Perez");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoNombreEmpresa");
        sleep(PAUSA_CORTA);
        write("Tech Solutions");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSectorEmpresa");
        sleep(PAUSA_CORTA);
        write("Tecnologia");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccionEmpresa");
        sleep(PAUSA_CORTA);
        write("Av. Principal 123");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
    }

    @Test
    public void pruebaCamposLlenosSinCombosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombreProyecto");
        sleep(PAUSA_CORTA);
        write("Sistema de inventario");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDescripcion");
        sleep(PAUSA_CORTA);
        write("Sistema para gestionar inventario");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoResponsable");
        sleep(PAUSA_CORTA);
        write("Juan Perez");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoNombreEmpresa");
        sleep(PAUSA_CORTA);
        write("Tech Solutions");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSectorEmpresa");
        sleep(PAUSA_CORTA);
        write("Tecnologia");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccionEmpresa");
        sleep(PAUSA_CORTA);
        write("Av. Principal 123");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("incompleta"));
    }

    @Test
    public void pruebaCancelarBorraTodosLosCampos() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombreProyecto");
        sleep(PAUSA_CORTA);
        write("Sistema de inventario");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDescripcion");
        sleep(PAUSA_CORTA);
        write("Sistema para gestionar inventario");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoResponsable");
        sleep(PAUSA_CORTA);
        write("Juan Perez");
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelar");
        sleep(PAUSA_LARGA);
        TextField campoNombre = lookup("#campoTextoNombreProyecto").query();
        TextField campoDescripcion = lookup("#campoTextoDescripcion").query();
        TextField campoResponsable = lookup("#campoTextoResponsable").query();
        assertTrue(campoNombre.getText().isEmpty());
        assertTrue(campoDescripcion.getText().isEmpty());
        assertTrue(campoResponsable.getText().isEmpty());
    }

    @Test
    public void pruebaCancelarOcultaPanelError() {
        sleep(PAUSA_LARGA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_LARGA);
        clickOn("#botonCancelar");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
    }
}