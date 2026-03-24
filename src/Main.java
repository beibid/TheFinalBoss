//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import acceso.bd.ConectarBaseDeDatos;
public class Main {
    public static void main(String[] args) {
        ConectarBaseDeDatos conexion = new ConectarBaseDeDatos();

        conexion.conectar();


    }
}