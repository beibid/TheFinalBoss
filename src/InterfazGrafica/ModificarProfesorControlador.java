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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarProfesorControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarProfesorControlador.class.getName());

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

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private Profesor profesorSeleccionado;

    @FXML
    public void initialize() {
        comboBoxTurno.setItems(FXCollections.observableArrayList(Turno.values()));
        cargarProfesores();
    }

    private void cargarProfesores() {
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            List<Profesor> lista = profesorDao.obtenerProfesoresActivos();
            ObservableList<Profesor> profesoresObservable = FXCollections.observableArrayList(lista);
            comboBoxProfesores.setItems(profesoresObservable);
            comboBoxProfesores.setCellFactory(l -> crearCeldaProfesor(false));
            comboBoxProfesores.setButtonCell(crearCeldaProfesor(true));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores", excepcion);
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", excepcion.getMessage());
        }
    }

    private ListCell<Profesor> crearCeldaProfesor(boolean esBoton) {
        return new ListCell<Profesor>() {
            @Override
            protected void updateItem(Profesor profesor, boolean vacio) {
                super.updateItem(profesor, vacio);
                if (vacio || profesor == null) {
                    setText(esBoton ? "-- Selecciona un profesor --" : null);
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
            ocultarPaneles();
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
        if (confirmarAccion("¿Desea guardar los cambios?")) {
            procesarModificacion();
        }
    }

    private void procesarModificacion() {
        if (camposValidos()) {
            ProfesorDao profesorDao = new ProfesorDao();
            try {
                Profesor profesorModificado = construirProfesor();
                int filasAfectadas = profesorDao.modificarProfesor(
                        profesorSeleccionado.getNumeroDePersonalProfesor(), profesorModificado);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxProfesores.setValue(null);
                    cargarProfesores();
                    mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                            "Profesor modificado", "Profesor actualizado exitosamente.");
                } else {
                    mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                            "Error al modificar", "No se pudo modificar el profesor. Intente de nuevo.");
                }
            } catch (UsuariosExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al modificar profesor", excepcion);
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error inesperado", excepcion.getMessage().toUpperCase());
            }
        } else {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacíos", "Verifica la información e intenta de nuevo.");
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

    private boolean camposValidos() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String correo = campoCorreo.getText().trim();

        List<String> campos = List.of(nombre, apellidos, correo);
        boolean camposTextosValidos = !camposVacios(campos);
        boolean turnoValido = comboBoxTurno.getValue() != null;

        if (!camposTextosValidos) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacios", "Verifique la informacion e intente de nuevo.");
        }
        if (!turnoValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Turno no seleccionado", "Seleccione un turno para el profesor.");
        }
        return camposTextosValidos && turnoValido;
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
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
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
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        return alerta.showAndWait().filter(botonPresionado -> botonPresionado == botonSi).isPresent();
    }

    private void ocultarTodo() {
        ocultarPaneles();
        ocultarPanel(panelFormulario);
    }

    private void ocultarPaneles() {
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