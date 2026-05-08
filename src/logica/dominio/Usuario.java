package logica.dominio;

import logica.dominio.enums.Estado;

public class Usuario {
    protected String nombre;
    protected String apellidos;
    protected String contrasena;
    protected String correo;
    protected Estado estado;

    public Usuario(String nombre, String apellidos, String contrasena, String correo, Estado estado) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.contrasena = contrasena;
        this.correo = correo;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreo() {
        return correo;
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

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}