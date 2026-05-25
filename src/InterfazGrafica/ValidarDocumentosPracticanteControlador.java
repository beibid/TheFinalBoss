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
            comboBoxPracticantes.setCellFactory(l -> new ListCell<Practicante>() {
                @Override
                protected void updateItem(Practicante practicante, boolean vacio) {
                    super.updateItem(practicante, vacio);
                    if (vacio || practicante == null) {
                        setText(null);
                    } else {
                        setText(practicante.getNombre() + " " + practicante.getApellidos() + " - " + practicante.getMatricula());
                    }
                }
            });
            comboBoxPracticantes.setButtonCell(new ListCell<Practicante>() {
                @Override
                protected void updateItem(Practicante practicante, boolean vacio) {
                    super.updateItem(practicante, vacio);
                    if (vacio || practicante == null) {
                        setText("-- Selecciona un practicante --");
                    } else {
                        setText(practicante.getNombre() + " " + practicante.getApellidos());
                    }
                }
            });
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage());
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
                mostrarError("Sin documentos", "El practicante no tiene documentos pendientes de validación.");
                return;
            }
            ObservableList<DocumentacionPracticante> lista = FXCollections.observableArrayList(documentos);
            comboBoxDocumentos.setItems(lista);
            comboBoxDocumentos.setCellFactory(l -> new ListCell<DocumentacionPracticante>() {
                @Override
                protected void updateItem(DocumentacionPracticante doc, boolean vacio) {
                    super.updateItem(doc, vacio);
                    if (vacio || doc == null) {
                        setText(null);
                    } else {
                        setText("Documento #" + doc.getIdDocumentacionPracticante());
                    }
                }
            });
            comboBoxDocumentos.setButtonCell(new ListCell<DocumentacionPracticante>() {
                @Override
                protected void updateItem(DocumentacionPracticante doc, boolean vacio) {
                    super.updateItem(doc, vacio);
                    if (vacio || doc == null) {
                        setText("-- Selecciona un documento --");
                    } else {
                        setText("Documento #" + doc.getIdDocumentacionPracticante());
                    }
                }
            });
            panelDocumentos.setVisible(true);
            panelDocumentos.setManaged(true);
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar documentos", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage());
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
        ocultarError();
        if (confirmarAccion("¿Desea aprobar este documento?")) {
            procesarValidacion(EstadoRevision.Aprobado, null);
        }
    }

    @FXML
    private void botonRechazar() {
        ocultarError();
        panelMotivoRechazo.setVisible(true);
        panelMotivoRechazo.setManaged(true);
        if (!motivoValido()) {
            mostrarError("Motivo requerido", "Debes ingresar el motivo del rechazo.");
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
            if (filasAfectadas > 0) {
                limpiarTodo();
                mostrarExito("Documento validado",
                        "El documento fue " + estado.name().toLowerCase() + " exitosamente.");
            } else {
                mostrarError("Error al validar",
                        "No se pudo validar el documento. Intente de nuevo.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al validar documento", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
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
        ocultarError();
        ocultarExito();
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

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
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
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }





}
