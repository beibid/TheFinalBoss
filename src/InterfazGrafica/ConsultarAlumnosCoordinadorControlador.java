package InterfazGrafica;

import javafx.beans.property.SimpleStringProperty;
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
import logica.dao.objetos.ActividadDao;
import logica.dao.objetos.AutoevaluacionPracticanteDao;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ProyectoDao;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Actividad;
import logica.dominio.AutoevaluacionPracticante;
import logica.dominio.Practicante;
import logica.dominio.Proyecto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultarAlumnosCoordinadorControlador {

    private static final Logger LOGGER = Logger.getLogger(ConsultarAlumnosCoordinadorControlador.class.getName());
    private static final String FILTRO_TODOS = "Todos";
    private static final String FILTRO_TERMINADOS = "Terminados (420+ horas)";
    private static final String FILTRO_EN_PROCESO = "En proceso";
    private static final String FILTRO_SIN_PROYECTO = "Sin proyecto";
    private static final int HORAS_PARA_TERMINAR = 420;

    @FXML private TableView<Practicante> tablaAlumnos;
    @FXML private TableColumn<Practicante, String> columnaMatricula;
    @FXML private TableColumn<Practicante, String> columnaNombre;
    @FXML private TableColumn<Practicante, String> columnaApellidos;
    @FXML private TableColumn<Practicante, String> columnaCorreo;
    @FXML private ComboBox<String> comboFiltro;
    @FXML private Label etiquetaProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private Label etiquetaHoras;
    @FXML private Label etiquetaReportes;
    @FXML private Label etiquetaEstado;
    @FXML private Label etiquetaAutoevaluacion;
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

    @FXML
    public void initialize() {
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
                FILTRO_TODOS, FILTRO_TERMINADOS, FILTRO_EN_PROCESO, FILTRO_SIN_PROYECTO
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
            todosLosAlumnos = practicanteDao.obtenerPracticantesActivos();
            tablaAlumnos.setItems(FXCollections.observableArrayList(todosLosAlumnos));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar alumnos", excepcion);
            mostrarError("Error al cargar alumnos", excepcion.getMessage().toUpperCase());
        }
    }

    private void aplicarFiltro(String filtro) {
        ActividadDao actividadDao = new ActividadDao();
        ProyectoDao proyectoDao = new ProyectoDao();
        List<Practicante> filtrados = new ArrayList<>();
        try {
            for (Practicante practicante : todosLosAlumnos) {
                String matricula = practicante.getMatricula();
                boolean incluir = false;
                if (filtro.equals(FILTRO_TODOS)) {
                    incluir = true;
                } else if (filtro.equals(FILTRO_TERMINADOS)) {
                    int horas = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
                    incluir = horas >= HORAS_PARA_TERMINAR;
                } else if (filtro.equals(FILTRO_EN_PROCESO)) {
                    int horas = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
                    Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante(matricula);
                    incluir = proyecto != null && horas < HORAS_PARA_TERMINAR;
                } else if (filtro.equals(FILTRO_SIN_PROYECTO)) {
                    Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante(matricula);
                    incluir = proyecto == null;
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
            int horas = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            int mensuales = reporteDao.contarReportesEvaluados(matricula, "Mensual");
            int parciales = reporteDao.contarReportesEvaluados(matricula, "Parcial");
            List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante(matricula);
            mostrarInfoProyecto(proyecto);
            etiquetaHoras.setText(horas + " horas acumuladas");
            etiquetaReportes.setText("Mensuales: " + mensuales + "  |  Parciales: " + parciales);
            mostrarEstado(horas);
            mostrarEstadoAutoevaluacion(matricula, horas);
            tablaActividades.setItems(FXCollections.observableArrayList(actividades));
            panelDetalle.setVisible(true);
            panelDetalle.setManaged(true);
            ocultarError();
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar detalle del alumno", excepcion);
            mostrarError("Error al cargar detalle", excepcion.getMessage().toUpperCase());
        }
    }

    private void mostrarInfoProyecto(Proyecto proyecto) {
        if (proyecto != null) {
            etiquetaProyecto.setText(proyecto.getNombreProyecto());
            etiquetaOrganizacion.setText(proyecto.getNombreOrganizacion());
        } else {
            etiquetaProyecto.setText("Sin proyecto asignado");
            etiquetaOrganizacion.setText("—");
        }
    }

    private void mostrarEstado(int horas) {
        if (horas >= HORAS_PARA_TERMINAR) {
            etiquetaEstado.setText("✔ Terminado");
            etiquetaEstado.setStyle("-fx-text-fill: #2d6a2d; -fx-font-weight: bold;");
        } else {
            etiquetaEstado.setText("⏳ En proceso");
            etiquetaEstado.setStyle("-fx-text-fill: #b07d00; -fx-font-weight: bold;");
        }
    }

    private void mostrarEstadoAutoevaluacion(String matricula, int horas) throws MensajeriaExcepcion {
        if (horas >= HORAS_PARA_TERMINAR) {
            AutoevaluacionPracticanteDao autoevaluacionDao = new AutoevaluacionPracticanteDao();
            AutoevaluacionPracticante autoevaluacion = autoevaluacionDao.obtenerAutoevaluacion(matricula);
            if (autoevaluacion != null) {
                etiquetaAutoevaluacion.setText("✔ Autoevaluación entregada");
                etiquetaAutoevaluacion.setStyle("-fx-text-fill: #2d6a2d; -fx-font-weight: bold;");
            } else {
                etiquetaAutoevaluacion.setText("✖ Autoevaluación pendiente");
                etiquetaAutoevaluacion.setStyle("-fx-text-fill: #8B0000; -fx-font-weight: bold;");
            }
            etiquetaAutoevaluacion.setVisible(true);
            etiquetaAutoevaluacion.setManaged(true);
        } else {
            etiquetaAutoevaluacion.setVisible(false);
            etiquetaAutoevaluacion.setManaged(false);
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