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
import logica.dominio.enums.EstadoProyecto;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ModificarProyectoControlGUI{

    @FXML private ComboBox<Proyecto> comboBoxProyectos;
    @FXML private ComboBox<EstadoProyecto> comboBoxEstado;
    @FXML private VBox panelFormulario;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;
    @FXML private TextField campoNombreProyecto;
    @FXML private TextField campoDescripcion;
    @FXML private TextField campoResponsable;
    @FXML private TextField campoNombreEmpresa;
    @FXML private TextField campoSectorEmpresa;
    @FXML private TextField campoDireccionEmpresa;
    @FXML private TextField campoMatricula;
    @FXML private TextField campoNumPersonalCoordinador;
    @FXML private TextField campoFechaRegistro;

    private Proyecto proyectoSeleccionado;

    @FXML
    public void initialize() {
        cargarProyectosDisponibles();
        comboBoxEstado.setItems(FXCollections.observableArrayList(EstadoProyecto.values()));
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
            rellenarFormulario(proyectoSeleccionado);
        }
    }

    private void rellenarFormulario(Proyecto proyecto) {
        campoNombreProyecto.setText(proyecto.getNombreProyecto());
        campoDescripcion.setText(proyecto.getDescripcion());
        campoResponsable.setText(proyecto.getResponsableDelProyecto());
        comboBoxEstado.setValue(proyecto.getEstado());
        campoNombreEmpresa.setText(proyecto.getNombreEmpresa());
        campoSectorEmpresa.setText(proyecto.getSectorEmpresa());
        campoDireccionEmpresa.setText(proyecto.getDireccionEmpresa());
        campoNumPersonalCoordinador.setText(proyecto.getNumPersonalCoordinador());
        campoFechaRegistro.setText(proyecto.getFechaRegistro() != null ?
                proyecto.getFechaRegistro().toString() : "");
        panelFormulario.setVisible(true);
        panelFormulario.setManaged(true);
    }

    @FXML
    private void botonGuardar() {
        if (confirmarAccion("¿Seguro que desea guardar los cambios?")) {
            procesarModificacion();
        }
    }

    private void procesarModificacion() {
        if (!camposValidos()) {
            mostrarError("Campos obligatorios vacíos",
                    "Verifique la información e intente de nuevo.");
            return;
        }
        ProyectoDao proyectoDao = new ProyectoDao();
        try {
            Proyecto proyectoModificado = construirProyecto();
            int filasAfectadas = proyectoDao.modificarProyecto(
                    proyectoSeleccionado.getIdProyecto(), proyectoModificado);
            if (filasAfectadas > 0) {
                ocultarTodo();
                comboBoxProyectos.setValue(null);
                cargarProyectosDisponibles();
                mostrarExito("Proyecto modificado",
                        "El proyecto fue modificado exitosamente.");
            } else {
                mostrarError("Error al modificar",
                        "No se pudo modificar el proyecto. Intente de nuevo.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean camposValidos() {
        return !campoNombreProyecto.getText().trim().isEmpty()
                && !campoDescripcion.getText().trim().isEmpty()
                && !campoResponsable.getText().trim().isEmpty()
                && comboBoxEstado.getValue() != null
                && !campoNombreEmpresa.getText().trim().isEmpty()
                && !campoSectorEmpresa.getText().trim().isEmpty()
                && !campoDireccionEmpresa.getText().trim().isEmpty()
                && !campoMatricula.getText().trim().isEmpty()
                && !campoNumPersonalCoordinador.getText().trim().isEmpty()
                && !campoFechaRegistro.getText().trim().isEmpty();
    }

    private Proyecto construirProyecto() throws MensajeriaExcepcion {
        Proyecto proyectoModificado = new Proyecto();
        proyectoModificado.setNombreProyecto(campoNombreProyecto.getText().trim());
        proyectoModificado.setDescripcion(campoDescripcion.getText().trim());
        proyectoModificado.setResponsableDelProyecto(campoResponsable.getText().trim());
        proyectoModificado.setEstado(comboBoxEstado.getValue());
        proyectoModificado.setNombreEmpresa(campoNombreEmpresa.getText().trim());
        proyectoModificado.setSectorEmpresa(campoSectorEmpresa.getText().trim());
        proyectoModificado.setDireccionEmpresa(campoDireccionEmpresa.getText().trim());
        proyectoModificado.setIdOrganizacion(proyectoSeleccionado.getIdOrganizacion());
        proyectoModificado.setNumPersonalCoordinador(campoNumPersonalCoordinador.getText().trim());
        try {
            proyectoModificado.setFechaRegistro(Date.valueOf(campoFechaRegistro.getText().trim()));
        } catch (IllegalArgumentException excepcion) {
            throw new MensajeriaExcepcion("Formato de fecha inválido. Use YYYY-MM-DD.");
        }
        return proyectoModificado;
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
        panelFormulario.setVisible(false);
        panelFormulario.setManaged(false);
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