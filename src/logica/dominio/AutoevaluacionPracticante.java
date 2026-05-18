package logica.dominio;


public class AutoevaluacionPracticante {

    private int idAutoevaluacion;
    private String matriculaPracticante;
    private int idProyecto;
    private int respuesta1;
    private int respuesta2;
    private int respuesta3;
    private int respuesta4;
    private int respuesta5;
    private int respuesta6;
    private int respuesta7;
    private int respuesta8;
    private int respuesta9;
    private int respuesta10;
    private String nombreProyecto;
    private String responsableDelProyecto;
    private String nombreOrganizacion;

    public AutoevaluacionPracticante(String matriculaPracticante, int idProyecto,
                                     int respuesta1, int respuesta2, int respuesta3,
                                     int respuesta4, int respuesta5, int respuesta6,
                                     int respuesta7, int respuesta8, int respuesta9,
                                     int respuesta10) {
        this.matriculaPracticante = matriculaPracticante;
        this.idProyecto = idProyecto;
        this.respuesta1 = respuesta1;
        this.respuesta2 = respuesta2;
        this.respuesta3 = respuesta3;
        this.respuesta4 = respuesta4;
        this.respuesta5 = respuesta5;
        this.respuesta6 = respuesta6;
        this.respuesta7 = respuesta7;
        this.respuesta8 = respuesta8;
        this.respuesta9 = respuesta9;
        this.respuesta10 = respuesta10;
    }

    public int getIdAutoevaluacion() {
        return idAutoevaluacion;
    }

    public String getMatriculaPracticante() {
        return matriculaPracticante;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public int getRespuesta1() {
        return respuesta1;
    }

    public int getRespuesta2() {
        return respuesta2;
    }

    public int getRespuesta3() {
        return respuesta3;
    }

    public int getRespuesta4() {
        return respuesta4;
    }

    public int getRespuesta5() {
        return respuesta5;
    }

    public int getRespuesta6() {
        return respuesta6;
    }

    public int getRespuesta7() {
        return respuesta7;
    }

    public int getRespuesta8() {
        return respuesta8;
    }

    public int getRespuesta9() {
        return respuesta9;
    }

    public int getRespuesta10() {
        return respuesta10;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public String getResponsableDelProyecto() {
        return responsableDelProyecto;
    }

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public void setIdAutoevaluacion(int idAutoevaluacion) {
        this.idAutoevaluacion = idAutoevaluacion;
    }

    public void setMatriculaPracticante(String matriculaPracticante) {
        this.matriculaPracticante = matriculaPracticante;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public void setRespuesta1(int respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public void setRespuesta2(int respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public void setRespuesta3(int respuesta3) {
        this.respuesta3 = respuesta3;
    }

    public void setRespuesta4(int respuesta4) {
        this.respuesta4 = respuesta4;
    }

    public void setRespuesta5(int respuesta5) {
        this.respuesta5 = respuesta5;
    }

    public void setRespuesta6(int respuesta6) {
        this.respuesta6 = respuesta6;
    }

    public void setRespuesta7(int respuesta7) {
        this.respuesta7 = respuesta7;
    }

    public void setRespuesta8(int respuesta8) {
        this.respuesta8 = respuesta8;
    }

    public void setRespuesta9(int respuesta9) {
        this.respuesta9 = respuesta9;
    }

    public void setRespuesta10(int respuesta10) {
        this.respuesta10 = respuesta10;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public void setResponsableDelProyecto(String responsableDelProyecto) {
        this.responsableDelProyecto = responsableDelProyecto;
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }
}


