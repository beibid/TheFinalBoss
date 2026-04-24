package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfesorControlGUI implements Initializable {

    @FXML private TextField txtNombreProfesor;
    @FXML private TextField txtApellidoPaternoProfesor;
    @FXML private TextField txtApellidoMaternoProfesor;
    @FXML private TextField txtNumeroPersonal;
    @FXML private RadioButton rdiobtMatutino;
    @FXML private RadioButton rdiobtVespertino;
    @FXML private RadioButton rdiobtMixto;
    @FXML private VBox panelError;
    @FXML private Label lblTituloError;
    @FXML private Label lblMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label lblTituloExito;
    @FXML private Label lblMensajeExito;

    private ToggleGroup grupoTurno = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rdiobtMatutino.setToggleGroup(grupoTurno);
        rdiobtVespertino.setToggleGroup(grupoTurno);
        rdiobtMixto.setToggleGroup(grupoTurno);
    }

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar registro");
        alerta.setHeaderText("¿Seguro que desea registrar al profesor?");
        alerta.setContentText("");

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);

        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnSi) {

                String nombre = txtNombreProfesor.getText().trim();
                String apellidoPaterno = txtApellidoPaternoProfesor.getText().trim();
                String apellidoMaterno = txtApellidoMaternoProfesor.getText().trim();
                String numeroPersonal = txtNumeroPersonal.getText().trim();

                if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || numeroPersonal.isEmpty()) {
                    mostrarError("Campos obligatorios vacios",
                            "Verifique la informacion e intente de nuevo");
                    return;
                }

                if (grupoTurno.getSelectedToggle() == null) {
                    mostrarError("Turno no seleccionado",
                            "Seleccione un turno para el profesor.");
                    return;
                }

                Turno turno;
                if (rdiobtMatutino.isSelected()) {
                    turno = Turno.Matutino;
                } else if (rdiobtVespertino.isSelected()) {
                    turno = Turno.Vespertino;
                } else {
                    turno = Turno.Mixto;
                }

                Profesor profesor = new Profesor();
                ProfesorDao profesorDao = new ProfesorDao();

                String contrasenaGenerada = generarContrasena(nombre, numeroPersonal);
                profesor.setNombre(nombre);
                profesor.setApellidoPaterno(apellidoPaterno);
                profesor.setApellidoMaterno(apellidoMaterno);
                profesor.setTurno(turno);
                profesor.setNumeroDePersonalProfesor(numeroPersonal);
                profesor.setContrasena(contrasenaGenerada);
                profesor.setEstado(Estado.Activo);

                try {
                    int filasAfectadas = profesorDao.insertarProfesor(profesor);

                    if (filasAfectadas > 0) {
                        limpiarCamposRegistros();
                        mostrarExito("Profesor en estado activo",
                                "El profesor fue registrado exitosamente");
                    } else {
                        mostrarError("Error al registrar",
                                "No fue posible registrar al profesor, intente mas tarde");
                    }
                } catch (RegistroDuplicadoExcepcion e) {
                    mostrarError("Numero de personal repetido",
                            "El numero de personal ya existe en el sistema, verifique la informacion");
                } catch (UsuariosExcepcion e) {
                    mostrarError("Error inesperado", e.getMessage().toUpperCase());
                }
            }
        });
    }

    @FXML
    private void botonCancelar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Cancelar registro");
        alerta.setHeaderText("¿Seguro que desea cancelar?");
        alerta.setContentText("");

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);

        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnSi) {
                limpiarCamposRegistros();
            }
        });
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/SeccionCoordinador.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private String generarContrasena(String nombre, String numeroPersonal) {
        return nombre.toLowerCase() + numeroPersonal;
    }

    private void limpiarCamposRegistros() {
        ocultarError();
        ocultarExito();
        txtNombreProfesor.clear();
        txtApellidoPaternoProfesor.clear();
        txtApellidoMaternoProfesor.clear();
        txtNumeroPersonal.clear();
        grupoTurno.selectToggle(null);
    }

    private void mostrarError(String titulo, String mensaje) {
        lblTituloError.setText(titulo);
        lblMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        lblTituloExito.setText(titulo);
        lblMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}