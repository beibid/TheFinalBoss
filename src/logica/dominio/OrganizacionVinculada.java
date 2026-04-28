package logica.dominio;


public class OrganizacionVinculada {
    protected String nombre;
    protected String direccion;
    protected int idOrganizacion;

    public OrganizacionVinculada (String nombre, String direccion){
        this.nombre = nombre;
        this.direccion = direccion;
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

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {

        this.direccion = direccion;
    }

    public void setIdOrganizacion(int idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    @Override
    public String toString() {
        return nombre + " " + idOrganizacion;
    }
}
