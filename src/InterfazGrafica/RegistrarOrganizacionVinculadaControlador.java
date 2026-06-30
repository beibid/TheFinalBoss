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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarOrganizacionVinculadaControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarOrganizacionVinculadaControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int LIMITE_NOMBRE = 80;
    private static final int LIMITE_DIRECCION = 80;
    private static final int LIMITE_TELEFONO = 15;
    private static final int LIMITE_CORREO = 100;
    private static final int LIMITE_SECTOR = 50;

    private final OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();

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

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Seguro que desea registrar la organizacion?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
            limpiarCampos();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void procesarRegistro() {
        boolean camposCorrectos = verificarCampos();
        if (camposCorrectos) {
            guardarOrganizacion(construirOrganizacion());
        }
    }

    private boolean verificarCampos() {
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
        boolean longitudNombreValida = nombre.length() <= LIMITE_NOMBRE;
        boolean longitudDireccionValida = direccion.length() <= LIMITE_DIRECCION;
        boolean longitudCorreoValida = correo.length() <= LIMITE_CORREO;
        boolean longitudSectorValida = sector.length() <= LIMITE_SECTOR;
        boolean valido = true;
        if (!camposFormularioValido) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE NO DEBE CONTENER CARACTERES ESPECIALES.");
            valido = false;
        } else if (!longitudNombreValida) {
            mostrarError("Nombre demasiado largo", "EL NOMBRE NO PUEDE EXCEDER " + LIMITE_NOMBRE + " CARACTERES.");
            valido = false;
        } else if (!direccionValida) {
            mostrarError("Direccion invalida", "LA DIRECCION CONTIENE CARACTERES NO PERMITIDOS.");
            valido = false;
        } else if (!longitudDireccionValida) {
            mostrarError("Direccion demasiado larga", "LA DIRECCION NO PUEDE EXCEDER " + LIMITE_DIRECCION + " CARACTERES.");
            valido = false;
        } else if (!telefonoValido) {
            mostrarError("Telefono invalido", "EL TELEFONO DEBE CONTENER EXACTAMENTE 10 DIGITOS NUMERICOS.");
            valido = false;
        } else if (!correoValido) {
            mostrarError("Correo invalido", "INGRESE UN CORREO ELECTRONICO VALIDO.");
            valido = false;
        } else if (!longitudCorreoValida) {
            mostrarError("Correo demasiado largo", "EL CORREO NO PUEDE EXCEDER " + LIMITE_CORREO + " CARACTERES.");
            valido = false;
        } else if (!sectorValido) {
            mostrarError("Sector invalido", "EL SECTOR NO DEBE CONTENER NUMEROS NI CARACTERES ESPECIALES.");
            valido = false;
        } else if (!longitudSectorValida) {
            mostrarError("Sector demasiado largo", "EL SECTOR NO PUEDE EXCEDER " + LIMITE_SECTOR + " CARACTERES.");
            valido = false;
        }
        return valido;
    }

    private OrganizacionVinculada construirOrganizacion() {
        String nombre = campoTextoNombre.getText().trim();
        String direccion = campoTextoDireccion.getText().trim();
        String telefono = campoTextoTelefono.getText().trim();
        String correo = campoTextoCorreo.getText().trim();
        String sector = campoTextoSector.getText().trim();
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada();
        organizacionVinculada.setNombre(nombre);
        organizacionVinculada.setDireccion(direccion);
        organizacionVinculada.setTelefono(telefono);
        organizacionVinculada.setCorreo(correo);
        organizacionVinculada.setSector(sector);
        organizacionVinculada.setEstadoOrganizacion(EstadoOrganizacion.Activa);
        return organizacionVinculada;
    }

    private void guardarOrganizacion(OrganizacionVinculada organizacionVinculada) {
        try {
            int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacionVinculada);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCampos();
                mostrarExito("Organizacion vinculada registrada", "ORGANIZACION REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR LA ORGANIZACION. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar organizacion vinculada", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
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

    private void limpiarCampos() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        campoTextoNombre.clear();
        campoTextoDireccion.clear();
        campoTextoTelefono.clear();
        campoTextoCorreo.clear();
        campoTextoSector.clear();
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