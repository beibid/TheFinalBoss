package logica.dominio;

import java.sql.Date;

public class EntregaDocumentacion {
    private int idEntregaDocumento;
    private Date fechaEntrega;
    private String entregaMatricula;
    private int entregaDocumentacion;

    public EntregaDocumentacion(Date fechaEntrega, String entregaMatricula, int entregaDocumentacion) {
        this.fechaEntrega = fechaEntrega;
        this.entregaMatricula = entregaMatricula;
        this.entregaDocumentacion = entregaDocumentacion;
    }

    public int getIdEntregaDocumento() {
        return idEntregaDocumento;
    }
    public Date getFechaEntrega() {
        return fechaEntrega;
    }
    public String getEntregaMatricula() {
        return entregaMatricula;
    }
    public int getEntregaDocumentacion() {
        return entregaDocumentacion;
    }

    public void setIdEntregaDocumento(int idEntregaDocumento) {
        this.idEntregaDocumento = idEntregaDocumento;
    }
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
    public void setEntregaMatricula(String entregaMatricula) {
        this.entregaMatricula = entregaMatricula;
    }
    public void setEntregaDocumentacion(int entregaDocumentacion) {
        this.entregaDocumentacion = entregaDocumentacion;
    }
}
