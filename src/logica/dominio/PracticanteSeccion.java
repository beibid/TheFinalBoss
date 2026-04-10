package logica.dominio;

public class PracticanteSeccion {
    private String matricula;
    private String noSeccion;

    public PracticanteSeccion(String matricula, String noSeccion) {
        this.matricula = matricula;
        this.noSeccion = noSeccion;
    }

    public String getMatricula() { return matricula; }
    public String getNoSeccion() { return noSeccion; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setNoSeccion(String noSeccion) { this.noSeccion = noSeccion; }
}