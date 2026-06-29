package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ActividadDao;
import logica.dao.objetos.ProyectoDao;
import logica.dao.objetos.ReporteDao;
import logica.dominio.Proyecto;
import logica.dominio.SesionUsuario;
import logica.dominio.enums.Rol;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(MenuPracticanteControlador.class.getName());
    private static final int HORAS_MINIMAS_PARCIAL = 210;
    private static final int HORAS_MINIMAS_FINAL = 420;
    private static final int PARCIALES_PARA_COMPLETAR = 2;

    @FXML private Label lblBienvenida;
    @FXML private Label lblNombre;
    @FXML private Label lblRol;
    @FXML private Button botonGenerarReporteMensual;
    @FXML private Button botonReporteParcial;
    @FXML private Button botonReporteFinal;
    @FXML private Button botonRegistrarActividad;
    @FXML private Button botonSubirReporte;
    @FXML private Button botonSubirDocumentacion;
    @FXML private Button botonSeleccionarProyectos;
    @FXML private Button botonAutoevaluacion;
    @FXML private Label etiquetaEstado;

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
            verificarAccesoSegunEstado();
        }
    }

    private void verificarAccesoSegunEstado() {
        String matricula = SesionUsuario.getInstance().getMatricula();
        ProyectoDao proyectoDao = new ProyectoDao();
        ActividadDao actividadDao = new ActividadDao();
        ReporteDao reporteDao = new ReporteDao();

        boolean tieneProyecto = false;
        int horasTotales = 0;
        boolean yaTermino = false;

        try {
            Proyecto proyecto = proyectoDao.obtenerProyectoPorPracticante(matricula);
            tieneProyecto = proyecto != null;
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar proyecto", excepcion);
        }

        try {
            horasTotales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al obtener horas", excepcion);
        }

        try {
            int parcialesEvaluados = reporteDao.contarReportesEvaluados(matricula, "Parcial");
            yaTermino = horasTotales >= HORAS_MINIMAS_FINAL && parcialesEvaluados >= PARCIALES_PARA_COMPLETAR;
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar reportes", excepcion);
        }

        if (botonAutoevaluacion != null) {
            botonAutoevaluacion.setDisable(true);
        }

        if (yaTermino) {
            if (botonAutoevaluacion != null) {
                botonAutoevaluacion.setDisable(false);
            }
            bloquearTodo();
            if (etiquetaEstado != null) {
                etiquetaEstado.setText("✔ Prácticas completadas — solo lectura");
                etiquetaEstado.setStyle("-fx-text-fill: #2d6a2d; -fx-font-weight: bold;");
                etiquetaEstado.setVisible(true);
            }
        } else if (!tieneProyecto) {
            deshabilitarReportesYActividad();
        } else {
            if (botonReporteParcial != null) {
                botonReporteParcial.setDisable(horasTotales < HORAS_MINIMAS_PARCIAL);
            }
            if (botonReporteFinal != null) {
                botonReporteFinal.setDisable(horasTotales < HORAS_MINIMAS_FINAL);
            }
        }
    }

    private void bloquearTodo() {
        if (botonGenerarReporteMensual != null) botonGenerarReporteMensual.setDisable(true);
        if (botonReporteParcial != null) botonReporteParcial.setDisable(true);
        if (botonReporteFinal != null) botonReporteFinal.setDisable(true);
        if (botonRegistrarActividad != null) botonRegistrarActividad.setDisable(true);
        if (botonSubirReporte != null) botonSubirReporte.setDisable(true);
        if (botonSubirDocumentacion != null) botonSubirDocumentacion.setDisable(true);
        if (botonSeleccionarProyectos != null) botonSeleccionarProyectos.setDisable(true);
        if (botonAutoevaluacion != null) botonAutoevaluacion.setDisable(true);
    }

    private void deshabilitarReportesYActividad() {
        if (botonGenerarReporteMensual != null) botonGenerarReporteMensual.setDisable(true);
        if (botonReporteParcial != null) botonReporteParcial.setDisable(true);
        if (botonReporteFinal != null) botonReporteFinal.setDisable(true);
        if (botonRegistrarActividad != null) botonRegistrarActividad.setDisable(true);
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

    @FXML private void abrirGenerarReporteMensual(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/GenerarReporteMensualVista.fxml", "Generar Reporte Mensual");
    }
    @FXML private void abrirGenerarReporteParcial(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/GenerarReporteParcialVista.fxml", "Generar Reporte Parcial");
    }
    @FXML private void abrirGenerarReporteFinal(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/GenerarReporteFinalVista.fxml", "Generar Reporte Final");
    }
    @FXML private void abrirSubirReporte(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/SubirReporteVista.fxml", "Subir Reporte");
    }
    @FXML private void abrirMisReportes(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/HistorialReportesVista.fxml", "Mis Reportes");
    }
    @FXML private void abrirSubirDocumentacion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/SubirDocumentacionVista.fxml", "Subir Documentación");
    }
    @FXML private void abrirDescargarDocumentacion(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/DescargarDocumentacionVista.fxml", "Descargar Documentación");
    }
    @FXML private void abrirSeleccionProyectos(ActionEvent event) throws IOException {
        Parent ruta = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/SeleccionPreferenciasProyectoVista.fxml"));
        Stage escenario = new Stage();
        escenario.setTitle("Seleccionar proyectos");
        escenario.setScene(new Scene(ruta));
        escenario.show();
    }
    @FXML private void abrirMensajeria(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/EnviarMensajeVista.fxml", "Enviar Mensaje");
    }
    @FXML private void abrirBuzon(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/ConsultarBuzonVista.fxml", "Buzón");
    }
    @FXML private void cerrarSesion(ActionEvent event) throws IOException {
        SesionUsuario.getInstance().cerrarSesion();
        Parent ruta = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/IniciarSesionVista.fxml"));
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.setScene(new Scene(ruta));
        escenario.show();
    }
    @FXML private void abrirRegistrarActividad(ActionEvent event) throws IOException {
        abrirVentana("/InterfazGrafica/vistas/RegistrarActividadVista.fxml", "Registrar Actividad");
    }
    @FXML private void abrirAutoevaluacionPracticante(ActionEvent event) throws IOException {
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