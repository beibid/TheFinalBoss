package logica;

public class Coordinador extends Usuario{
    private String numeroDePersonalCoordinador;
    private int idUsuarioCoordinador;

    public Coordinador(String numeroDePersonalCoordinador, int idUsuarioCoordinador, String nombre, String apellidoPaterno, String apellidoMaterno, String contrasena, Estado estado){
        super(nombre, apellidoPaterno, apellidoMaterno, contrasena, estado);
        this.numeroDePersonalCoordinador = numeroDePersonalCoordinador;
        this.idUsuarioCoordinador = idUsuarioCoordinador;
    }

    public String getNumeroDePersonalCoordinador() {
        return numeroDePersonalCoordinador;
    }

    public int getIdUsuarioCoordinador() {
        return idUsuarioCoordinador;
    }

    public void setNumeroDePersonalCoordinador(String numeroDePersonalCoordinador) {
        this.numeroDePersonalCoordinador = numeroDePersonalCoordinador;
    }

    public void setIdUsuarioCoordinador(int idUsuarioCoordinador) {
        this.idUsuarioCoordinador = idUsuarioCoordinador;
    }
}
