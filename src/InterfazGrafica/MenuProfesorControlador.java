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

public class MenuProfesorControlador {

    private static final Logger LOGGER = Logger.getLogger(MenuProfesorControlador.class.getName());

    @FXML private Label lblBienvenida;
    @FXML private Label lblNombre;
    @FXML private Label lblRol;

    @FXML
    public void initialize() {
        if (!SesionUsuario.getInstance().tieneRol(Rol.Profesor)) {
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
    private void abrirConsultarAlumnos(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ConsultarAlumnosProfesorVista.fxml", "Mis Alumnos");
    }

    @FXML
    private void abrirCalificarReporte(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/EvaluacionDeReportesVista.fxml", "Calificar Reporte");
    }

    @FXML
    private void abrirHistorialReportes(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ConsultarHistorialReportesVista.fxml", "Historial de Reportes");
    }

    @FXML
    private void abrirMensajeria(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/EnviarMensajeVista.fxml", "Enviar mensaje");
    }

    @FXML
    private void abrirBuzon(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ConsultarBuzonVista.fxml", "Buzón");
    }

    @FXML
    private void abrirValidarDocumentacion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ValidarDocumentosPracticanteVista.fxml", "Validar documentos");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        SesionUsuario.getInstance().cerrarSesion();
        Parent ruta = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.setScene(new Scene(ruta));
        escenario.show();
    }

    private void abrirVentana(String fxml, String titulo) throws IOException {
        Parent ruta = FXMLLoader.load(getClass().getResource(fxml));
        Stage escenario = new Stage();
        escenario.setTitle(titulo);
        escenario.setScene(new Scene(ruta));
        escenario.show();
    }
}