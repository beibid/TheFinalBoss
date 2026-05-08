package logica.dominio;


import logica.dominio.enums.TipoReporte;
import logica.dominio.enums.EstadoDeCalificacion;

import java.sql.Date;


public class Reporte {
    private TipoReporte tipoReporte;
    private String descripcion;
    private Date fechaGeneracion;
    private Double calificacion;
    private String observaciones;
    private EstadoDeCalificacion estadoDeCalificacion;
    private String matriculaPracticante;
    private String numPersonalProfesor;
    private String archivoAdjunto;
    private String nombreArchivo;

    public Reporte (TipoReporte tipoReporte, String descripcion, Date fechaGeneracion,
                    Double calificacion, String observaciones,
                    EstadoDeCalificacion estadoDeCalificacion, String matriculaPracticante,
                    String numPersonalProfesor, String archivoAdjunto, String nombreArchivo){

        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
        this.fechaGeneracion = fechaGeneracion;
        this.calificacion = calificacion;
        this.observaciones = observaciones;
        this.estadoDeCalificacion = estadoDeCalificacion;
        this.matriculaPracticante = matriculaPracticante;
        this.numPersonalProfesor = numPersonalProfesor;
        this.archivoAdjunto = archivoAdjunto;
        this.nombreArchivo = nombreArchivo;

    }

    public Reporte(TipoReporte tipoReporte, String descripcion, String matriculaPracticante, String archivoAdjunto, String nombreArchivo) {
        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
        this.matriculaPracticante = matriculaPracticante;
        this.archivoAdjunto = archivoAdjunto;
        this.nombreArchivo = nombreArchivo;
    }

    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public EstadoDeCalificacion getEstadoDeCalificacion() {
        return estadoDeCalificacion;
    }

    public String getMatriculaPracticante() {
        return matriculaPracticante;
    }

    public String getNumPersonalProfesor() {
        return numPersonalProfesor;
    }

    public String getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setEstadoDeCalificacion(EstadoDeCalificacion estadoDeCalificacion) {
        this.estadoDeCalificacion = estadoDeCalificacion;
    }

    public void setMatriculaPracticante(String matriculaPracticante) {
        this.matriculaPracticante = matriculaPracticante;
    }

    public void setNumPersonalProfesor(String numPersonalProfesor) {
        this.numPersonalProfesor = numPersonalProfesor;
    }

    public void setArchivoAdjunto(String archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}
