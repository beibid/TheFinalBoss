package acceso_bd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarBaseDeDatos {
    static String url = "jdbc:mysql://localhost:3306/practicas_profesionales";
    static String user = "usuario_practicas";
    static String pass = "FEI0123";

    public static Connection conectar(){

        Connection con = null;
        try{
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexion a base de datos realizada");
        }
        catch(SQLException e){
            System.out.println("Conexion fallida");
            e.printStackTrace();
        }
        return con;
    }

}
