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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InactivarOrganizacionVinculadaControlador {

    private static final Logger LOGGER = Logger.getLogger(InactivarOrganizacionVinculadaControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<OrganizacionVinculada> comboBoxOrganizacion;
    @FXML private VBox panelDatos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private Label etiquetaNombre;
    @FXML private Label etiquetaIdentificadorOrganizacion;

    private OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();
    private OrganizacionVinculada organizacionSeleccionada;

    @FXML
    public void initialize() {
        cargarOrganizacionesActivas();
    }

    private void cargarOrganizacionesActivas() {
        try {
            List<OrganizacionVinculada> organizaciones = organizacionDao.obtenerOrganizacionesActivas();
            ObservableList<OrganizacionVinculada> organizacionesObservable = FXCollections.observableArrayList(organizaciones);
            comboBoxOrganizacion.setItems(organizacionesObservable);
            comboBoxOrganizacion.setCellFactory(listaOrganizaciones -> crearCeldaOrganizacion());
            comboBoxOrganizacion.setButtonCell(crearCeldaOrganizacion());
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar organizaciones", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LAS ORGANIZACIONES VINCULADAS.");
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
            ocultarPaneles();
            mostrarDatosOrganizacion(organizacionSeleccionada);
        }
    }

    private void mostrarDatosOrganizacion(OrganizacionVinculada organizacion) {
        etiquetaNombre.setText(organizacion.getNombre());
        etiquetaIdentificadorOrganizacion.setText(String.valueOf(organizacion.getIdOrganizacion()));
        panelDatos.setVisible(true);
        panelDatos.setManaged(true);
    }

    @FXML
    private void botonInactivar() {
        if (organizacionSeleccionada == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UNA ORGANIZACIÓN.");
        } else if (confirmarAccion("¿Seguro que desea inactivar esta organización?")) {
            ejecutarInactivacion(organizacionSeleccionada);
        }
    }

    private void ejecutarInactivacion(OrganizacionVinculada organizacion) {
        try {
            int filasAfectadas = organizacionDao.inactivarOrganizacionVinculada(organizacion.getIdOrganizacion());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                cargarOrganizacionesActivas();
                mostrarExito("Organización inactivada", "LA ORGANIZACIÓN FUE INACTIVADA EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR LA ORGANIZACIÓN.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al inactivar organización", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiar();
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

    private void limpiar() {
        comboBoxOrganizacion.setValue(null);
        organizacionSeleccionada = null;
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
    }

    private void ocultarPaneles() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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