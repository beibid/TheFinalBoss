package InterfazGrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.BuzonDao;
import logica.dao.objetos.MensajeDao;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Buzon;
import logica.dominio.Mensaje;
import logica.dominio.Practicante;
import logica.dominio.Profesor;
import logica.dominio.SesionUsuario;
import logica.dominio.Usuario;
import logica.dominio.enums.RolMensaje;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnviarMensajeControlador {

    private static final Logger LOGGER = Logger.getLogger(EnviarMensajeControlador.class.getName());
    private static final int LIMITE_CARACTERES_MENSAJE = 500;

    @FXML private ComboBox<Usuario> comboBoxDestinatario;
    @FXML private TextArea areaTextoMensaje;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @FXML
    public void initialize() {
        cargarDestinatarios();
    }

    private void cargarDestinatarios() {
        try {
            List<Usuario> destinatarios = obtenerDestinatariosFiltrados();
            if (destinatarios.isEmpty()) {
                mostrarError("Sin destinatarios", "NO HAY DESTINATARIOS DISPONIBLES EN EL SISTEMA.");
                comboBoxDestinatario.setDisable(true);
            } else {
                comboBoxDestinatario.setItems(FXCollections.observableArrayList(destinatarios));
                configurarCeldasComboBox();
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar destinatarios", excepcion);
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS DESTINATARIOS.");
            comboBoxDestinatario.setDisable(true);
        }
    }

    private List<Usuario> obtenerDestinatariosFiltrados() throws UsuariosExcepcion {
        PracticanteDao practicanteDao = new PracticanteDao();
        ProfesorDao profesorDao = new ProfesorDao();
        int idUsuarioActual = SesionUsuario.getInstance().getIdUsuario();
        List<Usuario> destinatarios = new ArrayList<>();
        destinatarios.addAll(practicanteDao.obtenerPracticantesActivos());
        destinatarios.addAll(profesorDao.obtenerProfesoresActivos());
        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario usuario : destinatarios) {
            if (usuario.getIdUsuario() != idUsuarioActual) {
                filtrados.add(usuario);
            }
        }
        return filtrados;
    }

    private void configurarCeldasComboBox() {
        comboBoxDestinatario.setCellFactory(listaUsuarios -> new ListCell<>() {
            @Override
            protected void updateItem(Usuario usuario, boolean estaVacio) {
                super.updateItem(usuario, estaVacio);
                if (estaVacio || usuario == null) {
                    setText(null);
                } else if (usuario instanceof Practicante) {
                    setText("[Practicante] " + usuario.getNombre() + " " + usuario.getApellidos());
                } else if (usuario instanceof Profesor) {
                    setText("[Profesor] " + usuario.getNombre() + " " + usuario.getApellidos());
                } else {
                    setText(usuario.getNombre() + " " + usuario.getApellidos());
                }
            }
        });
        comboBoxDestinatario.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Usuario usuario, boolean estaVacio) {
                super.updateItem(usuario, estaVacio);
                if (estaVacio || usuario == null) {
                    setText("-- Selecciona un destinatario --");
                } else {
                    setText(usuario.getNombre() + " " + usuario.getApellidos());
                }
            }
        });
    }

    @FXML
    private void botonEnviar() {
        ocultarError();
        ocultarExito();
        if (datosValidos()) {
            if (confirmarAccion("¿Seguro que desea enviar el mensaje?")) {
                ejecutarEnvio(
                        comboBoxDestinatario.getSelectionModel().getSelectedItem(),
                        areaTextoMensaje.getText().trim()
                );
            }
        }
    }

    private boolean datosValidos() {
        Usuario destinatario = comboBoxDestinatario.getSelectionModel().getSelectedItem();
        String contenido = areaTextoMensaje.getText().trim();
        boolean valido = destinatario != null
                && !contenido.isEmpty()
                && contenido.length() <= LIMITE_CARACTERES_MENSAJE;
        verificarDatos(destinatario, contenido);
        return valido;
    }

    private void verificarDatos(Usuario destinatario, String contenido) {
        if (destinatario == null) {
            mostrarError("Sin destinatario", "POR FAVOR SELECCIONA UN DESTINATARIO.");
        } else if (contenido.isEmpty()) {
            mostrarError("Mensaje vacío", "POR FAVOR ESCRIBE UN MENSAJE.");
        } else if (contenido.length() > LIMITE_CARACTERES_MENSAJE) {
            mostrarError("Mensaje largo", "EL MENSAJE NO DEBE SUPERAR LOS " + LIMITE_CARACTERES_MENSAJE + " CARACTERES.");
        }
    }

    private void ejecutarEnvio(Usuario destinatario, String contenido) {
        MensajeDao mensajeDao = new MensajeDao();
        BuzonDao buzonDao = new BuzonDao();
        try {
            Mensaje mensaje = new Mensaje(contenido);
            int idMensaje = mensajeDao.agregarMensaje(mensaje);
            int idRemitente = SesionUsuario.getInstance().getIdUsuario();
            buzonDao.agregarBuzon(new Buzon(RolMensaje.Remitente, idMensaje, idRemitente));
            buzonDao.agregarBuzon(new Buzon(RolMensaje.destinatario, idMensaje, destinatario.getIdUsuario()));
            limpiar();
            mostrarExito("Mensaje enviado", "EL MENSAJE FUE ENVIADO EXITOSAMENTE.");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al enviar mensaje", excepcion);
            mostrarError("Error al enviar", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiar();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
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

    private void limpiar() {
        comboBoxDestinatario.getSelectionModel().clearSelection();
        areaTextoMensaje.clear();
        ocultarError();
        ocultarExito();
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
        ocultarExito();
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
        ocultarError();
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}