package logica.dao.interfaces;
import logica.OrganizacionVinculada;
import logica.dao.excepciones.DaoExcepcion;

public interface OrganizacionVinculadaDaoInterfaz {
    void insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws DaoExcepcion;
}
