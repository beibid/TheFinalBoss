package logica.dominio;


import java.sql.Date;


public class Mensaje {
    private int idMensaje;
    private String contenido;
    private Date fechaEnvio;

    public Mensaje(String contenido) {
        this.contenido = contenido;
    }

    public int getIdMensaje() {
        return idMensaje;
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
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
