package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.PreferenciaProyectoDao;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Practicante;
import logica.dominio.PreferenciaProyecto;
import logica.dominio.Proyecto;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsignarProyectoControlador {

    @FXML private ComboBox<Practicante> comboBoxPracticantes;
    @FXML private ComboBox<Proyecto> comboBoxProyectos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private ListView<String> listaPreferencias;
    @FXML private VBox panelPreferencias;

    private static final Logger LOGGER = Logger.getLogger(AsignarProyectoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int CAPACIDAD_MINIMA = 1;

    private final PreferenciaProyectoDao preferenciaDao = new PreferenciaProyectoDao();
    private final PracticanteDao practicanteDao = new PracticanteDao();
    private final ProyectoDao proyectoDao = new ProyectoDao();

    @FXML
    public void initialize() {
        cargarPracticantesActivos();
        cargarProyectos();
    }

    private void cargarPracticantesActivos() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            comboBoxPracticantes.setItems(FXCollections.observableArrayList(practicantes));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarProyectos() {
        try {
            List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
            comboBoxProyectos.setItems(FXCollections.observableArrayList(proyectos));
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyectos", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROYECTOS DISPONIBLES.");
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (practicante != null) {
            cargarPreferenciasPracticante(practicante.getMatricula());
        }
    }

    private void cargarPreferenciasPracticante(String matricula) {
        try {
            List<PreferenciaProyecto> preferencias = preferenciaDao.obtenerPreferencias(matricula);
            listaPreferencias.getItems().clear();
            if (preferencias.isEmpty()) {
                listaPreferencias.getItems().add("El practicante no tiene preferencias registradas");
            } else {
                for (PreferenciaProyecto preferencia : preferencias) {
                    listaPreferencias.getItems().add(
                            "Prioridad " + preferencia.getPrioridad() + ": " + preferencia.getNombreProyecto()
                    );
                }
            }
            panelPreferencias.setVisible(true);
            panelPreferencias.setManaged(true);
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar preferencias del practicante", excepcion);
            mostrarError("Error al cargar preferencias", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonAsignar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        boolean asignacionConfirmada = seleccionValida()
                && practicanteDisponible()
                && proyectoConCapacidad()
                && confirmarAccion("¿Seguro que desea asignar a "
                + comboBoxPracticantes.getSelectionModel().getSelectedItem().getNombre()
                + " el proyecto "
                + comboBoxProyectos.getSelectionModel().getSelectedItem().getNombreProyecto() + "?");
        if (asignacionConfirmada) {
            ejecutarAsignacion();
        }
    }

    private boolean seleccionValida() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        Proyecto proyecto = comboBoxProyectos.getSelectionModel().getSelectedItem();
        verificarSeleccion(practicante, proyecto);
        return practicante != null && proyecto != null;
    }

    private void verificarSeleccion(Practicante practicante, Proyecto proyecto) {
        if (practicante == null) {
            mostrarError("Sin seleccion", "POR FAVOR SELECCIONA UN PRACTICANTE.");
        } else if (proyecto == null) {
            mostrarError("Sin seleccion", "POR FAVOR SELECCIONA UN PROYECTO.");
        }
    }

    private boolean practicanteDisponible() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        boolean disponible = true;
        try {
            Proyecto proyectoActual = proyectoDao.obtenerProyectoPorPracticante(practicante.getMatricula());
            if (proyectoActual != null) {
                mostrarError("Practicante no disponible",
                        "EL PRACTICANTE YA TIENE UN PROYECTO ASIGNADO: "
                                + proyectoActual.getNombreProyecto().toUpperCase() + ".");
                disponible = false;
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar proyecto del practicante", excepcion);
            mostrarError("Error", "NO SE PUDO VERIFICAR EL ESTADO DEL PRACTICANTE.");
            disponible = false;
        }
        return disponible;
    }

    private boolean proyectoConCapacidad() {
        Proyecto proyecto = comboBoxProyectos.getSelectionModel().getSelectedItem();
        boolean conCapacidad = proyecto.getCapacidad() >= CAPACIDAD_MINIMA;
        if (!conCapacidad) {
            mostrarError("Sin capacidad", "EL PROYECTO SELECCIONADO YA NO CUENTA CON LUGARES DISPONIBLES.");
        }
        return conCapacidad;
    }

    private void ejecutarAsignacion() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        Proyecto proyecto = comboBoxProyectos.getSelectionModel().getSelectedItem();
        try {
            int filasAfectadas = practicanteDao.asignarProyecto(practicante.getMatricula(), proyecto.getIdProyecto());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarSeleccion();
                mostrarExito("Asignacion exitosa", "EL PRACTICANTE FUE ASIGNADO AL PROYECTO EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO REALIZAR LA ASIGNACION.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al asignar proyecto al practicante", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarSeleccion();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmacion");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Si");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        return alerta.showAndWait().filter(botonPresionado -> botonPresionado == botonSi).isPresent();
    }

    private void limpiarSeleccion() {
        comboBoxPracticantes.getSelectionModel().clearSelection();
        comboBoxProyectos.getSelectionModel().clearSelection();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
    }

    private void mostrarError(String titulo, String mensaje) {
        mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, titulo, mensaje);
    }

    private void mostrarExito(String titulo, String mensaje) {
        mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito, titulo, mensaje);
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