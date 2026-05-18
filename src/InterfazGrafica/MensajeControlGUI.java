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
import logica.dominio.SesionUsuario;
import logica.dominio.Usuario;
import logica.dominio.Practicante;
import logica.dominio.Profesor;
import logica.dominio.enums.RolMensaje;
import java.util.ArrayList;
import java.util.List;


public class MensajeControlGUI {
    @FXML private ComboBox<Usuario> comboBoxDestinatario;
    @FXML private TextArea areaTextoMensaje;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private final PracticanteDao practicanteDao = new PracticanteDao();
    private final ProfesorDao profesorDao = new ProfesorDao();
    private final MensajeDao mensajeDao = new MensajeDao();
    private final BuzonDao buzonDao = new BuzonDao();

    @FXML
    public void initialize() {
        cargarDestinatarios();
    }

    private void cargarDestinatarios() {
        try {
            int idUsuarioActual = SesionUsuario.getInstance().getIdUsuario();
            List<Usuario> destinatarios = new ArrayList<>();
            destinatarios.addAll(practicanteDao.obtenerPracticantesActivos());
            destinatarios.addAll(profesorDao.obtenerProfesoresActivos());
            List<Usuario> destinatariosFiltrados = new ArrayList<>();
            for (Usuario usuario : destinatarios) {
                if (usuario.getIdUsuario() != idUsuarioActual) {
                    destinatariosFiltrados.add(usuario);
                }
            }

            comboBoxDestinatario.setItems(FXCollections.observableArrayList(destinatariosFiltrados));
            comboBoxDestinatario.setCellFactory(listaUsuarios -> new ListCell<>() {
                @Override
                protected void updateItem(Usuario usuario, boolean estaVacio) {
                    super.updateItem(usuario, estaVacio);
                    if (estaVacio || usuario == null) {
                        setText(null);
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

            comboBoxDestinatario.setCellFactory(listaUsuarios -> new ListCell<>() {
                @Override
                protected void updateItem(Usuario usuario, boolean estaVacio) {
                    super.updateItem(usuario, estaVacio);
                    if (estaVacio || usuario == null) {
                        setText(null);
                    } else {
                        if (usuario instanceof Practicante) {
                            setText("[Practicante] " + usuario.getNombre() + " " + usuario.getApellidos());
                        } else if (usuario instanceof Profesor) {
                            setText("[Profesor] " + usuario.getNombre() + " " + usuario.getApellidos());
                        } else {
                            setText(usuario.getNombre() + " " + usuario.getApellidos());
                        }
                    }
                }
            });
        } catch (UsuariosExcepcion e) {
            mostrarError("Error al cargar", "NO SE PUDIERON CARGAR LOS DESTINATARIOS.");
        }
    }

    @FXML
    private void botonEnviar() {
        Usuario destinatario = comboBoxDestinatario.getSelectionModel().getSelectedItem();
        String contenido = areaTextoMensaje.getText().trim();

        if (destinatario == null) {
            mostrarError("Sin destinatario", "POR FAVOR SELECCIONA UN DESTINATARIO.");
            return;
        }
        if (contenido.isEmpty()) {
            mostrarError("Mensaje vacío", "POR FAVOR ESCRIBE UN MENSAJE.");
            return;
        }
        if (!confirmarAccion("¿Seguro que desea enviar el mensaje?")) {
            return;
        }
        ejecutarEnvio(destinatario, contenido);
    }

    private void ejecutarEnvio(Usuario destinatario, String contenido) {
        try {
            Mensaje mensaje = new Mensaje(contenido);
            int idMensaje = mensajeDao.agregarMensaje(mensaje);

            int idRemitente = SesionUsuario.getInstance().getIdUsuario();

            Buzon buzonRemitente = new Buzon(RolMensaje.Remitente, idMensaje, idRemitente);
            buzonDao.agregarBuzon(buzonRemitente);

            Buzon buzonDestinatario = new Buzon(RolMensaje.destinatario, idMensaje, destinatario.getIdUsuario());
            buzonDao.agregarBuzon(buzonDestinatario);

            limpiar();
            mostrarExito("Mensaje enviado", "EL MENSAJE FUE ENVIADO EXITOSAMENTE.");
        } catch (MensajeriaExcepcion e) {
            mostrarError("Error al enviar", e.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiar();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);
        return alerta.showAndWait().filter(r -> r == btnSi).isPresent();
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

