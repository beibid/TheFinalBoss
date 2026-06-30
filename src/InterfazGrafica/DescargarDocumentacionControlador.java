package InterfazGrafica;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
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

    private static final Logger LOGGER = Logger.getLogger(DescargarDocumentacionControlador.class.getName());

    @FXML private TableView<String[]> tablaDocumentos;
    @FXML private TableColumn<String[], String> columnaDocumento;
    @FXML private TableColumn<String[], Void> columnaAccion;
    @FXML private Label etiquetaMensaje;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDocumentos();
    }

    private void configurarColumnas() {
        columnaDocumento.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue()[0]));
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
                "PRAIS-04 Evaluacion de la organizacion",
                "/recursos/PRAIS-04-Evaluacion-de-la-organizacion.pdf",
                "PRAIS-04-Evaluacion-de-la-organizacion.pdf"
        });
        tablaDocumentos.getItems().add(new String[]{
                "F1 Solicitud de Practicas",
                "/recursos/F1-Solicitud-Practicas-3.pdf",
                "F1-Solicitud-Practicas-3.pdf"
        });
        tablaDocumentos.getItems().add(new String[]{
                "PRAIS-05 Reporte Final",
                "/recursos/PRAIS-05-Reporte-FINAL.pdf",
                "PRAIS-05-Reporte-FINAL.pdf"
        });
    }

    public boolean archivoExisteEnRecursos(String rutaRecurso) {
        return getClass().getResourceAsStream(rutaRecurso) != null;
    }

    private void descargarDocumento(String nombre, String rutaRecurso, String nombreArchivo) {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (!archivoExisteEnRecursos(rutaRecurso)) {
            mostrarError("Archivo no encontrado", "EL ARCHIVO NO EXISTE EN LOS RECURSOS DEL SISTEMA.");
        } else {
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
    }

    private void copiarArchivo(String rutaRecurso, File destino) {
        try (InputStream origen = getClass().getResourceAsStream(rutaRecurso);
             OutputStream salida = new FileOutputStream(destino)) {
            origen.transferTo(salida);
            mostrarExito("Descarga exitosa", "EL ARCHIVO FUE DESCARGADO CORRECTAMENTE.");
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al descargar el archivo", excepcion);
            mostrarError("Error al descargar", "NO SE PUDO DESCARGAR EL ARCHIVO.");
        }
    }

    @FXML
    private void cerrar() {
        Stage escenario = (Stage) tablaDocumentos.getScene().getWindow();
        escenario.close();
    }

    private void mostrarError(String titulo, String mensaje) {
        mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, titulo, mensaje);
    }

    private void mostrarExito(String titulo, String mensaje) {
        mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito, titulo, mensaje);
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