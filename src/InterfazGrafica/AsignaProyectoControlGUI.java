package InterfazGrafica;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Practicante;
import logica.dominio.Proyecto;
import java.util.List;



public class AsignaProyectoControlGUI {


    @FXML private ComboBox<Practicante> comboBoxPracticantes;
    @FXML private ComboBox<Proyecto> comboBoxProyectos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

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
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarProyectos() {
        try {
            List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
            comboBoxProyectos.setItems(FXCollections.observableArrayList(proyectos));
        } catch (MensajeriaExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROYECTOS DISPONIBLES.");
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        if (practicante != null) {
            ocultarError();
            ocultarExito();
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

            if (filasAfectadas > 0) {
                limpiarSeleccion();
                mostrarExito("Asignación exitosa", "EL PRACTICANTE FUE ASIGNADO AL PROYECTO EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO REALIZAR LA ASIGNACIÓN.");
            }
        } catch (UsuariosExcepcion e){
            mostrarError("ERROR", e.getMessage().toUpperCase());
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);
        return alerta.showAndWait().filter(r -> r == btnSi).isPresent();
    }

    private void limpiarSeleccion() {
        comboBoxPracticantes.getSelectionModel().clearSelection();
        comboBoxProyectos.getSelectionModel().clearSelection();
        ocultarError();
        ocultarExito();
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
        ocultarExito();
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
        ocultarError();
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }


}
