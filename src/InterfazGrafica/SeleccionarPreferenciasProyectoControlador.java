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
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PreferenciaProyectoDao;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.PreferenciaProyecto;
import logica.dominio.Proyecto;
import logica.dominio.SesionUsuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleccionarPreferenciasProyectoControlador {

    private static final Logger LOGGER = Logger.getLogger(SeleccionarPreferenciasProyectoControlador.class.getName());

    private final ProyectoDao proyectoDao = new ProyectoDao();
    private final PreferenciaProyectoDao preferenciaDao = new PreferenciaProyectoDao();

    @FXML private ComboBox<Proyecto> comboBoxPrimerPrioridad;
    @FXML private ComboBox<Proyecto> comboBoxSegundaPrioridad;
    @FXML private ComboBox<Proyecto> comboBoxTerceraPrioridad;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private String matriculaPracticante;
    private List<Proyecto> todosLosProyectos = new ArrayList<>();

    @FXML
    public void initialize() {
        matriculaPracticante = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        cargarProyectos();
        cargarPreferenciasGuardadas();
    }

    private void cargarProyectos() {
        try {
            todosLosProyectos = proyectoDao.obtenerProyectosDisponibles();
            comboBoxPrimerPrioridad.setItems(FXCollections.observableArrayList(todosLosProyectos));
            comboBoxSegundaPrioridad.setItems(FXCollections.observableArrayList(todosLosProyectos));
            comboBoxTerceraPrioridad.setItems(FXCollections.observableArrayList(todosLosProyectos));
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyectos", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROYECTOS DISPONIBLES.");
        }
    }

    private void cargarPreferenciasGuardadas() {
        try {
            List<PreferenciaProyecto> preferencias = preferenciaDao.obtenerPreferencias(matriculaPracticante);
            for (PreferenciaProyecto preferencia : preferencias) {
                Proyecto proyecto = buscarProyecto(preferencia.getIdProyecto());
                boolean proyectoEncontrado = proyecto != null;
                if (proyectoEncontrado) {
                    asignarPreferencia(preferencia.getPrioridad(), proyecto);
                }
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar preferencias", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR TUS PREFERENCIAS ANTERIORES.");
        }
    }

    private void asignarPreferencia(int prioridad, Proyecto proyecto) {
        switch (prioridad) {
            case 1:
                comboBoxPrimerPrioridad.getSelectionModel().select(proyecto);
                break;
            case 2:
                comboBoxSegundaPrioridad.getSelectionModel().select(proyecto);
                break;
            case 3:
                comboBoxTerceraPrioridad.getSelectionModel().select(proyecto);
                break;
            default:
                break;
        }
    }

    private Proyecto buscarProyecto(int idProyecto) {
        Proyecto proyectoEncontrado = null;
        for (Proyecto proyecto : todosLosProyectos) {
            if (proyecto.getIdProyecto() == idProyecto) {
                proyectoEncontrado = proyecto;
            }
        }
        return proyectoEncontrado;
    }

    @FXML
    private void validarSeleccion() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
    }

    @FXML
    private void botonGuardar() {
        Proyecto primerPrioridad = comboBoxPrimerPrioridad.getSelectionModel().getSelectedItem();
        Proyecto segundaPrioridad = comboBoxSegundaPrioridad.getSelectionModel().getSelectedItem();
        Proyecto terceraPrioridad = comboBoxTerceraPrioridad.getSelectionModel().getSelectedItem();
        boolean entradaValida = true;
        if (primerPrioridad == null) {
            mostrarError("Campo obligatorio", "DEBES SELECCIONAR AL MENOS TU PRIMERA PRIORIDAD.");
            entradaValida = false;
        } else if (hayRepetidos(primerPrioridad, segundaPrioridad, terceraPrioridad)) {
            mostrarError("Seleccion invalida", "NO PUEDES SELECCIONAR EL MISMO PROYECTO EN DOS PRIORIDADES.");
            entradaValida = false;
        } else if (!confirmarAccion("Deseas guardar tus preferencias de proyectos?")) {
            entradaValida = false;
        }
        if (entradaValida) {
            guardarPreferencias(primerPrioridad, segundaPrioridad, terceraPrioridad);
        }
    }

    private boolean hayRepetidos(Proyecto primerProyecto, Proyecto segundoProyecto, Proyecto tercerProyecto) {
        boolean segundoNoNulo = segundoProyecto != null;
        boolean terceroNoNulo = tercerProyecto != null;
        boolean primerYSegundoIguales = segundoNoNulo &&
                primerProyecto.getIdProyecto() == segundoProyecto.getIdProyecto();
        boolean primerYTerceroIguales = terceroNoNulo &&
                primerProyecto.getIdProyecto() == tercerProyecto.getIdProyecto();
        boolean segundoYTerceroIguales = segundoNoNulo && terceroNoNulo &&
                segundoProyecto.getIdProyecto() == tercerProyecto.getIdProyecto();
        return primerYSegundoIguales || primerYTerceroIguales || segundoYTerceroIguales;
    }

    private void guardarPreferencias(Proyecto primerProyecto, Proyecto segundoProyecto, Proyecto tercerProyecto) {
        try {
            List<Integer> idOrdenados = new ArrayList<>();
            idOrdenados.add(primerProyecto.getIdProyecto());
            boolean segundoNoNulo = segundoProyecto != null;
            boolean terceroNoNulo = tercerProyecto != null;
            if (segundoNoNulo) {
                idOrdenados.add(segundoProyecto.getIdProyecto());
            }
            if (terceroNoNulo) {
                idOrdenados.add(tercerProyecto.getIdProyecto());
            }
            preferenciaDao.guardarPreferencias(matriculaPracticante, idOrdenados);
            mostrarExito("Guardado exitoso", "TUS PREFERENCIAS FUERON GUARDADAS CORRECTAMENTE.");
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar preferencias", excepcion);
            mostrarError("Error al guardar", "NO SE PUDIERON GUARDAR TUS PREFERENCIAS.");
        }
    }

    @FXML
    private void botonLimpiar() {
        if (confirmarAccion("Deseas limpiar tu seleccion actual?")) {
            comboBoxPrimerPrioridad.getSelectionModel().clearSelection();
            comboBoxSegundaPrioridad.getSelectionModel().clearSelection();
            comboBoxTerceraPrioridad.getSelectionModel().clearSelection();
            ocultarPanel(panelError);
            ocultarPanel(panelExito);
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