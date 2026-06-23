package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
import logica.dominio.enums.EstadoDeCalificacion;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvaluacionReporteControlador {

    private static final Logger LOGGER = Logger.getLogger(EvaluacionReporteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final double CALIFICACION_MINIMA = 0;
    private static final double CALIFICACION_MAXIMA = 100;

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

    private final ReporteDao reporteDao = new ReporteDao();
    private final PracticanteDao practicanteDao = new PracticanteDao();

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
            if (practicantes.isEmpty()) {
                mostrarError("Sin practicantes", "NO HAY PRACTICANTES ACTIVOS EN EL SISTEMA.");
                comboBoxPracticante.setDisable(true);
            } else {
                ObservableList<Practicante> practicantesObservable = FXCollections.observableArrayList(practicantes);
                comboBoxPracticante.setItems(practicantesObservable);
                comboBoxPracticante.setCellFactory(listaPracticantes -> crearCeldaPracticante());
                comboBoxPracticante.setButtonCell(crearCeldaPracticante());
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar practicantes", excepcion.getMessage().toUpperCase());
            comboBoxPracticante.setDisable(true);
        }
    }

    private ListCell<Practicante> crearCeldaPracticante() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Practicante practicante, boolean vacio) {
                super.updateItem(practicante, vacio);
                if (vacio || practicante == null) {
                    setText("-- Selecciona un practicante --");
                } else {
                    setText(practicante.getNombre() + " " + practicante.getApellidos() + " - " + practicante.getMatricula());
                }
            }
        };
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
            if (reportes.isEmpty()) {
                mostrarError("Sin reportes", "ESTE PRACTICANTE AÚN NO TIENE REPORTES REGISTRADOS.");
                tablaReportes.getItems().clear();
            } else {
                tablaReportes.setItems(FXCollections.observableArrayList(reportes));
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar reportes", excepcion);
            mostrarError("Error al cargar reportes", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonEvaluar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (evaluacionValida()) {
            procesarEvaluacion();
        }
    }

    private boolean evaluacionValida() {
        Reporte reporte = tablaReportes.getSelectionModel().getSelectedItem();
        String calificacionTexto = campoCalificacion.getText().trim();
        String observaciones = campoObservaciones.getText().trim();
        boolean valido = reporte != null
                && !calificacionTexto.isEmpty()
                && !observaciones.isEmpty()
                && reporteNoevaluado(reporte)
                && esCalificacionValida(calificacionTexto);
        verificarCampos(reporte, calificacionTexto, observaciones);
        return valido;
    }

    private void verificarCampos(Reporte reporte, String calificacionTexto, String observaciones) {
        if (reporte == null) {
            mostrarError("Reporte no seleccionado", "SELECCIONA UN REPORTE DE LA TABLA.");
        } else if (reporte.getEstadoDeCalificacion() == EstadoDeCalificacion.Evaluado) {
            mostrarError("Reporte ya evaluado", "ESTE REPORTE YA FUE EVALUADO ANTERIORMENTE.");
        } else if (calificacionTexto.isEmpty() || observaciones.isEmpty()) {
            mostrarError("Campos vacíos", "INGRESA LA CALIFICACION Y LAS OBSERVACIONES.");
        } else if (!esCalificacionNumerica(calificacionTexto)) {
            mostrarError("Calificación inválida", "LA CALIFICACION DEBE SER UN NÚMERO.");
        } else if (!calificacionEnRango(calificacionTexto)) {
            mostrarError("Calificación inválida", "LA CALIFICACION DEBE ESTAR ENTRE 0 Y 100.");
        }
    }

    private boolean reporteNoevaluado(Reporte reporte) {
        return reporte.getEstadoDeCalificacion() != EstadoDeCalificacion.Evaluado;
    }

    private boolean esCalificacionValida(String calificacionTexto) {
        boolean valida = false;
        try {
            double calificacion = Double.parseDouble(calificacionTexto);
            valida = calificacion >= CALIFICACION_MINIMA && calificacion <= CALIFICACION_MAXIMA;
        } catch (NumberFormatException excepcion) {
            LOGGER.log(Level.WARNING, "Calificación con formato inválido", excepcion);
        }
        return valida;
    }

    private boolean esCalificacionNumerica(String calificacionTexto) {
        boolean numerica = true;
        try {
            Double.parseDouble(calificacionTexto);
        } catch (NumberFormatException excepcion) {
            LOGGER.log(Level.WARNING, "Calificación no numérica", excepcion);
            numerica = false;
        }
        return numerica;
    }

    private boolean calificacionEnRango(String calificacionTexto) {
        double calificacion = Double.parseDouble(calificacionTexto);
        return calificacion >= CALIFICACION_MINIMA && calificacion <= CALIFICACION_MAXIMA;
    }

    private void procesarEvaluacion() {
        Reporte reporteSeleccionado = tablaReportes.getSelectionModel().getSelectedItem();
        double calificacion = Double.parseDouble(campoCalificacion.getText().trim());
        String observaciones = campoObservaciones.getText().trim();
        String numPersonalProfesor = SesionUsuario.getInstance().getUsuarioActivo().getIdentificador();
        guardarEvaluacion(reporteSeleccionado.getIdReporte(), calificacion, observaciones, numPersonalProfesor);
    }

    private void guardarEvaluacion(int idReporte, double calificacion, String observaciones,
                                   String numPersonalProfesor) {
        try {
            int filasAfectadas = reporteDao.evaluarReporte(idReporte, calificacion, observaciones, numPersonalProfesor);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarFormulario();
                mostrarExito("Reporte evaluado", "EL REPORTE FUE EVALUADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al evaluar", "NO SE PUDO EVALUAR EL REPORTE.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar evaluación", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        limpiarFormulario();
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void limpiarFormulario() {
        campoCalificacion.clear();
        campoObservaciones.clear();
        comboBoxPracticante.setValue(null);
        tablaReportes.getItems().clear();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
    }

    private void mostrarPanel(VBox panel, Label etiquetaTitulo, Label etiquetaMensaje,
                              String titulo, String mensaje) {
        etiquetaTitulo.setText(titulo);
        etiquetaMensaje.setText(mensaje);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }

    private void mostrarError(String titulo, String mensaje) {
        ocultarPanel(panelExito);
        mostrarPanel(panelError, etiquetaTituloError, etiquetaMensajeError, titulo, mensaje);
    }

    private void mostrarExito(String titulo, String mensaje) {
        ocultarPanel(panelError);
        mostrarPanel(panelExito, etiquetaTituloExito, etiquetaMensajeExito, titulo, mensaje);
    }
}