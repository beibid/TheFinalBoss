package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import logica.dao.objetos.ReporteDao;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dominio.Reporte;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.TipoReporte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SubirReporteControlador {

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int REPORTES_MENSUALES_PARA_PARCIAL = 3;

    @FXML private ComboBox<TipoReporte> comboBoxTipoReporte;
    @FXML private TextField campoDescripcion;
    @FXML private TextField campoActividades;
    @FXML private Label etiquetaArchivo;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private static final String CARPETA_UPLOADS = "uploads/";
    private File archivoSeleccionado = null;
    private String matricula;

    @FXML
    public void initialize() {
        matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        comboBoxTipoReporte.getItems().addAll(TipoReporte.values());
    }

    @FXML
    private void botonSeleccionarPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(escenario);
        if (archivo != null) {
            archivoSeleccionado = archivo;
            etiquetaArchivo.setText(archivo.getName());
            ocultarPanel(panelError);
        }
    }

    @FXML
    private void botonSubir(ActionEvent event) {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (!validarFormulario()) {
            return;
        }
        if (comboBoxTipoReporte.getValue() == TipoReporte.Mensual) {
            if (verificarReporteMensualMesActual()) {
                return;
            }
        } else if (comboBoxTipoReporte.getValue() == TipoReporte.Parcial) {
            if (!verificarPuedeGenerarParcial()) {
                return;
            }
        }
        try {
            String rutaRelativa = copiarPDF(archivoSeleccionado);
            Reporte reporte = new Reporte(
                    comboBoxTipoReporte.getValue(),
                    campoDescripcion.getText().trim(),
                    campoActividades.getText().trim(),
                    matricula,
                    rutaRelativa,
                    archivoSeleccionado.getName()
            );
            guardarReporte(reporte);
        } catch (IOException e) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error de archivo",
                    "No se pudo copiar el PDF: " + e.getMessage());
        } catch (MensajeriaExcepcion e) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error", e.getMessage());
        }
    }

    private boolean validarFormulario() {
        boolean formularioValido = true;
        if (comboBoxTipoReporte.getValue() == null) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Campo requerido",
                    "Selecciona el tipo de reporte.");
            formularioValido = false;
        } else if (campoDescripcion.getText().trim().isEmpty()) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Campo requerido",
                    "La descripción no puede estar vacía.");
            formularioValido = false;
        } else if (archivoSeleccionado == null) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Archivo requerido",
                    "Debes seleccionar un archivo PDF.");
            formularioValido = false;
        }
        return formularioValido;
    }

    private boolean verificarReporteMensualMesActual() {
        boolean yaExiste = false;
        ReporteDao reporteDao = new ReporteDao();
        try {
            if (reporteDao.existeReporteMensualEnMesActual(matricula)) {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Reporte duplicado", "YA ENTREGASTE UN REPORTE MENSUAL ESTE MES");
                yaExiste = true;
            }
        } catch (MensajeriaExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
            yaExiste = true;
        }
        return yaExiste;
    }

    private boolean verificarPuedeGenerarParcial() {
        boolean puedeGenerar = false;
        ReporteDao reporteDao = new ReporteDao();
        try {
            int mensualesEvaluados = reporteDao.contarReportesEvaluados(matricula, "Mensual");
            int parcialesEvaluados = reporteDao.contarReportesEvaluados(matricula, "Parcial");
            if (mensualesEvaluados >= REPORTES_MENSUALES_PARA_PARCIAL && parcialesEvaluados == 0) {
                puedeGenerar = true;
            } else if (mensualesEvaluados >= REPORTES_MENSUALES_PARA_PARCIAL * 2 && parcialesEvaluados == 1) {
                puedeGenerar = true;
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Reportes incompletos", "NO CUMPLES CON LOS REQUISITOS PARA GENERAR EL REPORTE PARCIAL");
            }
        } catch (MensajeriaExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return puedeGenerar;
    }

    private String copiarPDF(File archivo) throws IOException {
        File carpeta = new File(CARPETA_UPLOADS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        Path destino = Path.of(CARPETA_UPLOADS + archivo.getName());
        Files.copy(archivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return CARPETA_UPLOADS + archivo.getName();
    }

    private void guardarReporte(Reporte reporte) throws MensajeriaExcepcion {
        ReporteDao dao = new ReporteDao();
        int resultado = dao.agregarReporte(reporte);
        if (resultado >= FILAS_AFECTADAS_ESPERADAS) {
            mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito, "Reporte subido",
                    "Tu reporte fue enviado correctamente.");
            limpiarFormulario();
        } else {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error",
                    "No se pudo guardar el reporte.");
        }
    }

    @FXML
    private void botonCancelar() {
        limpiarFormulario();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        campoDescripcion.clear();
        comboBoxTipoReporte.getSelectionModel().clearSelection();
        etiquetaArchivo.setText("Ningún archivo seleccionado");
        archivoSeleccionado = null;
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