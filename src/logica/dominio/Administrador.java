package logica.dominio;

import logica.dominio.enums.Estado;

public class Administrador extends Usuario {

    private String numeroDePersonalAdministrador;

    public Administrador(String numeroDePersonalAdministrador, String nombre, String apellidos, String contrasena, Estado estado) {
        super(nombre, apellidos, contrasena, estado);
        this.numeroDePersonalAdministrador = numeroDePersonalAdministrador;
    }

    public Administrador() {
        super("", "", "", null);
    }

    public String getNumeroDePersonalAdministrador() {
        return numeroDePersonalAdministrador;
    }

    public void setNumeroDePersonalAdministrador(String numeroDePersonalAdministrador) {
        this.numeroDePersonalAdministrador = numeroDePersonalAdministrador;
    }
}