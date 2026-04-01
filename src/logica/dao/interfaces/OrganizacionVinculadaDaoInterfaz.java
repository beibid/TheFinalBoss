package logica.dao.interfaces;
import logica.dominio.OrganizacionVinculada;
import logica.dao.excepciones.DaoExcepcion;

public interface OrganizacionVinculadaDaoInterfaz {
    void insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws DaoExcepcion;
}
