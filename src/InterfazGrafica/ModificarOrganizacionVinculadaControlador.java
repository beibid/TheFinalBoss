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
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarOrganizacionVinculadaControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarOrganizacionVinculadaControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<OrganizacionVinculada> comboBoxOrganizacion;
    @FXML private VBox panelFormulario;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private TextField campoNombre;
    @FXML private TextField campoDireccion;
    @FXML private TextField campoTelefono;
    @FXML private TextField campoCorreo;
    @FXML private TextField campoSector;

    private OrganizacionVinculada organizacionSeleccionada;

    @FXML
    public void initialize() {
        cargarOrganizaciones();
    }

    private void cargarOrganizaciones() {
        OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();
        try {
            List<OrganizacionVinculada> lista = organizacionDao.obtenerOrganizacionesActivas();
            ObservableList<OrganizacionVinculada> organizacionesObservable = FXCollections.observableArrayList(lista);
            comboBoxOrganizacion.setItems(organizacionesObservable);
            comboBoxOrganizacion.setCellFactory(listaOrganizaciones -> crearCeldaOrganizacion());
            comboBoxOrganizacion.setButtonCell(crearCeldaOrganizacion());
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar organizaciones", excepcion);
            mostrarError("Error al cargar", excepcion.getMessage());
        }
    }

    private ListCell<OrganizacionVinculada> crearCeldaOrganizacion() {
        return new ListCell<OrganizacionVinculada>() {
            @Override
            protected void updateItem(OrganizacionVinculada organizacion, boolean vacio) {
                super.updateItem(organizacion, vacio);
                if (vacio || organizacion == null) {
                    setText("-- Selecciona una organización --");
                } else {
                    setText(organizacion.getNombre());
                }
            }
        };
    }

    @FXML
    private void seleccionarOrganizacion() {
        organizacionSeleccionada = comboBoxOrganizacion.getValue();
        if (organizacionSeleccionada != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
            rellenarFormulario(organizacionSeleccionada);
        }
    }

    private void rellenarFormulario(OrganizacionVinculada organizacion) {
        campoNombre.setText(organizacion.getNombre());
        campoDireccion.setText(organizacion.getDireccion());
        campoTelefono.setText(organizacion.getTelefono());
        campoCorreo.setText(organizacion.getCorreo());
        campoSector.setText(organizacion.getSector());
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
            OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();
            try {
                OrganizacionVinculada organizacionModificada = construirOrganizacion();
                int filasAfectadas = organizacionDao.modificarOrganizacionVinculada(
                        organizacionSeleccionada.getIdOrganizacion(), organizacionModificada);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxOrganizacion.setValue(null);
                    cargarOrganizaciones();
                    mostrarExito("Organización modificada", "Organización actualizada exitosamente.");
                } else {
                    mostrarError("Error al modificar", "No se pudo modificar la organización. Intente de nuevo.");
                }
            } catch (UsuariosExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al modificar organización", excepcion);
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
        String direccion = campoDireccion.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String correo = campoCorreo.getText().trim();
        String sector = campoSector.getText().trim();

        List<String> campos = List.of(nombre, direccion, telefono, correo, sector);
        boolean camposFormularioValidos = !camposVacios(campos);
        boolean nombreValido = nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$");
        boolean direccionValida = direccion.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s#.,\\-]+$");
        boolean telefonoValido = telefono.matches("\\d{10}");
        boolean correoValido = correo.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
        boolean sectorValido = sector.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

        mostrarPrimerError(camposFormularioValidos, nombreValido, direccionValida, telefonoValido, correoValido, sectorValido);

        boolean formularioValido = camposFormularioValidos && nombreValido && direccionValida && telefonoValido && correoValido && sectorValido;
        return formularioValido;
    }
    private void mostrarPrimerError(boolean camposFormularioValidos, boolean nombreValido, boolean direccionValida,
                                    boolean telefonoValido, boolean correoValido, boolean sectorValido) {
        if (!camposFormularioValidos) {
            mostrarError("Campos obligatorios vacíos", "Por favor llene todos los campos.");
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE NO DEBE CONTENER CARACTERES ESPECIALES");
        } else if (!direccionValida) {
            mostrarError("Direccion invalida", "LA DIRECCION CONTIENE CARACTERES NO PERMITIDOS");
        } else if (!telefonoValido) {
            mostrarError("Telefono invalido", "EL TELEFONO DEBE CONTENER EXACTAMENTE 10 DIGITOS NUMERICOS");
        } else if (!correoValido) {
            mostrarError("Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO");
        } else if (!sectorValido) {
            mostrarError("Sector invalido", "EL SECTOR NO DEBE CONTENER NUMEROS NI CARACTERES ESPECIALES");
        }
    }

    private OrganizacionVinculada construirOrganizacion() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre(campoNombre.getText().trim());
        organizacion.setDireccion(campoDireccion.getText().trim());
        organizacion.setTelefono(campoTelefono.getText().trim());
        organizacion.setCorreo(campoCorreo.getText().trim());
        organizacion.setSector(campoSector.getText().trim());
        return organizacion;
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            ocultarTodo();
            comboBoxOrganizacion.setValue(null);
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
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        ocultarPanel(panelFormulario);
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