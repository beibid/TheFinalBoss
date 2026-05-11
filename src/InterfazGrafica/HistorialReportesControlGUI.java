package InterfazGrafica;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class HistorialReportesControlGUI implements Initializable {

    @FXML private TableView<?> tablaReportes;
    @FXML private TableColumn<?, ?> columnaTipo;
    @FXML private TableColumn<?, ?> columnaDescripcion;
    @FXML private TableColumn<?, ?> columnaFecha;
    @FXML private TableColumn<?, ?> columnaCalificacion;
    @FXML private TableColumn<?, ?> columnaObservaciones;
    @FXML private TableColumn<?, ?> columnaEstado;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}