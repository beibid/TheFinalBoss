package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import logica.dominio.enums.Estado;
import java.net.URL;
import java.util.ResourceBundle;

public class CoordinadorControlGUI implements Initializable {

    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidoPaterno;
    @FXML private TextField txtApellidoMaterno;
    @FXML private TextField txtNumeroPersonal;
    @FXML private Button btnRegresar;
    @FXML private VBox panelError;
    @FXML private Label lblTituloError;
    @FXML private Label lblMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label lblTituloExito;
    @FXML private Label lblMensajeExito;

    private CoordinadorDao coordinadorDao = new CoordinadorDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void botonRegistrar() {
        ocultarError();

        String nombre = txtNombres.getText().trim();
        String apellidoPaterno = txtApellidoPaterno.getText().trim();
        String apellidoMaterno = txtApellidoMaterno.getText().trim();
        String numeroPersonal = txtNumeroPersonal.getText().trim();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || numeroPersonal.isEmpty()) {
            mostrarError("Campos obligatorios vacios",
                    "POR FAVOR LLENA TODOS LOS CAMPOS REQUERIDOS.");
            return;
        }

        String contrasena = generarContrasena(nombre, numeroPersonal);

        Coordinador coordinador = new Coordinador();
        coordinador.setNombre(nombre);
        coordinador.setApellidoPaterno(apellidoPaterno);
        coordinador.setApellidoMaterno(apellidoMaterno);
        coordinador.setNumeroDePersonalCoordinador(numeroPersonal);
        coordinador.setContrasena(contrasena);
        coordinador.setEstado(Estado.Activo);

        try {
            int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);

            if (filasAfectadas > 0) {
                botonCancelar();
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
    @FXML
    private void botonCancelar() {
        ocultarError();
        ocultarExito();
        txtNombres.clear();
        txtApellidoPaterno.clear();
        txtApellidoMaterno.clear();
        txtNumeroPersonal.clear();
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/SeccionCoordinador.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private String generarContrasena(String nombre, String numeroPersonal) {
        return nombre.toLowerCase() + numeroPersonal;
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


