package logica.dominio;

import logica.dominio.enums.Estado;

public class Coordinador extends Usuario {
    private String numeroDePersonalCoordinador;

    public Coordinador(String numeroDePersonalCoordinador, String nombre, String apellidos, String contrasena, String correo, Estado estado) {
        super(nombre, apellidos, contrasena, correo, estado);
        this.numeroDePersonalCoordinador = numeroDePersonalCoordinador;
    }

    public Coordinador() {
        super("", "", "", "", null);
    }

    public String getNumeroDePersonalCoordinador() {
        return numeroDePersonalCoordinador;
    }

    public void setNumeroDePersonalCoordinador(String numeroDePersonalCoordinador) {
        this.numeroDePersonalCoordinador = numeroDePersonalCoordinador;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + numeroDePersonalCoordinador;
    }
}