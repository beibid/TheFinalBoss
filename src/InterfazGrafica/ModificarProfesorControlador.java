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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import logica.dominio.enums.Turno;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarProfesorControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarProfesorControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LONGITUD_MAXIMA_NOMBRE = 55;
    private static final int LONGITUD_MAXIMA_APELLIDOS = 55;
    private static final int LONGITUD_MAXIMA_CORREO = 100;

    private final ProfesorDao profesorDao = new ProfesorDao();

    @FXML private ComboBox<Profesor> comboBoxProfesores;
    @FXML private ComboBox<Turno> comboBoxTurno;
    @FXML private VBox panelFormulario;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private TextField campoNombre;
    @FXML private TextField campoApellidos;
    @FXML private TextField campoCorreo;

    private Profesor profesorSeleccionado;

    @FXML
    public void initialize() {
        comboBoxTurno.setItems(FXCollections.observableArrayList(Turno.values()));
        cargarProfesores();
    }

    private void cargarProfesores() {
        try {
            List<Profesor> lista = profesorDao.obtenerProfesoresActivos();
            if (lista.isEmpty()) {
                mostrarError("Sin profesores", "NO HAY PROFESORES ACTIVOS EN EL SISTEMA.");
                comboBoxProfesores.setDisable(true);
            } else {
                ObservableList<Profesor> profesoresObservable = FXCollections.observableArrayList(lista);
                comboBoxProfesores.setItems(profesoresObservable);
                comboBoxProfesores.setCellFactory(listaProfesores -> crearCeldaProfesor());
                comboBoxProfesores.setButtonCell(crearCeldaProfesor());
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROFESORES.");
            comboBoxProfesores.setDisable(true);
        }
    }

    private ListCell<Profesor> crearCeldaProfesor() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Profesor profesor, boolean vacio) {
                super.updateItem(profesor, vacio);
                boolean esVacioONulo = vacio || profesor == null;
                if (esVacioONulo) {
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
            rellenarFormulario(profesorSeleccionado);
        }
    }

    private void rellenarFormulario(Profesor profesor) {
        campoNombre.setText(profesor.getNombre());
        campoApellidos.setText(profesor.getApellidos());
        campoCorreo.setText(profesor.getCorreo());
        comboBoxTurno.setValue(profesor.getTurno());
        panelFormulario.setVisible(true);
        panelFormulario.setManaged(true);
    }

    @FXML
    private void botonGuardar() {
        if (confirmarAccion("Desea guardar los cambios?")) {
            procesarModificacion();
        }
    }

    private void procesarModificacion() {
        boolean camposCorrectos = verificarCampos();
        if (camposCorrectos) {
            try {
                Profesor profesorModificado = construirProfesor();
                int filasAfectadas = profesorDao.modificarProfesor(
                        profesorSeleccionado.getNumeroDePersonalProfesor(), profesorModificado);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxProfesores.setValue(null);
                    comboBoxProfesores.setDisable(false);
                    cargarProfesores();
                    mostrarExito("Profesor modificado", "PROFESOR ACTUALIZADO EXITOSAMENTE.");
                } else {
                    mostrarError("Error al modificar", "NO SE PUDO MODIFICAR EL PROFESOR. INTENTE DE NUEVO.");
                }
            } catch (UsuariosExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al modificar profesor", excepcion);
                mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
            }
        }
    }

    private boolean camposVacios(List<String> campos) {
        boolean hayCamposVacios = false;
        for (String campo : campos) {
            if (campo.isEmpty()) {
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private boolean verificarCampos() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String correo = campoCorreo.getText().trim();
        boolean camposFormularioValidos = !camposVacios(List.of(nombre, apellidos, correo));
        boolean valido = true;
        if (!camposFormularioValidos) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!verificarLongitudes(nombre, apellidos, correo)) {
            valido = false;
        } else if (!verificarFormatos(nombre, apellidos, correo)) {
            valido = false;
        } else if (comboBoxTurno.getValue() == null) {
            mostrarError("Turno no seleccionado", "SELECCIONE UN TURNO PARA EL PROFESOR.");
            valido = false;
        }
        return valido;
    }

    private boolean verificarLongitudes(String nombre, String apellidos, String correo) {
        boolean valido = true;
        if (nombre.length() > LONGITUD_MAXIMA_NOMBRE) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE NO PUEDE EXCEDER " + LONGITUD_MAXIMA_NOMBRE + " CARACTERES.");
            valido = false;
        } else if (apellidos.length() > LONGITUD_MAXIMA_APELLIDOS) {
            mostrarError("Apellidos demasiado largos", "LOS APELLIDOS NO PUEDEN EXCEDER " + LONGITUD_MAXIMA_APELLIDOS + " CARACTERES.");
            valido = false;
        } else if (correo.length() > LONGITUD_MAXIMA_CORREO) {
            mostrarError("Correo demasiado largo", "EL CORREO NO PUEDE EXCEDER " + LONGITUD_MAXIMA_CORREO + " CARACTERES.");
            valido = false;
        }
        return valido;
    }

    private boolean verificarFormatos(String nombre, String apellidos, String correo) {
        boolean valido = true;
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            mostrarError("Nombre invalido", "EL NOMBRE SOLO PUEDE CONTENER LETRAS.");
            valido = false;
        } else if (!apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            mostrarError("Apellidos invalidos", "LOS APELLIDOS SOLO PUEDEN CONTENER LETRAS.");
            valido = false;
        } else if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mostrarError("Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO.");
            valido = false;
        }
        return valido;
    }

    private Profesor construirProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre(campoNombre.getText().trim());
        profesor.setApellidos(campoApellidos.getText().trim());
        profesor.setCorreo(campoCorreo.getText().trim());
        profesor.setTurno(comboBoxTurno.getValue());
        profesor.setEstado(profesorSeleccionado.getEstado());
        profesor.setContrasena(profesorSeleccionado.getContrasena());
        return profesor;
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
            ocultarTodo();
            comboBoxProfesores.setValue(null);
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
        ocultarPanel(panelFormulario);
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