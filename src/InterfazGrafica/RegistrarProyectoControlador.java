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

public class RegistrarProyectoControlador {

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

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

    private ProyectoDao proyectoDao = new ProyectoDao();
    private ProfesorDao profesorDao = new ProfesorDao();
    private CoordinadorDao coordinadorDao = new CoordinadorDao();
    private OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();

    @FXML
    public void initialize() {
        cargarCombos();
    }

    private void cargarCombos() {
        cargarPracticantes();
        cargarCoordinadores();
        cargarOrganizaciones();
    }

    private void cargarPracticantes() {
        try {
            List<Profesor> profesores = profesorDao.obtenerProfesoresActivos();
            comboBoxProfesor.setItems(FXCollections.observableArrayList(profesores));
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarCoordinadores() {
        try {
            List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
            comboBoxCoordinador.setItems(FXCollections.observableArrayList(coordinadores));
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LOS COORDINADORES.");
        }
    }

    private void cargarOrganizaciones() {
        try {
            List<OrganizacionVinculada> organizaciones = organizacionDao.obtenerOrganizacionesActivas();
            comboBoxOrganizacion.setItems(FXCollections.observableArrayList(organizaciones));
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LAS ORGANIZACIONES.");
        }
    }

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);

        String nombreProyecto = campoTextoNombreProyecto.getText().trim();
        String descripcion = campoTextoDescripcion.getText().trim();
        String responsable = campoTextoResponsable.getText().trim();
        String nombreEmpresa = campoTextoNombreEmpresa.getText().trim();
        String sectorEmpresa = campoTextoSectorEmpresa.getText().trim();
        String direccionEmpresa = campoTextoDireccionEmpresa.getText().trim();

        List<String> campos = List.of(nombreProyecto, descripcion, responsable, nombreEmpresa, sectorEmpresa,
                direccionEmpresa);

        boolean entradaValida = true;

        if (camposVacios(campos)) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacíos", "POR FAVOR LLENA TODOS LOS CAMPOS.");
            entradaValida = false;
        } else if (comboBoxProfesor.getValue() == null || comboBoxCoordinador.getValue() == null
                || comboBoxOrganizacion.getValue() == null) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Selección incompleta", "POR FAVOR SELECCIONA PRACTICANTE, COORDINADOR Y ORGANIZACIÓN.");
            entradaValida = false;
        }

        if (entradaValida) {
            Proyecto proyecto = armarProyecto(nombreProyecto, descripcion, responsable, nombreEmpresa,
                    sectorEmpresa, direccionEmpresa);
            ejecutarRegistro(proyecto);
        }
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

    private Proyecto armarProyecto(String nombreProyecto, String descripcion, String responsable,
                                   String nombreEmpresa, String sectorEmpresa, String direccionEmpresa) {
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
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Proyecto registrado", "EL PROYECTO FUE REGISTRADO EXITOSAMENTE.");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "NO SE PUDO REGISTRAR EL PROYECTO.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
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

    private void mostrarPanel(Label etiquetaTitulo, Label etiquetaMensaje, VBox panel, String titulo, String mensaje) {
        etiquetaTitulo.setText(titulo);
        etiquetaMensaje.setText(mensaje);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }
}