package logica.dominio;


import logica.dominio.enums.Estado;


public class Usuario {
    protected String nombre;
    protected String apellidoPaterno;
    protected String apellidoMaterno;
    protected String contrasena;
    protected Estado estado;

    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, String contrasena, Estado estado){
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}
