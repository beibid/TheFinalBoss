package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InactivarProfesorControlGUI implements Initializable {

    @FXML private ComboBox<Profesor> comboBoxProfesores;
    @FXML private VBox panelDatos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private Label etiquetaNombre;
    @FXML private Label etiquetaApellidos;
    @FXML private Label etiquetaNumeroPersonal;

    private ProfesorDao profesorDao = new ProfesorDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarProfesoresActivos();
    }

    private void cargarProfesoresActivos() {
        try {
            List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
            ObservableList<Profesor> lista = FXCollections.observableArrayList(profesores);
            comboBoxProfesores.setItems(lista);
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROFESORES.");
        }
    }

    @FXML
    private void seleccionarProfesor() {
        Profesor profesor = comboBoxProfesores.getSelectionModel().getSelectedItem();
        if (profesor != null) {
            etiquetaNombre.setText(profesor.getNombre());
            etiquetaApellidos.setText(profesor.getApellidos());
            etiquetaNumeroPersonal.setText(profesor.getNumeroDePersonalProfesor());
            panelDatos.setVisible(true);
            panelDatos.setManaged(true);
        }
    }

    @FXML
    private void botonInactivar() {
        Profesor profesor = comboBoxProfesores.getSelectionModel().getSelectedItem();
        if (profesor == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PROFESOR.");
            return;
    }
        if (!confirmarAccion("¿Seguro que desea inactivar a este profesor?")){
            return;
        }
        ejecutarInactivacion(profesor);
    }

    private void ejecutarInactivacion(Profesor profesor){
        try {
            int filasAfectadas = profesorDao.inactivarProfesor(profesor.getNumeroDePersonalProfesor());
            if (filasAfectadas > 0) {
                limpiar();
                mostrarExito("Profesor inactivado", "EL PROFESOR FUE INACTIVADO EXITOSAMENTE.");
                cargarProfesoresActivos();
            } else {
                mostrarError("Error", "NO SE PUDO INACTIVAR AL PROFESOR.");
            }
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }


    }

    @FXML
    private void botonCancelar() {
        limpiar();
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



    private void limpiar() {
        comboBoxProfesores.getSelectionModel().clearSelection();
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