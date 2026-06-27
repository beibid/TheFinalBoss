package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PeriodoUniversitarioDao;
import logica.dao.objetos.ProfesorDao;
import logica.dao.objetos.SeccionDao;
import logica.dominio.PeriodoUniversitario;
import logica.dominio.Profesor;
import logica.dominio.Seccion;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarSeccionControlador {

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LONGITUD_MAXIMA_NRC = 10;
    private static final Logger LOGGER = Logger.getLogger(RegistrarSeccionControlador.class.getName());

    @FXML private TextField campoTextoNumeroSeccion;
    @FXML private ComboBox<PeriodoUniversitario> comboBoxPeriodo;
    @FXML private ComboBox<Profesor> comboBoxProfesor;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @FXML
    public void initialize() {
        cargarPeriodosAbiertos();
        cargarProfesoresActivos();
    }

    private void cargarPeriodosAbiertos() {
        PeriodoUniversitarioDao periodoDao = new PeriodoUniversitarioDao();
        try {
            List<PeriodoUniversitario> periodos = periodoDao.obtenerPeriodosAbiertos();
            comboBoxPeriodo.setItems(FXCollections.observableArrayList(periodos));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar periodos", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LOS PERIODOS DISPONIBLES");
        }
    }

    private void cargarProfesoresActivos() {
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
            comboBoxProfesor.setItems(FXCollections.observableArrayList(profesores));
            comboBoxProfesor.setConverter(new StringConverter<Profesor>() {
                @Override
                public String toString(Profesor profesor) {
                    if (profesor != null) {
                        return profesor.getNombre() + " " + profesor.getApellidos();
                    }
                    return null;
                }
                @Override
                public Profesor fromString(String texto) {
                    return null;
                }
            });
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LOS PROFESORES ACTIVOS");
        }
    }

    @FXML
    private void registrarSeccion() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("¿Seguro que desea registrar esta seccion?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void cancelarRegistro() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCamposRegistros();
        }
    }

    @FXML
    private void regresarMenu(ActionEvent evento) {
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

    private void procesarRegistro() {
        if (camposValidos()) {
            guardarSeccion(construirSeccion());
        }
    }

    private boolean camposValidos() {
        String numeroSeccion = campoTextoNumeroSeccion.getText().trim();
        PeriodoUniversitario periodoSeleccionado = comboBoxPeriodo.getValue();
        Profesor profesorSeleccionado = comboBoxProfesor.getValue();

        boolean nrcValido = !numeroSeccion.isEmpty() && numeroSeccion.matches("[a-zA-Z0-9]+");
        boolean longitudValida = numeroSeccion.length() <= LONGITUD_MAXIMA_NRC;
        boolean periodoValido = periodoSeleccionado != null;
        boolean profesorValido = profesorSeleccionado != null;

        if (numeroSeccion.isEmpty()) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campo vacio", "INGRESE EL NRC DE LA SECCION");
        } else if (!nrcValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "NRC invalido", "EL NRC SOLO PUEDE CONTENER LETRAS Y NUMEROS");
        } else if (!longitudValida) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "NRC invalido", "EL NRC NO PUEDE EXCEDER " + LONGITUD_MAXIMA_NRC + " CARACTERES");
        } else if (!periodoValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Periodo no seleccionado", "SELECCIONE UN PERIODO PARA LA SECCION");
        } else if (!profesorValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Profesor no seleccionado", "SELECCIONE UN PROFESOR PARA LA SECCION");
        }

        return nrcValido && longitudValida && periodoValido && profesorValido;
    }

    private Seccion construirSeccion() {
        String numeroSeccion = campoTextoNumeroSeccion.getText().trim();
        PeriodoUniversitario periodoSeleccionado = comboBoxPeriodo.getValue();
        Profesor profesorSeleccionado = comboBoxProfesor.getValue();
        Seccion seccion = new Seccion();
        seccion.setNoSeccion(numeroSeccion);
        seccion.setIdPeriodo(periodoSeleccionado.getIdPeriodo());
        seccion.setNumPersonalProfesor(profesorSeleccionado.getNumeroDePersonalProfesor());
        return seccion;
    }

    private void guardarSeccion(Seccion seccion) {
        SeccionDao seccionDao = new SeccionDao();
        try {
            int filasAfectadas = seccionDao.agregarSeccion(seccion);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCamposRegistros();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Seccion registrada", "SECCION REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "NO SE PUDO REGISTRAR LA SECCION. INTENTE DE NUEVO.");
            }
        } catch (RegistroDuplicadoExcepcion excepcion) {
            LOGGER.log(Level.WARNING, "NRC duplicado en el mismo periodo", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "NRC duplicado", "ESE NRC YA EXISTE EN EL PERIODO SELECCIONADO");
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar seccion", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void limpiarCamposRegistros() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNumeroSeccion.clear();
        comboBoxPeriodo.setValue(null);
        comboBoxProfesor.setValue(null);
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