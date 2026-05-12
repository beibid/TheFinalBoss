package InterfazGrafica;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.MensajeDao;
import logica.dominio.MensajeVista;
import logica.dominio.SesionUsuario;
import java.util.List;

public class BuzonControlGUI {

    @FXML private TableView<MensajeVista> tablaRecibidos;
    @FXML private TableColumn<MensajeVista, String> columnaRemitenteRecibidos;
    @FXML private TableColumn<MensajeVista, String> columnaFechaRecibidos;
    @FXML private TableColumn<MensajeVista, String> columnaContenidoRecibidos;

    @FXML private TableView<MensajeVista> tablaEnviados;
    @FXML private TableColumn<MensajeVista, String> columnaDestinatarioEnviados;
    @FXML private TableColumn<MensajeVista, String> columnaFechaEnviados;
    @FXML private TableColumn<MensajeVista, String> columnaContenidoEnviados;

    @FXML private TextArea areaContenidoMensaje;

    private final MensajeDao mensajeDao = new MensajeDao();

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarMensajes();
    }

    private void configurarColumnas() {
        columnaRemitenteRecibidos.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getNombreUsuario()));
        columnaFechaRecibidos.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getFechaEnvio() != null ?
                        celda.getValue().getFechaEnvio().toString() : ""));
        columnaContenidoRecibidos.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getContenido()));

        columnaDestinatarioEnviados.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getNombreUsuario()));
        columnaFechaEnviados.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getFechaEnvio() != null ?
                        celda.getValue().getFechaEnvio().toString() : ""));
        columnaContenidoEnviados.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getContenido()));
    }

    private void cargarMensajes() {
        int idUsuario = SesionUsuario.getInstance().getIdUsuario();
        try {
            List<MensajeVista> recibidos = mensajeDao.obtenerMensajesRecibidos(idUsuario);
            tablaRecibidos.setItems(FXCollections.observableArrayList(recibidos));

            List<MensajeVista> enviados = mensajeDao.obtenerMensajesEnviados(idUsuario);
            tablaEnviados.setItems(FXCollections.observableArrayList(enviados));
        } catch (MensajeriaExcepcion e) {
            areaContenidoMensaje.setText("Error al cargar los mensajes.");
        }
    }

    @FXML
    private void seleccionarMensajeRecibido() {
        MensajeVista mensaje = tablaRecibidos.getSelectionModel().getSelectedItem();
        if (mensaje != null) {
            areaContenidoMensaje.setText(mensaje.getContenido());
        }
    }

    @FXML
    private void seleccionarMensajeEnviado() {
        MensajeVista mensaje = tablaEnviados.getSelectionModel().getSelectedItem();
        if (mensaje != null) {
            areaContenidoMensaje.setText(mensaje.getContenido());
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}