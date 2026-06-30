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

public class GenerarReporteParcialControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarReporteParcialControlador.class.getName());
    private static final int HORAS_PARA_PARCIAL = 210;

    private final ProyectoDao proyectoDao = new ProyectoDao();
    private final ActividadDao actividadDao = new ActividadDao();
    private final ReporteDao reporteDao = new ReporteDao();

    @FXML private Label etiquetaMatricula;
    @FXML private Label etiquetaProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private Label etiquetaHorasAcumuladas;
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
        cargarHorasAcumuladas();
        precargarActividades();
    }

    @FXML
    private void botonGenerar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Desea generar el reporte parcial?")) {
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
            if (etiquetaHorasAcumuladas != null) {
                etiquetaHorasAcumuladas.setText(horasTotales + " horas acumuladas");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al obtener horas acumuladas", excepcion);
        }
    }

    private void precargarActividades() {
        try {
            List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticante(matricula);
            if (actividades.size() >= 1) {
                Actividad actividadUno = actividades.get(0);
                campoNombreActividadUno.setText(actividadUno.getTitulo());
                campoDescripcionActividadUno.setText(actividadUno.getDescripcion());
            }
            if (actividades.size() >= 2) {
                Actividad actividadDos = actividades.get(1);
                campoNombreActividadDos.setText(actividadDos.getTitulo());
                campoDescripcionActividadDos.setText(actividadDos.getDescripcion());
            }
            if (actividades.size() >= 3) {
                Actividad actividadTres = actividades.get(2);
                campoNombreActividadTres.setText(actividadTres.getTitulo());
                campoDescripcionActividadTres.setText(actividadTres.getDescripcion());
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al precargar actividades en el reporte parcial", excepcion);
        }
    }

    private boolean verificarHorasSuficientes() {
        boolean horasSuficientes = false;
        try {
            int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            if (horasTotales >= HORAS_PARA_PARCIAL) {
                horasSuficientes = true;
            } else {
                mostrarError("Horas insuficientes",
                        "NECESITAS AL MENOS " + HORAS_PARA_PARCIAL + " HORAS ACUMULADAS. "
                                + "ACTUALMENTE TIENES: " + horasTotales + " HORAS.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar horas para reporte parcial", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return horasSuficientes;
    }

    private boolean esNumerico(String texto) {
        boolean esValido = false;
        try {
            Integer.parseInt(texto);
            esValido = true;
        } catch (NumberFormatException excepcion) {
            LOGGER.log(Level.WARNING, "Valor no numerico en campo de tiempo", excepcion);
        }
        return esValido;
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

    private boolean verificarCampos() {
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
        boolean tiemposNumericos = esNumerico(tiempoPlaneadoUno) && esNumerico(tiempoRealUno)
                && esNumerico(tiempoPlaneadoDos) && esNumerico(tiempoRealDos)
                && esNumerico(tiempoPlaneadoTres) && esNumerico(tiempoRealTres);
        boolean validos = true;
        if (camposVacios(campos)) {
            mostrarError("Campo requerido", "TODOS LOS CAMPOS SON OBLIGATORIOS.");
            validos = false;
        } else if (!tiemposNumericos) {
            mostrarError("Tiempo invalido",
                    "LOS CAMPOS DE TIEMPO PLANEADO Y TIEMPO REAL DEBEN SER NUMEROS ENTEROS.");
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
        Reporte reporte = new Reporte(TipoReporte.Parcial, textoAreaDescripcion.getText().trim(),
                actividades, matricula, null, null);
        GeneradorPdfReporteParcial generador = new GeneradorPdfReporteParcial();
        String rutaPdf = generador.generarPdf(reporte, nombrePracticante, nombreProyecto,
                nombreOrganizacion, String.valueOf(HORAS_PARA_PARCIAL));
        if (rutaPdf != null) {
            procesarResultadoPdf(reporte, rutaPdf);
        } else {
            mostrarError("Error al generar", "NO SE PUDO GENERAR EL PDF. INTENTE DE NUEVO.");
        }
    }

    private void procesarResultadoPdf(Reporte reporte, String rutaPdf) {
        guardarEnBaseDeDatos(reporte, rutaPdf);
        limpiarFormulario();
        mostrarExito("PDF generado correctamente", "EL REPORTE SE GUARDO EN: " + rutaPdf);
    }

    private void guardarEnBaseDeDatos(Reporte reporte, String rutaPdf) {
        try {
            reporte.setArchivoAdjunto(rutaPdf);
            reporteDao.agregarReporte(reporte);
            LOGGER.info("Reporte parcial guardado en BD correctamente");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar reporte parcial en BD", excepcion);
            mostrarError("Error al guardar", "NO SE PUDO GUARDAR EL REPORTE EN LA BASE DE DATOS.");
        }
    }

    private String construirActividades() {
        StringBuilder actividades = new StringBuilder();
        actividades.append(campoNombreActividadUno.getText().trim()).append("|");
        actividades.append(campoDescripcionActividadUno.getText().trim()).append("|");
        actividades.append(campoTiempoPlaneadoUno.getText().trim()).append("|");
        actividades.append(campoTiempoRealUno.getText().trim()).append("|");
        actividades.append(campoMesUno.getText().trim()).append("|");
        actividades.append(campoSemanaUno.getText().trim()).append("\n");
        actividades.append(campoNombreActividadDos.getText().trim()).append("|");
        actividades.append(campoDescripcionActividadDos.getText().trim()).append("|");
        actividades.append(campoTiempoPlaneadoDos.getText().trim()).append("|");
        actividades.append(campoTiempoRealDos.getText().trim()).append("|");
        actividades.append(campoMesDos.getText().trim()).append("|");
        actividades.append(campoSemanaDos.getText().trim()).append("\n");
        actividades.append(campoNombreActividadTres.getText().trim()).append("|");
        actividades.append(campoDescripcionActividadTres.getText().trim()).append("|");
        actividades.append(campoTiempoPlaneadoTres.getText().trim()).append("|");
        actividades.append(campoTiempoRealTres.getText().trim()).append("|");
        actividades.append(campoMesTres.getText().trim()).append("|");
        actividades.append(campoSemanaTres.getText().trim());
        return actividades.toString();
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