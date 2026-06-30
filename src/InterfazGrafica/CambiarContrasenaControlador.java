package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.UsuarioDao;
import logica.dominio.CifracionContrasena;
import logica.dominio.SesionUsuario;
import logica.dominio.UsuarioSesion;
import logica.dominio.enums.Rol;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CambiarContrasenaControlador {

    @FXML private PasswordField campoTextoNuevaContrasena;
    @FXML private PasswordField campoTextoConfirmarContrasena;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private static final Logger LOGGER = Logger.getLogger(CambiarContrasenaControlador.class.getName());
    private static final int LONGITUD_MINIMA_CONTRASENA = 8;
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML
    private void botonCambiarContrasena() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (camposValidos()) {
            guardarNuevaContrasena();
        }
    }

    private boolean camposValidos() {
        String nuevaContrasena = campoTextoNuevaContrasena.getText().trim();
        String confirmarContrasena = campoTextoConfirmarContrasena.getText().trim();
        boolean valido = true;
        if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            mostrarError("Campos vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
            valido = false;
        } else if (nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            mostrarError("Contrasena muy corta", "LA CONTRASENA DEBE TENER AL MENOS " + LONGITUD_MINIMA_CONTRASENA + " CARACTERES");
            valido = false;
        } else if (!nuevaContrasena.equals(confirmarContrasena)) {
            mostrarError("Contrasenas no coinciden", "LAS CONTRASENAS INGRESADAS NO SON IGUALES");
            valido = false;
        }
        return valido;
    }

    private void guardarNuevaContrasena() {
        UsuarioSesion usuarioSesion = SesionUsuario.getInstance().getUsuarioActivo();
        String nuevaContrasena = campoTextoNuevaContrasena.getText().trim();
        String contrasenaCifrada = CifracionContrasena.cifrarContrasena(nuevaContrasena);
        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            int filasAfectadas = usuarioDao.actualizarContrasena(usuarioSesion.getIdUsuario(), contrasenaCifrada);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                usuarioSesion.setDebeCambiarContrasena(false);
                mostrarExito("Contrasena actualizada", "Tu contrasena ha sido cambiada exitosamente");
                redirigirAlMenu(usuarioSesion.getRol());
            } else {
                mostrarError("Error al actualizar", "NO SE PUDO ACTUALIZAR LA CONTRASENA. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al actualizar contrasena", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void redirigirAlMenu(Rol rol) {
        String rutaVista = obtenerRutaVista(rol);
        if (rutaVista == null) {
            mostrarError("Error", "TIPO DE USUARIO NO RECONOCIDO.");
        } else {
            try {
                Parent ruta = FXMLLoader.load(getClass().getResource(rutaVista));
                Stage escenario = (Stage) campoTextoNuevaContrasena.getScene().getWindow();
                escenario.setScene(new Scene(ruta));
                escenario.show();
            } catch (Exception excepcion) {
                LOGGER.log(Level.SEVERE, "Error al cargar pantalla de menu", excepcion);
                mostrarError("Error al cargar pantalla", excepcion.getMessage().toUpperCase());
            }
        }
    }

    private String obtenerRutaVista(Rol rol) {
        String rutaVista;
        switch (rol) {
            case Coordinador:
                rutaVista = "/InterfazGrafica/vistas/MenuCoordinadorVista.fxml";
                break;
            case Profesor:
                rutaVista = "/InterfazGrafica/vistas/MenuProfesorVista.fxml";
                break;
            case Practicante:
                rutaVista = "/InterfazGrafica/vistas/MenuPracticanteVista.fxml";
                break;
            case Administrador:
                rutaVista = "/InterfazGrafica/vistas/MenuAdministradorVista.fxml";
                break;
            default:
                rutaVista = null;
                break;
        }
        return rutaVista;
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