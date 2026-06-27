package logica.dominio;

public class Seccion {
    private String noSeccion;
    private int idPeriodo;
    private String numPersonalProfesor;
    private String nombrePeriodo;

    public Seccion(String noSeccion, int idPeriodo, String numPersonalProfesor) {
        this.noSeccion = noSeccion;
        this.idPeriodo = idPeriodo;
        this.numPersonalProfesor = numPersonalProfesor;
    }

    public Seccion() {
    }

    public String getNoSeccion() {
        return noSeccion;
    }

    public void setNoSeccion(String noSeccion) {
        this.noSeccion = noSeccion;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getNumPersonalProfesor() {
        return numPersonalProfesor;
    }

    public void setNumPersonalProfesor(String numPersonalProfesor) {
        this.numPersonalProfesor = numPersonalProfesor;
    }

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

    @Override
    public String toString() {
        return noSeccion + " - " + nombrePeriodo;
    }
}