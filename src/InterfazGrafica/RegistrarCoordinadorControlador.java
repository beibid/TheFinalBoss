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
import logica.dominio.GeneradoContrasena;
import logica.dominio.ServicioCorreo;
import logica.dominio.CifracionContrasena;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.CoordinadorDao;
import logica.dominio.Coordinador;
import logica.dominio.enums.Estado;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarCoordinadorControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarCoordinadorControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int COORDINADORES_ACTIVOS_PERMITIDOS = 0;
    private static final int LIMITE_NOMBRE = 55;
    private static final int LIMITE_APELLIDOS = 55;
    private static final int LIMITE_CORREO = 100;
    private static final int LIMITE_NUMERO_PERSONAL = 12;

    private final CoordinadorDao coordinadorDao = new CoordinadorDao();

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

    private String contrasenaGenerada;

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Seguro que desea registrar al Coordinador?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
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
        boolean confirmado = false;
        alerta.setTitle("Confirmacion");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("");
        ButtonType botonSi = new ButtonType("Si");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == botonSi) {
            confirmado = true;
        }
        return confirmado;
    }

    private void procesarRegistro() {
        boolean camposCorrectos = verificarCampos();
        boolean coordinadorValido = false;
        if (camposCorrectos) {
            coordinadorValido = verificarCoordinadorActivo();
        }
        if (camposCorrectos && coordinadorValido) {
            guardarCoordinador(construirCoordinador());
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
        try {
            int coordinadoresActivos = coordinadorDao.existeCoordinadorActivo();
            if (coordinadoresActivos > COORDINADORES_ACTIVOS_PERMITIDOS) {
                mostrarError("Error coordinador activo existente",
                        "YA EXISTE UN COORDINADOR ACTIVO EN EL SISTEMA.");
                registrarCoordinador = false;
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar coordinador activo", excepcion);
            mostrarError("Error", excepcion.getMessage().toUpperCase());
            registrarCoordinador = false;
        }
        return registrarCoordinador;
    }

    private boolean verificarCampos() {
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
        boolean longitudNombreValida = nombre.length() <= LIMITE_NOMBRE;
        boolean longitudApellidosValida = apellidos.length() <= LIMITE_APELLIDOS;
        boolean longitudCorreoValida = correo.length() <= LIMITE_CORREO;
        boolean longitudNumeroPersonalValida = numeroPersonal.length() <= LIMITE_NUMERO_PERSONAL;
        boolean valido = true;
        if (!camposFormularioValido) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE SOLO PUEDE CONTENER LETRAS.");
            valido = false;
        } else if (!longitudNombreValida) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE NO PUEDE EXCEDER " + LIMITE_NOMBRE + " CARACTERES.");
            valido = false;
        } else if (!apellidosValido) {
            mostrarError("Apellidos invalidos", "LOS APELLIDOS SOLO PUEDEN CONTENER LETRAS.");
            valido = false;
        } else if (!longitudApellidosValida) {
            mostrarError("Apellidos demasiado largos", "LOS APELLIDOS NO PUEDEN EXCEDER " + LIMITE_APELLIDOS + " CARACTERES.");
            valido = false;
        } else if (!correoValido) {
            mostrarError("Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO.");
            valido = false;
        } else if (!longitudCorreoValida) {
            mostrarError("Correo demasiado largo", "EL CORREO NO PUEDE EXCEDER " + LIMITE_CORREO + " CARACTERES.");
            valido = false;
        } else if (!numeroDePersonalValido) {
            mostrarError("Numero de personal invalido", "EL NUMERO DE PERSONAL SOLO PUEDE CONTENER LETRAS Y NUMEROS.");
            valido = false;
        } else if (!longitudNumeroPersonalValida) {
            mostrarError("Numero de personal demasiado largo", "EL NUMERO DE PERSONAL NO PUEDE EXCEDER " + LIMITE_NUMERO_PERSONAL + " CARACTERES.");
            valido = false;
        }
        return valido;
    }

    private Coordinador construirCoordinador() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        contrasenaGenerada = GeneradoContrasena.generarContrasenaTemportal();
        String contrasenaCifrada = CifracionContrasena.cifrarContrasena(contrasenaGenerada);
        Coordinador coordinador = new Coordinador();
        coordinador.setNombre(nombre);
        coordinador.setApellidos(apellidos);
        coordinador.setNumeroDePersonalCoordinador(numeroPersonal);
        coordinador.setCorreo(correo);
        coordinador.setContrasena(contrasenaCifrada);
        coordinador.setEstado(Estado.Activo);
        return coordinador;
    }

    private void guardarCoordinador(Coordinador coordinador) {
        try {
            int filasAfectadas = coordinadorDao.insertarCoordinador(coordinador);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                ServicioCorreo.enviarContrasenaInicial(coordinador.getCorreo(), coordinador.getNombre(), contrasenaGenerada);
                limpiarCamposRegistros();
                mostrarExito("Coordinador registrado exitosamente",
                        "SE HA ENVIADO LA CONTRASENA TEMPORAL AL CORREO DEL COORDINADOR.");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR EL COORDINADOR. INTENTE DE NUEVO.");
            }
        } catch (RegistroDuplicadoExcepcion excepcion) {
            LOGGER.log(Level.WARNING, "Numero de personal duplicado", excepcion);
            mostrarError("Numero de personal repetido",
                    "EL NUMERO DE PERSONAL YA EXISTE EN EL SISTEMA. VERIFIQUE LA INFORMACION.");
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar coordinador", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void limpiarCamposRegistros() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoCorreo.clear();
        campoTextoNumeroPersonal.clear();
    }

    private void mostrarPanel(VBox panelMostrar, VBox panelOcultar) {
        panelMostrar.setVisible(true);
        panelMostrar.setManaged(true);
        ocultarPanel(panelOcultar);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        mostrarPanel(panelError, panelExito);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        mostrarPanel(panelExito, panelError);
    }
}