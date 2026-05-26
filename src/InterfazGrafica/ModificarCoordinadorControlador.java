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
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarCoordinadorControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarCoordinadorControlador.class.getName());

    @FXML private ComboBox<Coordinador> comboBoxCoordinadores;
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

    private Coordinador coordinadorSeleccionado;
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML
    public void initialize() {
        cargarCoordinadores();
    }

    private void cargarCoordinadores() {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        try {
            List<Coordinador> lista = coordinadorDao.obtenerCoordinadoresActivos();
            ObservableList<Coordinador> coordinadoresObservable = FXCollections.observableArrayList(lista);
            comboBoxCoordinadores.setItems(coordinadoresObservable);
            comboBoxCoordinadores.setCellFactory(l -> new ListCell<Coordinador>() {
                @Override
                protected void updateItem(Coordinador coordinador, boolean vacio) {
                    super.updateItem(coordinador, vacio);
                    if (vacio || coordinador == null) {
                        setText(null);
                    } else {
                        setText(coordinador.getNombre() + " " + coordinador.getApellidos());
                    }
                }
            });
            comboBoxCoordinadores.setButtonCell(new ListCell<Coordinador>() {
                @Override
                protected void updateItem(Coordinador coordinador, boolean vacio) {
                    super.updateItem(coordinador, vacio);
                    if (vacio || coordinador == null) {
                        setText("-- Selecciona un coordinador --");
                    } else {
                        setText(coordinador.getNombre() + " " + coordinador.getApellidos());
                    }
                }
            });
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar coordinadores", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage());
        }
    }

    @FXML
    private void seleccionarCoordinador() {
        coordinadorSeleccionado = comboBoxCoordinadores.getValue();
        if (coordinadorSeleccionado != null) {
            ocultarPaneles();
            rellenarFormulario(coordinadorSeleccionado);
        }
    }

    private void rellenarFormulario(Coordinador coordinador) {
        campoNombre.setText(coordinador.getNombre());
        campoApellidos.setText(coordinador.getApellidos());
        if (coordinador.getCorreo() != null) {
            campoCorreo.setText(coordinador.getCorreo());
        } else {
            campoCorreo.setText("");
        }
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
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        try {
            Coordinador coordinadorModificado = construirCoordinador();
            int filasAfectadas = coordinadorDao.modificarCoordinador(
                    coordinadorSeleccionado.getNumeroDePersonalCoordinador(), coordinadorModificado);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                ocultarTodo();
                comboBoxCoordinadores.setValue(null);
                cargarCoordinadores();
                mostrarExito("Coordinador modificado",
                        "Coordinador actualizado exitosamente.");
            } else {
                mostrarError("Error al modificar",
                        "No se pudo modificar el coordinador. Intente de nuevo.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al modificar coordinador", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean camposValidos() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String correo = campoCorreo.getText().trim();
        return !nombre.isEmpty() && !apellidos.isEmpty() && !correo.isEmpty();
    }

    private Coordinador construirCoordinador() {
        Coordinador coordinador = new Coordinador();
        coordinador.setNombre(campoNombre.getText().trim());
        coordinador.setApellidos(campoApellidos.getText().trim());
        coordinador.setCorreo(campoCorreo.getText().trim());
        coordinador.setEstado(coordinadorSeleccionado.getEstado());
        coordinador.setContrasena(coordinadorSeleccionado.getContrasena());
        return coordinador;
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            ocultarTodo();
            comboBoxCoordinadores.setValue(null);
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