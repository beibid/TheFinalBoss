package logica.dominio;

public class PracticanteSeccion {
    private String matricula;
    private String noSeccion;
    private int idPeriodo;

    public PracticanteSeccion(String matricula, String noSeccion, int idPeriodo) {
        this.matricula = matricula;
        this.noSeccion = noSeccion;
        this.idPeriodo = idPeriodo;
    }

    public PracticanteSeccion() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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
}