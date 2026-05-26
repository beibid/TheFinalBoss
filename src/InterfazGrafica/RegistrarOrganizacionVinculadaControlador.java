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
    @FXML private VBox panelError;
    @FXML private Label etiquetaTituloError;
    @FXML private Label etiquetaMensajeError;
    @FXML private VBox panelExito;
    @FXML private Label etiquetaTituloExito;
    @FXML private Label etiquetaMensajeExito;

    private static final int FILAS_AFECTADAS_ESPERADAS = 1;

    private OrganizacionVinculada construirOrganizacion() {
        String nombre = campoTextoNombre.getText().trim();
        String direccion = campoTextoDireccion.getText().trim();

        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada();
        organizacionVinculada.setNombre(limitarTexto(nombre, 55));
        organizacionVinculada.setDireccion(limitarTexto(direccion, 55));
        organizacionVinculada.setEstadoOrganizacion(EstadoOrganizacion.Activa);
        return organizacionVinculada;
    }

    private void guardarOrganizacion(OrganizacionVinculada organizacionVinculada) {
        OrganizacionVinculadaDao organizacionVinculadaDao = new OrganizacionVinculadaDao();
        try {
            int filasAfectadas = organizacionVinculadaDao.insertarOrganizacionVinculada(organizacionVinculada);
            if (filasAfectadas >= FILAS_AFECTADAS_ESPERADAS) {
                limpiarCampos();
                mostrarExito("Organizacion vinculada con estado activa",
                        "ORGANIZACION REGISTRADA EXITOSAMENTE.");
            } else {
                mostrarError("Error al registrar",
                        "NO SE PUDO REGISTRAR LA ORGANIZACION. INTENTE DE NUEVO.");
            }
        } catch (UsuariosExcepcion e) {
            mostrarError("Error inesperado", e.getMessage().toUpperCase());
        }
    }

    private boolean camposVacios(List<String> campos){
        boolean hayCamposVacios = false;
        for (String campo : campos){
            if (campo.isEmpty()){
                hayCamposVacios = true;
            }
        }
        return hayCamposVacios;
    }

    private boolean camposValidos() {
        String nombre = campoTextoNombre.getText().trim();
        String direccion = campoTextoDireccion.getText().trim();

        List<String> campo = List.of(nombre, direccion);
        boolean camposFormularioVacios = !camposVacios(campo);

        if (!camposFormularioVacios) {
            mostrarError("Campos obligatorios vacios", "POR FAVOR LLENE TODOS LOS CAMPOS");
        }
        return camposFormularioVacios;
    }


    private void procesarRegistro() {
        if (!camposValidos()) {
            mostrarError("Campos obligatorios vacios",
                    "Verifica la informacion e intente de nuevo.");
            return;
        }
        guardarOrganizacion(construirOrganizacion());
    }

    @FXML
    private void botonRegistrar() {
        ocultarError();
        ocultarExito();
        if (confirmarAccion("¿Seguro que desea registrar la organizacion?")) {
            procesarRegistro();
        }
    }

    @FXML
    private void botonCancelar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Cancelar registro");
        alerta.setHeaderText("¿Seguro que desea cancelar?");
        alerta.setContentText("");

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);

        alerta.showAndWait().ifPresent(botonPresionado -> {
            if (botonPresionado == botonSi) {
                limpiarCampos();
            }
        });
    }

    @FXML
    private void botonRegresar(ActionEvent event) throws Exception{
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

    private String limitarTexto(String texto, int limite) {
        return texto.substring(0, Math.min(limite, texto.length()));
    }

    private void limpiarCampos() {
        ocultarError();
        ocultarExito();
        campoTextoNombre.clear();
        campoTextoDireccion.clear();
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
