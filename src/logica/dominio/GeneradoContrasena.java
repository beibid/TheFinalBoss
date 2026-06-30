package logica.dominio;

import java.security.SecureRandom;

public class GeneradoContrasena {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LONGITUD_CONTRASENA = 10;

    public static String generarContrasenaTemportal() {
        StringBuilder contrasena = new StringBuilder();
        SecureRandom aleatorio = new SecureRandom();
        for (int i = 0; i < LONGITUD_CONTRASENA; i++) {
            contrasena.append(CARACTERES.charAt(aleatorio.nextInt(CARACTERES.length())));
        }
        return contrasena.toString();
    }
}
