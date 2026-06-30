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

public class MenuCoordinadorControlador {

    private static final Logger LOGGER = Logger.getLogger(MenuCoordinadorControlador.class.getName());

    @FXML private Label etiquetaBienvenida;
    @FXML private Label etiquetaNombre;
    @FXML private Label etiquetaRol;

    @FXML
    public void initialize() {
        if (!SesionUsuario.getInstance().tieneRol(Rol.Coordinador)) {
            cerrarVentanaNoAutorizada();
        } else {
            String nombre = SesionUsuario.getInstance().getUsuarioActivo().getNombre();
            String rol = SesionUsuario.getInstance().getUsuarioActivo().getRol().toString();
            etiquetaBienvenida.setText("Bienvenido, " + nombre);
            etiquetaNombre.setText(nombre);
            etiquetaRol.setText("ROL: " + rol.toUpperCase());
        }
    }

    private void cerrarVentanaNoAutorizada() {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Acceso denegado");
        alerta.setHeaderText("NO TIENES PERMISO PARA ACCEDER A ESTA SECCION.");
        alerta.setContentText("SERAS REDIRIGIDO AL LOGIN.");
        alerta.showAndWait();
        try {
            Parent ruta = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
            Stage escenario = (Stage) etiquetaBienvenida.getScene().getWindow();
            escenario.setScene(new Scene(ruta));
            escenario.show();
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar la vista de login", excepcion);
        }
    }

    @FXML
    private void abrirConsultarAlumnos(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ConsultarAlumnosCoordinadorVista.fxml", "Consultar Alumnos");
    }

    @FXML
    private void abrirRegistrarPracticante(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarPracticanteVista.fxml", "Registrar Practicante");
    }

    @FXML
    private void abrirInactivarPracticante(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarPracticanteVista.fxml", "Inactivar Practicante");
    }

    @FXML
    private void abrirRegistrarProfesor(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarProfesorVista.fxml", "Registrar Profesor");
    }

    @FXML
    private void abrirInactivarProfesor(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarProfesorVista.fxml", "Inactivar Profesor");
    }

    @FXML
    private void abrirRegistrarProyecto(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarProyectoVista.fxml", "Registrar Proyecto");
    }

    @FXML
    private void abrirInactivarProyecto(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarProyectoVista.fxml", "Inactivar Proyecto");
    }

    @FXML
    private void abrirModificarProyecto(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ModificarProyectoVista.fxml", "Modificar Proyecto");
    }

    @FXML
    private void abrirRegistrarOrganizacion(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarOrganizacionVinculadaVista.fxml", "Registrar Organizacion");
    }

    @FXML
    private void abrirModificarPracticante(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ModificarPracticanteVista.fxml", "Modificar Practicante");
    }

    @FXML
    private void abrirInactivarOrganizacion(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/InactivarOrganizacionVinculadaVista.fxml", "Inactivar Organizacion");
    }

    @FXML
    private void abrirModificarOrganizacion(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ModificarOrganizacionVinculadaVista.fxml", "Modificar Organizacion");
    }

    @FXML
    private void abrirAsignarPracticanteEnSeccion(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/AsignarPracticanteEnSeccionVista.fxml", "Asignar Practicante a Seccion");
    }

    @FXML
    private void abrirAsignarProyectoAlPracticante(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/AsignarProyectoVista.fxml", "Asignar Proyecto a Practicante");
    }

    @FXML
    private void abrirModificarProfesor(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ModificarProfesorVista.fxml", "Modificar Profesor");
    }

    @FXML
    private void abrirRegistrarPeriodo(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarPeriodoVista.fxml", "Registrar Periodo");
    }

    @FXML
    private void abrirCerrarPeriodo(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/CerrarPeriodoVista.fxml", "Cerrar Periodo");
    }

    @FXML
    private void abrirRegistrarSeccion(ActionEvent evento) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarSeccionVista.fxml", "Registrar Seccion");
    }

    @FXML
    private void cerrarSesion(ActionEvent evento) throws IOException {
        SesionUsuario.getInstance().cerrarSesion();
        Parent ruta = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
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