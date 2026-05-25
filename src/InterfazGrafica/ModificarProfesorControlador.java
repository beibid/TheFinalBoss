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
            comboBoxProfesores.setCellFactory(l -> new ListCell<Profesor>() {
                @Override
                protected void updateItem(Profesor profesor, boolean vacio) {
                    super.updateItem(profesor, vacio);
                    if (vacio || profesor == null) {
                        setText(null);
                    } else {
                        setText(profesor.getNombre() + " " + profesor.getApellidos());
                    }
                }
            });
            comboBoxProfesores.setButtonCell(new ListCell<Profesor>() {
                @Override
                protected void updateItem(Profesor profesor, boolean vacio) {
                    super.updateItem(profesor, vacio);
                    if (vacio || profesor == null) {
                        setText("-- Selecciona un profesor --");
                    } else {
                        setText(profesor.getNombre() + " " + profesor.getApellidos());
                    }
                }
            });
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage());
        }
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
        if (!camposValidos()) {
            mostrarError("Campos obligatorios vacíos",
                    "Verifica la información e intenta de nuevo.");
            return;
        }
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            Profesor profesorModificado = construirProfesor();
            int filasAfectadas = profesorDao.modificarProfesor(
                    profesorSeleccionado.getNumeroDePersonalProfesor(), profesorModificado);
            if (filasAfectadas > 0) {
                ocultarTodo();
                comboBoxProfesores.setValue(null);
                cargarProfesores();
                mostrarExito("Profesor modificado",
                        "Profesor actualizado exitosamente.");
            } else {
                mostrarError("Error al modificar",
                        "No se pudo modificar el profesor. Intente de nuevo.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al modificar profesor", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean camposValidos() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String correo = campoCorreo.getText().trim();
        return !nombre.isEmpty() && !apellidos.isEmpty() && !correo.isEmpty() && comboBoxTurno.getValue() != null;
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
    private void botonRegresar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        panelFormulario.setVisible(false);
        panelFormulario.setManaged(false);
    }

    private void ocultarPaneles() {
        panelError.setVisible(false);
        panelError.setManaged(false);
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }
}
