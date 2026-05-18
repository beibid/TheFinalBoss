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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;

import java.util.List;


public class InactivarOrganizacionVinculadaControlGUI {
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

    @FXML
    public void initialize() {
        cargarOrganizacionesActivas();
    }

    private void cargarOrganizacionesActivas() {
        try {
            List<OrganizacionVinculada> organizaciones = organizacionDao.obtenerOrganizacionesActivas();
            ObservableList<OrganizacionVinculada> listaOrganizaciones = FXCollections.observableArrayList(organizaciones);
            comboBoxOrganizacion.setItems(listaOrganizaciones);
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LAS ORGANIZACIONES VINCULADAS.");
        }
    }

    @FXML
    private void seleccionarOrganizacion() {
        OrganizacionVinculada organizacion = comboBoxOrganizacion.getSelectionModel().getSelectedItem();
        if (organizacion != null) {
            etiquetaNombre.setText(organizacion.getNombre());
            etiquetaIdentificadorOrganizacion.setText(String.valueOf(organizacion.getIdOrganizacion()));
            panelDatos.setVisible(true);
            panelDatos.setManaged(true);
        }
    }

    @FXML
    private void botonInactivar() {
        OrganizacionVinculada organizacion = comboBoxOrganizacion.getSelectionModel().getSelectedItem();
        if (organizacion == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PRACTICANTE.");
            return;
        }
        if (!confirmarAccion("¿Seguro que desea inactivar a este practicante?")) {
            return;
        }
        ejecutarInactivacion(organizacion);
    }

    private void ejecutarInactivacion(OrganizacionVinculada organizacionInactiva) {
        try {
            int filasAfectadas = organizacionDao.inactivarOrganizacionVinculada(organizacionInactiva.getIdOrganizacion());
            if (filasAfectadas > 0) {
                limpiarSeleccion();
                mostrarExito("Organizacion inactivada", "LA ORGANIZACION FUE INACTIVADA EXITOSAMENTE.");
                cargarOrganizacionesActivas();
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR A LA ORGANIZACION");
            }
        } catch (MensajeriaExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")){
            limpiarSeleccion();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);
        return alerta.showAndWait().filter(r -> r == btnSi).isPresent();
    }

    private void limpiarSeleccion() {
        comboBoxOrganizacion.getSelectionModel().clearSelection();
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
        ocultarError();
        ocultarExito();
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }

}
