//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import acceso.bd.ConexionBaseDeDatos;

public class Main {
    public static void main(String[] args) {
        ConexionBaseDeDatos conexion = new ConexionBaseDeDatos();
        conexion.conectar();
      /**

/**
        try {
            Coordinador coordinador = new Coordinador("C001", "María", "López", "Sánchez", "abcd", Estado.Activo);

            CoordinadorDao coordinadorDao = new CoordinadorDao();
            coordinadorDao.insertarCoordinador(coordinador);
            System.out.println("CoordinadorDao se dio de alta");
        } catch (DaoExcepcion e) {
            System.out.println("CoordinadorDao no logro darse de alta por: " + e.getMessage());
        }*/
/**
        try {
            Profesor profesor = new Profesor("P001", Turno.Matutino, "Carlos", "Ramírez", "Torres", "5678", Estado.Activo);

            ProfesorDao profesorDao = new ProfesorDao();
            profesorDao.insertarProfesor(profesor);
            System.out.println("ProfesorDao se dio de alta");
        } catch (DaoExcepcion e) {
            System.out.println("ProfesorDao no logro darse de alta: " + e.getMessage());
        }*/
       /**try {
            Practicante practicante = new Practicante("S20013456", "Ninguna", Genero.Femenino, 2,"Luis", "Hernández", "Flores", "9012", Estado.Activo);

            PracticanteDao practicanteDao = new PracticanteDao();
            practicanteDao.insertarPracticante(practicante);
            System.out.println("PracticanteDao se dio de alta");
        } catch (InserccionUsuarioExcepcion e) {
            System.out.println("PracticanteDao no logro darse de alta: " + e.getMessage());
        }*/


    }
}