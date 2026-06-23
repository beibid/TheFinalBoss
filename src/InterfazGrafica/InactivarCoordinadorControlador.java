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
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InactivarCoordinadorControlador {

    private static final Logger LOGGER = Logger.getLogger(InactivarCoordinadorControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<Coordinador> comboBoxCoordinadores;
    @FXML private VBox panelDatos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private Label etiquetaNombre;
    @FXML private Label etiquetaApellidos;
    @FXML private Label etiquetaNumeroPersonal;

    private final CoordinadorDao coordinadorDao = new CoordinadorDao();
    private Coordinador coordinadorSeleccionado;

    @FXML
    public void initialize() {
        cargarCoordinadoresActivos();
    }

    private void cargarCoordinadoresActivos() {
        try {
            List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
            if (coordinadores.isEmpty()) {
                mostrarError("Sin coordinadores", "NO HAY COORDINADORES ACTIVOS EN EL SISTEMA.");
                comboBoxCoordinadores.setDisable(true);
            } else {
                ObservableList<Coordinador> coordinadoresObservable = FXCollections.observableArrayList(coordinadores);
                comboBoxCoordinadores.setItems(coordinadoresObservable);
                comboBoxCoordinadores.setCellFactory(listaCoordinadores -> crearCeldaCoordinador());
                comboBoxCoordinadores.setButtonCell(crearCeldaCoordinador());
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar coordinadores", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS COORDINADORES.");
            comboBoxCoordinadores.setDisable(true);
        }
    }

    private ListCell<Coordinador> crearCeldaCoordinador() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Coordinador coordinador, boolean vacio) {
                super.updateItem(coordinador, vacio);
                if (vacio || coordinador == null) {
                    setText("-- Selecciona un coordinador --");
                } else {
                    setText(coordinador.getNombre() + " " + coordinador.getApellidos());
                }
            }
        };
    }

    @FXML
    private void seleccionarCoordinador() {
        coordinadorSeleccionado = comboBoxCoordinadores.getValue();
        if (coordinadorSeleccionado != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
            mostrarDatosCoordinador(coordinadorSeleccionado);
        }
    }

    private void mostrarDatosCoordinador(Coordinador coordinador) {
        etiquetaNombre.setText(coordinador.getNombre());
        etiquetaApellidos.setText(coordinador.getApellidos());
        etiquetaNumeroPersonal.setText(coordinador.getNumeroDePersonalCoordinador());
        panelDatos.setVisible(true);
        panelDatos.setManaged(true);
    }

    @FXML
    private void botonInactivar() {
        if (coordinadorSeleccionado == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN COORDINADOR.");
        } else if (confirmarAccion("¿Seguro que desea inactivar a este coordinador?")) {
            ejecutarInactivacion(coordinadorSeleccionado);
        }
    }

    private void ejecutarInactivacion(Coordinador coordinador) {
        try {
            int filasAfectadas = coordinadorDao.inactivarCoordinador(coordinador.getNumeroDePersonalCoordinador());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                comboBoxCoordinadores.setDisable(false);
                cargarCoordinadoresActivos();
                mostrarExito("Coordinador inactivado", "EL COORDINADOR FUE INACTIVADO EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR AL COORDINADOR.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al inactivar coordinador", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiar();
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

    private void limpiar() {
        comboBoxCoordinadores.setValue(null);
        coordinadorSeleccionado = null;
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
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