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

public class SubirReportePrueba extends ApplicationTest {

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

    private void iniciarSesionYAbrirSubirReporte() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoIdentificador");
        sleep(PAUSA_CORTA);
        write(CORREO_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoContrasena");
        sleep(PAUSA_CORTA);
        write(CONTRASENA_PRACTICANTE);
        sleep(PAUSA_CORTA);
        clickOn("#botonIniciarSesion");
        sleep(PAUSA_LARGA);
        clickOn("Subir reporte");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaPanelErrorOcultoAlIniciar() {
        iniciarSesionYAbrirSubirReporte();
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaPanelExitoOcultoAlIniciar() {
        iniciarSesionYAbrirSubirReporte();
        VBox panelExito = lookup("#panelExito").query();
        assertFalse(panelExito.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaEtiquetaArchivoMuestraTextoInicial() {
        iniciarSesionYAbrirSubirReporte();
        Label etiquetaArchivo = lookup("#etiquetaArchivo").query();
        assertTrue(etiquetaArchivo.getText().equals("Ningún archivo seleccionado"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSubirSinTipoReporteMuestraError() {
        iniciarSesionYAbrirSubirReporte();
        clickOn("Subir reporte");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertTrue(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSubirSinTipoReporteMuestraMensajeCorrecto() {
        iniciarSesionYAbrirSubirReporte();
        clickOn("Subir reporte");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("requerido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSubirSinDescripcionMuestraError() {
        iniciarSesionYAbrirSubirReporte();
        clickOn("#comboBoxTipoReporte");
        sleep(PAUSA_LARGA);
        clickOn("Mensual");
        sleep(PAUSA_CORTA);
        clickOn("Subir reporte");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("requerido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSubirSinArchivoMuestraError() {
        iniciarSesionYAbrirSubirReporte();
        clickOn("#comboBoxTipoReporte");
        sleep(PAUSA_LARGA);
        clickOn("Mensual");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDescripcion");
        sleep(PAUSA_CORTA);
        write("Descripcion del reporte mensual");
        sleep(PAUSA_CORTA);
        clickOn("Subir reporte");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("requerido"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarLimpiaFormulario() {
        iniciarSesionYAbrirSubirReporte();
        clickOn("#comboBoxTipoReporte");
        sleep(PAUSA_LARGA);
        clickOn("Mensual");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDescripcion");
        sleep(PAUSA_CORTA);
        write("Descripcion del reporte");
        sleep(PAUSA_CORTA);
        clickOn("Cancelar");
        sleep(PAUSA_LARGA);
        Label etiquetaArchivo = lookup("#etiquetaArchivo").query();
        assertTrue(etiquetaArchivo.getText().equals("Ningún archivo seleccionado"));
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaCancelarOcultaPaneles() {
        iniciarSesionYAbrirSubirReporte();
        clickOn("Subir reporte");
        sleep(PAUSA_LARGA);
        clickOn("Cancelar");
        sleep(PAUSA_LARGA);
        VBox panelError = lookup("#panelError").query();
        assertFalse(panelError.isVisible());
        sleep(PAUSA_FINAL);
    }
}