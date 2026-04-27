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
import logica.dao.objetos.PracticanteDao;
import logica.dominio.Practicante;
import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;
import java.net.URL;
import java.util.ResourceBundle;

public class PracticanteControlGUI implements Initializable {

    @FXML private TextField txtMatricula;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private RadioButton rdiobtnFemenino;
    @FXML private RadioButton rdiobtnMasculino;
    @FXML private TextField txtLenguaIndigena;
    @FXML private VBox panelError;
    @FXML private Label lblTituloError;
    @FXML private Label lblMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label lblTituloExito;
    @FXML private Label lblMensajeExito;

    private ToggleGroup grupoGenero = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rdiobtnMasculino.setToggleGroup(grupoGenero);
        rdiobtnFemenino.setToggleGroup(grupoGenero);
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
    private void botonRegresar(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/InterfazGrafica/vistas/SeccionCoordinadorVista.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void radioBoton() {}

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
        Practicante practicante = construirPracticante();
        guardarPracticante(practicante);
    }

    private boolean camposValidos() {
        String nombre = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String matricula = txtMatricula.getText().trim();

        if (matricula.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
            mostrarError("Campos obligatorios vacios",
                    "Verifique la informacion e intente de nuevo");
            return false;
        }
        if (grupoGenero.getSelectedToggle() == null) {
            mostrarError("Genero no seleccionado",
                    "Seleccione un genero para el practicante.");
            return false;
        }
        return true;
    }

    private Practicante construirPracticante() {
        String nombre = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String matricula = txtMatricula.getText().trim();
        String lenguaIndigena = txtLenguaIndigena.getText().trim();
        Genero genero = rdiobtnMasculino.isSelected() ? Genero.Masculino : Genero.Femenino;
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
            if (filasAfectadas > 0) {
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
        txtNombres.clear();
        txtApellidos.clear();
        txtMatricula.clear();
        txtLenguaIndigena.clear();
        grupoGenero.selectToggle(null);
    }

    private void mostrarError(String titulo, String mensaje) {
        lblTituloError.setText(titulo);
        lblMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        lblTituloExito.setText(titulo);
        lblMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}