package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.Rol;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuCoordinadorControlGUI {

    private static final Logger LOGGER = Logger.getLogger(MenuCoordinadorControlGUI.class.getName());

    @FXML private Label lblBienvenida;
    @FXML private Label lblNombre;
    @FXML private Label lblRol;

    @FXML
    public void initialize() {
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
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar la vista de login", excepcion);
        }
    }

    @FXML
    private void abrirRegistrarPracticante(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/PracticanteVista.fxml", "Registrar Practicante");
    }

    @FXML
    private void abrirInactivarPracticante(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarPracticanteVista.fxml", "Inactivar Practicante");
    }

    @FXML
    private void abrirRegistrarProfesor(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ProfesorVista.fxml", "Registrar Profesor");
    }

    @FXML
    private void abrirInactivarProfesor(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarProfesorVista.fxml", "Inactivar Profesor");
    }

    @FXML
    private void abrirRegistrarProyecto(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ProyectoVista.fxml", "Registrar Proyecto");
    }

    @FXML
    private void abrirInactivarProyecto(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarProyectoVista.fxml", "Inactivar Proyecto");
    }

    @FXML
    private void abrirModificarProyecto(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ModificarProyectoVista.fxml", "Modificar Proyecto");
    }

    @FXML
    private void abrirRegistrarOrganizacion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarOrganizacionVista.fxml", "Registrar Organización");
    }

    @FXML
    private void abrirInactivarOrganizacion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarOrganizacionVista.fxml", "Inactivar Organización");
    }

    @FXML
    private void abrirModificarOrganizacion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ModificarOrganizacionVista.fxml", "Modificar Organización");
    }

    @FXML
    private void abrirAsignarPracticanteEnSeccion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/PracticanteEnSeccionVista.fxml", "Asignar Practicante a Seccion");
    }

    @FXML
    private void abrirAsignarProyectoAlPracticante(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/AsignarProyectoVista.fxml", "Asignar proyecto a practicante");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        SesionUsuario.getInstance().cerrarSesion();
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void abrirVentana(String fxml, String titulo) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.show();
    }
}