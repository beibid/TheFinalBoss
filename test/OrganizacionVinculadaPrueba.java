

import org.junit.jupiter.api.Test;
import logica.dao.objetos.OrganizacionVinculadaDao;
import logica.dominio.OrganizacionVinculada;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class OrganizacionVinculadaPrueba {
    @Test
    public void insertarOrganizacionVinculada(){
        OrganizacionVinculada nuevaOrganizacion = new OrganizacionVinculada("Oracle", "Su casa");
        OrganizacionVinculadaDao organizacionVinculada = new OrganizacionVinculadaDao();

        assertDoesNotThrow(() -> organizacionVinculada.insertarOrganizacionVinculada(nuevaOrganizacion));
    }

    @Test
    public void modificarOrgnaizacionVinculada(){
        OrganizacionVinculada organizacionVinculadaModificada = new OrganizacionVinculada("Oracle", "Monterrey");
        OrganizacionVinculadaDao organizacionVinculada = new OrganizacionVinculadaDao();

        assertDoesNotThrow(() -> organizacionVinculada.modificarOrganizacionVinculada(1, organizacionVinculadaModificada));
    }
}
