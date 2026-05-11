package InterfazGrafica;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.PreferenciaProyectoDao;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Practicante;
import logica.dominio.PreferenciaProyecto;
import logica.dominio.Proyecto;
import java.util.ArrayList;
import java.util.List;


public class AsignaProyectoControlGUI {

    @FXML private ComboBox<Practicante> comboBoxPracticantes;
    @FXML private ComboBox<Proyecto>    comboBoxProyectos;
    @FXML private VBox  panelError;
    @FXML private VBox  panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private final PracticanteDao         practicanteDao  = new PracticanteDao();
    private final ProyectoDao            proyectoDao     = new ProyectoDao();
    private final PreferenciaProyectoDao preferenciaDao  = new PreferenciaProyectoDao();

    @FXML
    public void initialize() {
        cargarPracticantesActivos();
        cargarProyectos();
    }

    private void cargarPracticantesActivos() {
        try {
            List<Practicante> practicantes = practicanteDao.obtenerPracticantesActivos();
            comboBoxPracticantes.setItems(FXCollections.observableArrayList(practicantes));
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PRACTICANTES.");
        }
    }

    private void cargarProyectos() {
        try {
            List<Proyecto> proyectos = proyectoDao.obtenerProyectosDisponibles();
            comboBoxProyectos.setItems(FXCollections.observableArrayList(proyectos));
            comboBoxProyectos.setCellFactory(null);
            comboBoxProyectos.setButtonCell(null);
        } catch (MensajeriaExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROYECTOS DISPONIBLES.");
        }
    }

    @FXML
    private void seleccionarPracticante() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        if (practicante == null) {
            return;
        }
        ocultarError();
        ocultarExito();
        cargarProyectosConPreferencias(practicante.getMatricula());
    }

    private void cargarProyectosConPreferencias(String matricula) {
        try {
            List<Proyecto> todosDisponibles = proyectoDao.obtenerProyectosDisponibles();
            List<PreferenciaProyecto> preferencias = preferenciaDao.obtenerPreferencias(matricula);
            List<Integer> idsPriorizados = obtenerIdsPriorizados(preferencias);
            List<Proyecto> ordenados = ordenarProyectos(todosDisponibles, idsPriorizados);
            mostrarProyectosEnComboBox(ordenados, idsPriorizados);
        } catch (MensajeriaExcepcion | UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS PROYECTOS.");
        }
    }

    private List<Integer> obtenerIdsPriorizados(List<PreferenciaProyecto> preferencias) {
        List<Integer> idsPriorizados = new ArrayList<>();
        for (PreferenciaProyecto prioritarios : preferencias) {
            idsPriorizados.add(prioritarios.getIdProyecto());
        }
        return idsPriorizados;
    }

    private List<Proyecto> ordenarProyectos(List<Proyecto> todosDisponibles, List<Integer> idsPriorizados) {
        List<Proyecto> priorizados = new ArrayList<>();
        for (Integer id : idsPriorizados) {
            for (Proyecto proyectos : todosDisponibles) {
                if (proyectos.getIdProyecto() == id) {
                    priorizados.add(proyectos);
                    break;
                }
            }
        }
        List<Proyecto> restantes = new ArrayList<>();
        for (Proyecto p : todosDisponibles) {
            if (!idsPriorizados.contains(p.getIdProyecto())) {
                restantes.add(p);
            }
        }
        List<Proyecto> ordenados = new ArrayList<>();
        ordenados.addAll(priorizados);
        ordenados.addAll(restantes);
        return ordenados;
    }

    private void mostrarProyectosEnComboBox(List<Proyecto> ordenados, List<Integer> idsPriorizados) {
        comboBoxProyectos.setItems(FXCollections.observableArrayList(ordenados));
        comboBoxProyectos.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Proyecto proyecto, boolean empty) {
                super.updateItem(proyecto, empty);
                if (empty || proyecto == null) {
                    setText(null);
                } else {
                    int idx = idsPriorizados.indexOf(proyecto.getIdProyecto());
                    if (idx >= 0) {
                        setText(" Prioridad " + (idx + 1) + " — " + proyecto.getNombreProyecto());
                    } else {
                        setText(proyecto.getNombreProyecto());
                    }
                }
            }
        });
        comboBoxProyectos.getSelectionModel().clearSelection();
    }

    @FXML
    private void botonAsignar() {
        Practicante practicante = comboBoxPracticantes.getSelectionModel().getSelectedItem();
        Proyecto proyecto = comboBoxProyectos.getSelectionModel().getSelectedItem();

        if (practicante == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PRACTICANTE.");
            return;
        }
        if (proyecto == null) {
            mostrarError("Sin selección", "POR FAVOR SELECCIONA UN PROYECTO.");
            return;
        }
        if (!confirmarAccion("¿Seguro que desea asignar a " + practicante.getNombre() + " el proyecto " + proyecto.getNombreProyecto() + "?")) {
            return;
        }
        ejecutarAsignacion(practicante, proyecto);
    }

    private void ejecutarAsignacion(Practicante practicante, Proyecto proyecto) {
        try {
            int filasAfectadas = practicanteDao.asignarProyecto(practicante.getMatricula(), proyecto.getIdProyecto());
            if (filasAfectadas > 0) {
                limpiarSeleccion();
                mostrarExito("Asignación exitosa", "EL PRACTICANTE FUE ASIGNADO AL PROYECTO EXITOSAMENTE.");
            } else {
                mostrarError("Error", "NO SE PUDO REALIZAR LA ASIGNACIÓN.");
            }
        } catch (UsuariosExcepcion e) {
            mostrarError("ERROR", e.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarSeleccion();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);
        return alerta.showAndWait().filter(r -> r == btnSi).isPresent();
    }

    private void limpiarSeleccion() {
        comboBoxPracticantes.getSelectionModel().clearSelection();
        comboBoxProyectos.getSelectionModel().clearSelection();
        cargarProyectos();
        ocultarError();
        ocultarExito();
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
        ocultarExito();
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
        ocultarError();
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}