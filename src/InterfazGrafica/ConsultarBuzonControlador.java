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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultarBuzonControlador {

    private static final Logger LOGGER = Logger.getLogger(ConsultarBuzonControlador.class.getName());

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
                obtenerFecha(celda.getValue()));
        columnaContenidoRecibidos.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getContenido()));
        columnaDestinatarioEnviados.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getNombreUsuario()));
        columnaFechaEnviados.setCellValueFactory(celda ->
                obtenerFecha(celda.getValue()));
        columnaContenidoEnviados.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getContenido()));
    }

    private SimpleStringProperty obtenerFecha(MensajeVista mensaje) {
        String fecha = "";
        if (mensaje.getFechaEnvio() != null) {
            fecha = mensaje.getFechaEnvio().toString();
        }
        return new SimpleStringProperty(fecha);
    }

    private void cargarMensajes() {
        cargarRecibidos();
        cargarEnviados();
    }

    private void cargarRecibidos() {
        int idUsuario = SesionUsuario.getInstance().getUsuarioActivo().getIdUsuario();
        try {
            List<MensajeVista> recibidos = mensajeDao.obtenerMensajesRecibidos(idUsuario);
            tablaRecibidos.setItems(FXCollections.observableArrayList(recibidos));
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar mensajes recibidos", excepcion);
            areaContenidoMensaje.setText("ERROR AL CARGAR LOS MENSAJES RECIBIDOS.");
        }
    }

    private void cargarEnviados() {
        int idUsuario = SesionUsuario.getInstance().getUsuarioActivo().getIdUsuario();
        try {
            List<MensajeVista> enviados = mensajeDao.obtenerMensajesEnviados(idUsuario);
            tablaEnviados.setItems(FXCollections.observableArrayList(enviados));
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar mensajes enviados", excepcion);
            areaContenidoMensaje.setText("ERROR AL CARGAR LOS MENSAJES ENVIADOS.");
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
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.close();
    }
}