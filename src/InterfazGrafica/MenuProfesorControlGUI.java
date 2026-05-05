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

public class MenuProfesorControlGUI implements Initializable {

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
    private void abrirCalificarReporte(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/EvaluacionDeReportesVista.fxml", "Calificar Reporte");
    }

    @FXML
    private void abrirHistorialReportes(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/HistorialReportesVista.fxml", "Historial de Reportes");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws Exception {
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