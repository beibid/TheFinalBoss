package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logica.dominio.SesionUsuario;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuAdministradorControlGUI implements Initializable {

    @FXML private Label lblBienvenida;
    @FXML private Label lblNombre;
    @FXML private Label lblRol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String nombre = SesionUsuario.getInstance().getNombre();
        String rol = SesionUsuario.getInstance().getUsuarioActivo().getRol().toString();
        lblBienvenida.setText("Bienvenido, " + nombre);
        lblNombre.setText(nombre);
        lblRol.setText("ROL: " + rol.toUpperCase());
    }

    @FXML
    private void botonRegistrarCoordinador(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/CoordinadorVista.fxml", "Registrar Coordinador");
    }

    @FXML
    private void botonInactivarCoordinador(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/InactivarCoordinadorVista.fxml", "Inactivar Coordinador");
    }

    @FXML
    private void botonRegistrarAdministrador(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/AdministradorVista.fxml", "Registrar Administrador");
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception {
        SesionUsuario.getInstance().cerrarSesion();
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void abrirVentana(String fxml, String titulo) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.show();
    }
}