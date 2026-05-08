package logica.dominio;


import java.sql.Date;
import logica.dominio.enums.EstadoProyecto;


public class Proyecto {
    private int idProyecto;
    private String nombreProyecto;
    private String descripcion;
    private String responsableDelProyecto;
    private EstadoProyecto estado;
    private String nombreEmpresa;
    private String sectorEmpresa;
    private String direccionEmpresa;
    private int idOrganizacion;
    private String numPersonalCoordinador;
    private String numPersonalProfesor;
    private Date fechaRegistro;
    private int capacidad;

    public Proyecto(int idProyecto, String nombreProyecto, String descripcion, String responsableDelProyecto,
                    EstadoProyecto estado, String nombreEmpresa, String sectorEmpresa, String direccionEmpresa,
                    int idOrganizacion, String numPersonalCoordinador, String numPersonalProfesor, Date fechaRegistro,
                    int capacidad) {
        this.idProyecto = idProyecto;
        this.nombreProyecto = nombreProyecto;
        this.descripcion = descripcion;
        this.responsableDelProyecto = responsableDelProyecto;
        this.estado = estado;
        this.nombreEmpresa = nombreEmpresa;
        this.sectorEmpresa = sectorEmpresa;
        this.direccionEmpresa = direccionEmpresa;
        this.idOrganizacion = idOrganizacion;
        this.numPersonalCoordinador = numPersonalCoordinador;
        this.numPersonalProfesor = numPersonalProfesor;
        this.fechaRegistro = fechaRegistro;
        this.capacidad = capacidad;
    }

    public Proyecto(){

    }

    public int getIdProyecto() {
        return idProyecto;
    }
    public String getNombreProyecto() {
        return nombreProyecto;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getResponsableDelProyecto() {
        return responsableDelProyecto;
    }
    public EstadoProyecto getEstado() {
        return estado;
    }
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }
    public String getSectorEmpresa() {
        return sectorEmpresa;
    }
    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }
    public int getIdOrganizacion() {
        return idOrganizacion;
    }
    public String getNumPersonalCoordinador() {
        return numPersonalCoordinador;
    }

    public String getNumPersonalProfesor() {
        return numPersonalProfesor;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setResponsableDelProyecto(String responsableDelProyecto) {
        this.responsableDelProyecto = responsableDelProyecto;
    }
    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    public void setSectorEmpresa(String sectorEmpresa) {
        this.sectorEmpresa = sectorEmpresa;
    }
    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }
    public void setIdOrganizacion(int idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }
    public void setNumPersonalCoordinador(String numPersonalCoordinador) {
        this.numPersonalCoordinador = numPersonalCoordinador;
    }

    public void setNumPersonalProfesor(String numPersonalProfesor) {
        this.numPersonalProfesor = numPersonalProfesor;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return idProyecto + " " + nombreProyecto    ;
    }
}