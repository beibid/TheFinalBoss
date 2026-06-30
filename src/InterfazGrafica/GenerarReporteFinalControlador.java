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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.archivos.GeneradorPdfReporteFinal;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ActividadDao;
import logica.dao.objetos.ProyectoDao;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Actividad;
import logica.dominio.Proyecto;
import logica.dominio.Reporte;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.TipoReporte;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerarReporteFinalControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarReporteFinalControlador.class.getName());
    private static final int HORAS_MINIMAS_FINAL = 420;

    private final ProyectoDao proyectoDao = new ProyectoDao();
    private final ActividadDao actividadDao = new ActividadDao();
    private final ReporteDao reporteDao = new ReporteDao();

    @FXML private Label etiquetaMatricula;
    @FXML private Label etiquetaProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private Label etiquetaHorasAcumuladas;
    @FXML private ComboBox<String> comboActividadUno;
    @FXML private ComboBox<String> comboActividadDos;
    @FXML private TextField campoAvanceUno;
    @FXML private TextArea campoObservacionUno;
    @FXML private TextField campoAvanceDos;
    @FXML private TextArea campoObservacionDos;
    @FXML private TextField campoEntregableUno;
    @FXML private TextField campoAvanceEntregableUno;
    @FXML private TextArea campoObservacionEntregableUno;
    @FXML private TextField campoEntregableDos;
    @FXML private TextField campoAvanceEntregableDos;
    @FXML private TextArea campoObservacionEntregableDos;
    @FXML private TextArea textoAreaObservacionesGenerales;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private String matricula;
    private String nombrePracticante;
    private String nombreProyecto;
    private String nombreOrganizacion;

    @FXML
    public void initialize() {
        matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        nombrePracticante = SesionUsuario.getInstance().getUsuarioActivo().getNombre();
        etiquetaMatricula.setText(matricula);
        cargarInformacionProyecto();
        cargarHorasAcumuladas();
        cargarActividades();
    }

    @FXML
    private void botonGenerar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Desea generar el reporte final?")) {
            procesarGeneracion();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
            limpiarFormulario();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void cargarInformacionProyecto() {
        try {
            Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante(matricula);
            if (proyecto != null) {
                nombreProyecto = proyecto.getNombreProyecto();
                nombreOrganizacion = proyecto.getNombreOrganizacion();
                etiquetaProyecto.setText(nombreProyecto);
                etiquetaOrganizacion.setText(nombreOrganizacion);
            } else {
                mostrarError("Sin proyecto asignado",
                        "NO SE ENCONTRO PROYECTO PARA LA MATRICULA: " + matricula);
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void cargarHorasAcumuladas() {
        try {
            int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            etiquetaHorasAcumuladas.setText(horasTotales + " horas acumuladas");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al obtener horas acumuladas", excepcion);
        }
    }

    private void cargarActividades() {
        try {
            List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante(matricula);
            ObservableList<String> nombres = FXCollections.observableArrayList();
            for (Actividad actividad : actividades) {
                nombres.add(actividad.getTitulo());
            }
            comboActividadUno.setItems(nombres);
            comboActividadDos.setItems(nombres);
            if (nombres.size() >= 1) {
                comboActividadUno.setValue(nombres.get(nombres.size() - 1));
            }
            if (nombres.size() >= 2) {
                comboActividadDos.setValue(nombres.get(nombres.size() - 2));
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar actividades", excepcion);
            mostrarError("Error al cargar actividades", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean verificarHorasSuficientes() {
        boolean horasSuficientes = false;
        try {
            int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            if (horasTotales >= HORAS_MINIMAS_FINAL) {
                horasSuficientes = true;
            } else {
                mostrarError("Horas insuficientes",
                        "NECESITAS AL MENOS " + HORAS_MINIMAS_FINAL + " HORAS ACUMULADAS PARA GENERAR EL REPORTE FINAL. "
                                + "ACTUALMENTE TIENES: " + horasTotales + " HORAS.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar horas para reporte final", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return horasSuficientes;
    }

    private boolean esNumeroEntreRango(String texto, int minimo, int maximo) {
        boolean esValido = false;
        try {
            int valor = Integer.parseInt(texto);
            esValido = valor >= minimo && valor <= maximo;
        } catch (NumberFormatException excepcion) {
            LOGGER.log(Level.WARNING, "Valor no numerico en campo de avance", excepcion);
        }
        return esValido;
    }

    private boolean verificarCampos() {
        boolean validos = true;
        if (comboActividadUno.getValue() == null || comboActividadUno.getValue().isEmpty()) {
            mostrarError("Campo requerido", "SELECCIONA LA ACTIVIDAD 1.");
            validos = false;
        } else if (campoAvanceUno.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA EL PORCENTAJE DE AVANCE DE LA ACTIVIDAD 1.");
            validos = false;
        } else if (!esNumeroEntreRango(campoAvanceUno.getText().trim(), 0, 100)) {
            mostrarError("Avance invalido", "EL AVANCE DE LA ACTIVIDAD 1 DEBE SER UN NUMERO ENTRE 0 Y 100.");
            validos = false;
        } else if (campoObservacionUno.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA LA OBSERVACION DE LA ACTIVIDAD 1.");
            validos = false;
        } else if (comboActividadDos.getValue() == null || comboActividadDos.getValue().isEmpty()) {
            mostrarError("Campo requerido", "SELECCIONA LA ACTIVIDAD 2.");
            validos = false;
        } else if (campoAvanceDos.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA EL PORCENTAJE DE AVANCE DE LA ACTIVIDAD 2.");
            validos = false;
        } else if (!esNumeroEntreRango(campoAvanceDos.getText().trim(), 0, 100)) {
            mostrarError("Avance invalido", "EL AVANCE DE LA ACTIVIDAD 2 DEBE SER UN NUMERO ENTRE 0 Y 100.");
            validos = false;
        } else if (campoObservacionDos.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA LA OBSERVACION DE LA ACTIVIDAD 2.");
            validos = false;
        } else if (campoEntregableUno.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA EL ENTREGABLE 1.");
            validos = false;
        } else if (campoAvanceEntregableUno.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA EL AVANCE DEL ENTREGABLE 1.");
            validos = false;
        } else if (campoObservacionEntregableUno.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA LA OBSERVACION DEL ENTREGABLE 1.");
            validos = false;
        } else if (campoEntregableDos.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA EL ENTREGABLE 2.");
            validos = false;
        } else if (campoAvanceEntregableDos.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA EL AVANCE DEL ENTREGABLE 2.");
            validos = false;
        } else if (campoObservacionEntregableDos.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA LA OBSERVACION DEL ENTREGABLE 2.");
            validos = false;
        } else if (textoAreaObservacionesGenerales.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA LAS OBSERVACIONES GENERALES.");
            validos = false;
        }
        return validos;
    }

    private void procesarGeneracion() {
        boolean camposCorrectos = verificarCampos();
        boolean horasSuficientes = false;
        if (camposCorrectos) {
            horasSuficientes = verificarHorasSuficientes();
        }
        if (camposCorrectos && horasSuficientes) {
            generarPdf();
        }
    }

    private void generarPdf() {
        String actividades = construirActividades();
        Reporte reporte = new Reporte(TipoReporte.Final, textoAreaObservacionesGenerales.getText().trim(),
                actividades, matricula, null, null);
        GeneradorPdfReporteFinal generador = new GeneradorPdfReporteFinal();
        String rutaPdf = generador.generarPdf(reporte, nombrePracticante, nombreProyecto,
                nombreOrganizacion, String.valueOf(HORAS_MINIMAS_FINAL));
        if (rutaPdf != null) {
            procesarResultadoPdf(reporte, rutaPdf);
        } else {
            mostrarError("Error al generar", "NO SE PUDO GENERAR EL PDF. INTENTE DE NUEVO.");
        }
    }

    private void procesarResultadoPdf(Reporte reporte, String rutaPdf) {
        guardarEnBaseDeDatos(reporte, rutaPdf);
        limpiarFormulario();
        mostrarExito("PDF generado correctamente",
                "EL REPORTE SE GUARDO EN: " + rutaPdf);
    }

    private void guardarEnBaseDeDatos(Reporte reporte, String rutaPdf) {
        try {
            reporte.setArchivoAdjunto(rutaPdf);
            reporteDao.agregarReporte(reporte);
            LOGGER.info("Reporte final guardado en BD correctamente");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar reporte final en BD", excepcion);
            mostrarError("Error al guardar", "NO SE PUDO GUARDAR EL REPORTE EN LA BASE DE DATOS.");
        }
    }

    private String construirActividades() {
        StringBuilder actividades = new StringBuilder();
        actividades.append(comboActividadUno.getValue()).append("|");
        actividades.append(campoAvanceUno.getText().trim()).append("|");
        actividades.append(campoObservacionUno.getText().trim()).append("\n");
        actividades.append(comboActividadDos.getValue()).append("|");
        actividades.append(campoAvanceDos.getText().trim()).append("|");
        actividades.append(campoObservacionDos.getText().trim()).append("\n");
        actividades.append("ENTREGABLE:").append(campoEntregableUno.getText().trim()).append("|");
        actividades.append(campoAvanceEntregableUno.getText().trim()).append("|");
        actividades.append(campoObservacionEntregableUno.getText().trim()).append("\n");
        actividades.append("ENTREGABLE:").append(campoEntregableDos.getText().trim()).append("|");
        actividades.append(campoAvanceEntregableDos.getText().trim()).append("|");
        actividades.append(campoObservacionEntregableDos.getText().trim());
        return actividades.toString();
    }

    private void limpiarFormulario() {
        comboActividadUno.getSelectionModel().clearSelection();
        comboActividadDos.getSelectionModel().clearSelection();
        campoAvanceUno.clear();
        campoObservacionUno.clear();
        campoAvanceDos.clear();
        campoObservacionDos.clear();
        campoEntregableUno.clear();
        campoAvanceEntregableUno.clear();
        campoObservacionEntregableUno.clear();
        campoEntregableDos.clear();
        campoAvanceEntregableDos.clear();
        campoObservacionEntregableDos.clear();
        textoAreaObservacionesGenerales.clear();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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