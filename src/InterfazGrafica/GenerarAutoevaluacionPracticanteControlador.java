package InterfazGrafica;

import logica.dao.objetos.AutoevaluacionPracticanteDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.objetos.ReporteDao;
import logica.dominio.AutoevaluacionPracticante;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.SesionUsuario;
import logica.archivos.GeneradorPdfAutoevaluacion;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerarAutoevaluacionPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarAutoevaluacionPracticanteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int MENSUALES_REQUERIDOS = 6;
    private static final int PARCIALES_REQUERIDOS = 2;

    private final AutoevaluacionPracticanteDao autoevaluacionDao = new AutoevaluacionPracticanteDao();
    private final ReporteDao reporteDao = new ReporteDao();

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
    private boolean autoevaluacionGuardada = false;

    @FXML
    public void initialize() {
        matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        nombrePracticante = SesionUsuario.getInstance().getUsuarioActivo().getNombre();
        etiquetaMatricula.setText(matricula);
        cargarInformacionProyecto();
        verificarAutoevaluacionExistente();
    }

    @FXML
    private void botonGuardar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (autoevaluacionGuardada) {
            mostrarError("Autoevaluacion ya registrada",
                    "YA GUARDASTE TU AUTOEVALUACION. SOLO PUEDES GENERAR EL PDF.");
        } else if (confirmarAccion("Seguro que desea guardar la autoevaluacion?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonGenerarPDF() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        boolean reportesCompletos = verificarReportesCompletos();
        boolean respuestasCompletas = respuestasValidas();
        boolean usuarioConfirmo = false;
        if (reportesCompletos && !respuestasCompletas) {
            mostrarError("Respuestas incompletas",
                    "DEBES RESPONDER TODAS LAS AFIRMACIONES ANTES DE GENERAR EL PDF.");
        } else if (reportesCompletos && respuestasCompletas) {
            usuarioConfirmo = confirmarAccion("Desea generar el PDF de la autoevaluacion?");
        }
        if (usuarioConfirmo) {
            generarPdf();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
            limpiarRespuestas();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void verificarAutoevaluacionExistente() {
        try {
            AutoevaluacionPracticante autoevaluacion = autoevaluacionDao.obtenerAutoevaluacion(matricula);
            if (autoevaluacion != null) {
                precargarRespuestas(autoevaluacion);
                deshabilitarRespuestas();
                autoevaluacionGuardada = true;
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar autoevaluacion existente", excepcion);
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

    private void deshabilitarRespuestas() {
        List<ToggleGroup> grupos = List.of(grupoRespuesta1, grupoRespuesta2, grupoRespuesta3,
                grupoRespuesta4, grupoRespuesta5, grupoRespuesta6, grupoRespuesta7,
                grupoRespuesta8, grupoRespuesta9, grupoRespuesta10);
        for (ToggleGroup grupo : grupos) {
            for (Toggle toggle : grupo.getToggles()) {
                ((ToggleButton) toggle).setDisable(true);
            }
        }
    }

    private void cargarInformacionProyecto() {
        try {
            AutoevaluacionPracticante informacion = autoevaluacionDao.obtenerInfoAutoevaluacion(matricula);
            if (informacion != null) {
                idProyecto = informacion.getIdProyecto();
                etiquetaOrganizacion.setText(informacion.getNombreOrganizacion());
                etiquetaNombreProyecto.setText(informacion.getNombreProyecto());
                etiquetaResponsable.setText(informacion.getResponsableDelProyecto());
            } else {
                mostrarError("Sin proyecto asignado",
                        "NO SE ENCONTRO INFORMACION DEL PROYECTO PARA LA MATRICULA: " + matricula);
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar informacion del proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void procesarRegistro() {
        boolean reportesCompletos = verificarReportesCompletos();
        boolean respuestasCompletas = respuestasValidas();
        if (reportesCompletos && !respuestasCompletas) {
            mostrarError("Respuestas incompletas", "DEBES RESPONDER TODO ANTES DE GUARDAR.");
        } else if (reportesCompletos && respuestasCompletas) {
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
            mostrarExito("PDF generado correctamente",
                    "LA AUTOEVALUACION SE GUARDO EN: " + rutaPdf);
        } else {
            mostrarError("Error al generar",
                    "NO SE PUDO GENERAR EL PDF. INTENTE DE NUEVO.");
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
        AutoevaluacionPracticante autoevaluacion = new AutoevaluacionPracticante(
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
        return autoevaluacion;
    }

    private int obtenerRespuesta(ToggleGroup grupo) {
        Toggle seleccionado = grupo.getSelectedToggle();
        return Integer.parseInt(seleccionado.getUserData().toString());
    }

    private void guardarAutoevaluacion(AutoevaluacionPracticante autoevaluacion) {
        try {
            int filasAfectadas = autoevaluacionDao.registrarAutoevaluacion(autoevaluacion);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                autoevaluacionGuardada = true;
                deshabilitarRespuestas();
                mostrarExito("Autoevaluacion registrada", "LA AUTOEVALUACION SE GUARDO EXITOSAMENTE.");
            } else {
                mostrarError("Error al guardar", "NO SE PUDO GUARDAR LA AUTOEVALUACION. INTENTE DE NUEVO.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar autoevaluacion", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean verificarReportesCompletos() {
        boolean puedeGenerar = false;
        try {
            int mensualesEvaluados = reporteDao.contarReportesEvaluados(matricula, "Mensual");
            int parcialesEvaluados = reporteDao.contarReportesEvaluados(matricula, "Parcial");
            boolean mensualesSuficientes = mensualesEvaluados >= MENSUALES_REQUERIDOS;
            boolean parcialesSuficientes = parcialesEvaluados >= PARCIALES_REQUERIDOS;
            if (mensualesSuficientes && parcialesSuficientes) {
                puedeGenerar = true;
            } else {
                mostrarError("Reportes incompletos",
                        "DEBES TENER 6 REPORTES MENSUALES Y 2 PARCIALES EVALUADOS PARA GENERAR LA AUTOEVALUACION.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar reportes completos", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return puedeGenerar;
    }

    private void limpiarRespuestas() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (!autoevaluacionGuardada) {
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