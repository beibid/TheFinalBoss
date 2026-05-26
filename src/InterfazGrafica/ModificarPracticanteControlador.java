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
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import logica.dominio.enums.Genero;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarPracticanteControlador.class.getName());

    @FXML private ComboBox<Practicante> comboBoxPracticantes;
    @FXML private ComboBox<Genero> comboBoxGenero;
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
    @FXML private TextField campoLenguaIndigena;

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private Practicante practicanteSeleccionado;

    @FXML
    public void initialize() {
        comboBoxGenero.setItems(FXCollections.observableArrayList(Genero.values()));
        cargarPracticantes();
    }

    private void cargarPracticantes() {
        PracticanteDao practicanteDao = new PracticanteDao();
        try {
            List<Practicante> lista = practicanteDao.obtenerPracticantesActivos();
            ObservableList<Practicante> practicantesObservable = FXCollections.observableArrayList(lista);
            comboBoxPracticantes.setItems(practicantesObservable);
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
        practicanteSeleccionado = comboBoxPracticantes.getValue();
        if (practicanteSeleccionado != null) {
            ocultarPaneles();
            rellenarFormulario(practicanteSeleccionado);
        }
    }

    private void rellenarFormulario(Practicante practicante) {
        campoNombre.setText(practicante.getNombre());
        campoApellidos.setText(practicante.getApellidos());
        campoCorreo.setText(practicante.getCorreo());
        if (practicante.getLenguaIndigena() != null) {
            campoLenguaIndigena.setText(practicante.getLenguaIndigena());
        } else {
            campoLenguaIndigena.setText("");
        }
        comboBoxGenero.setValue(practicante.getGenero());
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
        PracticanteDao practicanteDao = new PracticanteDao();
        try {
            Practicante practicanteModificado = construirPracticante();
            int filasAfectadas = practicanteDao.modificarPracticante(
                    practicanteSeleccionado.getMatricula(), practicanteModificado);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                ocultarTodo();
                comboBoxPracticantes.setValue(null);
                cargarPracticantes();
                mostrarExito("Practicante modificado",
                        "Practicante actualizado exitosamente.");
            } else {
                mostrarError("Error al modificar",
                        "No se pudo modificar el practicante. Intente de nuevo.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al modificar practicante", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean camposValidos() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String correo = campoCorreo.getText().trim();
        return !nombre.isEmpty() && !apellidos.isEmpty() && !correo.isEmpty() && comboBoxGenero.getValue() != null;
    }

    private Practicante construirPracticante() {
        Practicante practicante = new Practicante();
        practicante.setNombre(campoNombre.getText().trim());
        practicante.setApellidos(campoApellidos.getText().trim());
        practicante.setCorreo(campoCorreo.getText().trim());
        practicante.setLenguaIndigena(campoLenguaIndigena.getText().trim());
        practicante.setGenero(comboBoxGenero.getValue());
        practicante.setEstado(practicanteSeleccionado.getEstado());
        practicante.setContrasena(practicanteSeleccionado.getContrasena());
        return practicante;
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            ocultarTodo();
            comboBoxPracticantes.setValue(null);
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
        return alerta.showAndWait().filter(r -> r == botonSi).isPresent();
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