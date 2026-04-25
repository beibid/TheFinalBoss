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
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;
import java.net.URL;
import java.util.ResourceBundle;

public class OrganizacionVinculadaControlGUI implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
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

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar registro");
        alerta.setHeaderText("¿Seguro que desea registrar la organización?");
        alerta.setContentText("");

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);

        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnSi) {
                String nombre = txtNombre.getText().trim();
                String direccion = txtDireccion.getText().trim();

                if (nombre.isEmpty() || direccion.isEmpty()) {
                    mostrarError("Campos obligatorios vacios",
                            "Verifique la informacion e intente de nuevo");
                    return;
                }

                OrganizacionVinculada organizacion = new OrganizacionVinculada();
                OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();

                organizacion.setNombre(limitarTexto(nombre, 80));
                organizacion.setDireccion(limitarTexto(direccion, 80));

                try {
                    int filasAfectadas = organizacionDao.insertarOrganizacionVinculada(organizacion);

                    if (filasAfectadas > 0) {
                        limpiarCampos();
                        mostrarExito("Organización registrada",
                                "La organización fue registrada exitosamente");
                    } else {
                        mostrarError("Error al registrar",
                                "No fue posible registrar la organización, intente mas tarde");
                    }
                } catch (UsuariosExcepcion e) {
                    mostrarError("Error inesperado", e.getMessage().toUpperCase());
                }
            }
        });
    }

    @FXML
    private void botonCancelar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Cancelar registro");
        alerta.setHeaderText("¿Seguro que desea cancelar?");
        alerta.setContentText("");

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);

        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnSi) {
                limpiarCampos();
            }
        });
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/SeccionCoordinador.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private String limitarTexto(String texto, int limite) {
        if (texto == null) {
            return "";
        }
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCampos() {
        ocultarError();
        ocultarExito();
        txtNombre.clear();
        txtDireccion.clear();
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