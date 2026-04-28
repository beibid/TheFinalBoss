package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class EvaluacionReporteControlGUI implements Initializable {

    @FXML private ComboBox<?> comboBoxPracticante;
    @FXML private TableView<?> tablaReportes;
    @FXML private TableColumn<?, ?> columnaTipo;
    @FXML private TableColumn<?, ?> columnaDescripcion;
    @FXML private TableColumn<?, ?> columnaFecha;
    @FXML private TableColumn<?, ?> columnaEstado;
    @FXML private TextField campoCalificacion;
    @FXML private TextField campoObservaciones;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void seleccionarPracticante() {}

    @FXML
    private void botonEvaluar() {}

    @FXML
    private void botonCancelar() {
        campoCalificacion.clear();
        campoObservaciones.clear();
        comboBoxPracticante.getSelectionModel().clearSelection();
        ocultarError();
        ocultarExito();
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}