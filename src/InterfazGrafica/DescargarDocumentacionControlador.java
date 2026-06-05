package InterfazGrafica;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DescargarDocumentacionControlador {
    @FXML private TableView<String[]> tablaDocumentos;
    @FXML private TableColumn<String[], String> columnaDocumento;
    @FXML private TableColumn<String[], Void> columnaAccion;
    @FXML private Label etiquetaMensaje;

    private static final Logger LOGGER = Logger.getLogger(DescargarDocumentacionControlador.class.getName());

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDocumentos();
    }

    private void configurarColumnas() {
        columnaDocumento.setCellValueFactory(dato -> new javafx.beans.property.SimpleStringProperty(dato.getValue()[0]));
        columnaAccion.setCellFactory(columna -> new TableCell<>() {
            private final Button botonDescargar = new Button("Descargar");
            {
                botonDescargar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                botonDescargar.setOnAction(evento -> {
                    String[] fila = getTableView().getItems().get(getIndex());
                    descargarDocumento(fila[0], fila[1], fila[2]);
                });
            }
            @Override
            protected void updateItem(Void elemento, boolean vacio) {
                super.updateItem(elemento, vacio);
                if (vacio) {
                    setGraphic(null);
                } else {
                    setGraphic(botonDescargar);
                }
            }
        });
    }

    private void cargarDocumentos() {
        tablaDocumentos.getItems().add(new String[]{
                "PRAIS-04 Evaluación de la organización",
                "/recursos/PRAIS-04-Evaluacion-de-la-organizacion.pdf",
                "PRAIS-04-Evaluacion-de-la-organizacion.pdf"
        });
        tablaDocumentos.getItems().add(new String[]{
                "F1 Solicitud de Prácticas",
                "/recursos/F1-Solicitud-Practicas-3.pdf",
                "F1-Solicitud-Practicas-3.pdf"
        });
    }

    private void descargarDocumento(String nombre, String rutaRecurso, String nombreArchivo) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar documento");
        fileChooser.setInitialFileName(nombreArchivo);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        Stage escenario = (Stage) tablaDocumentos.getScene().getWindow();
        File destino = fileChooser.showSaveDialog(escenario);
        if (destino != null) {
            copiarArchivo(rutaRecurso, destino);
        }
    }

    private void copiarArchivo(String rutaRecurso, File destino) {
        try (InputStream origen = getClass().getResourceAsStream(rutaRecurso);
             OutputStream salida = new FileOutputStream(destino)) {
            if (origen == null) {
                LOGGER.log(Level.SEVERE, "No se encontró el archivo en recursos");
                etiquetaMensaje.setStyle("-fx-text-fill: red;");
                etiquetaMensaje.setText("No se encontró el archivo");
                return;
            }
            origen.transferTo(salida);
            etiquetaMensaje.setStyle("-fx-text-fill: green;");
            etiquetaMensaje.setText("Archivo descargado correctamente");
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al descargar el archivo", excepcion);
            etiquetaMensaje.setStyle("-fx-text-fill: red;");
            etiquetaMensaje.setText("Error al descargar el archivo");
        }
    }

    @FXML
    private void cerrar() {
        Stage escenario = (Stage) tablaDocumentos.getScene().getWindow();
        escenario.close();
    }
}
