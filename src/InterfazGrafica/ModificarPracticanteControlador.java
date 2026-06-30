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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarPracticanteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LONGITUD_MAXIMA_NOMBRE = 55;
    private static final int LONGITUD_MAXIMA_APELLIDOS = 55;
    private static final int LONGITUD_MAXIMA_CORREO = 100;
    private static final int LONGITUD_MAXIMA_LENGUA_INDIGENA = 30;

    private final PracticanteDao practicanteDao = new PracticanteDao();

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
        try {
            List<Practicante> lista = practicanteDao.obtenerPracticantesActivos();
            if (lista.isEmpty()) {
                mostrarError("Sin practicantes", "NO HAY PRACTICANTES ACTIVOS EN EL SISTEMA.");
                comboBoxPracticantes.setDisable(true);
            } else {
                ObservableList<Practicante> practicantesObservable = FXCollections.observableArrayList(lista);
                comboBoxPracticantes.setItems(practicantesObservable);
                comboBoxPracticantes.setCellFactory(listaPracticantes -> crearCeldaPracticante());
                comboBoxPracticantes.setButtonCell(crearCeldaPracticante());
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar practicantes", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
            comboBoxPracticantes.setDisable(true);
        }
    }

    private ListCell<Practicante> crearCeldaPracticante() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Practicante practicante, boolean vacio) {
                super.updateItem(practicante, vacio);
                boolean esVacioONulo = vacio || practicante == null;
                if (esVacioONulo) {
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
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
            rellenarFormulario(practicanteSeleccionado);
        }
    }

    private void rellenarFormulario(Practicante practicante) {
        campoNombre.setText(practicante.getNombre());
        campoApellidos.setText(practicante.getApellidos());
        campoCorreo.setText(practicante.getCorreo());
        boolean tieneLengua = practicante.getLenguaIndigena() != null;
        if (tieneLengua) {
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
        if (confirmarAccion("Desea guardar los cambios?")) {
            procesarModificacion();
        }
    }

    private void procesarModificacion() {
        boolean camposCorrectos = verificarCampos();
        if (camposCorrectos) {
            try {
                Practicante practicanteModificado = construirPracticante();
                int filasAfectadas = practicanteDao.modificarPracticante(
                        practicanteSeleccionado.getMatricula(), practicanteModificado);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxPracticantes.setValue(null);
                    comboBoxPracticantes.setDisable(false);
                    cargarPracticantes();
                    mostrarExito("Practicante modificado", "PRACTICANTE ACTUALIZADO EXITOSAMENTE.");
                } else {
                    mostrarError("Error al modificar", "NO SE PUDO MODIFICAR EL PRACTICANTE. INTENTE DE NUEVO.");
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

    private boolean verificarCampos() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String correo = campoCorreo.getText().trim();
        String lenguaIndigena = campoLenguaIndigena.getText().trim();
        boolean camposFormularioValidos = !camposVacios(List.of(nombre, apellidos, correo));
        boolean valido = true;
        if (!camposFormularioValidos) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!verificarLongitudes(nombre, apellidos, correo, lenguaIndigena)) {
            valido = false;
        } else if (!verificarFormatos(nombre, apellidos, correo)) {
            valido = false;
        } else if (comboBoxGenero.getValue() == null) {
            mostrarError("Genero no seleccionado", "SELECCIONA UN GENERO PARA EL PRACTICANTE.");
            valido = false;
        }
        return valido;
    }

    private boolean verificarLongitudes(String nombre, String apellidos, String correo,
                                        String lenguaIndigena) {
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
        } else if (lenguaIndigena.length() > LONGITUD_MAXIMA_LENGUA_INDIGENA) {
            mostrarError("Lengua indigena demasiado larga", "LA LENGUA INDIGENA NO PUEDE EXCEDER " + LONGITUD_MAXIMA_LENGUA_INDIGENA + " CARACTERES.");
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
        if (confirmarAccion("Seguro que desea cancelar?")) {
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