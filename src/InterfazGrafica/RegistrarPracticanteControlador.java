package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logica.CifracionContrasena;
import logica.dao.excepciones.RegistroDuplicadoExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.PracticanteDao;
import logica.dao.objetos.ProfesorDao;
import logica.dominio.Practicante;
import logica.dominio.Profesor;
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
    @FXML private ComboBox<Profesor> comboBoxProfesor;
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
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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

    private Genero obtenerGeneroPracticante () {
        Genero generoSeleccionado = null;
        if (radioBotonMasculino.isSelected()){
            generoSeleccionado = Genero.Masculino;
        } else if (radioBotonFemenino.isSelected()){
            generoSeleccionado = Genero.Femenino;
        }
        return generoSeleccionado;
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String matricula = campoTextoMatricula.getText().trim();

        List<String> campos = List.of(nombre, apellidos, correo, matricula);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean generoValido = grupoGenero.getSelectedToggle() != null;
        boolean profesorValido = comboBoxProfesor.getValue() != null;

        if (!camposFormularioValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
        }
        if (!generoValido) {
            mostrarPanel(etiquetaTituloError,etiquetaMensajeError, panelError,
                    "Genero no seleccionado", "Seleccione un genero para el practicante");
        }
        if (!profesorValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Profesor no seleccionado", "Seleccione un profesor para asignarlo al practicante");
        }

        boolean formularioCompletoValido = camposFormularioValido && generoValido && profesorValido;

        return formularioCompletoValido;
    }

    private Practicante construirPracticante() {
        String nombre = campoTextoNombres.getText().trim();
        String apellidos = campoTextoApellidos.getText().trim();
        String matricula = campoTextoMatricula.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String lenguaIndigena = campoTextoLenguaIndigena.getText().trim();
        Genero genero = obtenerGeneroPracticante();
        String contrasena = generarContrasena(nombre, matricula);
        String contrasenaCifrada = CifracionContrasena.cifrarContrasena(contrasena);
        Profesor profesorSeleccionado = comboBoxProfesor.getValue();

        Practicante practicante = new Practicante();
        practicante.setNombre(limitarTexto(nombre, 50));
        practicante.setApellidos(limitarTexto(apellidos, 50));
        practicante.setGenero(genero);
        practicante.setMatricula(limitarTexto(matricula, 12));
        practicante.setCorreo(limitarTexto(correo, 100));
        practicante.setLenguaIndigena(limitarTexto(lenguaIndigena, 50));
        practicante.setContrasena(contrasenaCifrada);
        practicante.setEstado(Estado.Activo);
        practicante.setNumeroPersonalProfesor(profesorSeleccionado.getIdUsuario());
        return practicante;
    }

    private void cargarProfesoresActivos() {
        ProfesorDao profesorDao = new ProfesorDao();
        try {
            List<Profesor> profesoresActivos = profesorDao.obtenerProfesoresActivos();
            comboBoxProfesor.getItems().addAll(profesoresActivos);
            comboBoxProfesor.setConverter(new StringConverter<Profesor>() {
                @Override
                public String toString(Profesor profesor) {
                    String profesorActivo = null;
                    if (profesor != null) {
                        profesorActivo = profesor.getNombre() + " " + profesor.getApellidos();
                    }
                    return profesorActivo;
                }
                @Override
                public Profesor fromString(String texto) {
                    return null;
                }
            });
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError, "Error al cargar profesores",
                    "No ha sido posible recuperar a los profesores activos");
        }
    }

    private void guardarPracticante(Practicante practicante) {
        PracticanteDao practicanteDao = new PracticanteDao();
        try {
            int filasAfectadas = practicanteDao.insertarPracticante(practicante);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCamposRegistrados();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Practicante en estado activo", "El practicante fue registrado exitosamente");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "No fue posible registrar al practicante, intente mas tarde");
            }
        } catch (RegistroDuplicadoExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Matricula repetida", "La matricula ya existe en el sistema, verifique la informacion");
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
        }
    }

    private String generarContrasena(String nombre, String matricula) {
        String contrasenaGenerada = nombre.toLowerCase() + matricula;
        return contrasenaGenerada;
    }

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCamposRegistrados() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNombres.clear();
        campoTextoApellidos.clear();
        campoTextoMatricula.clear();
        campoTextoLenguaIndigena.clear();
        grupoGenero.selectToggle(null);
        comboBoxProfesor.setValue(null);
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