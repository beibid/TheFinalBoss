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
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;
import logica.dominio.enums.EstadoOrganizacion;
import java.util.List;

public class RegistrarOrganizacionVinculadaControlador {

    @FXML private TextField campoTextoNombre;
    @FXML private TextField campoTextoDireccion;
    @FXML private TextField campoTextoTelefono;
    @FXML private TextField campoTextoCorreo;
    @FXML private TextField campoTextoSector;
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
        if (confirmarAccion("¿Seguro que desea registrar la organizacion?")) {
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

    private void procesarRegistro() {
        if (camposValidos()) {
            guardarOrganizacion(construirOrganizacion());
        }
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombre.getText().trim();
        String direccion = campoTextoDireccion.getText().trim();
        String telefono = campoTextoTelefono.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String sector = campoTextoSector.getText().trim();

        List<String> campos = List.of(nombre, direccion, telefono, correo, sector);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean nombreValido = nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$");
        boolean direccionValida = direccion.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s#.,\\-]+$");
        boolean telefonoValido = telefono.matches("\\d{10}");
        boolean correoValido = correo.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
        boolean sectorValido = sector.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

        mostrarPrimerError(camposFormularioValido, nombreValido, direccionValida, telefonoValido, correoValido, sectorValido);

        boolean formularioValido = camposFormularioValido && nombreValido && direccionValida && telefonoValido && correoValido && sectorValido;
        return formularioValido;
    }

    private void mostrarPrimerError(boolean camposFormularioValido, boolean nombreValido, boolean direccionValida,
                                    boolean telefonoValido, boolean correoValido, boolean sectorValido) {
        if (!camposFormularioValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
        } else if (!nombreValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Nombre invalido", "EL NOMBRE NO DEBE CONTENER CARACTERES ESPECIALES");
        } else if (!direccionValida) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Direccion invalida", "LA DIRECCION CONTIENE CARACTERES NO PERMITIDOS");
        } else if (!telefonoValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Telefono invalido", "EL TELEFONO DEBE CONTENER EXACTAMENTE 10 DIGITOS NUMERICOS");
        } else if (!correoValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO");
        } else if (!sectorValido) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Sector invalido", "EL SECTOR NO DEBE CONTENER NUMEROS NI CARACTERES ESPECIALES");
        }
    }

    private OrganizacionVinculada construirOrganizacion() {
        String nombre = campoTextoNombre.getText().trim();
        String direccion = campoTextoDireccion.getText().trim();
        String telefono = campoTextoTelefono.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String sector = campoTextoSector.getText().trim();

        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada();
        organizacionVinculada.setNombre(limitarTexto(nombre, 80));
        organizacionVinculada.setDireccion(limitarTexto(direccion, 80));
        organizacionVinculada.setTelefono(limitarTexto(telefono, 15));
        organizacionVinculada.setCorreo(limitarTexto(correo, 100));
        organizacionVinculada.setSector(limitarTexto(sector, 50));
        organizacionVinculada.setEstadoOrganizacion(EstadoOrganizacion.Activa);
        return organizacionVinculada;
    }

    private void guardarOrganizacion(OrganizacionVinculada organizacionVinculada) {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        try {
            int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacionVinculada);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCampos();
                mostrarPanel(etiquetaTituloExito, etiquetaMensajeExito, panelExito,
                        "Organizacion vinculada con estado activa", "ORGANIZACION REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                        "Error al registrar", "NO SE PUDO REGISTRAR LA ORGANIZACION. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarPanel(etiquetaTituloError, etiquetaMensajeError, panelError,
                    "Error inesperado", excepcion.getMessage().toUpperCase());
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

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCampos() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNombre.clear();
        campoTextoDireccion.clear();
        campoTextoTelefono.clear();
        campoTextoCorreo.clear();
        campoTextoSector.clear();
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