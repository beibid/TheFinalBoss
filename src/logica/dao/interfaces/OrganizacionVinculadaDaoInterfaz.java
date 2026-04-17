package logica.dao.interfaces;


import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.OrganizacionVinculada;


public interface OrganizacionVinculadaDaoInterfaz {
    void insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion;
    void modificarOrganizacionVinculada(int idOrganizacion, OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion;
}
