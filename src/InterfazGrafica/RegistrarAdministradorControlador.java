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
import logica.CifracionContrasena;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.AdministradorDao;
import logica.dominio.Administrador;
import logica.dominio.enums.Estado;
import java.util.List;

public class RegistrarAdministradorControlador {

    @FXML private TextField campoTextoNombres;
    @FXML private TextField campoTextoApellidos;
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
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("¿Seguro que desea registrar al administrador?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiarCampos();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
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
        if (camposValidos()) {
            guardarAdministrador(construirAdministrador());
        }
    }

    private boolean camposVacios(List<String> campos) {
        boolean hayCamposVacios = false;
        for (String campo : campos) {
            if (campo.isEmpty()) {
                hayCamposVacios = true;
            }
            
        }
        return hayCamposVacios;
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();

        List<String> campos = List.of(nombre, apellidos, numeroPersonal);
        boolean camposFormularioValido = !camposVacios(campos);

        if (!camposFormularioValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
        }
        return camposFormularioValido;
    }

    private Administrador construirAdministrador() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        String contrasena = generarContrasena(nombre, numeroPersonal);
        String contrasenaCifrada = CifracionContrasena.cifrarContrasena(contrasena);

        Administrador administrador = new Administrador();
        administrador.setNombre(limitarTexto(nombre, 55));
        administrador.setApellidos(limitarTexto(apellidos, 55));
        administrador.setNumeroDePersonalAdministrador(limitarTexto(numeroPersonal, 20));
        administrador.setContrasena(contrasenaCifrada);
        administrador.setEstado(Estado.Activo);
        return administrador;
    }

    private void guardarAdministrador(Administrador administrador) {
        AdministradorDao administradorDao = new AdministradorDao();
        try {
            int filasAfectadas = administradorDao.insertarAdministrador(administrador);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCampos();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Administrador registrado", "EL ADMINISTRADOR FUE REGISTRADO EXITOSAMENTE.");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "NO SE PUDO REGISTRAR EL ADMINISTRADOR. INTENTE DE NUEVO.");
            }
        } catch (RegistroDuplicadoExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Numero de personal repetido",
                    "EL NUMERO DE PERSONAL YA EXISTE EN EL SISTEMA. VERIFIQUE LA INFORMACION.");
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private String generarContrasena(String nombre, String numeroPersonal) {
        String contrasenaGenerada = nombre.toLowerCase() + numeroPersonal;
        return contrasenaGenerada;
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCampos() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoNumeroPersonal.clear();
    }

    private void mostrarPanel(Label etiquetaTitulo, Label etiquetaMensaje, VBox panel, String titulo, String mensaje) {
        etiquetaTitulo.setText(titulo);
        etiquetaMensaje.setText(mensaje);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }
}