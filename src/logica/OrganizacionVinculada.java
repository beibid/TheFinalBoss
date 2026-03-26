package logica;

public class OrganizacionVinculada {
    protected String nombre;
    protected String direccion;

    public OrganizacionVinculada (String nombre, String direccion){
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
