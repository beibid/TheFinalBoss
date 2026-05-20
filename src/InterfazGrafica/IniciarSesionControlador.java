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
import logica.dominio.SesionUsuario;
import logica.dominio.UsuarioSesion;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Rol;


public class IniciarSesionControlador {

    @FXML private TextField campoTextoIdentificador;
    @FXML private PasswordField campoTextoContrasena;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;

    private UsuarioDao usuarioDao = new UsuarioDao();

    @FXML
    private void botonIniciarSesion() {
        ocultarError();
        String correo = campoTextoIdentificador.getText().trim();
        String contrasena = campoTextoContrasena.getText().trim();

        if (camposVacios(correo, contrasena)) {
            mostrarError("Campos vacíos", "POR FAVOR INGRESA TUS CREDENCIALES.");
            return;
        }

        try {
            UsuarioSesion usuarioSesion = usuarioDao.buscarUsuario(correo, contrasena);
            procesarResultadoLogin(usuarioSesion);
        } catch (UsuariosExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private boolean camposVacios(String correo, String contrasena) {
        return correo.isEmpty() || contrasena.isEmpty();
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
        SesionUsuario.getInstance().iniciarSesion(usuarioSesion);
        String rutaFxml = obtenerRutaFxml(usuarioSesion.getRol());
        if (rutaFxml == null) {
            mostrarError("Error", "TIPO DE USUARIO NO RECONOCIDO.");
            return;
        }
        cargarVista(rutaFxml);
    }

    private String obtenerRutaFxml(Rol rol) {
        switch (rol) {
            case Coordinador:   return "/InterfazGrafica/vistas/MenuCoordinadorVista.fxml";
            case Profesor:      return "/InterfazGrafica/vistas/MenuProfesorVista.fxml";
            case Practicante:   return "/InterfazGrafica/vistas/MenuPracticanteVista.fxml";
            case Administrador: return "/InterfazGrafica/vistas/MenuAdministradorVista.fxml";
            default:            return null;
        }
    }

    private void cargarVista(String rutaFxml) {
        try {
            Parent ruta = FXMLLoader.load(getClass().getResource(rutaFxml));
            Stage escenario = (Stage) campoTextoIdentificador.getScene().getWindow();
            escenario.setScene(new Scene(ruta));
            escenario.show();
        } catch (Exception excepcion) {
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