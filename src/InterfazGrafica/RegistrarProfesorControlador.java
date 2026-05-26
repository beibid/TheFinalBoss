package InterfazGrafica;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import java.util.List;


public class RegistrarProfesorControlador {

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    @FXML private TextField campoTextoNombres;
    @FXML private TextField campoTextoApellidos;
    @FXML private TextField campoTextoNumeroPersonal;
    @FXML private TextField campoTextoCorreo;
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
    private void botonCancelar () {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCamposRegistros();
        }
    }

    @FXML
    private void botonRegresar (ActionEvent event) {
       Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
       escenario.close();
    }

    private boolean confirmarAccion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        return alerta.showAndWait().filter(botonPresionado -> botonPresionado == botonSi).isPresent();
    }

    private void procesarRegistro() {
        if (!camposValidos()) {
            return;
        }
        Profesor profesor = construirProfesor();
        guardarProfesor(profesor);
    }

    private boolean camposVacios(List<String> campos){
        boolean hayCamposVacios = false;
        for ( String campo : campos){
            if (campo.isEmpty()){
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();

        List<String> campos = List.of(nombre, apellidos, correo, numeroPersonal);
        boolean nombreValido = !camposVacios (campos);
        boolean turnoValido = grupoTurno.getSelectedToggle() != null;

        if (!nombreValido) {
            mostrarError("Campos obligatorios vacios", "Verifique la informacion e intente de nuevo");
        }
        if (!turnoValido) {
            mostrarError("Turno no seleccionado", "Seleccione un turno para el profesor.");
        }
        return nombreValido && turnoValido;
    }

    private Profesor construirProfesor() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        Turno turno = obtenerTurnoSeleccionado();
        String contrasena = generarContrasena(nombre, numeroPersonal);

        Profesor profesor = new Profesor();
        profesor.setNombre(limitarTexto(nombre, 55));
        profesor.setApellidos(limitarTexto(apellidos, 55));
        profesor.setTurno(turno);
        profesor.setCorreo(limitarTexto(correo, 100));
        profesor.setNumeroDePersonalProfesor(limitarTexto(numeroPersonal, 12));
        profesor.setContrasena(limitarTexto(contrasena, 12));
        profesor.setEstado(Estado.Activo);
        return profesor;
    }

    private Turno obtenerTurnoSeleccionado() {
        Turno turnoSeleccionado = null;
        if (radioBotonMatutino.isSelected()) {
            turnoSeleccionado = Turno.Matutino;
        } else if (radioBotonVespertino.isSelected()) {
            turnoSeleccionado = Turno.Vespertino;
        } else if (radioBotonMixto.isSelected()){
            turnoSeleccionado = Turno.Mixto;
        }
        return turnoSeleccionado;
    }

    private void guardarProfesor(Profesor profesor) {
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            int filasAfectadas = profesorDao.insertarProfesor(profesor);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
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

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCamposRegistros() {
        ocultarError();
        ocultarExito();
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoCorreo.clear();
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