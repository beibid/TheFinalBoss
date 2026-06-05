package InterfazGrafica;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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


public class AsignarProyectoControlador {

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

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
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarProyectos() {
        try {
            List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
            comboBoxProyectos.setItems(FXCollections.observableArrayList(proyectos));
        } catch (MensajeriaExcepcion excepcion) {
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
            mostrarError("Error al cargar preferencias", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonAsignar() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        Proyecto proyecto = comboBoxProyectos.getSelectionModel().getSelectedItem();

        if (practicante == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PRACTICANTE.");
            return;
        }
        if (proyecto == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PROYECTO.");
            return;
        }
        if (!confirmarAccion("¿Seguro que desea asignar a " + practicante.getNombre() + " el proyecto " + proyecto.getNombreProyecto() + "?")) {
            return;
        }
        ejecutarAsignacion(practicante, proyecto);
    }

    private void ejecutarAsignacion(Practicante practicante, Proyecto proyecto) {
        try {
            int filasAfectadas = practicanteDao.asignarProyecto(practicante.getMatricula(), proyecto.getIdProyecto());

            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarSeleccion();
                mostrarExito("Asignación exitosa", "EL PRACTICANTE FUE ASIGNADO AL PROYECTO EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO REALIZAR LA ASIGNACIÓN.");
            }
        } catch (UsuariosExcepcion excepcion){
            mostrarError("ERROR", excepcion.getMessage().toUpperCase());
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
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Sí");
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

    private void mostrarPanel(VBox panel, Label etiquetaTitulo, Label etiquetaMensaje, String titulo, String mensaje) {
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
