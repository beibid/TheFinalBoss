package logica.dominio;

import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;

public class Practicante extends Usuario {
    private String matricula;
    private String lenguaIndigena;
    private Genero genero;
    private int numeroPersonalProfesor;


    public Practicante(String matricula, String lenguaIndigena, Genero genero, String nombre,
                       String apellidos, String contrasena, String correo, Estado estado,
                       int numeroPersonalProfesor) {
        super(nombre, apellidos, contrasena, correo, estado);
        this.matricula = matricula;
        this.lenguaIndigena = lenguaIndigena;
        this.genero = genero;
        this.numeroPersonalProfesor = numeroPersonalProfesor;
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

    public int getNumeroPersonalProfesor() {
        return numeroPersonalProfesor;
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

    public void setNumeroPersonalProfesor (int numeroPersonalProfesor) {
        this.numeroPersonalProfesor = numeroPersonalProfesor;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + matricula;
    }
}