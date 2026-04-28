package InterfazGrafica;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.SeccionDao;
import logica.dominio.Seccion;

import java.net.URL;
import java.util.ResourceBundle;


public class SeccionControlGUI implements Initializable{

    @FXML private TextField campoTextoPeriodo;
    @FXML private TextField campoTextoNumeroSeccion;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar esta seccion?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCamposRegistros();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);
        return alerta.showAndWait().filter(r -> r == btnSi).isPresent();
    }

    private void procesarRegistro() {
        if (!camposValidos()) {
            mostrarError("Campos obligatorios vacios",
                    "Verifica la informacion e intente de nuevo.");
            return;
        }
        guardarSeccion(construirSeccion());
    }

    private boolean camposValidos() {
        String numeroSeccion = campoTextoNumeroSeccion.getText().trim();
        String periodo = campoTextoPeriodo.getText().trim();
        return !numeroSeccion.isEmpty() && !periodo.isEmpty();
    }

    private Seccion construirSeccion() {
        String numeroSeccion = campoTextoNumeroSeccion.getText().trim();
        String periodo = campoTextoPeriodo.getText().trim();

        Seccion seccion = new Seccion();
        seccion.setNoSeccion(limitarTexto(numeroSeccion, 55));
        seccion.setPeriodo(limitarTexto(periodo, 55));
        return seccion;
    }

    private void guardarSeccion(Seccion seccion) {
        SeccionDao seccionDao = new SeccionDao();
        try {
            int filasAfectadas = seccionDao.agregarSeccion(seccion);
            if (filasAfectadas > 0) {
                limpiarCamposRegistros();
                mostrarExito("Seccion registrada",
                        "SECCION REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar",
                        "NO SE PUDO REGISTRAR LA SECCION. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCamposRegistros() {
        ocultarError();
        ocultarExito();
        campoTextoNumeroSeccion.clear();
        campoTextoPeriodo.clear();
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}
