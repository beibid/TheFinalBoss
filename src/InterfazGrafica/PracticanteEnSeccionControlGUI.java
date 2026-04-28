package InterfazGrafica;


import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.SeccionDao;
import logica.dao.objetos.PracticanteSeccionDao;
import logica.dominio.Practicante;
import logica.dominio.Seccion;
import logica.dominio.PracticanteSeccion;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class PracticanteEnSeccionControlGUI implements Initializable{
    @FXML private ComboBox<Practicante> comboBoxProfesores;
    @FXML private ComboBox<Seccion>     comboBoxMotivo;

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

    private final PracticanteDao        practicanteDao        = new PracticanteDao();
    private final SeccionDao            seccionDao            = new SeccionDao();
    private final PracticanteSeccionDao practicanteSeccionDao = new PracticanteSeccionDao();

    // ─────────────────────────────────────────
    //  INICIALIZACIÓN
    // ─────────────────────────────────────────

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPracticantesActivos();
        cargarSecciones();
    }

    private void cargarPracticantesActivos() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            comboBoxProfesores.setItems(FXCollections.observableArrayList(practicantes));
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarSecciones() {
        try {
            List<Seccion> secciones = seccionDao.obtenerSecciones(); // ajusta si tu método se llama diferente
            comboBoxMotivo.setItems(FXCollections.observableArrayList(secciones));
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LAS SECCIONES.");
        }
    }

    // ─────────────────────────────────────────
    //  EVENTOS
    // ─────────────────────────────────────────

    @FXML
    private void seleccionarProfesor() {
        Practicante practicante = comboBoxProfesores.getSelectionModel().getSelectedItem();
        if (practicante != null) {
            etiquetaNombre.setText(practicante.getNombre());
            etiquetaApellidos.setText(practicante.getApellidos());
            etiquetaNumeroPersonal.setText(practicante.getMatricula());
            panelDatos.setVisible(true);
            panelDatos.setManaged(true);
            ocultarError();
            ocultarExito();
        }
    }

    @FXML
    private void botonInactivar() {  // nombre del método debe coincidir con onAction="#botonInactivar" del FXML
        Practicante practicante = comboBoxProfesores.getSelectionModel().getSelectedItem();
        Seccion     seccion     = comboBoxMotivo.getSelectionModel().getSelectedItem();

        if (practicante == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PRACTICANTE.");
            return;
        }
        if (seccion == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UNA SECCIÓN.");
            return;
        }
        if (!confirmarAccion("¿Seguro que desea asignar a " + practicante.getNombre() + " a la sección " + seccion.getNoSeccion() + "?")) {
            return;
        }

        ejecutarAsignacion(practicante, seccion);
    }

    private void ejecutarAsignacion(Practicante practicante, Seccion seccion) {
        try {
            PracticanteSeccion ps = new PracticanteSeccion();
            ps.setMatricula(practicante.getMatricula());
            ps.setNoSeccion(seccion.getNoSeccion()); // ajusta el getter según tu clase Seccion

            int filasAfectadas = practicanteSeccionDao.agregarPracticanteSeccion(ps);

            if (filasAfectadas > 0) {
                limpiarSeleccion();
                mostrarExito("Asignación exitosa", "EL PRACTICANTE FUE ASIGNADO A LA SECCIÓN EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO REALIZAR LA ASIGNACIÓN.");
            }
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarSeleccion();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // ─────────────────────────────────────────
    //  UTILIDADES
    // ─────────────────────────────────────────

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
        comboBoxProfesores.getSelectionModel().clearSelection();
        comboBoxMotivo.getSelectionModel().clearSelection();
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
        ocultarExito();
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
        ocultarError();
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }

}
