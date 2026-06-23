package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsultarBuzonPrueba extends ApplicationTest {

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

    private void iniciarSesionYAbrirBuzon() {
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
        clickOn("Ver buzón");
        sleep(PAUSA_LARGA);
    }

    @Test
    public void pruebaBuzonCargaTablaRecibidos() {
        iniciarSesionYAbrirBuzon();
        TableView tablaRecibidos = lookup("#tablaRecibidos").query();
        assertTrue(tablaRecibidos.getItems().size() >= 0);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBuzonCargaTablaEnviados() {
        iniciarSesionYAbrirBuzon();
        clickOn("Enviados");
        sleep(PAUSA_LARGA);
        TableView tablaEnviados = lookup("#tablaEnviados").query();
        assertTrue(tablaEnviados.getItems().size() >= 0);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaAreaContenidoVaciaAlIniciar() {
        iniciarSesionYAbrirBuzon();
        TextArea areaContenido = lookup("#areaContenidoMensaje").query();
        assertTrue(areaContenido.getText().isEmpty());
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBuzonTieneMensajesRecibidos() {
        iniciarSesionYAbrirBuzon();
        TableView tablaRecibidos = lookup("#tablaRecibidos").query();
        assertTrue(tablaRecibidos.getItems().size() > 0);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaBuzonTieneMensajesEnviados() {
        iniciarSesionYAbrirBuzon();
        clickOn("Enviados");
        sleep(PAUSA_LARGA);
        TableView tablaEnviados = lookup("#tablaEnviados").query();
        assertTrue(tablaEnviados.getItems().size() > 0);
        sleep(PAUSA_FINAL);
    }

    @Test
    public void pruebaSeleccionarMensajeRecibidoMuestraContenido() {
        iniciarSesionYAbrirBuzon();
        TableView tablaRecibidos = lookup("#tablaRecibidos").query();
        if (tablaRecibidos.getItems().size() > 0) {
            clickOn(tablaRecibidos);
            sleep(PAUSA_LARGA);
            TextArea areaContenido = lookup("#areaContenidoMensaje").query();
            assertFalse(areaContenido.getText().isEmpty());
        }
        sleep(PAUSA_FINAL);
    }
}