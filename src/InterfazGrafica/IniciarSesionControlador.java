package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.UsuarioDao;
import logica.dominio.CifracionContrasena;
import logica.dominio.SesionUsuario;
import logica.dominio.UsuarioSesion;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Rol;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IniciarSesionControlador {

    private static final Logger LOGGER = Logger.getLogger(IniciarSesionControlador.class.getName());

    @FXML private TextField campoTextoIdentificador;
    @FXML private PasswordField campoTextoContrasena;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;

    @FXML
    private void botonIniciarSesion() {
        ocultarError();
        String identificador = campoTextoIdentificador.getText().trim();
        String contrasena = campoTextoContrasena.getText().trim();

        if (camposVacios(List.of(identificador, contrasena))) {
            mostrarError("Campos vacíos", "POR FAVOR INGRESA TUS CREDENCIALES.");
            return;
        }

        try {
            UsuarioDao usuarioDao = new UsuarioDao();
            String contrasenaCifrada = CifracionContrasena.cifrarContrasena(contrasena);
            UsuarioSesion usuarioSesion = usuarioDao.buscarUsuario(identificador, contrasenaCifrada);
            procesarResultadoLogin(usuarioSesion);
        } catch (UsuariosExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean camposVacios(List<String> campos) {
        boolean hayCamposVacios = false;
        for (String campo : campos) {
            if (campo.isEmpty()) {
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private void procesarResultadoLogin(UsuarioSesion usuarioSesion) {
        if (usuarioSesion == null) {
            mostrarError("Credenciales incorrectas", "USUARIO O CONTRASEÑA INCORRECTOS.");
        } else if (usuarioSesion.getEstado() == Estado.Inactivo) {
            mostrarError("Usuario inactivo", "TU CUENTA SE ENCUENTRA INACTIVA.");
        } else {
            redirigir(usuarioSesion);
        }
    }

    private void redirigir(UsuarioSesion usuarioSesion) {
        SesionUsuario.getInstance().iniciarSesion(usuarioSesion);
        if (usuarioSesion.isDebeCambiarContrasena()) {
            cargarVista("/InterfazGrafica/vistas/CambiarContrasenaVista.fxml");
            return;
        }
        String rutaFxml = obtenerRutaFxml(usuarioSesion.getRol());
        if (rutaFxml == null) {
            mostrarError("Error", "TIPO DE USUARIO NO RECONOCIDO.");
            return;
        }
        cargarVista(rutaFxml);
    }

    private String obtenerRutaFxml(Rol rol) {
        String rutaFxml;
        switch (rol) {
            case Coordinador:
                rutaFxml = "/InterfazGrafica/vistas/MenuCoordinadorVista.fxml";
                break;
            case Profesor:
                rutaFxml = "/InterfazGrafica/vistas/MenuProfesorVista.fxml";
                break;
            case Practicante:
                rutaFxml = "/InterfazGrafica/vistas/MenuPracticanteVista.fxml";
                break;
            case Administrador:
                rutaFxml = "/InterfazGrafica/vistas/MenuAdministradorVista.fxml";
                break;
            default:
                rutaFxml = null;
                break;
        }
        return rutaFxml;
    }

    private void cargarVista(String rutaFxml) {
        try {
            Parent ruta = FXMLLoader.load(getClass().getResource(rutaFxml));
            Stage escenario = (Stage) campoTextoIdentificador.getScene().getWindow();
            escenario.setScene(new Scene(ruta));
            escenario.show();
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar vista", excepcion);
            mostrarError("Error al cargar pantalla", excepcion.getMessage().toUpperCase());
        }
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
}