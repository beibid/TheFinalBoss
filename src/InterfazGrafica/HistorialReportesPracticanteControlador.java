package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Reporte;
import logica.dominio.SesionUsuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistorialReportesPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(HistorialReportesPracticanteControlador.class.getName());

    private final ReporteDao reporteDao = new ReporteDao();

    @FXML private TableView<Reporte> tablaReportes;
    @FXML private TableColumn<Reporte, String> columnaTipo;
    @FXML private TableColumn<Reporte, String> columnaDescripcion;
    @FXML private TableColumn<Reporte, String> columnaFecha;
    @FXML private TableColumn<Reporte, Double> columnaCalificacion;
    @FXML private TableColumn<Reporte, String> columnaObservaciones;
    @FXML private TableColumn<Reporte, String> columnaEstado;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarReportes();
    }

    private void configurarColumnas() {
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoReporte"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaGeneracion"));
        columnaCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        columnaObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estadoDeCalificacion"));
    }

    private void cargarReportes() {
        String matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        try {
            List<Reporte> reportes = reporteDao.obtenerReportesPorMatricula(matricula);
            tablaReportes.setItems(FXCollections.observableArrayList(reportes));
            if (reportes.isEmpty()) {
                mostrarError("Sin reportes", "AUN NO HAS SUBIDO NINGUN REPORTE.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar reportes del practicante", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS REPORTES.");
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void mostrarPanel(VBox panelMostrar) {
        panelMostrar.setVisible(true);
        panelMostrar.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        mostrarPanel(panelError);
    }
}