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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarOrganizacionVinculadaControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarOrganizacionVinculadaControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LONGITUD_MAXIMA_NOMBRE = 80;
    private static final int LONGITUD_MAXIMA_DIRECCION = 80;
    private static final int LONGITUD_MAXIMA_TELEFONO = 10;
    private static final int LONGITUD_MAXIMA_CORREO = 100;
    private static final int LONGITUD_MAXIMA_SECTOR = 50;

    private final OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();

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
        try {
            List<OrganizacionVinculada> lista = organizacionDao.obtenerOrganizacionesActivas();
            if (lista.isEmpty()) {
                mostrarError("Sin organizaciones", "NO HAY ORGANIZACIONES VINCULADAS ACTIVAS EN EL SISTEMA.");
                comboBoxOrganizacion.setDisable(true);
            } else {
                ObservableList<OrganizacionVinculada> organizacionesObservable = FXCollections.observableArrayList(lista);
                comboBoxOrganizacion.setItems(organizacionesObservable);
                comboBoxOrganizacion.setCellFactory(listaOrganizaciones -> crearCeldaOrganizacion());
                comboBoxOrganizacion.setButtonCell(crearCeldaOrganizacion());
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar organizaciones", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LAS ORGANIZACIONES VINCULADAS.");
            comboBoxOrganizacion.setDisable(true);
        }
    }

    private ListCell<OrganizacionVinculada> crearCeldaOrganizacion() {
        return new ListCell<>() {
            @Override
            protected void updateItem(OrganizacionVinculada organizacion, boolean vacio) {
                super.updateItem(organizacion, vacio);
                boolean esVacioONulo = vacio || organizacion == null;
                if (esVacioONulo) {
                    setText("-- Selecciona una organizacion --");
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
        if (confirmarAccion("Desea guardar los cambios?")) {
            procesarModificacion();
        }
    }

    private void procesarModificacion() {
        boolean camposCorrectos = verificarCampos();
        if (camposCorrectos) {
            try {
                OrganizacionVinculada organizacionModificada = construirOrganizacion();
                int filasAfectadas = organizacionDao.modificarOrganizacionVinculada(
                        organizacionSeleccionada.getIdOrganizacion(), organizacionModificada);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxOrganizacion.setValue(null);
                    comboBoxOrganizacion.setDisable(false);
                    cargarOrganizaciones();
                    mostrarExito("Organizacion modificada", "ORGANIZACION ACTUALIZADA EXITOSAMENTE.");
                } else {
                    mostrarError("Error al modificar", "NO SE PUDO MODIFICAR LA ORGANIZACION. INTENTE DE NUEVO.");
                }
            } catch (UsuariosExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al modificar organizacion", excepcion);
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
        String direccion = campoDireccion.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String correo = campoCorreo.getText().trim();
        String sector = campoSector.getText().trim();
        List<String> campos = List.of(nombre, direccion, telefono, correo, sector);
        boolean camposFormularioValidos = !camposVacios(campos);
        boolean longitudNombreValida = nombre.length() <= LONGITUD_MAXIMA_NOMBRE;
        boolean longitudDireccionValida = direccion.length() <= LONGITUD_MAXIMA_DIRECCION;
        boolean longitudTelefonoValida = telefono.length() <= LONGITUD_MAXIMA_TELEFONO;
        boolean longitudCorreoValida = correo.length() <= LONGITUD_MAXIMA_CORREO;
        boolean longitudSectorValida = sector.length() <= LONGITUD_MAXIMA_SECTOR;
        boolean nombreValido = nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$");
        boolean direccionValida = direccion.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s#.,\\-]+$");
        boolean telefonoValido = telefono.matches("\\d{10}");
        boolean correoValido = correo.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
        boolean sectorValido = sector.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
        boolean valido = true;
        if (!camposFormularioValidos) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!longitudNombreValida) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE NO PUEDE EXCEDER " + LONGITUD_MAXIMA_NOMBRE + " CARACTERES.");
            valido = false;
        } else if (!longitudDireccionValida) {
            mostrarError("Direccion demasiado larga", "LA DIRECCION NO PUEDE EXCEDER " + LONGITUD_MAXIMA_DIRECCION + " CARACTERES.");
            valido = false;
        } else if (!longitudTelefonoValida) {
            mostrarError("Telefono demasiado largo", "EL TELEFONO NO PUEDE EXCEDER " + LONGITUD_MAXIMA_TELEFONO + " CARACTERES.");
            valido = false;
        } else if (!longitudCorreoValida) {
            mostrarError("Correo demasiado largo", "EL CORREO NO PUEDE EXCEDER " + LONGITUD_MAXIMA_CORREO + " CARACTERES.");
            valido = false;
        } else if (!longitudSectorValida) {
            mostrarError("Sector demasiado largo", "EL SECTOR NO PUEDE EXCEDER " + LONGITUD_MAXIMA_SECTOR + " CARACTERES.");
            valido = false;
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE NO DEBE CONTENER CARACTERES ESPECIALES.");
            valido = false;
        } else if (!direccionValida) {
            mostrarError("Direccion invalida", "LA DIRECCION CONTIENE CARACTERES NO PERMITIDOS.");
            valido = false;
        } else if (!telefonoValido) {
            mostrarError("Telefono invalido", "EL TELEFONO DEBE CONTENER EXACTAMENTE 10 DIGITOS NUMERICOS.");
            valido = false;
        } else if (!correoValido) {
            mostrarError("Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO.");
            valido = false;
        } else if (!sectorValido) {
            mostrarError("Sector invalido", "EL SECTOR NO DEBE CONTENER NUMEROS NI CARACTERES ESPECIALES.");
            valido = false;
        }
        return valido;
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
        if (confirmarAccion("Seguro que desea cancelar?")) {
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