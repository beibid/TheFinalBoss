package acceso_bd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarBaseDeDatos {
    static String enlace = "jdbc:mysql://localhost:3306/practicas_profesionales";
    static String usuarioIngresado = "usuario_practicas";
    static String contraseña = "FEI0123";

    public static Connection conectar(){

        Connection conexion = null;
        try{
            conexion = DriverManager.getConnection(enlace, usuarioIngresado, contraseña);
            System.out.println("Conexion a base de datos realizada");
        }
        catch(SQLException e){
            System.out.println("Conexion fallida");
            e.printStackTrace();
        }
        return conexion;
    }

}
