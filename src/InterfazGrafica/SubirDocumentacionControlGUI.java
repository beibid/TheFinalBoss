package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.DocumentacionPracticanteDao;
import logica.dao.objetos.EntregaDocumentacionDao;
import logica.dominio.DocumentacionPracticante;
import logica.dominio.EntregaDocumentacion;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.EstadoRevision;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubirDocumentacionControlGUI {

    private static final Logger LOGGER = Logger.getLogger(SubirDocumentacionControlGUI.class.getName());
    private static final String CARPETA_UPLOADS = "uploads/documentacion/";

    @FXML private Label etiquetaArchivo;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private File archivoSeleccionado = null;
    private DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
    private EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();

    @FXML
    private void botonSeleccionarPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);
        if (archivo != null) {
            archivoSeleccionado = archivo;
            etiquetaArchivo.setText(archivo.getName());
            ocultarError();
        }
    }

    @FXML
    private void botonSubir() {
        ocultarError();
        ocultarExito();
        if (!archivoSeleccionado()) {
            return;
        }
        procesarSubida();
    }

    private boolean archivoSeleccionado() {
        if (archivoSeleccionado == null) {
            mostrarError("Archivo requerido", "DEBES SELECCIONAR UN ARCHIVO PDF.");
            return false;
        }
        return true;
    }

    private void procesarSubida() {
        String rutaRelativa = copiarArchivo();
        if (rutaRelativa == null) {
            return;
        }
        guardarDocumentacion(rutaRelativa);
    }

    private String copiarArchivo() {
        try {
            File carpeta = new File(CARPETA_UPLOADS);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            Path destino = Path.of(CARPETA_UPLOADS + archivoSeleccionado.getName());
            Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            return CARPETA_UPLOADS + archivoSeleccionado.getName();
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al copiar el archivo", excepcion);
            mostrarError("Error de archivo", "NO SE PUDO COPIAR EL PDF.");
            return null;
        }
    }

    private void guardarDocumentacion(String rutaRelativa) {
        try {
            DocumentacionPracticante documentacion = new DocumentacionPracticante(
                    rutaRelativa, EstadoRevision.Pendiente
            );
            int idDocumentacion = documentacionDao.agregarDocumentacion(documentacion);
            if (idDocumentacion > 0) {
                guardarEntrega(idDocumentacion);
            } else {
                mostrarError("Error al subir", "NO SE PUDO GUARDAR LA DOCUMENTACION.");
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void guardarEntrega(int idDocumentacion) {
        try {
            String matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
            EntregaDocumentacion entrega = new EntregaDocumentacion(
                    Date.valueOf(LocalDate.now()),
                    matricula,
                    idDocumentacion
            );
            int filasAfectadas = entregaDao.agregarEntrega(entrega);
            if (filasAfectadas > 0) {
                limpiarFormulario();
                mostrarExito("Documentación subida", "TU DOCUMENTACION FUE ENVIADA CORRECTAMENTE.");
            } else {
                mostrarError("Error al subir", "NO SE PUDO REGISTRAR LA ENTREGA.");
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
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