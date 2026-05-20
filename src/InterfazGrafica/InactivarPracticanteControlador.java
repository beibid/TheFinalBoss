package InterfazGrafica;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import java.util.List;


public class InactivarPracticanteControlador {

    @FXML private ComboBox<Practicante> comboBoxPracticantes;
    @FXML private VBox panelDatos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private Label etiquetaNombre;
    @FXML private Label etiquetaApellidos;
    @FXML private Label etiquetaNumeroPersonal;

    private PracticanteDao practicanteDao = new PracticanteDao();

    @FXML
    public void initialize() {
        cargarPracticantesActivos();
    }

    private void cargarPracticantesActivos() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            ObservableList<Practicante> listaPracticantes = FXCollections.observableArrayList(practicantes);
            comboBoxPracticantes.setItems(listaPracticantes);
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS COORDINADORES.");
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicante= comboBoxPracticantes.getSelectionModel().getSelectedItem();
        if (practicante != null) {
            etiquetaNombre.setText(practicante.getNombre());
            etiquetaApellidos.setText(practicante.getApellidos());
            etiquetaNumeroPersonal.setText(practicante.getMatricula());
            panelDatos.setVisible(true);
            panelDatos.setManaged(true);
        }
    }

    @FXML
    private void botonInactivar() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        if (practicante == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PRACTICANTE.");
            return;
        }
        if (!confirmarAccion("¿Seguro que desea inactivar a este practicante?")) {
            return;
        }
        ejecutarInactivacion(practicante);
    }

    private void ejecutarInactivacion(Practicante practicante) {
        try {
            int filasAfectadas = practicanteDao.inactivarPracticante(practicante.getMatricula());
            if (filasAfectadas > 0) {
                limpiarSeleccion();
                mostrarExito("Practicante inactivado", "EL PRACTICANTE FUE INACTIVADO EXITOSAMENTE.");
                cargarPracticantesActivos();
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR AL PRACTICANTE");
            }
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")){
            limpiarSeleccion();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        return alerta.showAndWait().filter(r -> r == botonSi).isPresent();
    }

    private void limpiarSeleccion() {
        comboBoxPracticantes.getSelectionModel().clearSelection();
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
        ocultarError();
        ocultarExito();
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
