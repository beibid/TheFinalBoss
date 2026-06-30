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
import logica.dao.objetos.PeriodoUniversitarioDao;
import logica.dominio.PeriodoUniversitario;
import logica.dominio.enums.EstadoPeriodo;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarPeriodoControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarPeriodoControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    private final PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();

    @FXML private TextField campoTextoNombre;
    @FXML private TextField selectorFechaInicio;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Seguro que desea registrar el nuevo Periodo?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("Seguro que desea cancelar?")) {
            limpiar();
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
            PeriodoUniversitario periodoUniversitario = construirPeriodo();
            ejecutarRegistro(periodoUniversitario);
        }
    }

    private boolean verificarCampos() {
        String nombre = campoTextoNombre.getText().trim();
        String fechaTexto = selectorFechaInicio.getText().trim();
        List<String> campos = List.of(nombre, fechaTexto);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean nombreValido = nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$");
        boolean formatoFechaValido = fechaTexto.matches("\\d{4}-\\d{2}-\\d{2}");
        boolean fechaNoAnterior = false;
        if (formatoFechaValido) {
            fechaNoAnterior = !LocalDate.parse(fechaTexto).isBefore(LocalDate.now());
        }
        boolean fechaValida = formatoFechaValido && fechaNoAnterior;
        boolean valido = true;
        if (!camposFormularioValido) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE SOLO PUEDE CONTENER LETRAS, NUMEROS Y GUIONES.");
            valido = false;
        } else if (!fechaValida) {
            mostrarError("Fecha invalida", "INGRESE UNA FECHA VALIDA EN FORMATO AAAA-MM-DD Y NO ANTERIOR A HOY.");
            valido = false;
        }
        return valido;
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

    private PeriodoUniversitario construirPeriodo() {
        String nombre = campoTextoNombre.getText().trim();
        Date fechaInicio = Date.valueOf(selectorFechaInicio.getText().trim());
        PeriodoUniversitario periodoUniversitario = new PeriodoUniversitario();
        periodoUniversitario.setNombre(nombre);
        periodoUniversitario.setFechaInicio(fechaInicio);
        periodoUniversitario.setEstado(EstadoPeriodo.Abierto);
        return periodoUniversitario;
    }

    private void ejecutarRegistro(PeriodoUniversitario periodoUniversitario) {
        try {
            boolean hayPeriodoAbierto = periodoUniversitarioDao.verificarPeriodoAbierto();
            if (hayPeriodoAbierto) {
                mostrarError("Periodo ya abierto", "YA EXISTE UN PERIODO ABIERTO. CIERRELO ANTES DE REGISTRAR UNO NUEVO.");
            } else {
                int filasAfectadas = periodoUniversitarioDao.insertarPeriodo(periodoUniversitario);
                if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                    limpiar();
                    mostrarExito("Periodo registrado", "EL PERIODO FUE REGISTRADO EXITOSAMENTE.");
                } else {
                    mostrarError("Error al registrar", "NO SE PUDO REGISTRAR EL PERIODO. INTENTE DE NUEVO.");
                }
            }
        } catch (UsuariosExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar periodo", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
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

    private void limpiar() {
        campoTextoNombre.clear();
        selectorFechaInicio.clear();
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
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