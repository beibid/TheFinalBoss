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

public class RegistrarOrganizacionVinculadaPrueba extends ApplicationTest {

    private static final int PAUSA_CORTA = 500;
    private static final int PAUSA_LARGA = 1000;

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/InterfazGrafica/vistas/RegistrarOrganizacionVinculadaVista.fxml")
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
    public void pruebaNombreConCaracteresEspecialesMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa@#$");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccion");
        sleep(PAUSA_CORTA);
        write("Av. Principal 123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoTelefono");
        sleep(PAUSA_CORTA);
        write("2281234567");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("contacto@empresa.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSector");
        sleep(PAUSA_CORTA);
        write("Tecnologia");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaDireccionConCaracteresNoPermitidosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa XYZ");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccion");
        sleep(PAUSA_CORTA);
        write("Direccion@@@invalida");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoTelefono");
        sleep(PAUSA_CORTA);
        write("2281234567");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("contacto@empresa.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSector");
        sleep(PAUSA_CORTA);
        write("Tecnologia");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalida"));
    }

    @Test
    public void pruebaTelefonoConMenosDe10DigMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa XYZ");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccion");
        sleep(PAUSA_CORTA);
        write("Av. Principal 123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoTelefono");
        sleep(PAUSA_CORTA);
        write("123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("contacto@empresa.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSector");
        sleep(PAUSA_CORTA);
        write("Tecnologia");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaTelefonoConLetrasMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa XYZ");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccion");
        sleep(PAUSA_CORTA);
        write("Av. Principal 123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoTelefono");
        sleep(PAUSA_CORTA);
        write("228ABC4567");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("contacto@empresa.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSector");
        sleep(PAUSA_CORTA);
        write("Tecnologia");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaCorreoSinArrobaMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa XYZ");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccion");
        sleep(PAUSA_CORTA);
        write("Av. Principal 123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoTelefono");
        sleep(PAUSA_CORTA);
        write("2281234567");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("correoSinArroba.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSector");
        sleep(PAUSA_CORTA);
        write("Tecnologia");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaSectorConNumerosMuestraError() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa XYZ");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoDireccion");
        sleep(PAUSA_CORTA);
        write("Av. Principal 123");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoTelefono");
        sleep(PAUSA_CORTA);
        write("2281234567");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoCorreo");
        sleep(PAUSA_CORTA);
        write("contacto@empresa.com");
        sleep(PAUSA_CORTA);
        clickOn("#campoTextoSector");
        sleep(PAUSA_CORTA);
        write("Tecnologia123");
        sleep(PAUSA_CORTA);
        clickOn("#botonRegistrar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        Label etiquetaTitulo = lookup("#etiquetaTituloError").query();
        assertTrue(etiquetaTitulo.getText().contains("invalido"));
    }

    @Test
    public void pruebaCancelarConfirmadoBorraCampos() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa XYZ");
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("Sí");
        sleep(PAUSA_LARGA);
        TextField campoTexto = lookup("#campoTextoNombre").query();
        assertTrue(campoTexto.getText().isEmpty());
    }

    @Test
    public void pruebaCancelarSinConfirmarNoBorraCampos() {
        sleep(PAUSA_LARGA);
        clickOn("#campoTextoNombre");
        sleep(PAUSA_CORTA);
        write("Empresa XYZ");
        sleep(PAUSA_CORTA);
        clickOn("#botonCancelar");
        sleep(PAUSA_CORTA);
        clickOn("No");
        sleep(PAUSA_LARGA);
        TextField campoTexto = lookup("#campoTextoNombre").query();
        assertFalse(campoTexto.getText().isEmpty());
    }
}