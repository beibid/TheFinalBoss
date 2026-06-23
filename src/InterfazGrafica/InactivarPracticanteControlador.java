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
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InactivarPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(InactivarPracticanteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<Practicante> comboBoxPracticantes;
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

    private final PracticanteDao practicanteDao = new PracticanteDao();
    private Practicante practicanteSeleccionado;

    @FXML
    public void initialize() {
        cargarPracticantesActivos();
    }

    private void cargarPracticantesActivos() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            if (practicantes.isEmpty()) {
                mostrarError("Sin practicantes", "NO HAY PRACTICANTES ACTIVOS EN EL SISTEMA.");
                comboBoxPracticantes.setDisable(true);
            } else {
                ObservableList<Practicante> practicantesObservable = FXCollections.observableArrayList(practicantes);
                comboBoxPracticantes.setItems(practicantesObservable);
                comboBoxPracticantes.setCellFactory(listaPracticantes -> crearCeldaPracticante());
                comboBoxPracticantes.setButtonCell(crearCeldaPracticante());
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
            comboBoxPracticantes.setDisable(true);
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
        practicanteSeleccionado = comboBoxPracticantes.getValue();
        if (practicanteSeleccionado != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
            mostrarDatosPracticante(practicanteSeleccionado);
        }
    }

    private void mostrarDatosPracticante(Practicante practicante) {
        etiquetaNombre.setText(practicante.getNombre());
        etiquetaApellidos.setText(practicante.getApellidos());
        etiquetaNumeroPersonal.setText(practicante.getMatricula());
        panelDatos.setVisible(true);
        panelDatos.setManaged(true);
    }

    @FXML
    private void botonInactivar() {
        if (practicanteSeleccionado == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PRACTICANTE.");
        } else if (confirmarAccion("¿Seguro que desea inactivar a este practicante?")) {
            ejecutarInactivacion(practicanteSeleccionado);
        }
    }

    private void ejecutarInactivacion(Practicante practicante) {
        try {
            int filasAfectadas = practicanteDao.inactivarPracticante(practicante.getMatricula());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                comboBoxPracticantes.setDisable(false);
                cargarPracticantesActivos();
                mostrarExito("Practicante inactivado", "EL PRACTICANTE FUE INACTIVADO EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR AL PRACTICANTE.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al inactivar practicante", excepcion);
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
        comboBoxPracticantes.setValue(null);
        practicanteSeleccionado = null;
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