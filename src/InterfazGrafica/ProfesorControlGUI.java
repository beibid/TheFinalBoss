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

public class ProfesorControlGUI{

    @FXML private TextField campoTextoNombres;
    @FXML private TextField campoTextoApellidos;
    @FXML private TextField campoTextoNumeroPersonal;
    @FXML private RadioButton radioBotonMatutino;
    @FXML private RadioButton radioBotonVespertino;
    @FXML private RadioButton radioBotonMixto;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private ToggleGroup grupoTurno = new ToggleGroup();

    @FXML
    public void initialize() {
        radioBotonMatutino.setToggleGroup(grupoTurno);
        radioBotonVespertino.setToggleGroup(grupoTurno);
        radioBotonMixto.setToggleGroup(grupoTurno);
    }

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar al profesor?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCamposRegistros();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception {
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(btnSi, btnNo);
        return alerta.showAndWait().filter(r -> r == btnSi).isPresent();
    }

    private void procesarRegistro() {
        if (!camposValidos()) {
            return;
        }
        Profesor profesor = construirProfesor();
        guardarProfesor(profesor);
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || numeroPersonal.isEmpty()) {
            mostrarError("Campos obligatorios vacios",
                    "Verifique la informacion e intente de nuevo");
            return false;
        }
        if (grupoTurno.getSelectedToggle() == null) {
            mostrarError("Turno no seleccionado",
                    "Seleccione un turno para el profesor.");
            return false;
        }
        return true;
    }

    private Profesor construirProfesor() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        Turno turno = obtenerTurnoSeleccionado();
        String contrasena = generarContrasena(nombre, numeroPersonal);

        Profesor profesor = new Profesor();
        profesor.setNombre(nombre);
        profesor.setApellidos(apellidos);
        profesor.setTurno(turno);
        profesor.setNumeroDePersonalProfesor(numeroPersonal);
        profesor.setContrasena(contrasena);
        profesor.setEstado(Estado.Activo);
        return profesor;
    }

    private Turno obtenerTurnoSeleccionado() {
        if (radioBotonMatutino.isSelected()) {
            return Turno.Matutino;
        }
        if (radioBotonVespertino.isSelected()) {
            return Turno.Vespertino;
        }
        return Turno.Mixto;
    }

    private void guardarProfesor(Profesor profesor) {
        ProfesorDao profesorDao = new ProfesorDao();
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

    private String generarContrasena(String nombre, String numeroPersonal) {
        return nombre.toLowerCase() + numeroPersonal;
    }

    private void limpiarCamposRegistros() {
        ocultarError();
        ocultarExito();
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoNumeroPersonal.clear();
        grupoTurno.selectToggle(null);
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}