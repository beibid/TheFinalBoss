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

public class CambiarContrasenaControlador {

    @FXML private PasswordField campoTextoNuevaContrasena;
    @FXML private PasswordField campoTextoConfirmarContrasena;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

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

        if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
            return false;
        }
        if (nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Contrasena muy corta", "LA CONTRASENA DEBE TENER AL MENOS " + LONGITUD_MINIMA_CONTRASENA + " CARACTERES");
            return false;
        }
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Contrasenas no coinciden", "LAS CONTRASENAS INGRESADAS NO SON IGUALES");
            return false;
        }
        return true;
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
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Contrasena actualizada", "Tu contrasena ha sido cambiada exitosamente");
                redirigirAlMenu(usuarioSesion.getRol());
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al actualizar", "NO SE PUDO ACTUALIZAR LA CONTRASENA. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void redirigirAlMenu(Rol rol) {
        String rutaFxml = obtenerRutaFxml(rol);
        if (rutaFxml == null) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error", "TIPO DE USUARIO NO RECONOCIDO.");
            return;
        }
        try {
            Parent ruta = FXMLLoader.load(getClass().getResource(rutaFxml));
            Stage escenario = (Stage) campoTextoNuevaContrasena.getScene().getWindow();
            escenario.setScene(new Scene(ruta));
            escenario.show();
        } catch (Exception excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error al cargar pantalla", excepcion.getMessage().toUpperCase());
        }
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