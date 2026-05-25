package logica.dominio;

import logica.dominio.enums.EstadoRevision;

public class DocumentacionPracticante {
    private int idDocumentacionPracticante;
    private String rutaDeArchivo;
    private EstadoRevision estadoRevision;
    private String motivoRechazado;

    public DocumentacionPracticante(String rutaDeArchivo, EstadoRevision estadoRevision, String motivoRechazado) {
        this.rutaDeArchivo = rutaDeArchivo;
        this.estadoRevision = estadoRevision;
        this.motivoRechazado = motivoRechazado;
    }

    public int getIdDocumentacionPracticante() {
        return idDocumentacionPracticante;
    }
    public String getRutaDeArchivo() {
        return rutaDeArchivo;
    }
    public EstadoRevision getEstadoRevision() {
        return estadoRevision;
    }

    public String getMotivoRechazado() {
        return motivoRechazado;
    }

    public void setIdDocumentacionPracticante(int idDocumentacionPracticante) {
        this.idDocumentacionPracticante = idDocumentacionPracticante;
    }

    public void setRutaDeArchivo(String rutaDeArchivo) {
        this.rutaDeArchivo = rutaDeArchivo;
    }
    public void setEstadoRevision(EstadoRevision estadoRevision) {
        this.estadoRevision = estadoRevision;
    }

    public void setMotivoRechazado(String motivoRechazado) {
        this.motivoRechazado = motivoRechazado;
    }
}