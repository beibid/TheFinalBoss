package logica.dominio;

import java.sql.Date;
import logica.dominio.enums.EstadoPeriodo;

public class PeriodoUniversitario {
    private int idPeriodo;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    private EstadoPeriodo estado;

    public PeriodoUniversitario(int idPeriodo, String nombre, Date fechaInicio, Date fechaFin, EstadoPeriodo estado) {
        this.idPeriodo = idPeriodo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public PeriodoUniversitario() {
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }
    public String getNombre() {
        return nombre;
    }
    public Date getFechaInicio() {
        return fechaInicio;
    }
    public Date getFechaFin() {
        return fechaFin;
    }
    public EstadoPeriodo getEstado() {
        return estado;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    public void setEstado(EstadoPeriodo estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }
}