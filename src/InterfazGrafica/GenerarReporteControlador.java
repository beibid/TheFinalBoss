package InterfazGrafica;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.Reporte;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.TipoReporte;
import logica.archivos.GeneradorPdfReporte;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GenerarReporteControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarReporteControlador.class.getName());

    @FXML private Label etiquetaMatricula;
    @FXML private Label etiquetaProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private ComboBox<TipoReporte> comboBoxTipoReporte;
    @FXML private TextArea areaDescripcion;
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
        comboBoxTipoReporte.setItems(FXCollections.observableArrayList(TipoReporte.Parcial, TipoReporte.Mensual));
        cargarInformacionProyecto();
    }

    @FXML
    private void botonGenerar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Desea generar el reporte?")) {
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
                mostrarError("Sin proyecto asignado", "No se encontró proyecto para la matrícula: "
                        + matricula);
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar proyecto", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }


    private boolean camposVacios(List<String> campos){
        boolean hayCamposVacios = false;

        for (String campo : campos){
            if (campo.isEmpty()){
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private boolean camposValidos() {
        boolean tipoSeleccionado = comboBoxTipoReporte.getValue() != null;
        String descripcion = areaDescripcion.getText().trim();
        List<String> campos = List.of(descripcion);
        boolean camposTextosValidos = !camposVacios(campos);

        if (!tipoSeleccionado) {
            mostrarError("Tipo requerido", "Selecciona el tipo de reporte.");
        }
        if (!camposTextosValidos) {
            mostrarError("Campo requerido", "La descripción no puede estar vacía.");
        }
        return tipoSeleccionado && camposTextosValidos;
    }

    private void generarPdf() {
        Reporte reporte = new Reporte(comboBoxTipoReporte.getValue(), areaDescripcion.getText().trim(),
                matricula, null, null );

        GeneradorPdfReporte generadorPdf = new GeneradorPdfReporte();
        String rutaPdf = generadorPdf.generarPdf(reporte, nombrePracticante, nombreProyecto, nombreOrganizacion);
        if (rutaPdf != null) {
            limpiarFormulario();
            mostrarExito("PDF generado correctamente", "El reporte se guardó en: " + rutaPdf);
        } else {
            mostrarError("Error al generar", "No se pudo generar el PDF. Intente de nuevo.");
        }
    }

    private void procesarGeneracion() {
        if (camposValidos()) {
            generarPdf();
        }
    }

    private void limpiarFormulario() {
        comboBoxTipoReporte.getSelectionModel().clearSelection();
        areaDescripcion.clear();
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

