package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;

import java.util.List;

public class RegistrarPracticanteControlador {

    @FXML private TextField campoTextoMatricula;
    @FXML private TextField campoTextoNombres;
    @FXML private TextField campoTextoApellidos;
    @FXML private TextField campoTextoCorreo;
    @FXML private RadioButton radioBotonFemenino;
    @FXML private RadioButton radioBotonMasculino;
    @FXML private TextField campoTextoLenguaIndigena;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private ToggleGroup grupoGenero = new ToggleGroup();

    @FXML
    public void initialize() {
        radioBotonMasculino.setToggleGroup(grupoGenero);
        radioBotonFemenino.setToggleGroup(grupoGenero);
    }

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar al practicante?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCamposRegistrados();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
        Practicante practicante = construirPracticante();
        guardarPracticante(practicante);
    }

    private boolean camposVacios(List<String> campos){
        boolean hayCamposVacios = false;
        for ( String campo : campos ){
             if(campo.isEmpty()){
                 hayCamposVacios = true;
             }
        }
        return hayCamposVacios;
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String matricula = campoTextoMatricula.getText().trim();

        List<String> campos = List.of(nombre, apellidos, correo, matricula);
        boolean camposFormularioValido = !camposVacios(campos);

        if (!camposFormularioValido) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
        }
        return camposFormularioValido;
    }

    private Practicante construirPracticante() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String matricula = campoTextoMatricula.getText().trim();
        String lenguaIndigena = campoTextoLenguaIndigena.getText().trim();
        Genero genero = radioBotonMasculino.isSelected() ? Genero.Masculino : Genero.Femenino;
        String contrasena = generarContrasena(nombre, matricula);

        Practicante practicante = new Practicante();
        practicante.setNombre(limitarTexto(nombre, 50));
        practicante.setApellidos(limitarTexto(apellidos, 50));
        practicante.setGenero(genero);
        practicante.setMatricula(limitarTexto(matricula, 12));
        practicante.setLenguaIndigena(limitarTexto(lenguaIndigena, 50));
        practicante.setContrasena(limitarTexto(contrasena, 12));
        practicante.setEstado(Estado.Activo);
        return practicante;
    }

    private void guardarPracticante(Practicante practicante) {
        PracticanteDao practicanteDao = new PracticanteDao();
        try {
            int filasAfectadas = practicanteDao.insertarPracticante(practicante);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCamposRegistrados();
                mostrarExito("Practicante en estado activo",
                        "El practicante fue registrado exitosamente");
            } else {
                mostrarError("Error al registrar",
                        "No fue posible registrar al practicante, intente mas tarde");
            }
        } catch (RegistroDuplicadoExcepcion e) {
            mostrarError("Matricula repetida",
                    "La matricula ya existe en el sistema, verifique la informacion");
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    private String generarContrasena(String nombre, String matricula) {
        return nombre.toLowerCase() + matricula;
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCamposRegistrados() {
        ocultarError();
        ocultarExito();
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoMatricula.clear();
        campoTextoLenguaIndigena.clear();
        grupoGenero.selectToggle(null);
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