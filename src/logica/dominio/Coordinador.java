package logica.dominio;

import logica.dominio.enums.Estado;

public class Coordinador extends Usuario {
    private String numeroDePersonalCoordinador;

    public Coordinador(String numeroDePersonalCoordinador, String nombre, String apellidoPaterno, String apellidoMaterno, String contrasena, Estado estado){
        super(nombre, apellidoPaterno, apellidoMaterno, contrasena, estado);
        this.numeroDePersonalCoordinador = numeroDePersonalCoordinador;
    }

    public String getNumeroDePersonalCoordinador() {
        return numeroDePersonalCoordinador;
    }

    public void setNumeroDePersonalCoordinador(String numeroDePersonalCoordinador) {
        this.numeroDePersonalCoordinador = numeroDePersonalCoordinador;
    }

}
