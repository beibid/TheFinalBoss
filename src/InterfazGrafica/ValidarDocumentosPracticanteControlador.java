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
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.DocumentacionPracticanteDao;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.DocumentacionPracticante;
import logica.dominio.Practicante;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.EstadoRevision;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidarDocumentosPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(ValidarDocumentosPracticanteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<Practicante> comboBoxPracticantes;
    @FXML private ComboBox<DocumentacionPracticante> comboBoxDocumentos;
    @FXML private VBox panelDocumentos;
    @FXML private VBox panelAcciones;
    @FXML private VBox panelMotivoRechazo;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private Label etiquetaRutaArchivo;
    @FXML private TextArea areaMotivoRechazo;

    private DocumentacionPracticante documentoSeleccionado;
    private String numPersonalProfesor;

    @FXML
    public void initialize() {
        numPersonalProfesor = SesionUsuario.getInstance().getUsuarioActivo().getIdentificador();
        cargarPracticantes();
    }

    private void cargarPracticantes() {
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            List<Practicante> practicantes = profesorDao.obtenerPracticantesPorProfesor(numPersonalProfesor);
            ObservableList<Practicante> lista = FXCollections.observableArrayList(practicantes);
            comboBoxPracticantes.setItems(lista);
            comboBoxPracticantes.setCellFactory(l -> crearCeldaPracticante(false));
            comboBoxPracticantes.setButtonCell(crearCeldaPracticante(true));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error al cargar", excepcion.getMessage());
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicante = comboBoxPracticantes.getValue();
        if (practicante == null) {
            return;
        }
        ocultarPaneles();
        cargarDocumentosPendientes(practicante.getMatricula());
    }

    private void cargarDocumentosPendientes(String matricula) {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        try {
            List<DocumentacionPracticante> documentos = documentacionDao.obtenerDocumentosPendientes(matricula);
            if (documentos.isEmpty()) {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Sin documentos",
                        "El practicante no tiene documentos pendientes de validación.");
                return;
            }
            ObservableList<DocumentacionPracticante> lista = FXCollections.observableArrayList(documentos);
            comboBoxDocumentos.setItems(lista);
            comboBoxDocumentos.setCellFactory(l -> crearCeldaDocumento(false));
            comboBoxDocumentos.setButtonCell(crearCeldaDocumento(true));
            panelDocumentos.setVisible(true);
            panelDocumentos.setManaged(true);
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar documentos", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error al cargar", excepcion.getMessage());
        }
    }

    @FXML
    private void seleccionarDocumento() {
        documentoSeleccionado = comboBoxDocumentos.getValue();
        if (documentoSeleccionado == null) {
            return;
        }
        etiquetaRutaArchivo.setText(documentoSeleccionado.getRutaDeArchivo());
        panelMotivoRechazo.setVisible(false);
        panelMotivoRechazo.setManaged(false);
        areaMotivoRechazo.clear();
        panelAcciones.setVisible(true);
        panelAcciones.setManaged(true);
    }

    @FXML
    private void botonAprobar() {
        ocultarPanel(panelError);
        if (confirmarAccion("¿Desea aprobar este documento?")) {
            procesarValidacion(EstadoRevision.Aprobado, null);
        }
    }

    @FXML
    private void botonRechazar() {
        ocultarPanel(panelError);
        panelMotivoRechazo.setVisible(true);
        panelMotivoRechazo.setManaged(true);
        if (!motivoValido()) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Motivo requerido",
                    "Debes ingresar el motivo del rechazo.");
            return;
        }
        if (confirmarAccion("¿Desea rechazar este documento?")) {
            procesarValidacion(EstadoRevision.Rechazado, areaMotivoRechazo.getText().trim());
        }
    }

    private boolean motivoValido() {
        return !areaMotivoRechazo.getText().trim().isEmpty();
    }

    private void procesarValidacion(EstadoRevision estado, String motivo) {
        DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
        try {
            int filasAfectadas = documentacionDao.validarDocumento(
                    documentoSeleccionado.getIdDocumentacionPracticante(), estado, motivo);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarTodo();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito, "Documento validado",
                        "El documento fue " + estado.name().toLowerCase() + " exitosamente.");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error al validar",
                        "No se pudo validar el documento. Intente de nuevo.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al validar documento", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error inesperado",
                    excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarTodo();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void limpiarTodo() {
        comboBoxPracticantes.setValue(null);
        comboBoxDocumentos.getItems().clear();
        areaMotivoRechazo.clear();
        documentoSeleccionado = null;
        ocultarPaneles();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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

    private void ocultarPaneles() {
        panelDocumentos.setVisible(false);
        panelDocumentos.setManaged(false);
        panelAcciones.setVisible(false);
        panelAcciones.setManaged(false);
        panelMotivoRechazo.setVisible(false);
        panelMotivoRechazo.setManaged(false);
    }

    private ListCell<Practicante> crearCeldaPracticante(boolean esBoton) {
        return new ListCell<Practicante>() {
            @Override
            protected void updateItem(Practicante practicante, boolean vacio) {
                super.updateItem(practicante, vacio);
                if (vacio || practicante == null) {
                    setText(esBoton ? "-- Selecciona un practicante --" : null);
                } else if (esBoton) {
                    setText(practicante.getNombre() + " " + practicante.getApellidos());
                } else {
                    setText(practicante.getNombre() + " " + practicante.getApellidos() + " - " + practicante.getMatricula());
                }
            }
        };
    }

    private ListCell<DocumentacionPracticante> crearCeldaDocumento(boolean esBoton) {
        return new ListCell<DocumentacionPracticante>() {
            @Override
            protected void updateItem(DocumentacionPracticante doc, boolean vacio) {
                super.updateItem(doc, vacio);
                if (vacio || doc == null) {
                    setText(esBoton ? "-- Selecciona un documento --" : null);
                } else {
                    setText("Documento #" + doc.getIdDocumentacionPracticante());
                }
            }
        };
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