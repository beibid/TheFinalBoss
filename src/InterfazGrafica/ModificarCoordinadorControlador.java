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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarCoordinadorControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarCoordinadorControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LONGITUD_MAXIMA_NOMBRE = 55;
    private static final int LONGITUD_MAXIMA_APELLIDOS = 55;
    private static final int LONGITUD_MAXIMA_CORREO = 100;

    private final CoordinadorDao coordinadorDao = new CoordinadorDao();

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

    @FXML
    public void initialize() {
        cargarCoordinadores();
    }

    private void cargarCoordinadores() {
        try {
            List<Coordinador> lista = coordinadorDao.obtenerCoordinadoresActivos();
            if (lista.isEmpty()) {
                mostrarError("Sin coordinadores", "NO HAY COORDINADORES ACTIVOS EN EL SISTEMA.");
                comboBoxCoordinadores.setDisable(true);
            } else {
                ObservableList<Coordinador> coordinadoresObservable = FXCollections.observableArrayList(lista);
                comboBoxCoordinadores.setItems(coordinadoresObservable);
                comboBoxCoordinadores.setCellFactory(listaCoordinadores -> crearCeldaCoordinador());
                comboBoxCoordinadores.setButtonCell(crearCeldaCoordinador());
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar coordinadores", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS COORDINADORES.");
            comboBoxCoordinadores.setDisable(true);
        }
    }

    private ListCell<Coordinador> crearCeldaCoordinador() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Coordinador coordinador, boolean vacio) {
                super.updateItem(coordinador, vacio);
                boolean esVacioONulo = vacio || coordinador == null;
                if (esVacioONulo) {
                    setText("-- Selecciona un coordinador --");
                } else {
                    setText(coordinador.getNombre() + " " + coordinador.getApellidos());
                }
            }
        };
    }

    @FXML
    private void seleccionarCoordinador() {
        coordinadorSeleccionado = comboBoxCoordinadores.getValue();
        if (coordinadorSeleccionado != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
            rellenarFormulario(coordinadorSeleccionado);
        }
    }

    private void rellenarFormulario(Coordinador coordinador) {
        campoNombre.setText(coordinador.getNombre());
        campoApellidos.setText(coordinador.getApellidos());
        boolean tieneCorreo = coordinador.getCorreo() != null;
        if (tieneCorreo) {
            campoCorreo.setText(coordinador.getCorreo());
        } else {
            campoCorreo.setText("");
        }
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
                Coordinador coordinadorModificado = construirCoordinador();
                int filasAfectadas = coordinadorDao.modificarCoordinador(
                        coordinadorSeleccionado.getNumeroDePersonalCoordinador(), coordinadorModificado);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxCoordinadores.setValue(null);
                    comboBoxCoordinadores.setDisable(false);
                    cargarCoordinadores();
                    mostrarExito("Coordinador modificado", "COORDINADOR ACTUALIZADO EXITOSAMENTE.");
                } else {
                    mostrarError("Error al modificar", "NO SE PUDO MODIFICAR EL COORDINADOR. INTENTE DE NUEVO.");
                }
            } catch (UsuariosExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al modificar coordinador", excepcion);
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
        List<String> campos = List.of(nombre, apellidos, correo);
        boolean camposFormularioValidos = !camposVacios(campos);
        boolean nombreValido = nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean apellidosValidos = apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean correoValido = correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        boolean longitudNombreValida = nombre.length() <= LONGITUD_MAXIMA_NOMBRE;
        boolean longitudApellidosValida = apellidos.length() <= LONGITUD_MAXIMA_APELLIDOS;
        boolean longitudCorreoValida = correo.length() <= LONGITUD_MAXIMA_CORREO;
        boolean valido = true;
        if (!camposFormularioValidos) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!longitudNombreValida) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE NO PUEDE EXCEDER " + LONGITUD_MAXIMA_NOMBRE + " CARACTERES.");
            valido = false;
        } else if (!longitudApellidosValida) {
            mostrarError("Apellidos demasiado largos", "LOS APELLIDOS NO PUEDEN EXCEDER " + LONGITUD_MAXIMA_APELLIDOS + " CARACTERES.");
            valido = false;
        } else if (!longitudCorreoValida) {
            mostrarError("Correo demasiado largo", "EL CORREO NO PUEDE EXCEDER " + LONGITUD_MAXIMA_CORREO + " CARACTERES.");
            valido = false;
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE SOLO PUEDE CONTENER LETRAS.");
            valido = false;
        } else if (!apellidosValidos) {
            mostrarError("Apellidos invalidos", "LOS APELLIDOS SOLO PUEDEN CONTENER LETRAS.");
            valido = false;
        } else if (!correoValido) {
            mostrarError("Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO.");
            valido = false;
        }
        return valido;
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
        if (confirmarAccion("Seguro que desea cancelar?")) {
            ocultarTodo();
            comboBoxCoordinadores.setValue(null);
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