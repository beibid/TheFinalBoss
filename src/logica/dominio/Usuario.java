package logica.dominio;


import logica.dominio.enums.Estado;


public class Usuario {
    protected String nombre;
    protected String apellidos;
    protected String contrasena;
    protected Estado estado;

    public Usuario(String nombre, String apellidos, String contrasena, Estado estado){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.contrasena = contrasena;
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

    public Estado getEstado() {
        return estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos=apellidos;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}
