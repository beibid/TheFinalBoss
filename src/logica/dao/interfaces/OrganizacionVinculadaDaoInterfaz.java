package logica.dao.interfaces;


import logica.dominio.OrganizacionVinculada;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;


public interface OrganizacionVinculadaDaoInterfaz {
    void insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws InserccionBaseDeDatosExcepcion;
}
