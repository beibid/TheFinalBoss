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

public class SubirDocumentacionPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(SubirDocumentacionPracticanteControlador.class.getName());
    private static final String CARPETA_UPLOADS = "uploads/documentacion/";
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    private final DocumentacionPracticanteDao documentacionDao = new DocumentacionPracticanteDao();
    private final EntregaDocumentacionDao entregaDao = new EntregaDocumentacionDao();

    @FXML private Label etiquetaArchivo;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private File archivoSeleccionado = null;

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
    private void botonSubir() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        boolean hayArchivo = archivoSeleccionado != null;
        if (!hayArchivo) {
            mostrarError("Archivo requerido", "DEBES SELECCIONAR UN ARCHIVO PDF.");
        } else {
            procesarSubida();
        }
    }

    private void procesarSubida() {
        String rutaRelativa = copiarArchivo();
        boolean copiaExitosa = rutaRelativa != null;
        if (copiaExitosa) {
            guardarDocumentacion(rutaRelativa);
        }
    }

    private String copiarArchivo() {
        String ruta = null;
        try {
            File carpeta = new File(CARPETA_UPLOADS);
            boolean carpetaExiste = carpeta.exists();
            if (!carpetaExiste) {
                carpeta.mkdirs();
            }
            Path destino = Path.of(CARPETA_UPLOADS + archivoSeleccionado.getName());
            Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            ruta = CARPETA_UPLOADS + archivoSeleccionado.getName();
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al copiar el archivo", excepcion);
            mostrarError("Error de archivo", "NO SE PUDO COPIAR EL PDF.");
        }
        return ruta;
    }

    private void guardarDocumentacion(String rutaRelativa) {
        try {
            DocumentacionPracticante documentacion = new DocumentacionPracticante(
                    rutaRelativa, EstadoRevision.Pendiente, null
            );
            int idDocumentacion = documentacionDao.agregarDocumentacion(documentacion);
            if (idDocumentacion >= FILAS_AFECTADAS_ESPERADAS) {
                guardarEntrega(idDocumentacion);
            } else {
                mostrarError("Error al subir", "NO SE PUDO GUARDAR LA DOCUMENTACION.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar documentacion", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void guardarEntrega(int idDocumentacion) {
        try {
            String matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
            EntregaDocumentacion entrega = new EntregaDocumentacion(
                    Date.valueOf(LocalDate.now()), matricula, idDocumentacion
            );
            int filasAfectadas = entregaDao.agregarEntrega(entrega);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarFormulario();
                mostrarExito("Documentacion subida", "TU DOCUMENTACION FUE ENVIADA CORRECTAMENTE.");
            } else {
                mostrarError("Error al subir", "NO SE PUDO REGISTRAR LA ENTREGA.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al guardar entrega", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
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