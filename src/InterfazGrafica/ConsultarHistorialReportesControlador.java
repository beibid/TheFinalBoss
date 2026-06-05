package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Practicante;
import logica.dominio.Reporte;
import logica.dominio.SesionUsuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultarHistorialReportesControlador {

    private static final Logger LOGGER = Logger.getLogger(ConsultarHistorialReportesControlador.class.getName());

    @FXML private ComboBox<Practicante> comboBoxPracticante;
    @FXML private TableView<Reporte> tablaReportes;
    @FXML private TableColumn<Reporte, String> columnaTipo;
    @FXML private TableColumn<Reporte, String> columnaDescripcion;
    @FXML private TableColumn<Reporte, String> columnaFecha;
    @FXML private TableColumn<Reporte, Double> columnaCalificacion;
    @FXML private TableColumn<Reporte, String> columnaObservaciones;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;

    @FXML
    public void initialize() {
        tablaReportes.setTableMenuButtonVisible(false);
        tablaReportes.getColumns().forEach(columna -> columna.setReorderable(false));
    }

    private void configurarColumnas() {
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoReporte"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaGeneracion"));
        columnaCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        columnaObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
    }

    private void cargarPracticantes() {
        PracticanteDao practicanteDao = new PracticanteDao();
        int idProfesor = SesionUsuario.getInstance().getIdUsuario();
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesPorProfesor(idProfesor);
            comboBoxPracticante.setItems(FXCollections.observableArrayList(practicantes));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES");
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicanteSeleccionado = comboBoxPracticante.getValue();
        if (practicanteSeleccionado != null) {
            ocultarError();
            cargarReportes(practicanteSeleccionado.getMatricula());
        }
    }

    private void cargarReportes(String matricula) {
        ReporteDao reporteDao = new ReporteDao();
        try {
            List<Reporte> reportes = reporteDao.obtenerReportesPorMatricula(matricula);
            tablaReportes.setItems(FXCollections.observableArrayList(reportes));
            if (reportes.isEmpty()) {
                mostrarError("Sin reportes", "EL PRACTICANTE AUN NO HA SUBIDO REPORTES");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar reportes", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS REPORTES");
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.close();
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
}