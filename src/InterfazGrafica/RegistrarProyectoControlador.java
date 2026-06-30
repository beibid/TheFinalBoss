package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dao.objetos.ProfesorDao;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Coordinador;
import logica.dominio.OrganizacionVinculada;
import logica.dominio.Proyecto;
import logica.dominio.Profesor;
import logica.dominio.enums.EstadoProyecto;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarProyectoControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarProyectoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LIMITE_NOMBRE_PROYECTO = 100;
    private static final int LIMITE_DESCRIPCION = 255;
    private static final int LIMITE_RESPONSABLE = 100;
    private static final int LIMITE_NOMBRE_EMPRESA = 100;
    private static final int LIMITE_SECTOR_EMPRESA = 50;
    private static final int LIMITE_DIRECCION_EMPRESA = 150;

    private final ProyectoDao proyectoDao = new ProyectoDao();
    private final ProfesorDao profesorDao = new ProfesorDao();
    private final CoordinadorDao coordinadorDao = new CoordinadorDao();
    private final OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();

    @FXML private TextField campoTextoNombreProyecto;
    @FXML private TextField campoTextoDescripcion;
    @FXML private TextField campoTextoResponsable;
    @FXML private TextField campoTextoNombreEmpresa;
    @FXML private TextField campoTextoSectorEmpresa;
    @FXML private TextField campoTextoDireccionEmpresa;
    @FXML private ComboBox<Profesor> comboBoxProfesor;
    @FXML private ComboBox<Coordinador> comboBoxCoordinador;
    @FXML private ComboBox<OrganizacionVinculada> comboBoxOrganizacion;
    @FXML private Spinner<Integer> spinnerCapacidad;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @FXML
    public void initialize() {
        cargarCombos();
    }

    private void cargarCombos() {
        cargarProfesores();
        cargarCoordinadores();
        cargarOrganizaciones();
    }

    private void cargarProfesores() {
        try {
            List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
            comboBoxProfesor.setItems(FXCollections.observableArrayList(profesores));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROFESORES.");
        }
    }

    private void cargarCoordinadores() {
        try {
            List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
            comboBoxCoordinador.setItems(FXCollections.observableArrayList(coordinadores));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar coordinadores", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS COORDINADORES.");
        }
    }

    private void cargarOrganizaciones() {
        try {
            List<OrganizacionVinculada> organizaciones = organizacionDao.obtenerOrganizacionesActivas();
            comboBoxOrganizacion.setItems(FXCollections.observableArrayList(organizaciones));
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar organizaciones", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LAS ORGANIZACIONES.");
        }
    }

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        boolean camposCorrectos = verificarCampos();
        if (camposCorrectos) {
            Proyecto proyecto = armarProyecto();
            ejecutarRegistro(proyecto);
        }
    }

    private boolean verificarCampos() {
        String nombreProyecto = campoTextoNombreProyecto.getText().trim();
        String descripcion = campoTextoDescripcion.getText().trim();
        String responsable = campoTextoResponsable.getText().trim();
        String nombreEmpresa = campoTextoNombreEmpresa.getText().trim();
        String sectorEmpresa = campoTextoSectorEmpresa.getText().trim();
        String direccionEmpresa = campoTextoDireccionEmpresa.getText().trim();
        List<String> campos = List.of(nombreProyecto, descripcion, responsable, nombreEmpresa, sectorEmpresa, direccionEmpresa);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean profesorSeleccionado = comboBoxProfesor.getValue() != null;
        boolean coordinadorSeleccionado = comboBoxCoordinador.getValue() != null;
        boolean organizacionSeleccionada = comboBoxOrganizacion.getValue() != null;
        boolean combosValidos = profesorSeleccionado && coordinadorSeleccionado && organizacionSeleccionada;
        boolean longitudNombreValida = nombreProyecto.length() <= LIMITE_NOMBRE_PROYECTO;
        boolean longitudDescripcionValida = descripcion.length() <= LIMITE_DESCRIPCION;
        boolean longitudResponsableValida = responsable.length() <= LIMITE_RESPONSABLE;
        boolean longitudNombreEmpresaValida = nombreEmpresa.length() <= LIMITE_NOMBRE_EMPRESA;
        boolean longitudSectorValida = sectorEmpresa.length() <= LIMITE_SECTOR_EMPRESA;
        boolean longitudDireccionValida = direccionEmpresa.length() <= LIMITE_DIRECCION_EMPRESA;
        boolean valido = true;
        if (!camposFormularioValido) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENA TODOS LOS CAMPOS.");
            valido = false;
        } else if (!combosValidos) {
            mostrarError("Seleccion incompleta", "POR FAVOR SELECCIONA PROFESOR, COORDINADOR Y ORGANIZACION.");
            valido = false;
        } else if (!longitudNombreValida) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE DEL PROYECTO NO PUEDE EXCEDER " + LIMITE_NOMBRE_PROYECTO + " CARACTERES.");
            valido = false;
        } else if (!longitudDescripcionValida) {
            mostrarError("Descripcion demasiado larga", "LA DESCRIPCION NO PUEDE EXCEDER " + LIMITE_DESCRIPCION + " CARACTERES.");
            valido = false;
        } else if (!longitudResponsableValida) {
            mostrarError("Responsable demasiado largo", "EL RESPONSABLE NO PUEDE EXCEDER " + LIMITE_RESPONSABLE + " CARACTERES.");
            valido = false;
        } else if (!longitudNombreEmpresaValida) {
            mostrarError("Nombre de empresa demasiado largo", "EL NOMBRE DE EMPRESA NO PUEDE EXCEDER " + LIMITE_NOMBRE_EMPRESA + " CARACTERES.");
            valido = false;
        } else if (!longitudSectorValida) {
            mostrarError("Sector demasiado largo", "EL SECTOR NO PUEDE EXCEDER " + LIMITE_SECTOR_EMPRESA + " CARACTERES.");
            valido = false;
        } else if (!longitudDireccionValida) {
            mostrarError("Direccion demasiado larga", "LA DIRECCION NO PUEDE EXCEDER " + LIMITE_DIRECCION_EMPRESA + " CARACTERES.");
            valido = false;
        }
        return valido;
    }

    private boolean camposVacios(List<String> campos) {
        boolean hayCamposVacios = false;
        for (String campo : campos) {
            if (campo.isEmpty()) {
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private Proyecto armarProyecto() {
        String nombreProyecto = campoTextoNombreProyecto.getText().trim();
        String descripcion = campoTextoDescripcion.getText().trim();
        String responsable = campoTextoResponsable.getText().trim();
        String nombreEmpresa = campoTextoNombreEmpresa.getText().trim();
        String sectorEmpresa = campoTextoSectorEmpresa.getText().trim();
        String direccionEmpresa = campoTextoDireccionEmpresa.getText().trim();
        Proyecto proyecto = new Proyecto();
        proyecto.setNombreProyecto(nombreProyecto);
        proyecto.setDescripcion(descripcion);
        proyecto.setResponsableDelProyecto(responsable);
        proyecto.setNombreEmpresa(nombreEmpresa);
        proyecto.setSectorEmpresa(sectorEmpresa);
        proyecto.setDireccionEmpresa(direccionEmpresa);
        proyecto.setNumPersonalCoordinador(comboBoxCoordinador.getValue().getNumeroDePersonalCoordinador());
        proyecto.setNumPersonalProfesor(comboBoxProfesor.getValue().getNumeroDePersonalProfesor());
        proyecto.setIdOrganizacion(comboBoxOrganizacion.getValue().getIdOrganizacion());
        proyecto.setCapacidad(spinnerCapacidad.getValue());
        proyecto.setEstado(EstadoProyecto.Disponible);
        proyecto.setFechaRegistro(Date.valueOf(LocalDate.now()));
        return proyecto;
    }

    private void ejecutarRegistro(Proyecto proyecto) {
        try {
            int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                mostrarExito("Proyecto registrado", "EL PROYECTO FUE REGISTRADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR EL PROYECTO.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        limpiar();
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void limpiar() {
        campoTextoNombreProyecto.clear();
        campoTextoDescripcion.clear();
        campoTextoResponsable.clear();
        campoTextoNombreEmpresa.clear();
        campoTextoSectorEmpresa.clear();
        campoTextoDireccionEmpresa.clear();
        comboBoxProfesor.getSelectionModel().clearSelection();
        comboBoxCoordinador.getSelectionModel().clearSelection();
        comboBoxOrganizacion.getSelectionModel().clearSelection();
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