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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidarDocumentosPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(ValidarDocumentosPracticanteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    private final ProfesorDao profesorDao = new ProfesorDao();
    private final DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();

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
        try {
            List<Practicante> practicantes = profesorDao.obtenerPracticantesPorProfesor(numPersonalProfesor);
            ObservableList<Practicante> lista = FXCollections.observableArrayList(practicantes);
            comboBoxPracticantes.setItems(lista);
            comboBoxPracticantes.setCellFactory(l -> crearCeldaPracticante(false));
            comboBoxPracticantes.setButtonCell(crearCeldaPracticante(true));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicante = comboBoxPracticantes.getValue();
        boolean practicanteSeleccionado = practicante != null;
        if (practicanteSeleccionado) {
            ocultarPaneles();
            cargarDocumentosPendientes(practicante.getMatricula());
        }
    }

    private void cargarDocumentosPendientes(String matricula) {
        try {
            List<DocumentacionPracticante> documentos = documentacionDao.obtenerDocumentosPendientes(matricula);
            if (documentos.isEmpty()) {
                mostrarError("Sin documentos",
                        "EL PRACTICANTE NO TIENE DOCUMENTOS PENDIENTES DE VALIDACION.");
            } else {
                ObservableList<DocumentacionPracticante> lista = FXCollections.observableArrayList(documentos);
                comboBoxDocumentos.setItems(lista);
                comboBoxDocumentos.setCellFactory(l -> crearCeldaDocumento(false));
                comboBoxDocumentos.setButtonCell(crearCeldaDocumento(true));
                panelDocumentos.setVisible(true);
                panelDocumentos.setManaged(true);
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar documentos", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void seleccionarDocumento() {
        documentoSeleccionado = comboBoxDocumentos.getValue();
        boolean documentoElegido = documentoSeleccionado != null;
        if (documentoElegido) {
            etiquetaRutaArchivo.setText(documentoSeleccionado.getRutaDeArchivo());
            panelMotivoRechazo.setVisible(false);
            panelMotivoRechazo.setManaged(false);
            areaMotivoRechazo.clear();
            panelAcciones.setVisible(true);
            panelAcciones.setManaged(true);
        }
    }

    @FXML
    private void botonAprobar() {
        ocultarPanel(panelError);
        if (confirmarAccion("Desea aprobar este documento?")) {
            procesarValidacion(EstadoRevision.Aprobado, null);
        }
    }

    @FXML
    private void botonRechazar() {
        ocultarPanel(panelError);
        panelMotivoRechazo.setVisible(true);
        panelMotivoRechazo.setManaged(true);
        boolean motivoIngresado = !areaMotivoRechazo.getText().trim().isEmpty();
        if (!motivoIngresado) {
            mostrarError("Motivo requerido", "DEBES INGRESAR EL MOTIVO DEL RECHAZO.");
        } else if (confirmarAccion("Desea rechazar este documento?")) {
            procesarValidacion(EstadoRevision.Rechazado, areaMotivoRechazo.getText().trim());
        }
    }

    private void procesarValidacion(EstadoRevision estado, String motivo) {
        try {
            int filasAfectadas = documentacionDao.validarDocumento(
                    documentoSeleccionado.getIdDocumentacionPracticante(), estado, motivo);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarTodo();
                mostrarExito("Documento validado",
                        "EL DOCUMENTO FUE " + estado.name().toUpperCase() + " EXITOSAMENTE.");
            } else {
                mostrarError("Error al validar", "NO SE PUDO VALIDAR EL DOCUMENTO. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al validar documento", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
            limpiarTodo();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
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

    private void ocultarPaneles() {
        ocultarPanel(panelDocumentos);
        ocultarPanel(panelAcciones);
        ocultarPanel(panelMotivoRechazo);
    }

    private ListCell<Practicante> crearCeldaPracticante(boolean esBoton) {
        return new ListCell<Practicante>() {
            @Override
            protected void updateItem(Practicante practicante, boolean vacio) {
                super.updateItem(practicante, vacio);
                boolean esVacioONulo = vacio || practicante == null;
                if (esVacioONulo && esBoton) {
                    setText("-- Selecciona un practicante --");
                } else if (esVacioONulo) {
                    setText(null);
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
                boolean esVacioONulo = vacio || doc == null;
                if (esVacioONulo && esBoton) {
                    setText("-- Selecciona un documento --");
                } else if (esVacioONulo) {
                    setText(null);
                } else {
                    setText("Documento #" + doc.getIdDocumentacionPracticante());
                }
            }
        };
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