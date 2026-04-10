import acceso.bd.ConexionBaseDeDatos;
import logica.dao.excepciones.InserccionBaseDeDatosExcepcion;
import logica.dao.objetos.*;
import logica.dominio.*;

public class Main {
    public static void main(String[] args) {
        ConexionBaseDeDatos conexion = new ConexionBaseDeDatos();
        conexion.conectar();
/**
        try {
            OrganizacionVinculada org = new OrganizacionVinculada("Consultora MX", "Calle Juarez 456");
            OrganizacionVinculadaDao orgDao = new OrganizacionVinculadaDao();
            orgDao.insertarOrganizacionVinculada(org);
            System.out.println("OrganizacionVinculada insertada correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error OrganizacionVinculada: " + e.getMessage());
        }

        try {
            Coordinador coordinador = new Coordinador("C003", "Ana", "Gutierrez", "Mora", "pass1", Estado.Activo);
            CoordinadorDao coordinadorDao = new CoordinadorDao();
            coordinadorDao.insertarCoordinador(coordinador);
            System.out.println("Coordinador insertado correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error Coordinador: " + e.getMessage());
        }

        try {
            Profesor profesor = new Profesor("P003", Turno.Vespertino, "Roberto", "Mendoza", "Cruz", "pass2", Estado.Activo);
            ProfesorDao profesorDao = new ProfesorDao();
            profesorDao.insertarProfesor(profesor);
            System.out.println("Profesor insertado correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error Profesor: " + e.getMessage());
        }

        try {
            Practicante practicante = new Practicante("S20013458", "Nahuatl", Genero.Masculino, 4, "Jorge", "Perez", "Diaz", "pass3", Estado.Activo);
            PracticanteDao practicanteDao = new PracticanteDao();
            practicanteDao.insertarPracticante(practicante);
            System.out.println("Practicante insertado correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error Practicante: " + e.getMessage());
        }

        try {
            Seccion seccion = new Seccion("S03", "2026-2");
            SeccionDao seccionDao = new SeccionDao();
            seccionDao.agregarSeccion(seccion);
            System.out.println("Seccion insertada correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error Seccion: " + e.getMessage());
        }

        try {
            Proyecto proyecto = new Proyecto(2, "App Escolar", "Sistema de control escolar",
                    "Roberto Mendoza", EstadoProyecto.Disponible, "Consultora MX", "Educacion",
                    "Calle Juarez 456", 2, "S20013458", "C003", Date.valueOf("2026-04-10"));
            ProyectoDao proyectoDao = new ProyectoDao();
            proyectoDao.agregarProyecto(proyecto);
            System.out.println("Proyecto insertado correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error Proyecto: " + e.getMessage());
        }

        try {
            Reporte reporte = new Reporte(TipoReporte.Mensual, "Avance mensual satisfactorio",
                    Date.valueOf("2026-04-10"), 8.5, "Cumple con los objetivos",
                    EstadoDeCalificacion.Evaluado, "S20013458", "P003");
            ReporteDao reporteDao = new ReporteDao();
            reporteDao.agregarReporte(reporte);
            System.out.println("Reporte insertado correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error Reporte: " + e.getMessage());
        }
        **/
        try {
            PracticanteSeccion ps = new PracticanteSeccion("S20013458", "S03");
            PracticanteSeccionDao psDao = new PracticanteSeccionDao();
            psDao.agregarPracticanteSeccion(ps);
            System.out.println("PracticanteSeccion insertada correctamente");
        } catch (InserccionBaseDeDatosExcepcion e) {
            System.out.println("Error PracticanteSeccion: " + e.getMessage());
        }
    }
}