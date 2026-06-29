package InterfazGrafica;

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
import logica.archivos.GeneradorPdfReporteMensual;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ActividadDao;
import logica.dao.objetos.ProyectoDao;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Actividad;
import logica.dominio.Proyecto;
import logica.dominio.Reporte;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.TipoReporte;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerarReporteMensualControlador {

    private static final Logger LOGGER = Logger.getLogger(GenerarReporteMensualControlador.class.getName());
    private static final String[] MESES = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
    private static final Map<String, Integer> NUMERO_MES = Map.ofEntries(
            Map.entry("Enero", 1), Map.entry("Febrero", 2), Map.entry("Marzo", 3),
            Map.entry("Abril", 4), Map.entry("Mayo", 5), Map.entry("Junio", 6),
            Map.entry("Julio", 7), Map.entry("Agosto", 8), Map.entry("Septiembre", 9),
            Map.entry("Octubre", 10), Map.entry("Noviembre", 11), Map.entry("Diciembre", 12)
    );

    @FXML private Label etiquetaMatricula;
    @FXML private Label etiquetaProyecto;
    @FXML private Label etiquetaOrganizacion;
    @FXML private Label etiquetaHorasAcumuladas;
    @FXML private Label etiquetaActividadUno;
    @FXML private Label etiquetaActividadDos;
    @FXML private Label etiquetaActividadTres;
    @FXML private Label etiquetaActividadCuatro;
    @FXML private Label etiquetaActividadCinco;
    @FXML private Label etiquetaActividadSeis;
    @FXML private Label etiquetaActividadSiete;
    @FXML private ComboBox<String> comboMes;
    @FXML private TextArea textoAreaDescripcion;
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
    private Label[] etiquetasActividades;

    @FXML
    public void initialize() {
        matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        nombrePracticante = SesionUsuario.getInstance().getUsuarioActivo().getNombre();
        etiquetaMatricula.setText(matricula);
        inicializarArregloEtiquetas();
        cargarMeses();
        cargarInformacionProyecto();
        cargarHorasAcumuladas();
        comboMes.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, mesAnterior, mesNuevo) -> {
                    if (mesNuevo != null) {
                        cargarActividadesDelMes(mesNuevo);
                    }
                }
        );
    }

    @FXML
    private void botonGenerar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Desea generar el reporte mensual?")) {
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

    private void inicializarArregloEtiquetas() {
        etiquetasActividades = new Label[]{
                etiquetaActividadUno, etiquetaActividadDos, etiquetaActividadTres,
                etiquetaActividadCuatro, etiquetaActividadCinco, etiquetaActividadSeis,
                etiquetaActividadSiete
        };
    }

    private void cargarMeses() {
        for (String mes : MESES) {
            comboMes.getItems().add(mes);
        }
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
            if (etiquetaHorasAcumuladas != null) {
                etiquetaHorasAcumuladas.setText(String.valueOf(horasTotales));
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al obtener horas acumuladas", excepcion);
        }
    }

    private void cargarActividadesDelMes(String mesSeleccionado) {
        ActividadDao actividadDao = new ActividadDao();
        limpiarEtiquetasActividades();
        try {
            int mes = NUMERO_MES.getOrDefault(mesSeleccionado, 0);
            int anio = LocalDate.now().getYear();
            List<Actividad> actividades = actividadDao.obtenerActividadesPorPracticanteYMes(matricula, mes, anio);
            int indice = 0;
            for (Actividad actividad : actividades) {
                if (indice >= etiquetasActividades.length) {
                    break;
                }
                etiquetasActividades[indice].setText(actividad.getTitulo());
                indice++;
            }
            if (actividades.isEmpty()) {
                mostrarError("Sin actividades", "No tienes actividades registradas en " + mesSeleccionado + ".");
            } else {
                ocultarError();
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar actividades del mes", excepcion);
            mostrarError("Error al cargar actividades", excepcion.getMessage().toUpperCase());
        }
    }

    private void limpiarEtiquetasActividades() {
        for (Label etiqueta : etiquetasActividades) {
            etiqueta.setText("");
        }
    }

    private boolean tieneMesSeleccionado() {
        return comboMes.getSelectionModel().getSelectedItem() != null;
    }

    private boolean tieneAlMenosUnaActividad() {
        boolean tieneActividad = false;
        for (Label etiqueta : etiquetasActividades) {
            if (!etiqueta.getText().trim().isEmpty()) {
                tieneActividad = true;
            }
        }
        return tieneActividad;
    }

    private boolean verificarReporteDuplicado() {
        boolean yaExiste = false;
        String mesSeleccionado = comboMes.getSelectionModel().getSelectedItem();
        if (mesSeleccionado == null) {
            return false;
        }
        int mes = NUMERO_MES.getOrDefault(mesSeleccionado, 0);
        int anio = java.time.LocalDate.now().getYear();
        ReporteDao reporteDao = new ReporteDao();
        try {
            if (reporteDao.existeReporteMensualEnMes(matricula, mes, anio)) {
                mostrarError("Reporte duplicado", "YA GENERASTE UN REPORTE MENSUAL PARA " +
                        mesSeleccionado.toUpperCase() + ".");
                yaExiste = true;
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar reporte duplicado", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
            yaExiste = true;
        }
        return yaExiste;
    }

    private boolean camposValidos() {
        boolean validos = true;
        if (!tieneMesSeleccionado()) {
            mostrarError("Campo requerido", "Selecciona un mes antes de generar el reporte.");
            validos = false;
        } else if (!tieneAlMenosUnaActividad()) {
            mostrarError("Sin actividades", "No tienes actividades registradas en el mes seleccionado.");
            validos = false;
        } else if (textoAreaDescripcion.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "La descripción es obligatoria.");
            validos = false;
        }
        return validos;
    }

    private void procesarGeneracion() {
        if (camposValidos()) {
            if (!verificarReporteDuplicado()) {
                generarPdf();
            }
        }
    }

    private void generarPdf() {
        StringBuilder actividades = new StringBuilder();
        for (int i = 0; i < etiquetasActividades.length; i++) {
            String texto = etiquetasActividades[i].getText().trim();
            if (!texto.isEmpty()) {
                if (actividades.length() > 0) {
                    actividades.append("\n");
                }
                actividades.append("Actividad ").append(i + 1).append(": ").append(texto);
            }
        }

        Reporte reporte = new Reporte(TipoReporte.Mensual, textoAreaDescripcion.getText().trim(),
                actividades.toString(), matricula, null, null);

        GeneradorPdfReporteMensual generador = new GeneradorPdfReporteMensual();
        String rutaPdf = generador.generarPdf(reporte, nombrePracticante, nombreProyecto, nombreOrganizacion);

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
            LOGGER.info("Reporte mensual guardado en BD correctamente");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar reporte mensual en BD", excepcion);
        }
    }

    private void limpiarFormulario() {
        comboMes.getSelectionModel().clearSelection();
        textoAreaDescripcion.clear();
        limpiarEtiquetasActividades();
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