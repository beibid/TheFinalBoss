package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import java.util.List;


public class InactivarProyectoControlGUI{

    @FXML private ComboBox<Proyecto> comboBoxProyectos;
    @FXML private VBox panelDatos;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private Label etiquetaNombreProyecto;
    @FXML private Label etiquetaIdProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private Label etiquetaEstado;

    private Proyecto proyectoSeleccionado;

    @FXML
    public void initialize() {
        cargarProyectosDisponibles();
    }

    private void cargarProyectosDisponibles() {
        ProyectoDao proyectoDao = new ProyectoDao();
        try {
            List<Proyecto> listaProyectos = proyectoDao.obtenerProyectosDisponibles();
            ObservableList<Proyecto> proyectosObservable = FXCollections.observableArrayList(listaProyectos);
            comboBoxProyectos.setItems(proyectosObservable);
            comboBoxProyectos.setCellFactory(lista -> new ListCell<Proyecto>() {
                @Override
                protected void updateItem(Proyecto proyecto, boolean vacio) {
                    super.updateItem(proyecto, vacio);
                    if (vacio || proyecto == null) {
                        setText(null);
                    } else {
                        setText(proyecto.getNombreProyecto());
                    }
                }
            });
            comboBoxProyectos.setButtonCell(new ListCell<Proyecto>() {
                @Override
                protected void updateItem(Proyecto proyecto, boolean vacio) {
                    super.updateItem(proyecto, vacio);
                    if (vacio || proyecto == null) {
                        setText("-- Selecciona un proyecto --");
                    } else {
                        setText(proyecto.getNombreProyecto());
                    }
                }
            });
        } catch (MensajeriaExcepcion excepcion) {
            mostrarError("Error al cargar proyectos", excepcion.getMessage());
        }
    }

    @FXML
    private void seleccionarProyecto() {
        proyectoSeleccionado = comboBoxProyectos.getValue();
        if (proyectoSeleccionado != null) {
            ocultarPaneles();
            mostrarDatosProyecto(proyectoSeleccionado);
        }
    }

    private void mostrarDatosProyecto(Proyecto proyecto) {
        etiquetaNombreProyecto.setText(proyecto.getNombreProyecto());
        etiquetaIdProyecto.setText(String.valueOf(proyecto.getIdProyecto()));
        etiquetaOrganizacion.setText(proyecto.getNombreEmpresa());
        etiquetaEstado.setText(proyecto.getEstado().toString().replace("_", " "));
        panelDatos.setVisible(true);
        panelDatos.setManaged(true);
    }

    @FXML
    private void botonInactivar() {
        if (proyectoSeleccionado == null) {
            mostrarError("Sin selección", "Selecciona un proyecto de la lista.");
        } else if (confirmarAccion("¿Seguro que desea inactivar este proyecto?")) {
            procesarInactivacion();
        }
    }

    private void procesarInactivacion() {
        ProyectoDao proyectoDao = new ProyectoDao();
        try {
            int filasAfectadas = proyectoDao.inactivarProyecto(proyectoSeleccionado.getIdProyecto());
            if (filasAfectadas > 0) {
                ocultarTodo();
                comboBoxProyectos.setValue(null);
                cargarProyectosDisponibles();
                mostrarExito("Proyecto inactivado",
                        "El proyecto fue inactivado exitosamente.");
            } else {
                mostrarError("Error al inactivar",
                        "No se pudo inactivar el proyecto. Intente de nuevo.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            ocultarTodo();
            comboBoxProyectos.setValue(null);
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) throws Exception {
        Parent vistaAnterior = FXMLLoader.load(getClass().getResource(
                "/InterfazGrafica/vistas/SeccionCoordinadorVista.fxml"));
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.setScene(new Scene(vistaAnterior));
        escenario.show();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        return alerta.showAndWait().filter(respuesta -> respuesta == botonSi).isPresent();
    }

    private void ocultarTodo() {
        ocultarPaneles();
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
    }

    private void ocultarPaneles() {
        panelError.setVisible(false);
        panelError.setManaged(false);
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }
}