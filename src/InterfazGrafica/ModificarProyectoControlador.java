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
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.enums.EstadoProyecto;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarProyectoControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarProyectoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

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
        comboBoxEstado.setItems(FXCollections.observableArrayList(EstadoProyecto.values()));
        cargarProyectosDisponibles();
    }

    private void cargarProyectosDisponibles() {
        ProyectoDao proyectoDao = new ProyectoDao();
        try {
            List<Proyecto> listaProyectos = proyectoDao.obtenerProyectosDisponibles();
            if (listaProyectos.isEmpty()) {
                mostrarError("Sin proyectos", "NO HAY PROYECTOS DISPONIBLES EN EL SISTEMA.");
                comboBoxProyectos.setDisable(true);
            } else {
                ObservableList<Proyecto> proyectosObservable = FXCollections.observableArrayList(listaProyectos);
                comboBoxProyectos.setItems(proyectosObservable);
                comboBoxProyectos.setCellFactory(listaProyectos2 -> crearCeldaProyecto());
                comboBoxProyectos.setButtonCell(crearCeldaProyecto());
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyectos", excepcion);
            mostrarError("Error al cargar proyectos", "NO SE PUDIERON CARGAR LOS PROYECTOS.");
            comboBoxProyectos.setDisable(true);
        }
    }

    private ListCell<Proyecto> crearCeldaProyecto() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Proyecto proyecto, boolean vacio) {
                super.updateItem(proyecto, vacio);
                if (vacio || proyecto == null) {
                    setText("-- Selecciona un proyecto --");
                } else {
                    setText(proyecto.getNombreProyecto());
                }
            }
        };
    }

    @FXML
    private void seleccionarProyecto() {
        proyectoSeleccionado = comboBoxProyectos.getValue();
        if (proyectoSeleccionado != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
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
        campoFechaRegistro.setText(proyecto.getFechaRegistro() != null ? proyecto.getFechaRegistro().toString() : "");
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
        if (camposValidos()) {
            ProyectoDao proyectoDao = new ProyectoDao();
            try {
                Proyecto proyectoModificado = construirProyecto();
                int filasAfectadas = proyectoDao.modificarProyecto(
                        proyectoSeleccionado.getIdProyecto(), proyectoModificado);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    ocultarTodo();
                    comboBoxProyectos.setValue(null);
                    comboBoxProyectos.setDisable(false);
                    cargarProyectosDisponibles();
                    mostrarExito("Proyecto modificado", "EL PROYECTO FUE MODIFICADO EXITOSAMENTE.");
                } else {
                    mostrarError("Error al modificar", "NO SE PUDO MODIFICAR EL PROYECTO. INTENTE DE NUEVO.");
                }
            } catch (MensajeriaExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al modificar proyecto", excepcion);
                mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
            }
        }
    }

    private boolean camposVacios(List<TextField> campos) {
        boolean hayCamposVacios = false;
        for (TextField campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private boolean camposValidos() {
        List<TextField> camposObligatorios = List.of(
                campoNombreProyecto, campoDescripcion, campoResponsable,
                campoNombreEmpresa, campoSectorEmpresa, campoDireccionEmpresa,
                campoMatricula, campoNumPersonalCoordinador, campoFechaRegistro);
        boolean camposTextosValidos = !camposVacios(camposObligatorios);
        boolean estadoValido = comboBoxEstado.getValue() != null;
        boolean fechaValida = esFechaValida(campoFechaRegistro.getText().trim());

        verificarCampos(camposTextosValidos, estadoValido, fechaValida);

        return camposTextosValidos && estadoValido && fechaValida;
    }

    private void verificarCampos(boolean camposTextosValidos, boolean estadoValido, boolean fechaValida) {
        if (!camposTextosValidos) {
            mostrarError("Campos obligatorios vacíos", "POR FAVOR LLENE TODOS LOS CAMPOS.");
        } else if (!estadoValido) {
            mostrarError("Estado no seleccionado", "SELECCIONE UN ESTADO PARA EL PROYECTO.");
        } else if (!fechaValida) {
            mostrarError("Fecha inválida", "EL FORMATO DE FECHA DEBE SER YYYY-MM-DD.");
        }
    }

    private boolean esFechaValida(String fecha) {
        boolean valida = true;
        try {
            Date.valueOf(fecha);
        } catch (IllegalArgumentException excepcion) {
            LOGGER.log(Level.WARNING, "Formato de fecha inválido", excepcion);
            valida = false;
        }
        return valida;
    }

    private Proyecto construirProyecto() {
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
        proyectoModificado.setFechaRegistro(Date.valueOf(campoFechaRegistro.getText().trim()));
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