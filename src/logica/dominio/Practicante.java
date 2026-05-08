package logica.dominio;

import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;

public class Practicante extends Usuario {
    private String matricula;
    private String lenguaIndigena;
    private Genero genero;

    public Practicante(String matricula, String lenguaIndigena, Genero genero, String nombre, String apellidos, String contrasena, String correo, Estado estado) {
        super(nombre, apellidos, contrasena, correo, estado);
        this.matricula = matricula;
        this.lenguaIndigena = lenguaIndigena;
        this.genero = genero;
    }

    public Practicante() {
        super("", "", "", "", null);
    }

    public String getMatricula() {
        return matricula;
    }

    public String getLenguaIndigena() {
        return lenguaIndigena;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setLenguaIndigena(String lenguaIndigena) {
        this.lenguaIndigena = lenguaIndigena;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + matricula;
    }
}