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
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

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
            comboBoxPracticantes.setCellFactory(listaPracticantes -> crearCeldaPracticante());
            comboBoxPracticantes.setButtonCell(crearCeldaPracticante());
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage());
        }
    }

    private ListCell<Practicante> crearCeldaPracticante() {
        return new ListCell<Practicante>() {
            @Override
            protected void updateItem(Practicante practicante, boolean vacio) {
                super.updateItem(practicante, vacio);
                if (vacio || practicante == null) {
                    setText("-- Selecciona un practicante --");
                } else {
                    setText(practicante.getNombre() + " " + practicante.getApellidos() + " - " + practicante.getMatricula());
                }
            }
        };
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
        if (camposValidos()) {
            PracticanteDao practicanteDao = new PracticanteDao();
            try {
                Practicante practicanteModificado = construirPracticante();
                int filasAfectadas = practicanteDao.modificarPracticante(
                        practicanteSeleccionado.getMatricula(), practicanteModificado);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxPracticantes.setValue(null);
                    cargarPracticantes();
                    mostrarExito("Practicante modificado", "Practicante actualizado exitosamente.");
                } else {
                    mostrarError("Error al modificar", "No se pudo modificar el practicante. Intente de nuevo.");
                }
            } catch (UsuariosExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al modificar practicante", excepcion);
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

    private boolean camposValidos() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String correo = campoCorreo.getText().trim();

        List<String> campos = List.of(nombre, apellidos, correo);
        boolean camposTextosValidos = !camposVacios(campos);
        boolean generoValido = comboBoxGenero.getValue() != null;

        if (!camposTextosValidos) {
            mostrarError("Campos obligatorios vacíos", "Verifica la información e intenta de nuevo.");
        }
        if (!generoValido) {
            mostrarError("Género no seleccionado", "Selecciona un género para el practicante.");
        }
        boolean formularioCompleto = camposTextosValidos && generoValido;
        return formularioCompleto;
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
        panelFormulario.setVisible(false);
        panelFormulario.setManaged(false);
    }

    private void ocultarPaneles() {
        panelError.setVisible(false);
        panelError.setManaged(false);
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }

    private void mostrarPanel(VBox panel, Label etiquetaTitulo, Label etiquetaMensaje,
                              String titulo, String mensaje) {
        etiquetaTitulo.setText(titulo);
        etiquetaMensaje.setText(mensaje);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }

    private void mostrarError(String titulo, String mensaje) {
        ocultarPanel(panelExito);
        mostrarPanel(panelError, etiquetaTituloError, etiquetaMensajeError, titulo, mensaje);
    }

    private void mostrarExito(String titulo, String mensaje) {
        ocultarPanel(panelError);
        mostrarPanel(panelExito, etiquetaTituloExito, etiquetaMensajeExito, titulo, mensaje);
    }
}