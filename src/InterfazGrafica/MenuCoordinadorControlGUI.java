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

public class MenuCoordinadorControlGUI implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void abrirRegistrarPracticante(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/PracticanteVista.fxml", "Registrar Practicante");
    }

    @FXML
    private void abrirInactivarPracticante(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/InactivarPracticanteVista.fxml", "Inactivar Practicante");
    }

    @FXML
    private void abrirRegistrarProfesor(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/ProfesorVista.fxml", "Registrar Profesor");
    }

    @FXML
    private void abrirInactivarProfesor(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/InactivarProfesorVista.fxml", "Inactivar Profesor");
    }

    @FXML
    private void abrirRegistrarProyecto(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/ProyectoVista.fxml", "Registrar Proyecto");
    }

    @FXML
    private void abrirInactivarProyecto(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/InactivarProyectoVista.fxml", "Inactivar Proyecto");
    }

    @FXML
    private void abrirModificarProyecto(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/ModificarProyectoVista.fxml", "Modificar Proyecto");
    }

    @FXML
    private void abrirRegistrarOrganizacion(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/RegistrarOrganizacionVista.fxml", "Registrar Organización");
    }

    @FXML
    private void abrirInactivarOrganizacion(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/InactivarOrganizacionVista.fxml", "Inactivar Organización");
    }

    @FXML
    private void abrirModificarOrganizacion(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/ModificarOrganizacionVista.fxml", "Modificar Organización");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private

    private void abrirVentana(String fxml, String titulo) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.show();
    }
}