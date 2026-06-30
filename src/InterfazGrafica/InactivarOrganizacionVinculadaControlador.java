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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InactivarOrganizacionVinculadaControlador {

    private static final Logger LOGGER = Logger.getLogger(InactivarOrganizacionVinculadaControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    private final OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();

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

    private OrganizacionVinculada organizacionSeleccionada;

    @FXML
    public void initialize() {
        cargarOrganizacionesActivas();
    }

    private void cargarOrganizacionesActivas() {
        try {
            List<OrganizacionVinculada> organizaciones = organizacionDao.obtenerOrganizacionesActivas();
            if (organizaciones.isEmpty()) {
                mostrarError("Sin organizaciones", "NO HAY ORGANIZACIONES VINCULADAS ACTIVAS EN EL SISTEMA.");
                comboBoxOrganizacion.setDisable(true);
            } else {
                ObservableList<OrganizacionVinculada> organizacionesObservable = FXCollections.observableArrayList(organizaciones);
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
            mostrarError("Sin seleccion", "POR FAVOR SELECCIONA UNA ORGANIZACION.");
        } else if (confirmarAccion("Seguro que desea inactivar esta organizacion?")) {
            ejecutarInactivacion(organizacionSeleccionada);
        }
    }

    private void ejecutarInactivacion(OrganizacionVinculada organizacion) {
        try {
            int filasAfectadas = organizacionDao.inactivarOrganizacionVinculada(organizacion.getIdOrganizacion());
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                comboBoxOrganizacion.setDisable(false);
                cargarOrganizacionesActivas();
                mostrarExito("Organizacion inactivada", "LA ORGANIZACION FUE INACTIVADA EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR LA ORGANIZACION.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al inactivar organizacion", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
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

    private void limpiar() {
        comboBoxOrganizacion.setValue(null);
        organizacionSeleccionada = null;
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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