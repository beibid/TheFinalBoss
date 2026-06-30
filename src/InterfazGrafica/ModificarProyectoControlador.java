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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.enums.EstadoProyecto;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificarProyectoControlador {

    private static final Logger LOGGER = Logger.getLogger(ModificarProyectoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LONGITUD_MAXIMA_NOMBRE_PROYECTO = 100;
    private static final int LONGITUD_MAXIMA_DESCRIPCION = 150;
    private static final int LONGITUD_MAXIMA_RESPONSABLE = 50;
    private static final int LONGITUD_MAXIMA_NOMBRE_EMPRESA = 80;
    private static final int LONGITUD_MAXIMA_SECTOR_EMPRESA = 80;
    private static final int LONGITUD_MAXIMA_DIRECCION_EMPRESA = 80;

    private final ProyectoDao proyectoDao = new ProyectoDao();

    @FXML private ScrollPane scrollPane;
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
    @FXML private TextField campoNumPersonalCoordinador;
    @FXML private TextField campoFechaRegistro;

    private Proyecto proyectoSeleccionado;

    @FXML
    public void initialize() {
        comboBoxEstado.setItems(FXCollections.observableArrayList(EstadoProyecto.values()));
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        ocultarPanel(panelFormulario);
        cargarProyectosDisponibles();
    }

    private void cargarProyectosDisponibles() {
        try {
            List<Proyecto> listaProyectos = proyectoDao.obtenerProyectosDisponibles();
            if (listaProyectos.isEmpty()) {
                mostrarError("Sin proyectos", "NO HAY PROYECTOS DISPONIBLES EN EL SISTEMA.");
                comboBoxProyectos.setDisable(true);
            } else {
                ObservableList<Proyecto> proyectosObservable = FXCollections.observableArrayList(listaProyectos);
                comboBoxProyectos.setItems(proyectosObservable);
                comboBoxProyectos.setCellFactory(lista -> crearCeldaProyecto());
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
                boolean esVacioONulo = vacio || proyecto == null;
                if (esVacioONulo) {
                    setText("-- Selecciona un proyecto --");
                } else {
                    setText(proyecto.getNombreProyecto());
                }
            }
        };
    }

    @FXML
    private void seleccionarProyecto() {
        Proyecto proyectoResumen = comboBoxProyectos.getValue();
        if (proyectoResumen != null) {
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
            try {
                proyectoSeleccionado = proyectoDao.obtenerProyectoPorId(proyectoResumen.getIdProyecto());
                if (proyectoSeleccionado != null) {
                    rellenarFormulario(proyectoSeleccionado);
                } else {
                    mostrarError("Error", "NO SE ENCONTRO EL PROYECTO SELECCIONADO.");
                }
            } catch (MensajeriaExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al obtener proyecto", excepcion);
                mostrarError("Error al cargar proyecto", excepcion.getMessage().toUpperCase());
            }
        }
    }

    private void rellenarFormulario(Proyecto proyecto) {
        asignarTextoSiNoNulo(campoNombreProyecto, proyecto.getNombreProyecto());
        asignarTextoSiNoNulo(campoDescripcion, proyecto.getDescripcion());
        asignarTextoSiNoNulo(campoResponsable, proyecto.getResponsableDelProyecto());
        asignarTextoSiNoNulo(campoNombreEmpresa, proyecto.getNombreEmpresa());
        asignarTextoSiNoNulo(campoSectorEmpresa, proyecto.getSectorEmpresa());
        asignarTextoSiNoNulo(campoDireccionEmpresa, proyecto.getDireccionEmpresa());
        asignarTextoSiNoNulo(campoNumPersonalCoordinador, proyecto.getNumPersonalCoordinador());
        boolean tieneFecha = proyecto.getFechaRegistro() != null;
        if (tieneFecha) {
            campoFechaRegistro.setText(proyecto.getFechaRegistro().toString());
        } else {
            campoFechaRegistro.setText("");
        }
        comboBoxEstado.setValue(proyecto.getEstado());
        panelFormulario.setVisible(true);
        panelFormulario.setManaged(true);
    }

    private void asignarTextoSiNoNulo(TextField campo, String valor) {
        boolean tieneValor = valor != null;
        if (tieneValor) {
            campo.setText(valor);
        } else {
            campo.setText("");
        }
    }

    @FXML
    private void botonGuardar() {
        if (confirmarAccion("Seguro que desea guardar los cambios?")) {
            procesarModificacion();
        }
    }

    private void procesarModificacion() {
        boolean camposCorrectos = verificarCampos();
        if (camposCorrectos) {
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

    private boolean verificarCampos() {
        String nombreProyecto = campoNombreProyecto.getText().trim();
        String descripcion = campoDescripcion.getText().trim();
        String responsable = campoResponsable.getText().trim();
        String nombreEmpresa = campoNombreEmpresa.getText().trim();
        String sectorEmpresa = campoSectorEmpresa.getText().trim();
        String direccionEmpresa = campoDireccionEmpresa.getText().trim();
        List<TextField> camposObligatorios = List.of(
                campoNombreProyecto, campoDescripcion, campoResponsable,
                campoNombreEmpresa, campoSectorEmpresa, campoDireccionEmpresa,
                campoNumPersonalCoordinador, campoFechaRegistro);
        boolean camposTextosValidos = !camposVacios(camposObligatorios);
        boolean valido = true;
        if (!camposTextosValidos) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!verificarLongitudes(nombreProyecto, descripcion, responsable,
                nombreEmpresa, sectorEmpresa, direccionEmpresa)) {
            valido = false;
        } else if (comboBoxEstado.getValue() == null) {
            mostrarError("Estado no seleccionado", "SELECCIONE UN ESTADO PARA EL PROYECTO.");
            valido = false;
        } else if (!esFechaValida(campoFechaRegistro.getText().trim())) {
            mostrarError("Fecha invalida", "EL FORMATO DE FECHA DEBE SER YYYY-MM-DD.");
            valido = false;
        }
        return valido;
    }

    private boolean verificarLongitudes(String nombreProyecto, String descripcion, String responsable,
                                        String nombreEmpresa, String sectorEmpresa, String direccionEmpresa) {
        boolean valido = true;
        if (nombreProyecto.length() > LONGITUD_MAXIMA_NOMBRE_PROYECTO) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE DEL PROYECTO NO PUEDE EXCEDER " + LONGITUD_MAXIMA_NOMBRE_PROYECTO + " CARACTERES.");
            valido = false;
        } else if (descripcion.length() > LONGITUD_MAXIMA_DESCRIPCION) {
            mostrarError("Descripcion demasiado larga", "LA DESCRIPCION NO PUEDE EXCEDER " + LONGITUD_MAXIMA_DESCRIPCION + " CARACTERES.");
            valido = false;
        } else if (responsable.length() > LONGITUD_MAXIMA_RESPONSABLE) {
            mostrarError("Responsable demasiado largo", "EL RESPONSABLE NO PUEDE EXCEDER " + LONGITUD_MAXIMA_RESPONSABLE + " CARACTERES.");
            valido = false;
        } else if (nombreEmpresa.length() > LONGITUD_MAXIMA_NOMBRE_EMPRESA) {
            mostrarError("Nombre de empresa demasiado largo", "EL NOMBRE DE LA EMPRESA NO PUEDE EXCEDER " + LONGITUD_MAXIMA_NOMBRE_EMPRESA + " CARACTERES.");
            valido = false;
        } else if (sectorEmpresa.length() > LONGITUD_MAXIMA_SECTOR_EMPRESA) {
            mostrarError("Sector demasiado largo", "EL SECTOR NO PUEDE EXCEDER " + LONGITUD_MAXIMA_SECTOR_EMPRESA + " CARACTERES.");
            valido = false;
        } else if (direccionEmpresa.length() > LONGITUD_MAXIMA_DIRECCION_EMPRESA) {
            mostrarError("Direccion demasiado larga", "LA DIRECCION NO PUEDE EXCEDER " + LONGITUD_MAXIMA_DIRECCION_EMPRESA + " CARACTERES.");
            valido = false;
        }
        return valido;
    }

    private boolean esFechaValida(String fecha) {
        boolean valida = true;
        try {
            Date.valueOf(fecha);
        } catch (IllegalArgumentException excepcion) {
            LOGGER.log(Level.WARNING, "Formato de fecha invalido", excepcion);
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
        proyectoModificado.setNumPersonalProfesor(proyectoSeleccionado.getNumPersonalProfesor());
        proyectoModificado.setNumPersonalCoordinador(campoNumPersonalCoordinador.getText().trim());
        proyectoModificado.setFechaRegistro(Date.valueOf(campoFechaRegistro.getText().trim()));
        proyectoModificado.setCapacidad(proyectoSeleccionado.getCapacidad());
        return proyectoModificado;
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
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
        scrollPane.setVvalue(0);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        mostrarPanel(panelExito, panelError);
        scrollPane.setVvalue(0);
    }
}