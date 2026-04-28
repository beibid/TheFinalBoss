package logica.dominio;

import logica.dominio.enums.Estado;
import logica.dominio.enums.Turno;

public class Profesor extends Usuario {
    private String numeroDePersonalProfesor;
    private Turno turno;

    public Profesor(String numeroDePersonalProfesor, Turno turno, String nombre, String apellidos, String contrasena, Estado estado) {
        super(nombre, apellidos, contrasena, estado);
        this.numeroDePersonalProfesor = numeroDePersonalProfesor;
        this.turno = turno;
    }

    public Profesor() {
        super("", "", "", null);
    }

    public String getNumeroDePersonalProfesor() {
        return numeroDePersonalProfesor;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setNumeroDePersonalProfesor(String numeroDePersonalProfesor) {
        this.numeroDePersonalProfesor = numeroDePersonalProfesor;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + numeroDePersonalProfesor;
    }
}