package logica.dominio;


import logica.dominio.enums.EstadoOrganizacion;


public class OrganizacionVinculada {
    protected String nombre;
    protected String direccion;
    protected int idOrganizacion;
    protected EstadoOrganizacion estadoOrganizacion;

    public OrganizacionVinculada (String nombre, String direccion, EstadoOrganizacion estadoOrganizacion){
        this.nombre = nombre;
        this.direccion = direccion;
        this.estadoOrganizacion = estadoOrganizacion;
    }
    public OrganizacionVinculada(){

    }

    public String getNombre() {

        return nombre;
    }

    public String getDireccion() {

        return direccion;
    }

    public int getIdOrganizacion() {
        return idOrganizacion;
    }

    public EstadoOrganizacion getEstadoOrganizacion() {
        return estadoOrganizacion;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {

        this.direccion = direccion;
    }

    public void setIdOrganizacion(int idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public void setEstadoOrganizacion(EstadoOrganizacion estadoOrganizacion) {
        this.estadoOrganizacion = estadoOrganizacion;
    }

    @Override
    public String toString() {
        return nombre + " " + idOrganizacion;
    }
}
