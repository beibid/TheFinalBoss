package InterfazGrafica;

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
import logica.dao.objetos.PeriodoUniversitarioDao;
import logica.dominio.PeriodoUniversitario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CerrarPeriodoControlador {

    private static final Logger LOGGER = Logger.getLogger(CerrarPeriodoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private ComboBox<PeriodoUniversitario> comboBoxPeriodo;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @FXML
    public void initialize() {
        cargarPeriodos();
    }

    private void cargarPeriodos() {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        try {
            List<PeriodoUniversitario> periodos = periodoUniversitarioDao.obtenerPeriodosAbiertos();
            comboBoxPeriodo.setItems(FXCollections.observableArrayList(periodos));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar periodos", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PERIODOS");
        }
    }

    @FXML
    private void botonCerrarPeriodo() {
        ocultarError();
        ocultarExito();
        PeriodoUniversitario periodoSeleccionado = comboBoxPeriodo.getValue();
        if (periodoSeleccionado == null) {
            mostrarError("Periodo no seleccionado", "POR FAVOR SELECCIONA UN PERIODO PARA CERRAR");
            return;
        }
        if (confirmarAccion("¿Seguro que desea cerrar el periodo " + periodoSeleccionado.getNombre() + "?")) {
            ejecutarCierre(periodoSeleccionado.getIdPeriodo());
        }
    }

    private void ejecutarCierre(int idPeriodo) {
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        try {
            int filasAfectadas = periodoUniversitarioDao.cerrarPeriodo(idPeriodo);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                comboBoxPeriodo.setValue(null);
                cargarPeriodos();
                mostrarExito("Periodo cerrado", "EL PERIODO FUE CERRADO EXITOSAMENTE");
            } else {
                mostrarError("Error al cerrar", "NO SE PUDO CERRAR EL PERIODO. INTENTE DE NUEVO");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cerrar periodo", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
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