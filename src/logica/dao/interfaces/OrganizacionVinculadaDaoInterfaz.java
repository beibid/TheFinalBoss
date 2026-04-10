package logica.dao.interfaces;
import logica.dto.OrganizacionVinculada;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;

public interface OrganizacionVinculadaDaoInterfaz {
    void insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws InserccionBaseDeDatosExcepcion;
}
