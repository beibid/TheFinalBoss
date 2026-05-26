package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InactivarProyectoControlador {

    private static final Logger LOGGER = Logger.getLogger(InactivarProyectoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<Proyecto> comboBoxProyectos;
    @FXML private VBox panelDatos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private Label etiquetaNombreProyecto;
    @FXML private Label etiquetaIdProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private Label etiquetaEstado;

    private Proyecto proyectoSeleccionado;

    @FXML
    public void initialize() {
        cargarProyectosDisponibles();
    }

    private void cargarProyectosDisponibles() {
        ProyectoDao proyectoDao = new ProyectoDao();
        try {
            List<Proyecto> listaProyectos = proyectoDao.obtenerProyectosDisponibles();
            ObservableList<Proyecto> proyectosObservable = FXCollections.observableArrayList(listaProyectos);
            comboBoxProyectos.setItems(proyectosObservable);
            comboBoxProyectos.setCellFactory(listaProyectos2 -> crearCeldaProyecto());
            comboBoxProyectos.setButtonCell(crearCeldaProyecto());
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyectos", excepcion);
            mostrarError("Error al cargar proyectos", excepcion.getMessage());
        }
    }

    private ListCell<Proyecto> crearCeldaProyecto() {
        return new ListCell<Proyecto>() {
            @Override
            protected void updateItem(Proyecto proyecto, boolean vacio) {
                super.updateItem(proyecto, vacio);
                if (vacio || proyecto == null) {
                    setText("-- Selecciona un proyecto --");
                } else {
                    setText(proyecto.getNombreProyecto());
                }
            }
        };
    }

    @FXML
    private void seleccionarProyecto() {
        proyectoSeleccionado = comboBoxProyectos.getValue();
        if (proyectoSeleccionado != null) {
            ocultarPaneles();
            mostrarDatosProyecto(proyectoSeleccionado);
        }
    }

    private void mostrarDatosProyecto(Proyecto proyecto) {
        etiquetaNombreProyecto.setText(proyecto.getNombreProyecto());
        etiquetaIdProyecto.setText(String.valueOf(proyecto.getIdProyecto()));
        etiquetaOrganizacion.setText(proyecto.getNombreEmpresa());
        etiquetaEstado.setText(proyecto.getEstado().toString().replace("_", " "));
        panelDatos.setVisible(true);
        panelDatos.setManaged(true);
    }

    @FXML
    private void botonInactivar() {
        if (proyectoSeleccionado == null) {
            mostrarError("Sin selección", "Selecciona un proyecto de la lista.");
        } else if (confirmarAccion("¿Seguro que desea inactivar este proyecto?")) {
            procesarInactivacion();
        }
    }

    private void procesarInactivacion() {
        ProyectoDao proyectoDao = new ProyectoDao();
        try {
            int filasAfectadas = proyectoDao.inactivarProyecto(proyectoSeleccionado.getIdProyecto());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                ocultarTodo();
                comboBoxProyectos.setValue(null);
                cargarProyectosDisponibles();
                mostrarExito("Proyecto inactivado", "El proyecto fue inactivado exitosamente.");
            } else {
                mostrarError("Error al inactivar", "No se pudo inactivar el proyecto. Intente de nuevo.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al inactivar proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            ocultarTodo();
            comboBoxProyectos.setValue(null);
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
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

    private void ocultarTodo() {
        ocultarPaneles();
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
    }

    private void ocultarPaneles() {
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