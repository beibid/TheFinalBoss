package logica.dominio;

import logica.dominio.enums.EstadoRevision;

public class DocumentacionPracticante {
    private int idDocumentacionPracticante;
    private String rutaDeArchivo;
    private EstadoRevision estadoRevision;

    public DocumentacionPracticante(String rutaDeArchivo, EstadoRevision estadoRevision) {
        this.rutaDeArchivo = rutaDeArchivo;
        this.estadoRevision = estadoRevision;
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

    public void setIdDocumentacionPracticante(int idDocumentacionPracticante) {
        this.idDocumentacionPracticante = idDocumentacionPracticante;
    }
    public void setRutaDeArchivo(String rutaDeArchivo) {
        this.rutaDeArchivo = rutaDeArchivo;
    }
    public void setEstadoRevision(EstadoRevision estadoRevision) {
        this.estadoRevision = estadoRevision;
    }
}