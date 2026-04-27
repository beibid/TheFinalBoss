package logica.dominio;

import logica.dominio.enums.Estado;

public class UsuarioSesion {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipo;
    private Estado estado;

    public String getNombre() { return nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public String getTipo() { return tipo; }
    public Estado getEstado() { return estado; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEstado(Estado estado) { this.estado = estado; }
}
