package logica.dominio;


public class Seccion {
    private String noSeccion;
    private String periodo;

    public Seccion(String noSeccion, String periodo) {
        this.noSeccion = noSeccion;
        this.periodo = periodo;
    }

    public String getNoSeccion() {
        return noSeccion;
    }
    public String getPeriodo() {
        return periodo;
    }

    public void setNoSeccion(String noSeccion) {
        this.noSeccion = noSeccion;
    }
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
