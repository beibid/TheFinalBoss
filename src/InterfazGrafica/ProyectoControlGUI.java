package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Coordinador;
import logica.dominio.OrganizacionVinculada;
import logica.dominio.Practicante;
import logica.dominio.Proyecto;
import logica.dominio.enums.EstadoProyecto;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class ProyectoControlGUI implements Initializable{
    @FXML private TextField txtNombreProyecto;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtResponsable;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtSectorEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private ComboBox<Practicante> cmbPracticante;
    @FXML private ComboBox<Coordinador> cmbCoordinador;
    @FXML private ComboBox<OrganizacionVinculada> cmbOrganizacion;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label lblTituloError;
    @FXML private Label lblMensajeError;
    @FXML private Label lblTituloExito;
    @FXML private Label lblMensajeExito;

    private ProyectoDao proyectoDao = new ProyectoDao();
    private PracticanteDao practicanteDao = new PracticanteDao();
    private CoordinadorDao coordinadorDao = new CoordinadorDao();
    private OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
    }

    private void cargarCombos() {
        cargarPracticantes();
        cargarCoordinadores();
        cargarOrganizaciones();
    }

    private void cargarPracticantes() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            cmbPracticante.setItems(FXCollections.observableArrayList(practicantes));
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarCoordinadores() {
        try {
            List<Coordinador> coordinadores = coordinadorDao.obtenerCoordinadoresActivos();
            cmbCoordinador.setItems(FXCollections.observableArrayList(coordinadores));
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS COORDINADORES.");
        }
    }

    private void cargarOrganizaciones() {
        try {
            List<OrganizacionVinculada> organizaciones = organizacionDao.obtenerOrganizacionesActivas();
            cmbOrganizacion.setItems(FXCollections.observableArrayList(organizaciones));
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LAS ORGANIZACIONES.");
        }
    }

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();

        String nombreProyecto = txtNombreProyecto.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String responsable = txtResponsable.getText().trim();
        String nombreEmpresa = txtNombreEmpresa.getText().trim();
        String sectorEmpresa = txtSectorEmpresa.getText().trim();
        String direccionEmpresa = txtDireccionEmpresa.getText().trim();

        if (camposVacios(nombreProyecto, descripcion, responsable, nombreEmpresa, sectorEmpresa, direccionEmpresa)) {
            mostrarError("Campos obligatorios vacíos", "POR FAVOR LLENA TODOS LOS CAMPOS.");
            return;
        }

        if (cmbPracticante.getValue() == null || cmbCoordinador.getValue() == null || cmbOrganizacion.getValue() == null) {
            mostrarError("Selección incompleta", "POR FAVOR SELECCIONA PRACTICANTE, COORDINADOR Y ORGANIZACIÓN.");
            return;
        }

        Proyecto proyecto = armarProyecto(nombreProyecto, descripcion, responsable, nombreEmpresa, sectorEmpresa, direccionEmpresa);
        ejecutarRegistro(proyecto);
    }

    private boolean camposVacios(String... campos) {
        for (String campo : campos) {
            if (campo.isEmpty()) return true;
        }
        return false;
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
        proyecto.setMatricula(cmbPracticante.getValue().getMatricula());
        proyecto.setNumPersonalCoordinador(cmbCoordinador.getValue().getNumeroDePersonalCoordinador());
        proyecto.setIdOrganizacion(cmbOrganizacion.getValue().getIdOrganizacion());
        proyecto.setEstado(EstadoProyecto.Disponible);
        proyecto.setFechaRegistro(Date.valueOf(LocalDate.now()));
        return proyecto;
    }

    private void ejecutarRegistro(Proyecto proyecto) {
        try {
            int filasAfectadas = proyectoDao.agregarProyecto(proyecto);
            if (filasAfectadas > 0) {
                limpiar();
                mostrarExito("Proyecto registrado", "EL PROYECTO FUE REGISTRADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR EL PROYECTO.");
            }
        } catch (MensajeriaExcepcion e) {
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

    private void limpiar() {
        txtNombreProyecto.clear();
        txtDescripcion.clear();
        txtResponsable.clear();
        txtNombreEmpresa.clear();
        txtSectorEmpresa.clear();
        txtDireccionEmpresa.clear();
        cmbPracticante.getSelectionModel().clearSelection();
        cmbCoordinador.getSelectionModel().clearSelection();
        cmbOrganizacion.getSelectionModel().clearSelection();
        ocultarError();
        ocultarExito();
    }

    private void mostrarError(String titulo, String mensaje) {
        lblTituloError.setText(titulo);
        lblMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        lblTituloExito.setText(titulo);
        lblMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}
