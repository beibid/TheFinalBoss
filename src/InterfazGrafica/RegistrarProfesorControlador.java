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
import logica.dominio.ServicioCorreo;
import logica.dominio.CifracionContrasena;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.GeneradoContrasena;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;
import java.util.List;

public class RegistrarProfesorControlador {

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

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int PROFESORES_ACTIVOS_PERMITIDOS = 2;
    private ToggleGroup grupoTurno = new ToggleGroup();
    private String contrasenaGenerada;

    @FXML
    public void initialize() {
        radioBotonMatutino.setToggleGroup(grupoTurno);
        radioBotonVespertino.setToggleGroup(grupoTurno);
        radioBotonMixto.setToggleGroup(grupoTurno);
    }

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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
            if (verificarProfesorActivo()) {
                Profesor profesor = construirProfesor();
                guardarProfesor(profesor);
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

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();

        List<String> campos = List.of(nombre, apellidos, correo, numeroPersonal);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean turnoValido = grupoTurno.getSelectedToggle() != null;
        boolean nombreValido = nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean apellidosValido = apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean correoValido = correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        boolean numeroPersonalValido = numeroPersonal.matches("[a-zA-Z0-9]+");

        verificarCaracteresPermitidos(camposFormularioValido, turnoValido, nombreValido, apellidosValido, correoValido, numeroPersonalValido);

        return camposFormularioValido && turnoValido && nombreValido && apellidosValido && correoValido && numeroPersonalValido;
    }

    private void verificarCaracteresPermitidos(boolean camposFormularioValido, boolean turnoValido,
                                               boolean nombreValido, boolean apellidosValido,
                                               boolean correoValido, boolean numeroPersonalValido) {
        if (!camposFormularioValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacios", "Verifique la informacion e intente de nuevo");
        } else if (!turnoValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Turno no seleccionado", "Seleccione un turno para el profesor.");
        } else if (!nombreValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Nombre invalido", "EL NOMBRE SOLO PUEDE CONTENER LETRAS");
        } else if (!apellidosValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Apellidos invalidos", "LOS APELLIDOS SOLO PUEDEN CONTENER LETRAS");
        } else if (!correoValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO");
        } else if (!numeroPersonalValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Numero de personal invalido", "EL NUMERO DE PERSONAL SOLO PUEDE CONTENER LETRAS Y NUMEROS");
        }
    }

    private boolean verificarProfesorActivo() {
        boolean registrarProfesor = true;
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            int profesoresActivos = profesorDao.existeProfesorActivo();
            if (profesoresActivos > PROFESORES_ACTIVOS_PERMITIDOS) {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error profesores activos existentes", "Ya existen mas de dos profesores activos en el sistema");
                registrarProfesor = false;
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error", excepcion.getMessage().toUpperCase());
            registrarProfesor = false;
        }
        return registrarProfesor;
    }

    private Profesor construirProfesor() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String numeroPersonal = campoTextoNumeroPersonal.getText().trim();
        Turno turno = obtenerTurnoSeleccionado();
        contrasenaGenerada = GeneradoContrasena.generarContrasenaTemportal();
        String contrasenaCifrada = CifracionContrasena.cifrarContrasena(contrasenaGenerada);

        Profesor profesor = new Profesor();
        profesor.setNombre(limitarTexto(nombre, 55));
        profesor.setApellidos(limitarTexto(apellidos, 55));
        profesor.setTurno(turno);
        profesor.setCorreo(limitarTexto(correo, 100));
        profesor.setNumeroDePersonalProfesor(limitarTexto(numeroPersonal, 12));
        profesor.setContrasena(contrasenaCifrada);
        profesor.setEstado(Estado.Activo);
        return profesor;
    }

    private Turno obtenerTurnoSeleccionado() {
        Turno turnoSeleccionado = null;
        if (radioBotonMatutino.isSelected()) {
            turnoSeleccionado = Turno.Matutino;
        } else if (radioBotonVespertino.isSelected()) {
            turnoSeleccionado = Turno.Vespertino;
        } else if (radioBotonMixto.isSelected()) {
            turnoSeleccionado = Turno.Mixto;
        }
        return turnoSeleccionado;
    }

    private void guardarProfesor(Profesor profesor) {
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            int filasAfectadas = profesorDao.insertarProfesor(profesor);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                ServicioCorreo.enviarContrasenaInicial(profesor.getCorreo(), profesor.getNombre(), contrasenaGenerada);
                limpiarCamposRegistros();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Profesor registrado exitosamente",
                        "Se ha enviado la contraseña temporal al correo del profesor");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "No fue posible registrar al profesor, intente mas tarde");
            }
        } catch (RegistroDuplicadoExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Numero de personal repetido",
                    "El numero de personal ya existe en el sistema, verifique la informacion");
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCamposRegistros() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoCorreo.clear();
        campoTextoNumeroPersonal.clear();
        grupoTurno.selectToggle(null);
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