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
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.PracticanteSeccionDao;
import logica.dao.objetos.SeccionDao;
import logica.dominio.Practicante;
import logica.dominio.PracticanteSeccion;
import logica.dominio.Seccion;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsignarPracticanteEnSeccionControlador {

    @FXML private ComboBox<Practicante> comboBoxPracticantes;
    @FXML private ComboBox<Seccion> comboBoxSecciones;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private static final Logger LOGGER = Logger.getLogger(AsignarPracticanteEnSeccionControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    private final PracticanteDao practicanteDao = new PracticanteDao();
    private final SeccionDao seccionDao = new SeccionDao();
    private final PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();

    @FXML
    public void initialize() {
        cargarPracticantesActivos();
        cargarSecciones();
    }

    private void cargarPracticantesActivos() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            comboBoxPracticantes.setItems(FXCollections.observableArrayList(practicantes));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarSecciones() {
        try {
            List<Seccion> secciones = seccionDao.obtenerSecciones();
            comboBoxSecciones.setItems(FXCollections.observableArrayList(secciones));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar secciones", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LAS SECCIONES.");
        }
    }

    @FXML
    private void seleccionarProfesor() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        if (practicante != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
        }
    }

    @FXML
    private void botonAsignar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (seleccionValida()) {
            if (confirmarAccion("¿Seguro que desea asignar a "
                    + comboBoxPracticantes.getSelectionModel().getSelectedItem().getNombre()
                    + " a la seccion "
                    + comboBoxSecciones.getSelectionModel().getSelectedItem().getNoSeccion() + "?")) {
                ejecutarAsignacion();
            }
        }
    }

    private boolean seleccionValida() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        Seccion seccion = comboBoxSecciones.getSelectionModel().getSelectedItem();
        boolean seleccionCorrecta = practicante != null && seccion != null;
        verificarSeleccion(practicante, seccion);
        return seleccionCorrecta;
    }

    private void verificarSeleccion(Practicante practicante, Seccion seccion) {
        if (practicante == null) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Sin seleccion", "POR FAVOR SELECCIONA UN PRACTICANTE.");
        } else if (seccion == null) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Sin seleccion", "POR FAVOR SELECCIONA UNA SECCION.");
        }
    }

    private void ejecutarAsignacion() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        Seccion seccion = comboBoxSecciones.getSelectionModel().getSelectedItem();
        try {
            PracticanteSeccion practicanteEnSeccion = new PracticanteSeccion();
            practicanteEnSeccion.setMatricula(practicante.getMatricula());
            practicanteEnSeccion.setNoSeccion(seccion.getNoSeccion());
            practicanteEnSeccion.setIdPeriodo(seccion.getIdPeriodo());
            int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(practicanteEnSeccion);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarSeleccion();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Asignacion exitosa", "EL PRACTICANTE FUE ASIGNADO A LA SECCION EXITOSAMENTE.");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error", "NO SE PUDO REALIZAR LA ASIGNACION.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al asignar practicante a seccion", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        } catch(RegistroDuplicadoExcepcion excepcion) {
        LOGGER.log(Level.WARNING, "Practicante ya asignado", excepcion);
        mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                "Practicante ya asignado", "ESE PRACTICANTE YA ESTA ASIGNADO A UNA SECCION EN EL PERIODO ACTUAL.");
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
        comboBoxSecciones.getSelectionModel().clearSelection();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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