package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InactivarCoordinadorControlGUI implements Initializable{

    @FXML private ComboBox<Coordinador> comboBoxCoordinadores;
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

    private CoordinadorDao coordinadorDao = new CoordinadorDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCoordinadoresActivos();
    }

    private void cargarCoordinadoresActivos() {
        try {
            List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
            ObservableList<Coordinador> lista = FXCollections.observableArrayList(coordinadores);
            comboBoxCoordinadores.setItems(lista);
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS COORDINADORES.");
        }
    }

    @FXML
    private void seleccionarCoordinador() {
        Coordinador coordinador = comboBoxCoordinadores.getSelectionModel().getSelectedItem();
        if (coordinador != null) {
            etiquetaNombre.setText(coordinador.getNombre());
            etiquetaApellidos.setText(coordinador.getApellidos());
            etiquetaNumeroPersonal.setText(coordinador.getNumeroDePersonalCoordinador());
            panelDatos.setVisible(true);
            panelDatos.setManaged(true);
        }
    }

    @FXML
    private void botonInactivar() {
        Coordinador coordinador = comboBoxCoordinadores.getSelectionModel().getSelectedItem();
        if (coordinador == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN COORDINADOR.");
            return;
        }
        if (!confirmarAccion("¿Seguro que desea inactivar a este coordinador?")) {
            return;
        }
        ejecutarInactivacion(coordinador);
    }

    private void ejecutarInactivacion(Coordinador coordinador) {
        try {
            int filasAfectadas = coordinadorDao.inactivarCoordinador(coordinador.getNumeroDePersonalCoordinador());
            if (filasAfectadas > 0) {
                limpiarSeleccion();
                mostrarExito("Coordinador inactivado", "EL COORDINADOR FUE INACTIVADO EXITOSAMENTE.");
                cargarCoordinadoresActivos();
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR AL COORDINADOR.");
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

    private void limpiarSeleccion() {
        comboBoxCoordinadores.getSelectionModel().clearSelection();
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