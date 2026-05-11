package InterfazGrafica;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;


public class OrganizacionVinculadaControlGUI{
    @FXML private TextField campoTextoNombre;
    @FXML private TextField campoTextoDireccion;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

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

                String nombre = campoTextoNombre.getText().trim();
                String direccion = campoTextoDireccion.getText().trim();

                if (nombre.isEmpty() || direccion.isEmpty()) {
                    mostrarError("Campos obligatorios vacios",
                            "Verifique la informacion e intente de nuevo");
                    return;
                }

                OrganizacionVinculada organizacion = new OrganizacionVinculada();
                OrganizacionVinculadaDao organizacionDao = new OrganizacionVinculadaDao();

                organizacion.setNombre(nombre);
                organizacion.setDireccion(direccion);

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
    private void botonRegresar(ActionEvent event) throws Exception{
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void limpiarCampos() {
        ocultarError();
        ocultarExito();
        campoTextoNombre.clear();
        campoTextoDireccion.clear();
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
