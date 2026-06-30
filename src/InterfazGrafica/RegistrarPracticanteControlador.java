package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logica.dominio.GeneradoContrasena;
import logica.dominio.ServicioCorreo;
import logica.dominio.CifracionContrasena;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Practicante;
import logica.dominio.Profesor;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarPracticanteControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarPracticanteControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LIMITE_NOMBRE = 50;
    private static final int LIMITE_APELLIDOS = 50;
    private static final int LIMITE_MATRICULA = 12;
    private static final int LIMITE_CORREO = 100;
    private static final int LIMITE_LENGUA_INDIGENA = 50;

    private final PracticanteDao practicanteDao = new PracticanteDao();
    private final ProfesorDao profesorDao = new ProfesorDao();
    private final ToggleGroup grupoGenero = new ToggleGroup();

    @FXML private TextField campoTextoMatricula;
    @FXML private TextField campoTextoNombres;
    @FXML private TextField campoTextoApellidos;
    @FXML private TextField campoTextoCorreo;
    @FXML private RadioButton radioBotonFemenino;
    @FXML private RadioButton radioBotonMasculino;
    @FXML private TextField campoTextoLenguaIndigena;
    @FXML private ComboBox<Profesor> comboBoxProfesor;
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private String contrasenaGenerada;

    @FXML
    public void initialize() {
        radioBotonMasculino.setToggleGroup(grupoGenero);
        radioBotonFemenino.setToggleGroup(grupoGenero);
        cargarProfesoresActivos();
    }

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Seguro que desea registrar al practicante?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
            limpiarCamposRegistrados();
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
        if (camposCorrectos) {
            Practicante practicante = construirPracticante();
            guardarPracticante(practicante);
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

    private Genero obtenerGeneroPracticante() {
        Genero generoSeleccionado = null;
        if (radioBotonMasculino.isSelected()) {
            generoSeleccionado = Genero.Masculino;
        } else if (radioBotonFemenino.isSelected()) {
            generoSeleccionado = Genero.Femenino;
        }
        return generoSeleccionado;
    }

    private boolean verificarCampos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String matricula = campoTextoMatricula.getText().trim();
        String lenguaIndigena = campoTextoLenguaIndigena.getText().trim();
        List<String> campos = List.of(nombre, apellidos, correo, matricula);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean generoValido = grupoGenero.getSelectedToggle() != null;
        boolean profesorValido = comboBoxProfesor.getValue() != null;
        boolean nombreValido = nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean apellidosValidos = apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
        boolean correoValido = correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        boolean matriculaValida = matricula.matches("[a-zA-Z0-9]+");
        boolean longitudNombreValida = nombre.length() <= LIMITE_NOMBRE;
        boolean longitudApellidosValida = apellidos.length() <= LIMITE_APELLIDOS;
        boolean longitudMatriculaValida = matricula.length() <= LIMITE_MATRICULA;
        boolean longitudCorreoValida = correo.length() <= LIMITE_CORREO;
        boolean longitudLenguaValida = lenguaIndigena.length() <= LIMITE_LENGUA_INDIGENA;
        boolean valido = true;
        if (!camposFormularioValido) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!generoValido) {
            mostrarError("Genero no seleccionado", "SELECCIONE UN GENERO PARA EL PRACTICANTE.");
            valido = false;
        } else if (!profesorValido) {
            mostrarError("Profesor no seleccionado", "ASIGNE UN PROFESOR AL PRACTICANTE.");
            valido = false;
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE SOLO PUEDE TENER LETRAS.");
            valido = false;
        } else if (!longitudNombreValida) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE NO PUEDE EXCEDER " + LIMITE_NOMBRE + " CARACTERES.");
            valido = false;
        } else if (!apellidosValidos) {
            mostrarError("Apellidos invalidos", "LOS APELLIDOS SOLO PUEDEN TENER LETRAS.");
            valido = false;
        } else if (!longitudApellidosValida) {
            mostrarError("Apellidos demasiado largos", "LOS APELLIDOS NO PUEDEN EXCEDER " + LIMITE_APELLIDOS + " CARACTERES.");
            valido = false;
        } else if (!correoValido) {
            mostrarError("Correo invalido", "INGRESE UN CORREO VALIDO.");
            valido = false;
        } else if (!longitudCorreoValida) {
            mostrarError("Correo demasiado largo", "EL CORREO NO PUEDE EXCEDER " + LIMITE_CORREO + " CARACTERES.");
            valido = false;
        } else if (!matriculaValida) {
            mostrarError("Matricula invalida", "INGRESE UNA MATRICULA VALIDA.");
            valido = false;
        } else if (!longitudMatriculaValida) {
            mostrarError("Matricula demasiado larga", "LA MATRICULA NO PUEDE EXCEDER " + LIMITE_MATRICULA + " CARACTERES.");
            valido = false;
        } else if (!longitudLenguaValida) {
            mostrarError("Lengua indigena demasiado larga", "LA LENGUA INDIGENA NO PUEDE EXCEDER " + LIMITE_LENGUA_INDIGENA + " CARACTERES.");
            valido = false;
        }
        return valido;
    }

    private Practicante construirPracticante() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String matricula = campoTextoMatricula.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String lenguaIndigena = campoTextoLenguaIndigena.getText().trim();
        Genero genero = obtenerGeneroPracticante();
        contrasenaGenerada = GeneradoContrasena.generarContrasenaTemportal();
        String contrasenaCifrada = CifracionContrasena.cifrarContrasena(contrasenaGenerada);
        Profesor profesorSeleccionado = comboBoxProfesor.getValue();
        Practicante practicante = new Practicante();
        practicante.setNombre(nombre);
        practicante.setApellidos(apellidos);
        practicante.setGenero(genero);
        practicante.setMatricula(matricula);
        practicante.setCorreo(correo);
        practicante.setLenguaIndigena(lenguaIndigena);
        practicante.setContrasena(contrasenaCifrada);
        practicante.setEstado(Estado.Activo);
        practicante.setNumeroPersonalProfesor(profesorSeleccionado.getIdUsuario());
        return practicante;
    }

    private void cargarProfesoresActivos() {
        try {
            List<Profesor> profesoresActivos = profesorDao.obtenerProfesoresActivos();
            comboBoxProfesor.getItems().addAll(profesoresActivos);
            comboBoxProfesor.setConverter(new StringConverter<Profesor>() {
                @Override
                public String toString(Profesor profesor) {
                    String nombreCompleto = null;
                    boolean profesorNoNulo = profesor != null;
                    if (profesorNoNulo) {
                        nombreCompleto = profesor.getNombre() + " " + profesor.getApellidos();
                    }
                    return nombreCompleto;
                }
                @Override
                public Profesor fromString(String texto) {
                    Profesor profesorVacio = null;
                    return profesorVacio;
                }
            });
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cargar profesores activos", excepcion);
            mostrarError("Error al cargar profesores", "NO HA SIDO POSIBLE RECUPERAR A LOS PROFESORES ACTIVOS.");
        }
    }

    private void guardarPracticante(Practicante practicante) {
        try {
            int filasAfectadas = practicanteDao.insertarPracticante(practicante);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                ServicioCorreo.enviarContrasenaInicial(practicante.getCorreo(), practicante.getNombre(), contrasenaGenerada);
                limpiarCamposRegistrados();
                mostrarExito("Practicante registrado exitosamente",
                        "SE HA ENVIADO LA CONTRASENA TEMPORAL AL CORREO DEL PRACTICANTE.");
            } else {
                mostrarError("Error al registrar", "NO FUE POSIBLE REGISTRAR AL PRACTICANTE. INTENTE MAS TARDE.");
            }
        } catch (RegistroDuplicadoExcepcion excepcion) {
            LOGGER.log(Level.WARNING, "Dato duplicado al registrar practicante", excepcion);
            mostrarError("Dato repetido", excepcion.getMessage().toUpperCase());
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar practicante", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private void limpiarCamposRegistrados() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoMatricula.clear();
        campoTextoCorreo.clear();
        campoTextoLenguaIndigena.clear();
        grupoGenero.selectToggle(null);
        comboBoxProfesor.setValue(null);
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