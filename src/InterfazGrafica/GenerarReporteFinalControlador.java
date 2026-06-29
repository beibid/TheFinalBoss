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
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerarReporteFinalControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarReporteFinalControlador.class.getName());
    private static final int HORAS_MINIMAS_FINAL = 420;
    private static final String HORAS_FINAL_TEXTO = "420";

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
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Desea generar el reporte final?")) {
            procesarGeneracion();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarFormulario();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void cargarInformacionProyecto() {
        ProyectoDao proyectoDao = new ProyectoDao();
        try {
            Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante(matricula);
            if (proyecto != null) {
                nombreProyecto = proyecto.getNombreProyecto();
                nombreOrganizacion = proyecto.getNombreOrganizacion();
                etiquetaProyecto.setText(nombreProyecto);
                etiquetaOrganizacion.setText(nombreOrganizacion);
            } else {
                mostrarError("Sin proyecto asignado", "No se encontró proyecto para la matrícula: " + matricula);
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void cargarHorasAcumuladas() {
        ActividadDao actividadDao = new ActividadDao();
        try {
            int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            etiquetaHorasAcumuladas.setText(horasTotales + " horas acumuladas");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al obtener horas acumuladas", excepcion);
        }
    }

    private void cargarActividades() {
        ActividadDao actividadDao = new ActividadDao();
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
        ActividadDao actividadDao = new ActividadDao();
        try {
            int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            if (horasTotales >= HORAS_MINIMAS_FINAL) {
                horasSuficientes = true;
            } else {
                mostrarError("Horas insuficientes",
                        "NECESITAS AL MENOS " + HORAS_MINIMAS_FINAL + " HORAS ACUMULADAS PARA GENERAR EL REPORTE FINAL. " +
                                "ACTUALMENTE TIENES: " + horasTotales + " HORAS.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar horas para reporte final", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return horasSuficientes;
    }

    private boolean camposValidos() {
        boolean validos = true;

        if (comboActividadUno.getValue() == null || comboActividadUno.getValue().isEmpty()) {
            mostrarError("Campo requerido", "SELECCIONA LA ACTIVIDAD 1.");
            validos = false;
        } else if (campoAvanceUno.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "INGRESA EL PORCENTAJE DE AVANCE DE LA ACTIVIDAD 1.");
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
        if (camposValidos()) {
            if (verificarHorasSuficientes()) {
                generarPdf();
            }
        }
    }

    private void generarPdf() {
        String actividades = construirActividades();
        Reporte reporte = new Reporte(TipoReporte.Final, textoAreaObservacionesGenerales.getText().trim(),
                actividades, matricula, null, null);

        GeneradorPdfReporteFinal generador = new GeneradorPdfReporteFinal();
        String rutaPdf = generador.generarPdf(reporte, nombrePracticante, nombreProyecto,
                nombreOrganizacion, HORAS_FINAL_TEXTO);

        if (rutaPdf != null) {
            guardarEnBaseDeDatos(reporte, rutaPdf);
            limpiarFormulario();
            mostrarExito("PDF generado correctamente", "El reporte se guardó en: " + rutaPdf);
        } else {
            mostrarError("Error al generar", "No se pudo generar el PDF. Intente de nuevo.");
        }
    }

    private void guardarEnBaseDeDatos(Reporte reporte, String rutaPdf) {
        ReporteDao reporteDao = new ReporteDao();
        try {
            reporte.setArchivoAdjunto(rutaPdf);
            reporteDao.agregarReporte(reporte);
            LOGGER.info("Reporte final guardado en BD correctamente");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar reporte final en BD", excepcion);
        }
    }

    private String construirActividades() {
        return comboActividadUno.getValue() + "|" +
                campoAvanceUno.getText().trim() + "|" +
                campoObservacionUno.getText().trim() + "\n" +
                comboActividadDos.getValue() + "|" +
                campoAvanceDos.getText().trim() + "|" +
                campoObservacionDos.getText().trim() + "\n" +
                "ENTREGABLE:" + campoEntregableUno.getText().trim() + "|" +
                campoAvanceEntregableUno.getText().trim() + "|" +
                campoObservacionEntregableUno.getText().trim() + "\n" +
                "ENTREGABLE:" + campoEntregableDos.getText().trim() + "|" +
                campoAvanceEntregableDos.getText().trim() + "|" +
                campoObservacionEntregableDos.getText().trim();
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
        ocultarError();
        ocultarExito();
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

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
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
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}