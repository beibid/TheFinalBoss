package logica.dominio;

import logica.dominio.enums.Estado;
import logica.dominio.enums.Rol;

public class UsuarioSesion {
    private String nombre;
    private String apellidos;
    private Rol rol;
    private Estado estado;
    private String matricula;
    private int idUsuario;
    private String identificador;
    private boolean debeCambiarContrasena;

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

    public String getMatricula() {
        return matricula;
    }

    public int getIdUsuario() {
        return idUsuario;
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

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isDebeCambiarContrasena() {
        return debeCambiarContrasena;
    }

    public void setDebeCambiarContrasena(boolean debeCambiarContrasena) {
        this.debeCambiarContrasena = debeCambiarContrasena;
    }
}