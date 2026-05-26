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

    @FXML private ComboBox<TipoReporte> comboBoxTipoReporte;
    @FXML private TextField campoDescripcion;
    @FXML private Label etiquetaArchivo;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private static final String CARPETA_UPLOADS = "uploads/";
    private File archivoSeleccionado = null;

    @FXML
    public void initialize() {
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
            ocultarError();
        }
    }

    @FXML
    private void botonSubir(ActionEvent event) {
        ocultarError();
        ocultarExito();

        if (!validarFormulario()) {
            return;
        }

        try {
            String rutaRelativa = copiarPDF(archivoSeleccionado);
            String matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();

            Reporte reporte = new Reporte(
                    comboBoxTipoReporte.getValue(),
                    campoDescripcion.getText().trim(),
                    matricula,
                    rutaRelativa,
                    archivoSeleccionado.getName()
            );

            guardarReporte(reporte);

        } catch (IOException e) {
            mostrarError("Error de archivo", "No se pudo copiar el PDF: " + e.getMessage());
        } catch (MensajeriaExcepcion e) {
            mostrarError("Error", e.getMessage());
        }
    }

    private boolean validarFormulario() {
        if (comboBoxTipoReporte.getValue() == null) {
            mostrarError("Campo requerido", "Selecciona el tipo de reporte.");
            return false;
        }
        if (campoDescripcion.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "La descripción no puede estar vacía.");
            return false;
        }
        if (archivoSeleccionado == null) {
            mostrarError("Archivo requerido", "Debes seleccionar un archivo PDF.");
            return false;
        }
        return true;
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
            mostrarExito("Reporte subido", "Tu reporte fue enviado correctamente.");
            limpiarFormulario();
        } else {
            mostrarError("Error", "No se pudo guardar el reporte.");
        }
    }
    @FXML
    private void botonCancelar() {
        limpiarFormulario();
        ocultarError();
        ocultarExito();
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