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

public class SeleccionarPreferenciasProyectoControlador {

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
    private final ProyectoDao proyectoDao = new ProyectoDao();
    private final PreferenciaProyectoDao preferenciaDao = new PreferenciaProyectoDao();
    private List<Proyecto> todosLosProyectos = new ArrayList<>();

    @FXML
    public void initialize() {
        matriculaPracticante = SesionUsuario.getInstance().getMatricula();
        cargarProyectos();
        cargarPreferenciasGuardadas();
    }

    private void cargarProyectos() {
        try {
            todosLosProyectos = proyectoDao.obtenerProyectosDisponibles();
            comboBoxPrimerPrioridad.setItems(FXCollections.observableArrayList(todosLosProyectos));
            comboBoxSegundaPrioridad.setItems(FXCollections.observableArrayList(todosLosProyectos));
            comboBoxTerceraPrioridad.setItems(FXCollections.observableArrayList(todosLosProyectos));
        } catch (MensajeriaExcepcion e) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR LOS PROYECTOS DISPONIBLES.");
        }
    }

    private void cargarPreferenciasGuardadas() {
        try {
            List<PreferenciaProyecto> preferencias = preferenciaDao.obtenerPreferencias(matriculaPracticante);
            for (PreferenciaProyecto preferencia : preferencias) {
                Proyecto proyecto = buscarProyecto(preferencia.getIdProyecto());
                if (proyecto == null) {
                    continue;
                }
                switch (preferencia.getPrioridad()) {
                    case 1:
                        comboBoxPrimerPrioridad.getSelectionModel().select(proyecto);
                        break;
                    case 2:
                        comboBoxSegundaPrioridad.getSelectionModel().select(proyecto);
                        break;
                    case 3:
                        comboBoxTerceraPrioridad.getSelectionModel().select(proyecto);
                        break;
                }
            }
        } catch (UsuariosExcepcion e) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar", "NO SE PUDIERON CARGAR TUS PREFERENCIAS ANTERIORES.");
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
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campo obligatorio", "DEBES SELECCIONAR AL MENOS TU PRIMERA PRIORIDAD.");
            entradaValida = false;
        } else if (hayRepetidos(primerPrioridad, segundaPrioridad, terceraPrioridad)) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Selección inválida", "NO PUEDES SELECCIONAR EL MISMO PROYECTO EN DOS PRIORIDADES.");
            entradaValida = false;
        } else if (!confirmarAccion("¿Deseas guardar tus preferencias de proyectos?")) {
            entradaValida = false;
        }

        if (entradaValida) {
            guardarPreferencias(primerPrioridad, segundaPrioridad, terceraPrioridad);
        }
    }

    private boolean hayRepetidos(Proyecto primerProyecto, Proyecto segundoProyecto, Proyecto tercerProyecto) {
        boolean primerYSegundoIguales = segundoProyecto != null &&
                primerProyecto.getIdProyecto() == segundoProyecto.getIdProyecto();
        boolean primerYTerceroIguales = tercerProyecto != null &&
                primerProyecto.getIdProyecto() == tercerProyecto.getIdProyecto();
        boolean segundoYTerceroIguales = segundoProyecto != null && tercerProyecto != null &&
                segundoProyecto.getIdProyecto() == tercerProyecto.getIdProyecto();
        return primerYSegundoIguales || primerYTerceroIguales || segundoYTerceroIguales;
    }

    private void guardarPreferencias(Proyecto primerProyecto, Proyecto segundoProyecto, Proyecto tercerProyecto) {
        try {
            List<Integer> idOrdenados = new ArrayList<>();
            idOrdenados.add(primerProyecto.getIdProyecto());
            if (segundoProyecto != null) {
                idOrdenados.add(segundoProyecto.getIdProyecto());
            }
            if (tercerProyecto != null) {
                idOrdenados.add(tercerProyecto.getIdProyecto());
            }
            preferenciaDao.guardarPreferencias(matriculaPracticante, idOrdenados);
            mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                    "Guardado exitoso", "TUS PREFERENCIAS FUERON GUARDADAS CORRECTAMENTE.");
        } catch (UsuariosExcepcion e) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al guardar", "NO SE PUDIERON GUARDAR TUS PREFERENCIAS.");
        }
    }

    @FXML
    private void botonLimpiar() {
        if (!confirmarAccion("¿Deseas limpiar tu selección actual?")) {
            return;
        }
        comboBoxPrimerPrioridad.getSelectionModel().clearSelection();
        comboBoxSegundaPrioridad.getSelectionModel().clearSelection();
        comboBoxTerceraPrioridad.getSelectionModel().clearSelection();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
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