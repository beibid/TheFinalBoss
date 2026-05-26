package InterfazGrafica;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import logica.dominio.enums.Estado;

import java.util.List;


public class RegistrarCoordinadorControlador {

    @FXML private TextField campoTextoNombres;
    @FXML private TextField campoTextoApellidos;
    @FXML private TextField campoTextoCorreo;
    @FXML private TextField campoTextoNumeroPersonal;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;


    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar al Coordinador?")) {
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
    private void botonRegresar(ActionEvent event) {
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
            mostrarError("Campos obligatorios vacios",
                    "Verifica la informacion e intente de nuevo.");
            return;
        }
        guardarCoordinador(construirCoordinador());
    }

    private boolean camposVacios(List<String> campos){
        boolean hayCamposVacios = false;
        for (String campo : campos){
            if (campo.isEmpty()) {
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


        List<String> campo = List.of(nombre, apellidos, correo, numeroPersonal);

        boolean camposFormularioValido = !camposVacios(campo);

        if (!camposFormularioValido){
            mostrarError("Campos obligatorios vacios" , "POR FAVOR LLENE TODOS LOS CAMPOS");
        }
        return camposFormularioValido;
    }

    private Coordinador construirCoordinador() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        String contrasena = generarContrasena(nombre, numeroPersonal);

        Coordinador coordinador = new Coordinador();
        coordinador.setNombre(limitarTexto(nombre, 55));
        coordinador.setApellidos(limitarTexto(apellidos, 55));
        coordinador.setNumeroDePersonalCoordinador(limitarTexto(numeroPersonal, 12));
        coordinador.setContrasena(limitarTexto(contrasena, 12));
        coordinador.setEstado(Estado.Activo);
        return coordinador;
    }

    private void guardarCoordinador(Coordinador coordinador) {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        try {
            int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCamposRegistros();
                mostrarExito("Coordinador con estado activo",
                        "COORDINADOR REGISTRADO EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar",
                        "NO SE PUDO REGISTRAR EL COORDINADOR. INTENTE DE NUEVO.");
            }
        } catch (RegistroDuplicadoExcepcion e) {
            mostrarError("Numero de personal repetido",
                    "EL NUMERO DE PERSONAL YA EXISTE EN EL SISTEMA. VERIFIQUE LA INFORMACION.");
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
        campoTextoNumeroPersonal.clear();
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