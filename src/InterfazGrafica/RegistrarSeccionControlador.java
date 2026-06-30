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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarSeccionControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarSeccionControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LONGITUD_MAXIMA_NRC = 10;

    private final PeriodoUniversitarioDao periodoDao = new PeriodoUniversitarioDao();
    private final ProfesorDao profesorDao = new ProfesorDao();
    private final SeccionDao seccionDao = new SeccionDao();

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
        try {
            List<PeriodoUniversitario> periodos = periodoDao.obtenerPeriodosAbiertos();
            comboBoxPeriodo.setItems(FXCollections.observableArrayList(periodos));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar periodos", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PERIODOS DISPONIBLES.");
        }
    }

    private void cargarProfesoresActivos() {
        try {
            List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
            comboBoxProfesor.setItems(FXCollections.observableArrayList(profesores));
            comboBoxProfesor.setConverter(new StringConverter<Profesor>() {
                @Override
                public String toString(Profesor profesor) {
                    String nombreCompleto = null;
                    boolean profesorNoNulo = profesor != null;
                    if (profesorNoNulo) {
                        nombreCompleto = profesor.getNombre() + " " + profesor.getApellidos();
                    }
                    return nombreCompleto;
                }
                @Override
                public Profesor fromString(String texto) {
                    Profesor profesorVacio = null;
                    return profesorVacio;
                }
            });
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROFESORES ACTIVOS.");
        }
    }

    @FXML
    private void registrarSeccion() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Seguro que desea registrar esta seccion?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void cancelarRegistro() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
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

    private void procesarRegistro() {
        boolean camposCorrectos = verificarCampos();
        if (camposCorrectos) {
            guardarSeccion(construirSeccion());
        }
    }

    private boolean verificarCampos() {
        String numeroSeccion = campoTextoNumeroSeccion.getText().trim();
        PeriodoUniversitario periodoSeleccionado = comboBoxPeriodo.getValue();
        Profesor profesorSeleccionado = comboBoxProfesor.getValue();
        boolean nrcNoVacio = !numeroSeccion.isEmpty();
        boolean nrcValido = nrcNoVacio && numeroSeccion.matches("[a-zA-Z0-9]+");
        boolean longitudValida = numeroSeccion.length() <= LONGITUD_MAXIMA_NRC;
        boolean periodoValido = periodoSeleccionado != null;
        boolean profesorValido = profesorSeleccionado != null;
        boolean valido = true;
        if (!nrcNoVacio) {
            mostrarError("Campo vacio", "INGRESE EL NRC DE LA SECCION.");
            valido = false;
        } else if (!nrcValido) {
            mostrarError("NRC invalido", "EL NRC SOLO PUEDE CONTENER LETRAS Y NUMEROS.");
            valido = false;
        } else if (!longitudValida) {
            mostrarError("NRC invalido", "EL NRC NO PUEDE EXCEDER " + LONGITUD_MAXIMA_NRC + " CARACTERES.");
            valido = false;
        } else if (!periodoValido) {
            mostrarError("Periodo no seleccionado", "SELECCIONE UN PERIODO PARA LA SECCION.");
            valido = false;
        } else if (!profesorValido) {
            mostrarError("Profesor no seleccionado", "SELECCIONE UN PROFESOR PARA LA SECCION.");
            valido = false;
        }
        return valido;
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
        try {
            int filasAfectadas = seccionDao.agregarSeccion(seccion);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCamposRegistros();
                mostrarExito("Seccion registrada", "SECCION REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR LA SECCION. INTENTE DE NUEVO.");
            }
        } catch (RegistroDuplicadoExcepcion excepcion) {
            LOGGER.log(Level.WARNING, "NRC duplicado en el mismo periodo", excepcion);
            mostrarError("NRC duplicado", "ESE NRC YA EXISTE EN EL PERIODO SELECCIONADO.");
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar seccion", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void limpiarCamposRegistros() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNumeroSeccion.clear();
        comboBoxPeriodo.setValue(null);
        comboBoxProfesor.setValue(null);
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