package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ActividadDao;
import logica.dominio.Actividad;
import logica.dominio.SesionUsuario;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarActividadControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarActividadControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int HORAS_MAXIMAS_POR_DIA = 8;
    private static final int HORAS_MAXIMAS_ACUMULADAS = 420;

    @FXML private TextField campoTextoTitulo;
    @FXML private TextField campoTextoDescripcion;
    @FXML private DatePicker campoTextoFechaInicio;
    @FXML private DatePicker campoTextoFechaFin;
    @FXML private TextField campoTextoHoras;
    @FXML private VBox panelError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private String matricula;

    @FXML
    public void initialize() {
        matricula = SesionUsuario.getInstance().getMatricula();
    }

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar la actividad?")) {
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
            if (verificarLimiteHoras()) {
                Actividad actividad = construirActividad();
                ejecutarRegistro(actividad);
            }
        }
    }

    private boolean verificarLimiteHoras() {
        boolean dentroDeLimite = false;
        ActividadDao actividadDao = new ActividadDao();
        try {
            int horasActuales = actividadDao.obtenerHorasTotalesPorPracticante(matricula);
            int horasNuevas = Integer.parseInt(campoTextoHoras.getText().trim());
            if (horasActuales + horasNuevas > HORAS_MAXIMAS_ACUMULADAS) {
                mostrarError("Limite de horas alcanzado",
                        "NO PUEDES SUPERAR LAS " + HORAS_MAXIMAS_ACUMULADAS +
                                " HORAS ACUMULADAS. TIENES " + horasActuales +
                                " HORAS Y QUIERES AGREGAR " + horasNuevas + ".");
            } else {
                dentroDeLimite = true;
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al verificar horas acumuladas", excepcion);
            mostrarError("Error inesperado", excepcion.getMessage().toUpperCase());
        }
        return dentroDeLimite;
    }

    private boolean camposValidos() {
        String titulo = campoTextoTitulo.getText().trim();
        String descripcion = campoTextoDescripcion.getText().trim();
        String horasTexto = campoTextoHoras.getText().trim();
        LocalDate fechaInicio = campoTextoFechaInicio.getValue();
        LocalDate fechaFin = campoTextoFechaFin.getValue();

        if (titulo.isEmpty() || descripcion.isEmpty() || horasTexto.isEmpty()) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
            return false;
        }
        if (fechaInicio == null) {
            mostrarError("Fecha invalida", "SELECCIONA LA FECHA DE INICIO");
            return false;
        }
        if (fechaFin == null) {
            mostrarError("Fecha invalida", "SELECCIONA LA FECHA DE FIN");
            return false;
        }

        boolean tituloValido = titulo.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$");
        if (!tituloValido) {
            mostrarError("Titulo invalido", "EL TITULO SOLO PUEDE CONTENER LETRAS, NUMEROS Y GUIONES");
            return false;
        }

        if (fechaInicio.isAfter(LocalDate.now())) {
            mostrarError("Fecha invalida", "LA FECHA DE INICIO NO PUEDE SER POSTERIOR A HOY");
            return false;
        }
        if (fechaFin.isBefore(fechaInicio)) {
            mostrarError("Fecha invalida", "LA FECHA FIN NO PUEDE SER ANTERIOR A LA FECHA DE INICIO");
            return false;
        }

        int horas;
        try {
            horas = Integer.parseInt(horasTexto);
        } catch (NumberFormatException e) {
            mostrarError("Horas invalidas", "LAS HORAS DEBEN SER UN NUMERO ENTERO");
            return false;
        }

        if (horas <= 0) {
            mostrarError("Horas invalidas", "LAS HORAS DEBEN SER MAYOR A CERO");
            return false;
        }

        long diasEntreFechas = fechaFin.toEpochDay() - fechaInicio.toEpochDay() + 1;
        int horasMaximas = (int) (diasEntreFechas * HORAS_MAXIMAS_POR_DIA);
        if (horas > horasMaximas) {
            mostrarError("Horas invalidas",
                    "NO PUEDES REGISTRAR MAS DE " + HORAS_MAXIMAS_POR_DIA +
                            " HORAS POR DIA. MAXIMO PERMITIDO PARA EL PERIODO: " + horasMaximas + " HORAS");
            return false;
        }

        return true;
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

    private Actividad construirActividad() {
        String titulo = campoTextoTitulo.getText().trim();
        String descripcion = campoTextoDescripcion.getText().trim();
        Date fechaInicio = Date.valueOf(campoTextoFechaInicio.getValue());
        Date fechaFin = Date.valueOf(campoTextoFechaFin.getValue());
        int horas = Integer.parseInt(campoTextoHoras.getText().trim());

        Actividad actividad = new Actividad();
        actividad.setTitulo(titulo);
        actividad.setDescripcion(descripcion);
        actividad.setFechaInicio(fechaInicio);
        actividad.setFechaFin(fechaFin);
        actividad.setHorasActividad(horas);
        actividad.setMatriculaPracticante(matricula);
        return actividad;
    }

    private void ejecutarRegistro(Actividad actividad) {
        ActividadDao actividadDao = new ActividadDao();
        try {
            int filasAfectadas = actividadDao.registrarActividad(actividad);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                mostrarExito("Actividad registrada", "LA ACTIVIDAD FUE REGISTRADA EXITOSAMENTE");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR LA ACTIVIDAD. INTENTE DE NUEVO");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar actividad", excepcion);
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
        campoTextoTitulo.clear();
        campoTextoDescripcion.clear();
        campoTextoFechaInicio.setValue(null);
        campoTextoFechaFin.setValue(null);
        campoTextoHoras.clear();
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