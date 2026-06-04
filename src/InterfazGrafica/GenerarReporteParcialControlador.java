package InterfazGrafica;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.archivos.GeneradorPdfReporteParcial;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ProyectoDao;
import logica.dominio.Proyecto;
import logica.dominio.Reporte;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.TipoReporte;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GenerarReporteParcialControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarReporteParcialControlador.class.getName());

    @FXML private Label etiquetaMatricula;
    @FXML private Label etiquetaProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private TextArea textoAreaDescripcion;
    @FXML private TextField campoNombreActividadUno;
    @FXML private TextArea campoDescripcionActividadUno;
    @FXML private TextField campoTiempoPlaneadoUno;
    @FXML private TextField campoTiempoRealUno;
    @FXML private TextField campoMesUno;
    @FXML private TextField campoSemanaUno;
    @FXML private TextField campoNombreActividadDos;
    @FXML private TextArea campoDescripcionActividadDos;
    @FXML private TextField campoTiempoPlaneadoDos;
    @FXML private TextField campoTiempoRealDos;
    @FXML private TextField campoMesDos;
    @FXML private TextField campoSemanaDos;
    @FXML private TextField campoNombreActividadTres;
    @FXML private TextArea campoDescripcionActividadTres;
    @FXML private TextField campoTiempoPlaneadoTres;
    @FXML private TextField campoTiempoRealTres;
    @FXML private TextField campoMesTres;
    @FXML private TextField campoSemanaTres;
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
    }

    @FXML
    private void botonGenerar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Desea generar el reporte parcial?")) {
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

    private boolean camposVacios(List<String> campos) {
        boolean hayCamposVacios = false;
        for (String campo : campos) {
            if (campo.isEmpty()) {
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private boolean camposValidos() {
        String descripcion = textoAreaDescripcion.getText().trim();
        String nombreUno = campoNombreActividadUno.getText().trim();
        String descripcionUno = campoDescripcionActividadUno.getText().trim();
        String tiempoPlaneadoUno = campoTiempoPlaneadoUno.getText().trim();
        String tiempoRealUno = campoTiempoRealUno.getText().trim();
        String mesUno = campoMesUno.getText().trim();
        String semanaUno = campoSemanaUno.getText().trim();
        String nombreDos = campoNombreActividadDos.getText().trim();
        String descripcionDos = campoDescripcionActividadDos.getText().trim();
        String tiempoPlaneadoDos = campoTiempoPlaneadoDos.getText().trim();
        String tiempoRealDos = campoTiempoRealDos.getText().trim();
        String mesDos = campoMesDos.getText().trim();
        String semanaDos = campoSemanaDos.getText().trim();
        String nombreTres = campoNombreActividadTres.getText().trim();
        String descripcionTres = campoDescripcionActividadTres.getText().trim();
        String tiempoPlaneadoTres = campoTiempoPlaneadoTres.getText().trim();
        String tiempoRealTres = campoTiempoRealTres.getText().trim();
        String mesTres = campoMesTres.getText().trim();
        String semanaTres = campoSemanaTres.getText().trim();

        List<String> campos = List.of(descripcion, nombreUno, descripcionUno, tiempoPlaneadoUno,
                tiempoRealUno, mesUno, semanaUno, nombreDos, descripcionDos, tiempoPlaneadoDos,
                tiempoRealDos, mesDos, semanaDos, nombreTres, descripcionTres, tiempoPlaneadoTres,
                tiempoRealTres, mesTres, semanaTres);

        boolean validos = !camposVacios(campos);
        if (!validos) {
            mostrarError("Campo requerido", "Todos los campos son obligatorios.");
        }
        return validos;
    }

    private void generarPdf() {
        String actividades = construirActividades();
        Reporte reporte = new Reporte(TipoReporte.Parcial, textoAreaDescripcion.getText().trim(),
                actividades, matricula, null, null);

        GeneradorPdfReporteParcial generador = new GeneradorPdfReporteParcial();
        String rutaPdf = generador.generarPdf(reporte, nombrePracticante, nombreProyecto, nombreOrganizacion, "210");

        if (rutaPdf != null) {
            limpiarFormulario();
            mostrarExito("PDF generado correctamente", "El reporte se guardó en: " + rutaPdf);
        } else {
            mostrarError("Error al generar", "No se pudo generar el PDF. Intente de nuevo.");
        }
    }

    private String construirActividades() {
        String actividadUno = campoNombreActividadUno.getText().trim() + "|" +
                campoDescripcionActividadUno.getText().trim() + "|" +
                campoTiempoPlaneadoUno.getText().trim() + "|" +
                campoTiempoRealUno.getText().trim() + "|" +
                campoMesUno.getText().trim() + "|" +
                campoSemanaUno.getText().trim();

        String actividadDos = campoNombreActividadDos.getText().trim() + "|" +
                campoDescripcionActividadDos.getText().trim() + "|" +
                campoTiempoPlaneadoDos.getText().trim() + "|" +
                campoTiempoRealDos.getText().trim() + "|" +
                campoMesDos.getText().trim() + "|" +
                campoSemanaDos.getText().trim();

        String actividadTres = campoNombreActividadTres.getText().trim() + "|" +
                campoDescripcionActividadTres.getText().trim() + "|" +
                campoTiempoPlaneadoTres.getText().trim() + "|" +
                campoTiempoRealTres.getText().trim() + "|" +
                campoMesTres.getText().trim() + "|" +
                campoSemanaTres.getText().trim();

        return actividadUno + "\n" + actividadDos + "\n" + actividadTres;
    }

    private void procesarGeneracion() {
        if (camposValidos()) {
            generarPdf();
        }
    }

    private void limpiarFormulario() {
        textoAreaDescripcion.clear();
        campoNombreActividadUno.clear();
        campoDescripcionActividadUno.clear();
        campoTiempoPlaneadoUno.clear();
        campoTiempoRealUno.clear();
        campoMesUno.clear();
        campoSemanaUno.clear();
        campoNombreActividadDos.clear();
        campoDescripcionActividadDos.clear();
        campoTiempoPlaneadoDos.clear();
        campoTiempoRealDos.clear();
        campoMesDos.clear();
        campoSemanaDos.clear();
        campoNombreActividadTres.clear();
        campoDescripcionActividadTres.clear();
        campoTiempoPlaneadoTres.clear();
        campoTiempoRealTres.clear();
        campoMesTres.clear();
        campoSemanaTres.clear();
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
