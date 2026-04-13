import logica.dao.excepciones.MensajeriaExcepcion;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.objetos.*;
import logica.dominio.*;
import logica.dominio.enums.*;
import java.sql.Date;

public class Main {
    public static void main(String[] args) {

        try {
            OrganizacionVinculada org = new OrganizacionVinculada("Innovacion Digital", "Av. Veracruz 321");
            OrganizacionVinculadaDao orgDao = new OrganizacionVinculadaDao();
            orgDao.insertarOrganizacionVinculada(org);
            System.out.println("OrganizacionVinculada insertada correctamente");
        } catch (UsuariosExcepcion e) {
            System.out.println("Error OrganizacionVinculada: " + e.getMessage());
        }

        try {
            Coordinador coordinador = new Coordinador("C005", "Ricardo", "Fuentes", "Castillo", "pass7", Estado.Activo);
            CoordinadorDao coordinadorDao = new CoordinadorDao();
            coordinadorDao.insertarCoordinador(coordinador);
            System.out.println("Coordinador insertado correctamente");
        } catch (UsuariosExcepcion e) {
            System.out.println("Error Coordinador: " + e.getMessage());
        }

        try {
            Profesor profesor = new Profesor("P005", Turno.Matutino, "Elena", "Vargas", "Rios", "pass8", Estado.Activo);
            ProfesorDao profesorDao = new ProfesorDao();
            profesorDao.insertarProfesor(profesor);
            System.out.println("Profesor insertado correctamente");
        } catch (UsuariosExcepcion e) {
            System.out.println("Error Profesor: " + e.getMessage());
        }

        try {
            Practicante practicante = new Practicante("S20013461", "Ninguna", Genero.Masculino, 8, "Miguel", "Castro", "Lara", "pass9", Estado.Activo);
            PracticanteDao practicanteDao = new PracticanteDao();
            practicanteDao.insertarPracticante(practicante);
            System.out.println("Practicante insertado correctamente");
        } catch (UsuariosExcepcion e) {
            System.out.println("Error Practicante: " + e.getMessage());
        }

        try {
            Seccion seccion = new Seccion("S05", "2026-2");
            SeccionDao seccionDao = new SeccionDao();
            seccionDao.agregarSeccion(seccion);
            System.out.println("Seccion insertada correctamente");
        } catch (UsuariosExcepcion e) {
            System.out.println("Error Seccion: " + e.getMessage());
        }

        try {
            Proyecto proyecto = new Proyecto(4, "Sistema de Nomina", "Automatizacion de nomina empresarial",
                    "Elena Vargas", EstadoProyecto.Disponible, "Innovacion Digital", "Finanzas",
                    "Av. Veracruz 321", 4, "S20013461", "C005", Date.valueOf("2026-04-10"));
            ProyectoDao proyectoDao = new ProyectoDao();
            proyectoDao.agregarProyecto(proyecto);
            System.out.println("Proyecto insertado correctamente");
        } catch (MensajeriaExcepcion e) {
            System.out.println("Error Proyecto: " + e.getMessage());
        }

        try {
            Reporte reporte = new Reporte(TipoReporte.Mensual, "Reporte de avance de nomina",
                    Date.valueOf("2026-04-10"), 8.0, "Progreso adecuado",
                    EstadoDeCalificacion.Evaluado, "S20013461", "P005");
            ReporteDao reporteDao = new ReporteDao();
            reporteDao.agregarReporte(reporte);
            System.out.println("Reporte insertado correctamente");
        } catch (MensajeriaExcepcion e) {
            System.out.println("Error Reporte: " + e.getMessage());
        }

        try {
            PracticanteSeccion ps = new PracticanteSeccion("S20013461", "S05");
            PracticanteSeccionDao psDao = new PracticanteSeccionDao();
            psDao.agregarPracticanteSeccion(ps);
            System.out.println("PracticanteSeccion insertada correctamente");
        } catch (UsuariosExcepcion e) {
            System.out.println("Error PracticanteSeccion: " + e.getMessage());
        }

        try {
            Mensaje mensaje = new Mensaje("Recordatorio de entrega de reporte mensual");
            MensajeDao mensajeDao = new MensajeDao();
            mensajeDao.agregarMensaje(mensaje);
            System.out.println("Mensaje insertado correctamente");
        } catch (MensajeriaExcepcion e) {
            System.out.println("Error Mensaje: " + e.getMessage());
        }

        try {
            Buzon buzon = new Buzon(RolMensaje.destinatario, 3, 3);
            BuzonDao buzonDao = new BuzonDao();
            buzonDao.agregarBuzon(buzon);
            System.out.println("Buzon insertado correctamente");
        } catch (MensajeriaExcepcion e) {
            System.out.println("Error Buzon: " + e.getMessage());
        }
    }
}