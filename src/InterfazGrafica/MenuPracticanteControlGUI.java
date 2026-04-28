package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPracticanteControlGUI implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void abrirSubirReporte(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/SubirReporteVista.fxml", "Subir Reporte");
    }

    @FXML
    private void abrirMisReportes(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/HistorialReportesVista.fxml", "Mis Reportes");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws Exception {
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