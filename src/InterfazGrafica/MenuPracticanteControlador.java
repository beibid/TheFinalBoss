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


public class MenuPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(MenuPracticanteControlador.class.getName());

    @FXML private Label lblBienvenida;
    @FXML private Label lblNombre;
    @FXML private Label lblRol;

    @FXML
    public void initialize() {
        if (!SesionUsuario.getInstance().tieneRol(Rol.Practicante)) {
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
            Parent ruta = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
            Stage escenario = (Stage) lblBienvenida.getScene().getWindow();
            escenario.setScene(new Scene(ruta));
            escenario.show();
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar la vista de login", excepcion);
        }
    }

    @FXML
    private void abrirSubirReporte(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/SubirReporteVista.fxml", "Subir Reporte");
    }

    @FXML
    private void abrirMisReportes(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/HistorialReportesVista.fxml", "Mis Reportes");
    }

    @FXML
    private void abrirSubirDocumentacion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/SubirDocumentacionVista.fxml", "Subir Documentación");
    }

    @FXML
    private void abrirSeleccionProyectos(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/SeleccionPreferenciasProyectoVista.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Seleccionar proyectos");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void abrirMensajeria(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/EnviarMensajeVista.fxml", "Enviar Mensaje");
    }

    @FXML
    private void abrirBuzon(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ConsultarBuzonVista.fxml", "Buzón");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        SesionUsuario.getInstance().cerrarSesion();
        Parent ruta = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.setScene(new Scene(ruta));
        escenario.show();
    }
    @FXML
    private void abrirAutoevaluacionPracticante(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/GenerarAutoevaluacionPracticanteVista.fxml", "Realizar Autoevaluacion");
    }

    private void abrirVentana(String fxml, String titulo) throws IOException {
        Parent ruta = FXMLLoader.load(getClass().getResource(fxml));
        Stage escenario = new Stage();
        escenario.setTitle(titulo);
        escenario.setScene(new Scene(ruta));
        escenario.show();
    }
}