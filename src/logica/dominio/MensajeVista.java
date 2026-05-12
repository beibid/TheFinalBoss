package logica.dominio;

import java.sql.Date;

public class MensajeVista {
    private int idMensaje;
    private String nombreUsuario;
    private String contenido;
    private Date fechaEnvio;

    public MensajeVista() {}

    public int getIdMensaje() {
        return idMensaje;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}