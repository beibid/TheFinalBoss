package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class EvaluacionReporteControlGUI {

    @FXML private ComboBox<Practicante> comboBoxPracticante;
    @FXML private TableView<Reporte> tablaReportes;
    @FXML private TableColumn<Reporte, String> columnaTipo;
    @FXML private TableColumn<Reporte, String> columnaDescripcion;
    @FXML private TableColumn<Reporte, String> columnaFecha;
    @FXML private TableColumn<Reporte, String> columnaEstado;
    @FXML private TextField campoCalificacion;
    @FXML private TextField campoObservaciones;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private ReporteDao reporteDao = new ReporteDao();
    private PracticanteDao practicanteDao = new PracticanteDao();

    @FXML
    public void initialize() {
        configurarTabla();
        cargarPracticantes();
    }

    private void configurarTabla() {
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoReporte"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaGeneracion"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estadoDeCalificacion"));
    }

    private void cargarPracticantes() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            ObservableList<Practicante> lista = FXCollections.observableArrayList(practicantes);
            comboBoxPracticante.setItems(lista);
        } catch (UsuariosExcepcion excepcion) {
            mostrarError("Error al cargar practicantes", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicanteSeleccionado = comboBoxPracticante.getValue();
        if (practicanteSeleccionado != null) {
            cargarReportes(practicanteSeleccionado.getMatricula());
        }
    }

    private void cargarReportes(String matricula) {
        try {
            List<Reporte> reportes = reporteDao.obtenerReportesPorPracticante(matricula);
            tablaReportes.setItems(FXCollections.observableArrayList(reportes));
        } catch (MensajeriaExcepcion excepcion) {
            mostrarError("Error al cargar reportes", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonEvaluar() {
        ocultarError();
        ocultarExito();
        if (!evaluacionValida()) {
            return;
        }
        procesarEvaluacion();
    }

    private boolean evaluacionValida() {
        if (tablaReportes.getSelectionModel().getSelectedItem() == null) {
            mostrarError("Reporte no seleccionado", "SELECCIONA UN REPORTE DE LA TABLA.");
            return false;
        }
        if (campoCalificacion.getText().trim().isEmpty() || campoObservaciones.getText().trim().isEmpty()) {
            mostrarError("Campos vacios", "INGRESA LA CALIFICACION Y LAS OBSERVACIONES.");
            return false;
        }
        if (!esCalificacionValida(campoCalificacion.getText().trim())) {
            return false;
        }
        return true;
    }

    private boolean esCalificacionValida(String calificacionTexto) {
        try {
            double calificacion = Double.parseDouble(calificacionTexto);
            if (calificacion < 0 || calificacion > 100) {
                mostrarError("Calificacion invalida", "LA CALIFICACION DEBE ESTAR ENTRE 0 Y 100.");
                return false;
            }
            return true;
        } catch (NumberFormatException excepcion) {
            mostrarError("Calificacion invalida", "LA CALIFICACION DEBE SER UN NUMERO.");
            return false;
        }
    }

    private void procesarEvaluacion() {
        Reporte reporteSeleccionado = tablaReportes.getSelectionModel().getSelectedItem();
        double calificacion = Double.parseDouble(campoCalificacion.getText().trim());
        String observaciones = campoObservaciones.getText().trim();
        String numPersonalProfesor = SesionUsuario.getInstance().getUsuarioActivo().getIdentificador();

        guardarEvaluacion(reporteSeleccionado.getIdReporte(), calificacion, observaciones, numPersonalProfesor);
    }

    private void guardarEvaluacion(int idReporte, double calificacion, String observaciones, String numPersonalProfesor) {
        try {
            int filasAfectadas = reporteDao.evaluarReporte(idReporte, calificacion, observaciones, numPersonalProfesor);
            if (filasAfectadas > 0) {
                limpiarFormulario();
                mostrarExito("Reporte evaluado", "EL REPORTE FUE EVALUADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al evaluar", "NO SE PUDO EVALUAR EL REPORTE.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        limpiarFormulario();
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        campoCalificacion.clear();
        campoObservaciones.clear();
        comboBoxPracticante.getSelectionModel().clearSelection();
        tablaReportes.getItems().clear();
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