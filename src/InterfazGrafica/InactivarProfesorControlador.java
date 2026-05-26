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
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InactivarProfesorControlador {

    private static final Logger LOGGER = Logger.getLogger(InactivarProfesorControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<Profesor> comboBoxProfesores;
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

    private ProfesorDao profesorDao = new ProfesorDao();
    private Profesor profesorSeleccionado;

    @FXML
    public void initialize() {
        cargarProfesoresActivos();
    }

    private void cargarProfesoresActivos() {
        try {
            List<Profesor> listaProfesores = profesorDao.obtenerProfesoresActivos();
            ObservableList<Profesor> profesoresObservable = FXCollections.observableArrayList(listaProfesores);
            comboBoxProfesores.setItems(profesoresObservable);
            comboBoxProfesores.setCellFactory(listaProfesores2 -> crearCeldaProfesor());
            comboBoxProfesores.setButtonCell(crearCeldaProfesor());
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROFESORES.");
        }
    }

    private ListCell<Profesor> crearCeldaProfesor() {
        return new ListCell<Profesor>() {
            @Override
            protected void updateItem(Profesor profesor, boolean vacio) {
                super.updateItem(profesor, vacio);
                if (vacio || profesor == null) {
                    setText("-- Selecciona un profesor --");
                } else {
                    setText(profesor.getNombre() + " " + profesor.getApellidos());
                }
            }
        };
    }

    @FXML
    private void seleccionarProfesor() {
        profesorSeleccionado = comboBoxProfesores.getValue();
        if (profesorSeleccionado != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
            mostrarDatosProfesor(profesorSeleccionado);
        }
    }

    private void mostrarDatosProfesor(Profesor profesor) {
        etiquetaNombre.setText(profesor.getNombre());
        etiquetaApellidos.setText(profesor.getApellidos());
        etiquetaNumeroPersonal.setText(profesor.getNumeroDePersonalProfesor());
        panelDatos.setVisible(true);
        panelDatos.setManaged(true);
    }

    @FXML
    private void botonInactivar() {
        if (profesorSeleccionado == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PROFESOR.");
        } else if (confirmarAccion("¿Seguro que desea inactivar a este profesor?")) {
            ejecutarInactivacion(profesorSeleccionado);
        }
    }

    private void ejecutarInactivacion(Profesor profesor) {
        try {
            int filasAfectadas = profesorDao.inactivarProfesor(profesor.getNumeroDePersonalProfesor());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                cargarProfesoresActivos();
                mostrarExito("Profesor inactivado", "EL PROFESOR FUE INACTIVADO EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR AL PROFESOR.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al inactivar profesor", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        limpiar();
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
        comboBoxProfesores.setValue(null);
        profesorSeleccionado = null;
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