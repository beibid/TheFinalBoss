package InterfazGrafica;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;

public class ModificarOrganizacionVinculadaControlGUI {

    @FXML private ComboBox<OrganizacionVinculada> comboBoxOrganizacion;
    @FXML private TextField campoNombre;
    @FXML private TextField campoDireccion;
    @FXML private Label etiquetaMensaje;
    @FXML private Button botonCancelar;

    private static final Logger LOGGER = Logger.getLogger(ModificarOrganizacionVinculadaControlGUI.class.getName());

    @FXML
    public void initialize() {
        cargarOrganizaciones();
        comboBoxOrganizacion.setOnAction(evento -> cargarDatosOrganizacion());
    }

    private void cargarOrganizaciones() {
        try {
            OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();
            List<OrganizacionVinculada> organizaciones = organizacionDao.obtenerOrganizacionesActivas();
            comboBoxOrganizacion.getItems().addAll(organizaciones);
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar organizaciones", excepcion);
            etiquetaMensaje.setText("Error al cargar las organizaciones");
        }
    }

    private void cargarDatosOrganizacion() {
        OrganizacionVinculada organizacionSeleccionada = comboBoxOrganizacion.getValue();
        if (organizacionSeleccionada != null) {
            campoNombre.setText(organizacionSeleccionada.getNombre());
            campoDireccion.setText(organizacionSeleccionada.getDireccion());
        }
    }

    @FXML
    private void guardar() {
        OrganizacionVinculada organizacionSeleccionada = comboBoxOrganizacion.getValue();
        if (organizacionSeleccionada == null) {
            etiquetaMensaje.setText("Selecciona una organización");
        } else {
            String nombre = campoNombre.getText().trim();
            String direccion = campoDireccion.getText().trim();
            if (nombre.isEmpty() || direccion.isEmpty()) {
                etiquetaMensaje.setText("Todos los campos son obligatorios");
            } else {
                organizacionSeleccionada.setNombre(nombre);
                organizacionSeleccionada.setDireccion(direccion);
                guardarOrganizacion(organizacionSeleccionada);
            }
        }
    }

    private void guardarOrganizacion(OrganizacionVinculada organizacionSeleccionada) {
        try {
            OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();
            organizacionDao.modificarOrganizacionVinculada(
                    organizacionSeleccionada.getIdOrganizacion(), organizacionSeleccionada
            );
            etiquetaMensaje.setStyle("-fx-text-fill: green;");
            etiquetaMensaje.setText("Organización modificada correctamente");
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al modificar organización", excepcion);
            etiquetaMensaje.setText("Error al modificar la organización");
        }
    }

    @FXML
    private void cancelar() {
        Stage escenario = (Stage) botonCancelar.getScene().getWindow();
        escenario.close();
    }
}