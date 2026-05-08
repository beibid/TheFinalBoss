package logica.dominio;

import logica.dominio.enums.Estado;
import logica.dominio.enums.Rol;

public class UsuarioSesion {
    private String nombre;
    private String apellidos;
    private Rol rol;
    private Estado estado;


    public String getNombre() {
        return nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public Rol getRol() {
        return rol;
    }
    public Estado getEstado() {
        return estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}