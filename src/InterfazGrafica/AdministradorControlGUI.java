package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.AdministradorDao;
import logica.dominio.Administrador;
import logica.dominio.enums.Estado;
import java.net.URL;
import java.util.ResourceBundle;

public class AdministradorControlGUI implements Initializable {

    @FXML private TextField campoTextoNombres;
    @FXML private TextField campoTextoApellidos;
    @FXML private TextField campoTextoNumeroPersonal;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar al administrador?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCampos();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/MenuAdministradorVista.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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

    private void procesarRegistro() {
        if (!camposValidos()) {
            mostrarError("Campos obligatorios vacios",
                    "Verifique la informacion e intente de nuevo.");
            return;
        }
        guardarAdministrador(construirAdministrador());
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        return !nombre.isEmpty() && !apellidos.isEmpty() && !numeroPersonal.isEmpty();
    }

    private Administrador construirAdministrador() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        String contrasena = generarContrasena(nombre, numeroPersonal);

        Administrador administrador = new Administrador();
        administrador.setNombre(limitarTexto(nombre, 55));
        administrador.setApellidos(limitarTexto(apellidos, 55));
        administrador.setNumeroDePersonalAdministrador(limitarTexto(numeroPersonal, 20));
        administrador.setContrasena(limitarTexto(contrasena, 12));
        administrador.setEstado(Estado.Activo);
        return administrador;
    }

    private void guardarAdministrador(Administrador administrador) {
        AdministradorDao administradorDao = new AdministradorDao();
        try {
            int filasAfectadas = administradorDao.insertarAdministrador(administrador);
            if (filasAfectadas > 0) {
                limpiarCampos();
                mostrarExito("Administrador registrado",
                        "EL ADMINISTRADOR FUE REGISTRADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar",
                        "NO SE PUDO REGISTRAR EL ADMINISTRADOR. INTENTE DE NUEVO.");
            }
        } catch (RegistroDuplicadoExcepcion e) {
            mostrarError("Numero de personal repetido",
                    "EL NUMERO DE PERSONAL YA EXISTE EN EL SISTEMA. VERIFIQUE LA INFORMACION.");
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    private String generarContrasena(String nombre, String numeroPersonal) {
        return nombre.toLowerCase() + numeroPersonal;
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCampos() {
        ocultarError();
        ocultarExito();
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoNumeroPersonal.clear();
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

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}