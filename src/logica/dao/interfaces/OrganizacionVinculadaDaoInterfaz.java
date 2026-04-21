package logica.dao.interfaces;

import logica.dao.excepciones.UsuariosExcepcion;
import logica.dominio.OrganizacionVinculada;

public interface OrganizacionVinculadaDaoInterfaz {
    int insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion;
    int modificarOrganizacionVinculada(int idOrganizacion, OrganizacionVinculada organizacionVinculada) throws UsuariosExcepcion;
}