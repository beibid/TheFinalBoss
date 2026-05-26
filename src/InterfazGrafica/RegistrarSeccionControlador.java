package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class RegistrarSeccionControlador {

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private TextField campoTextoPeriodo;
    @FXML private TextField campoTextoNumeroSeccion;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        return alerta.showAndWait().filter(botonPresionado -> botonPresionado == botonSi).isPresent();
    }

    private void procesarRegistro() {
        if (!camposValidos()) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacios", "Verifica la informacion e intente de nuevo.");
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
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCamposRegistros();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Seccion registrada", "SECCION REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "NO SE PUDO REGISTRAR LA SECCION. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCamposRegistros() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNumeroSeccion.clear();
        campoTextoPeriodo.clear();
    }

    private void mostrarPanel(Label etiquetaTitulo, Label etiquetaMensaje, VBox panel, String titulo, String mensaje) {
        etiquetaTitulo.setText(titulo);
        etiquetaMensaje.setText(mensaje);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }
}