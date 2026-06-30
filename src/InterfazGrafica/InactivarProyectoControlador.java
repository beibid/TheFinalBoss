package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InactivarProyectoControlador {

    private static final Logger LOGGER = Logger.getLogger(InactivarProyectoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    private final ProyectoDao proyectoDao = new ProyectoDao();

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
        try {
            List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
            if (proyectos.isEmpty()) {
                mostrarError("Sin proyectos", "NO HAY PROYECTOS DISPONIBLES EN EL SISTEMA.");
                comboBoxProyectos.setDisable(true);
            } else {
                ObservableList<Proyecto> proyectosObservable = FXCollections.observableArrayList(proyectos);
                comboBoxProyectos.setItems(proyectosObservable);
                comboBoxProyectos.setCellFactory(listaProyectos -> crearCeldaProyecto());
                comboBoxProyectos.setButtonCell(crearCeldaProyecto());
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyectos", excepcion);
            mostrarError("Error al cargar proyectos", excepcion.getMessage().toUpperCase());
            comboBoxProyectos.setDisable(true);
        }
    }

    private ListCell<Proyecto> crearCeldaProyecto() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Proyecto proyecto, boolean vacio) {
                super.updateItem(proyecto, vacio);
                boolean esVacioONulo = vacio || proyecto == null;
                if (esVacioONulo) {
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
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
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
            mostrarError("Sin seleccion", "SELECCIONA UN PROYECTO DE LA LISTA.");
        } else if (confirmarAccion("Seguro que desea inactivar este proyecto?")) {
            procesarInactivacion();
        }
    }

    private void procesarInactivacion() {
        try {
            int filasAfectadas = proyectoDao.inactivarProyecto(proyectoSeleccionado.getIdProyecto());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                ocultarTodo();
                comboBoxProyectos.setValue(null);
                comboBoxProyectos.setDisable(false);
                cargarProyectosDisponibles();
                mostrarExito("Proyecto inactivado", "EL PROYECTO FUE INACTIVADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al inactivar", "NO SE PUDO INACTIVAR EL PROYECTO. INTENTE DE NUEVO.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al inactivar proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
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
        boolean confirmado = false;
        alerta.setTitle("Confirmacion");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Si");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == botonSi) {
            confirmado = true;
        }
        return confirmado;
    }

    private void ocultarTodo() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        ocultarPanel(panelDatos);
    }

    private void mostrarPanel(VBox panelMostrar, VBox panelOcultar) {
        panelMostrar.setVisible(true);
        panelMostrar.setManaged(true);
        ocultarPanel(panelOcultar);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        mostrarPanel(panelError, panelExito);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        mostrarPanel(panelExito, panelError);
    }
}