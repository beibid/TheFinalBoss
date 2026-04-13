package logica.dominio;


import logica.dominio.enums.RolMensaje;


public class Buzon {
    private int idBuzon;
    private RolMensaje rolMensaje;
    private int idMensaje;
    private int idUsuario;

    public Buzon(RolMensaje rolMensaje, int idMensaje, int idUsuario) {
        this.rolMensaje = rolMensaje;
        this.idMensaje = idMensaje;
        this.idUsuario = idUsuario;
    }

    public int getIdBuzon() {
        return idBuzon;
    }
    public RolMensaje getRolMensaje() {
        return rolMensaje;
    }
    public int getIdMensaje() {
        return idMensaje;
    }
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdBuzon(int idBuzon) {
        this.idBuzon = idBuzon;
    }
    public void setRolMensaje(RolMensaje rolMensaje) {
        this.rolMensaje = rolMensaje;
    }
    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}