package logica.dominio;

public class PreferenciaProyecto {

    private String matricula;
    private int    idProyecto;
    private int    prioridad;
    private String nombreProyecto;
    private String nombreEmpresa;
    private String descripcion;
    private String estado;

    public PreferenciaProyecto() {}

    public PreferenciaProyecto(String matricula, int idProyecto, int prioridad) {
        this.matricula  = matricula;
        this.idProyecto = idProyecto;
        this.prioridad  = prioridad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Prioridad " + prioridad + ": " + nombreProyecto + " — " + nombreEmpresa;
    }
}