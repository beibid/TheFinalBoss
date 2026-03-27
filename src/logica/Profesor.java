package logica;

public class Profesor extends Usuario {
    private String numeroDePersonalProfesor;
    private Turno turno;
    private int idUsuarioPersonal;

    public Profesor (String numeroDePersonalProfesor, Turno turno, int idUsuarioPersonal, String nombre, String apellidoPaterno, String apellidoMaterno,String contrasena, Estado estado ){
        super(nombre, apellidoPaterno, apellidoMaterno, contrasena, estado);
        this.numeroDePersonalProfesor = numeroDePersonalProfesor;
        this.turno = turno;
        this.idUsuarioPersonal = idUsuarioPersonal;
    }

    public String getNumeroDePersonalProfesor() {
        return numeroDePersonalProfesor;
    }

    public Turno getTurno() {
        return turno;
    }

    public int getIdUsuarioPersonal() {
        return idUsuarioPersonal;
    }

    public void setNumeroDePersonalProfesor(String numeroDePersonalProfesor) {
        this.numeroDePersonalProfesor = numeroDePersonalProfesor;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public void setIdUsuarioPersonal(int idUsuarioPersonal) {
        this.idUsuarioPersonal = idUsuarioPersonal;
    }
}

