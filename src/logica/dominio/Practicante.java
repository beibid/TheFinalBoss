package logica.dominio;


import logica.dominio.enums.Estado;
import logica.dominio.enums.Genero;


public class Practicante extends Usuario {
    private String matricula;
    private String lenguaIndigena;
    private Genero genero;
    private int idUsuarioPracticante;

    public Practicante(String matricula, String lenguaIndigena, Genero genero, int idUsuarioPracticante,String nombre, String apellidoPaterno, String apellidoMaterno, String contrasena, Estado estado){
        super(nombre, apellidoPaterno, apellidoMaterno, contrasena, estado);
        this.matricula = matricula;
        this.lenguaIndigena = lenguaIndigena;
        this.genero = genero;
        this.idUsuarioPracticante = idUsuarioPracticante;
    }

    public String getMatricula() {

        return matricula;
    }

    public String getLenguaIndigena() {

        return lenguaIndigena;
    }

    public Genero getGenero() {

        return genero;
    }

    public int getIdUsuarioPracticante() {

        return idUsuarioPracticante;
    }

    public void setMatricula(String matricula) {

        this.matricula = matricula;
    }

    public void setLenguaIndigena(String lenguaIndigena) {

        this.lenguaIndigena = lenguaIndigena;
    }

    public void setGenero(Genero genero) {

        this.genero = genero;
    }

    public void setIdUsuarioPracticante(int idUsuarioPracticante) {

        this.idUsuarioPracticante = idUsuarioPracticante;
    }
}
