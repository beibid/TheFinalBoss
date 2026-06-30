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
import logica.dao.objetos.ActividadDao;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubirReporteControlador {

    private static final Logger LOGGER = Logger.getLogger(SubirReporteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int HORAS_MINIMAS_PARCIAL = 210;
    private static final int HORAS_MINIMAS_FINAL = 420;
    private static final String CARPETA_UPLOADS = "uploads/";

    private final ActividadDao actividadDao = new ActividadDao();
    private final ReporteDao reporteDao = new ReporteDao();

    @FXML private ComboBox<TipoReporte> comboBoxTipoReporte;
    @FXML private ComboBox<String> comboBoxMes;
    @FXML private TextField campoTextoDescripcion;
    @FXML private Label etiquetaArchivo;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private File archivoSeleccionado = null;
    private String matricula;

    @FXML
    public void initialize() {
        matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        comboBoxTipoReporte.getItems().addAll(TipoReporte.values());
        comboBoxMes.getItems().addAll(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        );
        comboBoxMes.setDisable(false);
    }

    @FXML
    private void botonSeleccionarPDF(ActionEvent evento) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(escenario);
        boolean archivoElegido = archivo != null;
        if (archivoElegido) {
            archivoSeleccionado = archivo;
            etiquetaArchivo.setText(archivo.getName());
            ocultarPanel(panelError);
        }
    }

    @FXML
    private void botonSubir(ActionEvent evento) {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        boolean formularioValido = verificarCampos();
        if (formularioValido) {
            procesarSubidaDeReporte();
        }
    }

    private void procesarSubidaDeReporte() {
        TipoReporte tipoSeleccionado = comboBoxTipoReporte.getValue();
        boolean horasValidas = verificarHorasSegunTipo(tipoSeleccionado);
        if (horasValidas) {
            try {
                String rutaRelativa = copiarPDF(archivoSeleccionado);
                String etiquetaMes = obtenerEtiquetaMes(tipoSeleccionado);
                Reporte reporte = new Reporte(
                        tipoSeleccionado,
                        campoTextoDescripcion.getText().trim(),
                        etiquetaMes,
                        matricula,
                        rutaRelativa,
                        archivoSeleccionado.getName()
                );
                guardarReporte(reporte);
            } catch (IOException excepcion) {
                LOGGER.log(Level.SEVERE, "Error al copiar el PDF", excepcion);
                mostrarError("Error de archivo", "NO SE PUDO COPIAR EL PDF.");
            } catch (MensajeriaExcepcion excepcion) {
                LOGGER.log(Level.SEVERE, "Error al guardar el reporte", excepcion);
                mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
            }
        }
    }

    private boolean verificarHorasSegunTipo(TipoReporte tipoSeleccionado) {
        boolean horasValidas = true;
        if (tipoSeleccionado == TipoReporte.Parcial) {
            horasValidas = verificarHorasParaParcial();
        } else if (tipoSeleccionado == TipoReporte.Final) {
            horasValidas = verificarHorasParaFinal();
        }
        return horasValidas;
    }

    private String obtenerEtiquetaMes(TipoReporte tipoSeleccionado) {
        String mesSeleccionado = comboBoxMes.getValue();
        String etiquetaMes = tipoSeleccionado.toString();
        boolean mesElegido = mesSeleccionado != null;
        if (mesElegido) {
            etiquetaMes = mesSeleccionado;
        }
        return etiquetaMes;
    }

    private boolean verificarCampos() {
        boolean esValido = true;
        if (comboBoxTipoReporte.getValue() == null) {
            mostrarError("Campo requerido", "SELECCIONA EL TIPO DE REPORTE.");
            esValido = false;
        } else if (campoTextoDescripcion.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "LA DESCRIPCION NO PUEDE ESTAR VACIA.");
            esValido = false;
        } else if (archivoSeleccionado == null) {
            mostrarError("Archivo requerido", "DEBES SELECCIONAR UN ARCHIVO PDF.");
            esValido = false;
        }
        return esValido;
    }

    private boolean verificarHorasParaParcial() {
        boolean puedeSubir = false;
        try {
            int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            if (horasTotales >= HORAS_MINIMAS_PARCIAL) {
                puedeSubir = true;
            } else {
                mostrarError("Horas insuficientes",
                        "NECESITAS AL MENOS " + HORAS_MINIMAS_PARCIAL +
                                " HORAS ACUMULADAS. TIENES: " + horasTotales + " HORAS.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar horas para parcial", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return puedeSubir;
    }

    private boolean verificarHorasParaFinal() {
        boolean puedeSubir = false;
        try {
            int horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            if (horasTotales >= HORAS_MINIMAS_FINAL) {
                puedeSubir = true;
            } else {
                mostrarError("Horas insuficientes",
                        "NECESITAS AL MENOS " + HORAS_MINIMAS_FINAL +
                                " HORAS ACUMULADAS. TIENES: " + horasTotales + " HORAS.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar horas para final", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return puedeSubir;
    }

    private String copiarPDF(File archivo) throws IOException {
        File carpeta = new File(CARPETA_UPLOADS);
        boolean carpetaExiste = carpeta.exists();
        if (!carpetaExiste) {
            carpeta.mkdirs();
        }
        Path destino = Path.of(CARPETA_UPLOADS + archivo.getName());
        Files.copy(archivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return CARPETA_UPLOADS + archivo.getName();
    }

    private void guardarReporte(Reporte reporte) throws MensajeriaExcepcion {
        int resultado = reporteDao.agregarReporte(reporte);
        if (resultado >= FILAS_AFECTADAS_ESPERADAS) {
            limpiarFormulario();
            mostrarExito("Reporte subido", "TU REPORTE FUE ENVIADO CORRECTAMENTE.");
        } else {
            mostrarError("Error", "NO SE PUDO GUARDAR EL REPORTE.");
        }
    }

    @FXML
    private void botonCancelar() {
        limpiarFormulario();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void limpiarFormulario() {
        campoTextoDescripcion.clear();
        comboBoxTipoReporte.getSelectionModel().clearSelection();
        comboBoxMes.getSelectionModel().clearSelection();
        etiquetaArchivo.setText("Ningun archivo seleccionado");
        archivoSeleccionado = null;
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