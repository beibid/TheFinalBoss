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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnviarMensajeControlador {

    private static final Logger LOGGER = Logger.getLogger(EnviarMensajeControlador.class.getName());
    private static final int LIMITE_CARACTERES_MENSAJE = 500;

    private final PracticanteDao practicanteDao = new PracticanteDao();
    private final ProfesorDao profesorDao = new ProfesorDao();
    private final MensajeDao mensajeDao = new MensajeDao();
    private final BuzonDao buzonDao = new BuzonDao();

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
        int idUsuarioActual = SesionUsuario.getInstance().getUsuarioActivo().getIdUsuario();
        List<Usuario> todos = new ArrayList<>();
        List<Usuario> filtrados = new ArrayList<>();
        todos.addAll(practicanteDao.obtenerPracticantesActivos());
        todos.addAll(profesorDao.obtenerProfesoresActivos());
        for (Usuario usuario : todos) {
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
                boolean esVacioONulo = estaVacio || usuario == null;
                if (esVacioONulo) {
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
                boolean esVacioONulo = estaVacio || usuario == null;
                if (esVacioONulo) {
                    setText("-- Selecciona un destinatario --");
                } else {
                    setText(usuario.getNombre() + " " + usuario.getApellidos());
                }
            }
        });
    }

    @FXML
    private void botonEnviar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (verificarCampos()) {
            if (confirmarAccion("Seguro que desea enviar el mensaje?")) {
                ejecutarEnvio(
                        comboBoxDestinatario.getSelectionModel().getSelectedItem(),
                        areaTextoMensaje.getText().trim()
                );
            }
        }
    }

    private boolean verificarCampos() {
        Usuario destinatario = comboBoxDestinatario.getSelectionModel().getSelectedItem();
        String contenido = areaTextoMensaje.getText().trim();
        boolean valido = true;
        if (destinatario == null) {
            mostrarError("Sin destinatario", "POR FAVOR SELECCIONA UN DESTINATARIO.");
            valido = false;
        } else if (contenido.isEmpty()) {
            mostrarError("Mensaje vacio", "POR FAVOR ESCRIBE UN MENSAJE.");
            valido = false;
        } else if (contenido.length() > LIMITE_CARACTERES_MENSAJE) {
            mostrarError("Mensaje largo", "EL MENSAJE NO DEBE SUPERAR LOS " + LIMITE_CARACTERES_MENSAJE + " CARACTERES.");
            valido = false;
        }
        return valido;
    }

    private void ejecutarEnvio(Usuario destinatario, String contenido) {
        try {
            Mensaje mensaje = new Mensaje(contenido);
            int idMensaje = mensajeDao.agregarMensaje(mensaje);
            int idRemitente = SesionUsuario.getInstance().getUsuarioActivo().getIdUsuario();
            int idDestinatario = destinatario.getIdUsuario();
            buzonDao.agregarBuzon(new Buzon(RolMensaje.Remitente, idMensaje, idRemitente));
            buzonDao.agregarBuzon(new Buzon(RolMensaje.destinatario, idMensaje, idDestinatario));
            limpiar();
            mostrarExito("Mensaje enviado", "EL MENSAJE FUE ENVIADO EXITOSAMENTE.");
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al enviar mensaje", excepcion);
            mostrarError("Error al enviar", excepcion.getMessage().toUpperCase());
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
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
        boolean confirmado = false;
        alerta.setTitle("Confirmacion");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Si");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == botonSi) {
            confirmado = true;
        }
        return confirmado;
    }

    private void limpiar() {
        comboBoxDestinatario.getSelectionModel().clearSelection();
        areaTextoMensaje.clear();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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