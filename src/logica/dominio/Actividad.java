package logica.dominio;

import java.sql.Date;

public class Actividad {
    private int idActividad;
    private String titulo;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private int horasActividad;
    private String matriculaPracticante;

    public Actividad(int idActividad, String titulo, String descripcion,
                     Date fechaInicio, Date fechaFin, int horasActividad,
                     String matriculaPracticante) {
        this.idActividad = idActividad;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasActividad = horasActividad;
        this.matriculaPracticante = matriculaPracticante;
    }

    public Actividad() {
    }

    public int getIdActividad() {
        return idActividad;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public int getHorasActividad() {
        return horasActividad;
    }

    public String getMatriculaPracticante() {
        return matriculaPracticante;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setHorasActividad(int horasActividad) {
        this.horasActividad = horasActividad;
    }

    public void setMatriculaPracticante(String matriculaPracticante) {
        this.matriculaPracticante = matriculaPracticante;
    }

    @Override
    public String toString() {
        return idActividad + " " + titulo;
    }
}