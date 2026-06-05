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

public class RegistrarPeriodoControlador {

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

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
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar el nuevo Periodo?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        if (confirmarAccion("¿Seguro que desea cancelar?")) {
            limpiar();
        }
    }

    @FXML
    private void botonRegresar(ActionEvent evento) {
        Stage escenario = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        escenario.close();
    }

    private void procesarRegistro() {
        if (camposValidos()) {
            PeriodoUniversitario periodoUniversitario = construirPeriodo();
            ejecutarRegistro(periodoUniversitario);
        }
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombre.getText().trim();
        String fechaTexto = selectorFechaInicio.getText().trim();

        List<String> campos = List.of(nombre, fechaTexto);
        boolean camposFormularioValido = !camposVacios(campos);
        boolean nombreValido = nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$");
        boolean fechaValida = fechaTexto.matches("\\d{4}-\\d{2}-\\d{2}") &&
                !LocalDate.parse(fechaTexto).isBefore(LocalDate.now());

        mostrarPrimerError(camposFormularioValido, nombreValido, fechaValida);

        boolean formularioValido = camposFormularioValido && nombreValido && fechaValida;
        return formularioValido;
    }

    private void mostrarPrimerError(boolean camposFormularioValido, boolean nombreValido, boolean fechaValida) {
        if (!camposFormularioValido) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
        } else if (!nombreValido) {
            mostrarError("Nombre invalido", "EL NOMBRE SOLO PUEDE CONTENER LETRAS, NUMEROS Y GUIONES");
        } else if (!fechaValida) {
            mostrarError("Fecha invalida", "INGRESE UNA FECHA VALIDA EN FORMATO AAAA-MM-DD Y NO ANTERIOR A HOY");
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
        PeriodoUniversitarioDao periodoUniversitarioDao = new PeriodoUniversitarioDao();
        try {
            boolean hayPeriodoAbierto = periodoUniversitarioDao.verificarPeriodoAbierto();
            if (hayPeriodoAbierto) {
                mostrarError("Periodo ya abierto", "YA EXISTE UN PERIODO ABIERTO. CIERRELO ANTES DE REGISTRAR UNO NUEVO");
                return;
            }
            int filasAfectadas = periodoUniversitarioDao.insertarPeriodo(periodoUniversitario);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                mostrarExito("Periodo registrado", "EL PERIODO FUE REGISTRADO EXITOSAMENTE");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR EL PERIODO. INTENTE DE NUEVO");
            }
        } catch (UsuariosExcepcion excepcion) {
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
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

    private void limpiar() {
        campoTextoNombre.clear();
        selectorFechaInicio.clear();
        ocultarError();
        ocultarExito();
    }

    private void mostrarError(String titulo, String mensaje) {
        etiquetaTituloError.setText(titulo);
        etiquetaMensajeError.setText(mensaje);
        panelError.setVisible(true);
        panelError.setManaged(true);
    }

    private void ocultarError() {
        panelError.setVisible(false);
        panelError.setManaged(false);
    }

    private void mostrarExito(String titulo, String mensaje) {
        etiquetaTituloExito.setText(titulo);
        etiquetaMensajeExito.setText(mensaje);
        panelExito.setVisible(true);
        panelExito.setManaged(true);
    }

    private void ocultarExito() {
        panelExito.setVisible(false);
        panelExito.setManaged(false);
    }
}