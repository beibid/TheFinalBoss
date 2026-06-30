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
    private static final int HORAS_PARA_TERMINAR = 420;

    @FXML private TableView<Practicante> tablaAlumnos;
    @FXML private TableColumn<Practicante, String> columnaMatricula;
    @FXML private TableColumn<Practicante, String> columnaNombre;
    @FXML private TableColumn<Practicante, String> columnaApellidos;
    @FXML private TableColumn<Practicante, String> columnaCorreo;
    @FXML private ComboBox<String> comboFiltro;
    @FXML private Label etiquetaProyecto;
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

    private final PracticanteDao practicanteDao = new PracticanteDao();
    private final ProyectoDao proyectoDao = new ProyectoDao();
    private final ActividadDao actividadDao = new ActividadDao();
    private final ReporteDao reporteDao = new ReporteDao();

    private List<Practicante> todosLosAlumnos = new ArrayList<>();
    private int idProfesor;

    @FXML
    public void initialize() {
        idProfesor = SesionUsuario.getInstance().getUsuarioActivo().getIdUsuario();
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
                new SimpleStringProperty(obtenerFechaInicio(cellData.getValue())));
        columnaFechaFin.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerFechaFin(cellData.getValue())));
        columnaHoras.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getHorasActividad())));
    }

    private String obtenerFechaInicio(Actividad actividad) {
        String fecha = "";
        if (actividad.getFechaInicio() != null) {
            fecha = actividad.getFechaInicio().toString();
        }
        return fecha;
    }

    private String obtenerFechaFin(Actividad actividad) {
        String fecha = "";
        if (actividad.getFechaFin() != null) {
            fecha = actividad.getFechaFin().toString();
        }
        return fecha;
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
        try {
            todosLosAlumnos = practicanteDao.obtenerPracticantesPorProfesor(idProfesor);
            tablaAlumnos.setItems(FXCollections.observableArrayList(todosLosAlumnos));
            if (todosLosAlumnos.isEmpty()) {
                mostrarError("Sin alumnos", "NO TIENES ALUMNOS ASIGNADOS.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar alumnos del profesor", excepcion);
            mostrarError("Error al cargar alumnos", excepcion.getMessage().toUpperCase());
        }
    }

    private void aplicarFiltro(String filtro) {
        List<Practicante> filtrados = new ArrayList<>();
        try {
            for (Practicante practicante : todosLosAlumnos) {
                if (debeIncluir(practicante, filtro)) {
                    filtrados.add(practicante);
                }
            }
            tablaAlumnos.setItems(FXCollections.observableArrayList(filtrados));
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al filtrar alumnos", excepcion);
            mostrarError("Error al filtrar", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean debeIncluir(Practicante practicante, String filtro) throws MensajeriaExcepcion {
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
        return incluir;
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
        try {
            Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante(matricula);
            List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante(matricula);
            int horas = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            mostrarInfoProyecto(proyecto);
            etiquetaHoras.setText(horas + " horas acumuladas");
            mostrarInfoReportes(matricula);
            mostrarEstado(horas);
            mostrarEstadoAutoevaluacion(matricula, horas);
            tablaActividades.setItems(FXCollections.observableArrayList(actividades));
            panelDetalle.setVisible(true);
            panelDetalle.setManaged(true);
            ocultarPanel(panelError);
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar detalle del alumno", excepcion);
            mostrarError("Error al cargar detalle", excepcion.getMessage().toUpperCase());
        }
    }

    private void mostrarInfoProyecto(Proyecto proyecto) {
        if (proyecto != null) {
            etiquetaProyecto.setText(proyecto.getNombreProyecto() + " - " + proyecto.getNombreOrganizacion());
        } else {
            etiquetaProyecto.setText("Sin proyecto asignado");
        }
    }

    private void mostrarInfoReportes(String matricula) throws MensajeriaExcepcion {
        int mensuales = reporteDao.contarReportesEvaluados(matricula, "Mensual");
        int parciales = reporteDao.contarReportesEvaluados(matricula, "Parcial");
        etiquetaReportes.setText("Mensuales evaluados: " + mensuales + "  |  Parciales evaluados: " + parciales);
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
        mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, titulo, mensaje);
    }

    private void mostrarPanel(Label etiquetaTitulo, Label etiquetaMensaje, VBox panel, String titulo, String mensaje) {
        etiquetaTitulo.setText(titulo);
        etiquetaMensaje.setText(mensaje);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }
}