package logica.dao.interfaces;
import logica.dominio.OrganizacionVinculada;
import logica.dao.excepciones.InserccionUsuarioExcepcion;

public interface OrganizacionVinculadaDaoInterfaz {
    void insertarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) throws InserccionUsuarioExcepcion;
}
