package logica;

public class Profesor extends Usuario {
    private String numeroDePersonalProfesor;
    private Turno turno;

    public Profesor (String numeroDePersonalProfesor, Turno turno, String nombre, String apellidoPaterno, String apellidoMaterno,String contrasena, Estado estado ){
        super(nombre, apellidoPaterno, apellidoMaterno, contrasena, estado);
        this.numeroDePersonalProfesor = numeroDePersonalProfesor;
        this.turno = turno;
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

}

