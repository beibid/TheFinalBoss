package logica.dominio;

import logica.dominio.enums.Estado;

public class UsuarioSesion {
    private String nombre;
    private String apellidos;
    private String tipo;
    private Estado estado;

    public String getNombre() {
        return nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public Estado getEstado() {
        return estado;
    }
    public String getApellidos(){
        return apellidos;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
