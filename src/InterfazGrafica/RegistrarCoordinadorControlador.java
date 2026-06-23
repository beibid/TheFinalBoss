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
import logica.dominio.CifracionContrasena;
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
    private static final int COORDINADORES_ACTIVOS_PERMITIDOS = 0;
    private String contrasenaGenerada;

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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
            if (verificarCoordinadorActivo()) {
                guardarCoordinador(construirCoordinador());
            }
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

    private boolean verificarCoordinadorActivo() {
        boolean registrarCoordinador = true;
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        try {
            int coordinadoresActivos = coordinadorDao.existeCoordinadorActivo();
            if (coordinadoresActivos > COORDINADORES_ACTIVOS_PERMITIDOS) {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error coordinador activo existente", "Ya existe un coordinador activo en el sistema");
                registrarCoordinador = false;
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error", excepcion.getMessage().toUpperCase());
            registrarCoordinador = false;
        }
        return registrarCoordinador;
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();

        List<String> campos = List.of(nombre, apellidos, correo, numeroPersonal);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean nombreValido = nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean apellidosValido = apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean correoValido = correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        boolean numeroDePersonalValido = numeroPersonal.matches("[a-zA-Z0-9]+");

        verficarCaracteresNoPermitidos(camposFormularioValido, nombreValido, apellidosValido, correoValido, numeroDePersonalValido);

        boolean formularioValido = camposFormularioValido && nombreValido && apellidosValido && correoValido && numeroDePersonalValido;
        return formularioValido;
    }

    private void verficarCaracteresNoPermitidos(boolean camposFormularioValido, boolean nombreValido,
                                                boolean apellidosValido, boolean correoValido, boolean numeroPersonalValido) {
        if (!camposFormularioValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
        } else if (!nombreValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "nombre invalido", "EL NOMBRE SOLO PUEDE CONTENER LETRAS");
        } else if (!apellidosValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "apellidos invalidos", "LOS APELLIDOS SOLO PUEDEN CONTENER LETRAS");
        } else if (!correoValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO");
        } else if (!numeroPersonalValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "numero de personal invalido", "EL NUMERO DE PERSONAL SOLO PUEDE CONTENER LETRAS Y NUMEROS");
        }
    }

    private Coordinador construirCoordinador() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        contrasenaGenerada = generarContrasena(nombre, numeroPersonal);
        String contrasenaCifrada = CifracionContrasena.cifrarContrasena(contrasenaGenerada);

        Coordinador coordinador = new Coordinador();
        coordinador.setNombre(limitarTexto(nombre, 55));
        coordinador.setApellidos(limitarTexto(apellidos, 55));
        coordinador.setNumeroDePersonalCoordinador(limitarTexto(numeroPersonal, 12));
        coordinador.setCorreo(limitarTexto(correo, 100));
        coordinador.setContrasena(contrasenaCifrada);
        coordinador.setEstado(Estado.Activo);
        return coordinador;
    }

    private void guardarCoordinador(Coordinador coordinador) {
        CoordinadorDao coordinadorDao = new CoordinadorDao();
        try {
            int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCamposRegistros();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Coordinador registrado exitosamente",
                        "Contraseña temporal: " + contrasenaGenerada);
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "NO SE PUDO REGISTRAR EL COORDINADOR. INTENTE DE NUEVO.");
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
        String contrasena = nombre.toLowerCase() + numeroPersonal;
        return contrasena;
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCamposRegistros() {
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