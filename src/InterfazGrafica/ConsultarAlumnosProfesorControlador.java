package InterfazGrafica;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import logica.dao.objetos.ActividadDao;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ProyectoDao;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Actividad;
import logica.dominio.Practicante;
import logica.dominio.Proyecto;
import logica.dominio.SesionUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultarAlumnosProfesorControlador {

    private static final Logger LOGGER = Logger.getLogger(ConsultarAlumnosProfesorControlador.class.getName());
    private static final String FILTRO_TODOS = "Todos";
    private static final String FILTRO_CON_REPORTES = "Con reportes";
    private static final String FILTRO_SIN_REPORTES = "Sin reportes";
    private static final String FILTRO_CON_ARCHIVOS = "Con archivos";
    private static final String FILTRO_SIN_ARCHIVOS = "Sin archivos";

    @FXML private TableView<Practicante> tablaAlumnos;
    @FXML private TableColumn<Practicante, String> columnaMatricula;
    @FXML private TableColumn<Practicante, String> columnaNombre;
    @FXML private TableColumn<Practicante, String> columnaApellidos;
    @FXML private TableColumn<Practicante, String> columnaCorreo;
    @FXML private ComboBox<String> comboFiltro;
    @FXML private Label etiquetaProyecto;
    @FXML private Label etiquetaHoras;
    @FXML private Label etiquetaReportes;
    @FXML private TableView<Actividad> tablaActividades;
    @FXML private TableColumn<Actividad, String> columnaTituloActividad;
    @FXML private TableColumn<Actividad, String> columnaFechaInicio;
    @FXML private TableColumn<Actividad, String> columnaFechaFin;
    @FXML private TableColumn<Actividad, String> columnaHoras;
    @FXML private VBox panelDetalle;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;

    private List<Practicante> todosLosAlumnos = new ArrayList<>();
    private int idProfesor;

    @FXML
    public void initialize() {
        idProfesor = SesionUsuario.getInstance().getIdUsuario();
        configurarTablaAlumnos();
        configurarTablaActividades();
        configurarFiltros();
        cargarAlumnos();
        configurarSeleccionAlumno();
        if (panelDetalle != null) {
            panelDetalle.setVisible(false);
            panelDetalle.setManaged(false);
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void configurarTablaAlumnos() {
        columnaMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }

    private void configurarTablaActividades() {
        columnaTituloActividad.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        columnaFechaInicio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaInicio() != null
                        ? cellData.getValue().getFechaInicio().toString() : ""));
        columnaFechaFin.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaFin() != null
                        ? cellData.getValue().getFechaFin().toString() : ""));
        columnaHoras.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getHorasActividad())));
    }

    private void configurarFiltros() {
        comboFiltro.getItems().addAll(
                FILTRO_TODOS, FILTRO_CON_REPORTES, FILTRO_SIN_REPORTES,
                FILTRO_CON_ARCHIVOS, FILTRO_SIN_ARCHIVOS
        );
        comboFiltro.setValue(FILTRO_TODOS);
        comboFiltro.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, nuevo) -> {
                    if (nuevo != null) {
                        aplicarFiltro(nuevo);
                    }
                }
        );
    }

    private void cargarAlumnos() {
        PracticanteDao practicanteDao = new PracticanteDao();
        try {
            todosLosAlumnos = practicanteDao.obtenerPracticantesPorSeccionProfesor(idProfesor);
            tablaAlumnos.setItems(FXCollections.observableArrayList(todosLosAlumnos));
            if (todosLosAlumnos.isEmpty()) {
                mostrarError("Sin alumnos", "No tienes alumnos asignados en ninguna sección.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar alumnos del profesor", excepcion);
            mostrarError("Error al cargar alumnos", excepcion.getMessage().toUpperCase());
        }
    }

    private void aplicarFiltro(String filtro) {
        ReporteDao reporteDao = new ReporteDao();
        List<Practicante> filtrados = new ArrayList<>();
        try {
            for (Practicante practicante : todosLosAlumnos) {
                String matricula = practicante.getMatricula();
                boolean incluir = false;
                if (filtro.equals(FILTRO_TODOS)) {
                    incluir = true;
                } else if (filtro.equals(FILTRO_CON_REPORTES)) {
                    incluir = reporteDao.contarReportesEvaluados(matricula, "Mensual") > 0
                            || reporteDao.contarReportesEvaluados(matricula, "Parcial") > 0;
                } else if (filtro.equals(FILTRO_SIN_REPORTES)) {
                    incluir = reporteDao.contarReportesEvaluados(matricula, "Mensual") == 0
                            && reporteDao.contarReportesEvaluados(matricula, "Parcial") == 0;
                } else if (filtro.equals(FILTRO_CON_ARCHIVOS)) {
                    incluir = reporteDao.contarReportesEvaluados(matricula, "Mensual") > 0;
                } else if (filtro.equals(FILTRO_SIN_ARCHIVOS)) {
                    incluir = reporteDao.contarReportesEvaluados(matricula, "Mensual") == 0;
                }
                if (incluir) {
                    filtrados.add(practicante);
                }
            }
            tablaAlumnos.setItems(FXCollections.observableArrayList(filtrados));
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al filtrar alumnos", excepcion);
            mostrarError("Error al filtrar", excepcion.getMessage().toUpperCase());
        }
    }

    private void configurarSeleccionAlumno() {
        tablaAlumnos.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        cargarDetalleAlumno(seleccionado);
                    }
                }
        );
    }

    private void cargarDetalleAlumno(Practicante practicante) {
        String matricula = practicante.getMatricula();
        ProyectoDao proyectoDao = new ProyectoDao();
        ActividadDao actividadDao = new ActividadDao();
        ReporteDao reporteDao = new ReporteDao();
        try {
            Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante(matricula);
            if (proyecto != null) {
                etiquetaProyecto.setText(proyecto.getNombreProyecto() + " — " + proyecto.getNombreOrganizacion());
            } else {
                etiquetaProyecto.setText("Sin proyecto asignado");
            }
            int horas = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            etiquetaHoras.setText(horas + " horas acumuladas");
            int mensuales = reporteDao.contarReportesEvaluados(matricula, "Mensual");
            int parciales = reporteDao.contarReportesEvaluados(matricula, "Parcial");
            etiquetaReportes.setText("Mensuales evaluados: " + mensuales + "  |  Parciales evaluados: " + parciales);
            List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante(matricula);
            tablaActividades.setItems(FXCollections.observableArrayList(actividades));
            panelDetalle.setVisible(true);
            panelDetalle.setManaged(true);
            ocultarError();
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar detalle del alumno", excepcion);
            mostrarError("Error al cargar detalle", excepcion.getMessage().toUpperCase());
        }
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