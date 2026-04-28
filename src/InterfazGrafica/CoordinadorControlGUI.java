package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import logica.dominio.enums.Estado;
import java.net.URL;
import java.util.ResourceBundle;

public class CoordinadorControlGUI implements Initializable {

    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumeroPersonal;
    @FXML private VBox panelError;
    @FXML private Label lblTituloError;
    @FXML private Label lblMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label lblTituloExito;
    @FXML private Label lblMensajeExito;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar al Coordinador?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCamposRegistros();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/SeccionAdministradorVista.fxml"));
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
                    "Verifica la informacion e intente de nuevo.");
            return;
        }
        guardarCoordinador(construirCoordinador());
    }

    private boolean camposValidos() {
        String nombre = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String numeroPersonal = txtNumeroPersonal.getText().trim();
        return !nombre.isEmpty() && !apellidos.isEmpty() && !numeroPersonal.isEmpty();
    }

    private Coordinador construirCoordinador() {
        String nombre = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String numeroPersonal = txtNumeroPersonal.getText().trim();
        String contrasena = generarContrasena(nombre, numeroPersonal);

        Coordinador coordinador = new Coordinador();
        coordinador.setNombre(limitarTexto(nombre, 55));
        coordinador.setApellidos(limitarTexto(apellidos, 55));
        coordinador.setNumeroDePersonalCoordinador(limitarTexto(numeroPersonal, 12));
        coordinador.setContrasena(limitarTexto(contrasena, 12));
        coordinador.setEstado(Estado.Activo);
        return coordinador;
    }

    private void guardarCoordinador(Coordinador coordinador) {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        try {
            int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
            if (filasAfectadas > 0) {
                limpiarCamposRegistros();
                mostrarExito("Coordinador con estado activo",
                        "COORDINADOR REGISTRADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar",
                        "NO SE PUDO REGISTRAR EL COORDINADOR. INTENTE DE NUEVO.");
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

    private void limpiarCamposRegistros() {
        ocultarError();
        ocultarExito();
        txtNombres.clear();
        txtApellidos.clear();
        txtNumeroPersonal.clear();
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

    private void mostrarExito(String titulo, String mensaje) {
        lblTituloExito.setText(titulo);
        lblMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}