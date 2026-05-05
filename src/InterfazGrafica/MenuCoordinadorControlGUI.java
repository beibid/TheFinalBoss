package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.Rol;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuCoordinadorControlGUI implements Initializable {

    @FXML private Label lblBienvenida;
    @FXML private Label lblNombre;
    @FXML private Label lblRol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!SesionUsuario.getInstance().tieneRol(Rol.Coordinador)) {
            cerrarVentanaNoAutorizada();
        } else {
            String nombre = SesionUsuario.getInstance().getNombre();
            String rol = SesionUsuario.getInstance().getUsuarioActivo().getRol().toString();
            lblBienvenida.setText("Bienvenido, " + nombre);
            lblNombre.setText(nombre);
            lblRol.setText("ROL: " + rol.toUpperCase());
        }
    }

    private void cerrarVentanaNoAutorizada() {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Acceso denegado");
        alerta.setHeaderText("No tienes permiso para acceder a esta sección.");
        alerta.setContentText("Serás redirigido al login.");
        alerta.showAndWait();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
            Stage stage = (Stage) lblBienvenida.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

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
    private void abrirAsignarPracticanteEnSeccion(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/PracticanteEnSeccionVista.fxml", "Asignar Practicante a Seccion");
    }

    @FXML
    private void abrirAsignarProyectoAlPracticante(ActionEvent event) throws Exception {
        abrirVentana("/InterfazGrafica/vistas/AsignarProyectoVista.fxml", "Asignar proyecto a practicante");
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