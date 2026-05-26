package InterfazGrafica;

import logica.dao.objetos.AutoevaluacionPracticanteDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dominio.AutoevaluacionPracticante;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.SesionUsuario;
import logica.archivos.GeneradorPdfAutoevaluacion;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerarAutoevaluacionPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarAutoevaluacionPracticanteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private Label etiquetaMatricula;
    @FXML private Label etiquetaOrganizacion;
    @FXML private Label etiquetaNombreProyecto;
    @FXML private Label etiquetaResponsable;
    @FXML private ToggleGroup grupoRespuesta1;
    @FXML private ToggleGroup grupoRespuesta2;
    @FXML private ToggleGroup grupoRespuesta3;
    @FXML private ToggleGroup grupoRespuesta4;
    @FXML private ToggleGroup grupoRespuesta5;
    @FXML private ToggleGroup grupoRespuesta6;
    @FXML private ToggleGroup grupoRespuesta7;
    @FXML private ToggleGroup grupoRespuesta8;
    @FXML private ToggleGroup grupoRespuesta9;
    @FXML private ToggleGroup grupoRespuesta10;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private String matricula;
    private String nombrePracticante;
    private int idProyecto;

    @FXML
    public void initialize() {
        matricula = SesionUsuario.getInstance().getMatricula();
        nombrePracticante = SesionUsuario.getInstance().getNombre();
        etiquetaMatricula.setText(matricula);
        cargarInformacionProyecto();
        verificarAutoevaluacionExistente();
    }

    @FXML
    private void botonGuardar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("¿Seguro que desea guardar la autoevaluación?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonGenerarPDF() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (!respuestasValidas()) {
            mostrarError("Respuestas incompletas",
                    "Debes responder todas las afirmaciones antes de generar el PDF.");
        } else if (confirmarAccion("¿Desea generar el PDF de la autoevaluación?")) {
            generarPdf();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarRespuestas();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void verificarAutoevaluacionExistente() {
        AutoevaluacionPracticanteDao autoevaluacionDao = new AutoevaluacionPracticanteDao();
        try {
            AutoevaluacionPracticante autoevaluacion = autoevaluacionDao.obtenerAutoevaluacion(matricula);
            if (autoevaluacion != null) {
                precargarRespuestas(autoevaluacion);
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar autoevaluación existente", excepcion);
        }
    }

    private void precargarRespuestas(AutoevaluacionPracticante autoevaluacion) {
        seleccionarRespuesta(grupoRespuesta1, autoevaluacion.getRespuesta1());
        seleccionarRespuesta(grupoRespuesta2, autoevaluacion.getRespuesta2());
        seleccionarRespuesta(grupoRespuesta3, autoevaluacion.getRespuesta3());
        seleccionarRespuesta(grupoRespuesta4, autoevaluacion.getRespuesta4());
        seleccionarRespuesta(grupoRespuesta5, autoevaluacion.getRespuesta5());
        seleccionarRespuesta(grupoRespuesta6, autoevaluacion.getRespuesta6());
        seleccionarRespuesta(grupoRespuesta7, autoevaluacion.getRespuesta7());
        seleccionarRespuesta(grupoRespuesta8, autoevaluacion.getRespuesta8());
        seleccionarRespuesta(grupoRespuesta9, autoevaluacion.getRespuesta9());
        seleccionarRespuesta(grupoRespuesta10, autoevaluacion.getRespuesta10());
    }

    private void seleccionarRespuesta(ToggleGroup grupo, int valor) {
        boolean encontrado = false;
        int indice = 0;
        List<Toggle> toggles = grupo.getToggles();
        while (!encontrado && indice < toggles.size()) {
            Toggle toggle = toggles.get(indice);
            if (Integer.parseInt(toggle.getUserData().toString()) == valor) {
                toggle.setSelected(true);
                encontrado = true;
            }
            indice++;
        }
    }

    private void cargarInformacionProyecto() {
        AutoevaluacionPracticanteDao autoevaluacionDao = new AutoevaluacionPracticanteDao();
        try {
            AutoevaluacionPracticante informacion = autoevaluacionDao.obtenerInfoAutoevaluacion(matricula);
            if (informacion != null) {
                idProyecto = informacion.getIdProyecto();
                etiquetaOrganizacion.setText(informacion.getNombreOrganizacion());
                etiquetaNombreProyecto.setText(informacion.getNombreProyecto());
                etiquetaResponsable.setText(informacion.getResponsableDelProyecto());
            } else {
                mostrarError("Sin proyecto asignado",
                        "No se encontró información del proyecto para la matrícula: " + matricula);
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar información del proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void procesarRegistro() {
        if (!respuestasValidas()) {
            mostrarError("Respuestas incompletas", "Debes responder todo antes de guardar.");
        } else {
            guardarAutoevaluacion(construirAutoevaluacion());
        }
    }

    private void generarPdf() {
        AutoevaluacionPracticante autoevaluacion = construirAutoevaluacion();
        GeneradorPdfAutoevaluacion generador = new GeneradorPdfAutoevaluacion();
        String rutaPdf = generador.generarPdf(autoevaluacion, nombrePracticante,
                etiquetaNombreProyecto.getText(), etiquetaOrganizacion.getText(),
                etiquetaResponsable.getText());
        if (rutaPdf != null) {
            mostrarExito("PDF generado correctamente", "La autoevaluación se guardó en: " + rutaPdf);
        } else {
            mostrarError("Error al generar", "No se pudo generar el PDF. Intente de nuevo.");
        }
    }

    private boolean respuestasValidas() {
        List<ToggleGroup> grupos = List.of(grupoRespuesta1, grupoRespuesta2, grupoRespuesta3,
                grupoRespuesta4, grupoRespuesta5, grupoRespuesta6, grupoRespuesta7,
                grupoRespuesta8, grupoRespuesta9, grupoRespuesta10);
        boolean todasRespondidas = true;
        for (ToggleGroup grupo : grupos) {
            if (grupo.getSelectedToggle() == null) {
                todasRespondidas = false;
            }
        }
        return todasRespondidas;
    }

    private AutoevaluacionPracticante construirAutoevaluacion() {
        AutoevaluacionPracticante autoevaluacionPracticante = new AutoevaluacionPracticante(
                matricula, idProyecto,
                obtenerRespuesta(grupoRespuesta1),
                obtenerRespuesta(grupoRespuesta2),
                obtenerRespuesta(grupoRespuesta3),
                obtenerRespuesta(grupoRespuesta4),
                obtenerRespuesta(grupoRespuesta5),
                obtenerRespuesta(grupoRespuesta6),
                obtenerRespuesta(grupoRespuesta7),
                obtenerRespuesta(grupoRespuesta8),
                obtenerRespuesta(grupoRespuesta9),
                obtenerRespuesta(grupoRespuesta10)
        );
        return autoevaluacionPracticante;
    }

    private int obtenerRespuesta(ToggleGroup grupo) {
        Toggle seleccionado = grupo.getSelectedToggle();
        return Integer.parseInt(seleccionado.getUserData().toString());
    }

    private void guardarAutoevaluacion(AutoevaluacionPracticante autoevaluacion) {
        AutoevaluacionPracticanteDao autoevaluacionDao = new AutoevaluacionPracticanteDao();
        try {
            int filasAfectadas = autoevaluacionDao.registrarAutoevaluacion(autoevaluacion);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarRespuestas();
                mostrarExito("Autoevaluación registrada", "LA AUTOEVALUACIÓN SE GUARDÓ EXITOSAMENTE.");
            } else {
                mostrarError("Error al guardar", "NO SE PUDO GUARDAR LA AUTOEVALUACIÓN. INTENTE DE NUEVO.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar autoevaluación", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void limpiarRespuestas() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        grupoRespuesta1.selectToggle(null);
        grupoRespuesta2.selectToggle(null);
        grupoRespuesta3.selectToggle(null);
        grupoRespuesta4.selectToggle(null);
        grupoRespuesta5.selectToggle(null);
        grupoRespuesta6.selectToggle(null);
        grupoRespuesta7.selectToggle(null);
        grupoRespuesta8.selectToggle(null);
        grupoRespuesta9.selectToggle(null);
        grupoRespuesta10.selectToggle(null);
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

    private void mostrarPanel(VBox panel, Label etiquetaTitulo, Label etiquetaMensaje,String titulo, String mensaje) {
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