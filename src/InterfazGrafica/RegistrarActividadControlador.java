package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.objetos.ActividadDao;
import logica.dominio.Actividad;
import logica.dominio.SesionUsuario;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarActividadControlador {

    private static final Logger LOGGER = Logger.getLogger(RegistrarActividadControlador.class.getName());
    private static final int FILAS_AFECTADAS_ESPERADAS = 1;
    private static final int HORAS_MAXIMAS_POR_DIA = 8;
    private static final int HORAS_MAXIMAS_ACUMULADAS = 420;

    private final ActividadDao actividadDao = new ActividadDao();

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
        matricula = SesionUsuario.getInstance().getUsuarioActivo().getMatricula();
        final LocalDate minimoPermitido = LocalDate.now().minusYears(1);
        Callback<DatePicker, DateCell> factoriaCeldas = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate fecha, boolean vacio) {
                        super.updateItem(fecha, vacio);
                        setDisable(fecha.isBefore(minimoPermitido));
                    }
                };
            }
        };
        campoTextoFechaInicio.setDayCellFactory(factoriaCeldas);
        campoTextoFechaFin.setDayCellFactory(factoriaCeldas);
    }

    @FXML
    private void botonRegistrar() {
        ocultarPanel(panelError);
        ocultarPanel(panelExito);
        if (confirmarAccion("Seguro que desea registrar la actividad?")) {
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
        boolean horasValidas = false;
        if (camposCorrectos) {
            horasValidas = verificarLimiteHoras();
        }
        if (camposCorrectos && horasValidas) {
            Actividad actividad = construirActividad();
            ejecutarRegistro(actividad);
        }
    }

    private boolean verificarLimiteHoras() {
        boolean dentroDeLimite = false;
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

    private boolean verificarCampos() {
        String titulo = campoTextoTitulo.getText().trim();
        String descripcion = campoTextoDescripcion.getText().trim();
        String horasTexto = campoTextoHoras.getText().trim();
        LocalDate fechaInicio = campoTextoFechaInicio.getValue();
        LocalDate fechaFin = campoTextoFechaFin.getValue();
        boolean valido = true;
        if (camposVacios(List.of(titulo, descripcion, horasTexto))) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS.");
            valido = false;
        } else if (fechaInicio == null) {
            mostrarError("Fecha invalida", "SELECCIONA LA FECHA DE INICIO.");
            valido = false;
        } else if (fechaFin == null) {
            mostrarError("Fecha invalida", "SELECCIONA LA FECHA DE FIN.");
            valido = false;
        } else if (!titulo.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$")) {
            mostrarError("Titulo invalido", "EL TITULO SOLO PUEDE CONTENER LETRAS, NUMEROS Y GUIONES.");
            valido = false;
        } else if (fechaFin.isBefore(fechaInicio)) {
            mostrarError("Fecha invalida", "LA FECHA FIN NO PUEDE SER ANTERIOR A LA FECHA DE INICIO.");
            valido = false;
        } else {
            valido = verificarHorasTexto(horasTexto, fechaInicio, fechaFin);
        }
        return valido;
    }

    private boolean verificarHorasTexto(String horasTexto, LocalDate fechaInicio, LocalDate fechaFin) {
        boolean valido = true;
        int horas = 0;
        boolean esNumero = true;
        try {
            horas = Integer.parseInt(horasTexto);
        } catch (NumberFormatException excepcion) {
            LOGGER.log(Level.WARNING, "Horas no numericas", excepcion);
            esNumero = false;
        }
        if (!esNumero) {
            mostrarError("Horas invalidas", "LAS HORAS DEBEN SER UN NUMERO ENTERO.");
            valido = false;
        } else if (horas <= 0) {
            mostrarError("Horas invalidas", "LAS HORAS DEBEN SER MAYOR A CERO.");
            valido = false;
        } else {
            long diasEntreFechas = fechaFin.toEpochDay() - fechaInicio.toEpochDay() + 1;
            int horasMaximas = (int) (diasEntreFechas * HORAS_MAXIMAS_POR_DIA);
            if (horas > horasMaximas) {
                mostrarError("Horas invalidas",
                        "NO PUEDES REGISTRAR MAS DE " + HORAS_MAXIMAS_POR_DIA +
                                " HORAS POR DIA. MAXIMO PERMITIDO PARA EL PERIODO: " + horasMaximas + " HORAS.");
                valido = false;
            }
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
        try {
            int filasAfectadas = actividadDao.registrarActividad(actividad);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiar();
                mostrarExito("Actividad registrada", "LA ACTIVIDAD FUE REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar", "NO SE PUDO REGISTRAR LA ACTIVIDAD. INTENTE DE NUEVO.");
            }
        } catch (MensajeriaExcepcion excepcion) {
            LOGGER.log(Level.SEVERE, "Error al registrar actividad", excepcion);
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
        campoTextoTitulo.clear();
        campoTextoDescripcion.clear();
        campoTextoFechaInicio.setValue(null);
        campoTextoFechaFin.setValue(null);
        campoTextoHoras.clear();
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