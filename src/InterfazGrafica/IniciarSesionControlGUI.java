package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.UsuarioDao;
import logica.dominio.UsuarioSesion;
import logica.dominio.enums.Estado;
import java.net.URL;
import java.util.ResourceBundle;

public class IniciarSesionControlGUI implements Initializable {

    @FXML private TextField txtIdentificador;
    @FXML private PasswordField txtContrasena;
    @FXML private VBox panelError;
    @FXML private Label lblTituloError;
    @FXML private Label lblMensajeError;

    private UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void botonIniciarSesion() {
        ocultarError();
        String identificador = txtIdentificador.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (camposVacios(identificador, contrasena)) {
            mostrarError("Campos vacíos", "POR FAVOR INGRESA TUS CREDENCIALES.");
            return;
        }

        try {
            UsuarioSesion usuarioSesion = usuarioDao.buscarUsuario(identificador, contrasena);
            procesarResultadoLogin(usuarioSesion);
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    private boolean camposVacios(String identificador, String contrasena) {
        return identificador.isEmpty() || contrasena.isEmpty();
    }

    private void procesarResultadoLogin(UsuarioSesion usuarioSesion) {
        if (usuarioSesion == null) {
            mostrarError("Credenciales incorrectas",
                    "USUARIO O CONTRASEÑA INCORRECTOS.");
        } else if (usuarioSesion.getEstado() == Estado.Inactivo) {
            mostrarError("Usuario inactivo",
                    "TU CUENTA SE ENCUENTRA INACTIVA.");
        } else {
            redirigir(usuarioSesion);
        }
    }

    private void redirigir(UsuarioSesion usuarioSesion) {
        String fxml = obtenerRutaFxml(usuarioSesion.getTipo());
        if (fxml == null) {
            mostrarError("Error", "TIPO DE USUARIO NO RECONOCIDO.");
            return;
        }
        cargarVista(fxml);
    }

    private String obtenerRutaFxml(String tipo) {
        switch (tipo) {
            case "Coordinador": return "/InterfazGrafica/vistas/MenuCoordinadorVista.fxml";
            case "Profesor":    return "/InterfazGrafica/vistas/MenuProfesorVista.fxml";
            case "Practicante": return "/InterfazGrafica/vistas/MenuPracticanteVista.fxml";
            case "Administrador":  return "/InterfazGrafica/vistas/MenuAdministradorVista.fxml";
            default:            return null;
        }
    }

    private void cargarVista(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) txtIdentificador.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarError("Error al cargar pantalla", e.getMessage().toUpperCase());
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        lblTituloError.setText(titulo);
        lblMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }
}