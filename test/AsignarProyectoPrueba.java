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

public class AsignarProyectoPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 800;
    private static final int PAUSA_LARGA = 1500;
    private static final int PAUSA_FINAL = 2000;
    private static final String NOMBRE_PRACTICANTE = "Diego Hernandez Lopez - S21013001";
    private static final String NOMBRE_PROYECTO = "1 Sistema de nomina";

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/AsignarProyectoVista.fxml")
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
    public void pruebaPanelPreferenciasOcultoAlIniciar() {
        sleep(PAUSA_LARGA);
        VBox panelPreferencias = lookup("#panelPreferencias").query();
        assertFalse(panelPreferencias.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaAsignarSinSeleccionarPracticanteMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#botonAsignar");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarPracticanteMuestraPreferencias() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxPracticantes");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_LARGA);
        VBox panelPreferencias = lookup("#panelPreferencias").query();
        assertTrue(panelPreferencias.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaAsignarSinSeleccionarProyectoMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxPracticantes");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("#botonAsignar");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("selección"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarPracticanteYProyectoNoMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxPracticantes");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("#comboBoxProyectos");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PROYECTO);
        sleep(PAUSA_CORTA);
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarConfirmadoLimpiaSeleccion() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxPracticantes");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        javafx.scene.control.ComboBox comboPracticantes =
                lookup("#comboBoxPracticantes").query();
        assertTrue(comboPracticantes.getSelectionModel().getSelectedItem() == null);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarSinConfirmarMantienSeleccion() {
        sleep(PAUSA_LARGA);
        clickOn("#comboBoxPracticantes");
        sleep(PAUSA_LARGA);
        clickOn(NOMBRE_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        javafx.scene.control.ComboBox comboPracticantes =
                lookup("#comboBoxPracticantes").query();
        assertTrue(comboPracticantes.getSelectionModel().getSelectedItem() != null);
        sleep(PAUSA_FINAL);
    }
}