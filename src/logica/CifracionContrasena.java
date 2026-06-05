package logica;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CifracionContrasena {

    private static final Logger LOGGER = Logger.getLogger(CifracionContrasena.class.getName());

    public static String cifrarContrasena(String contrasena) {
        String hashResultante = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            StringBuilder constructorHex = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                constructorHex.append(String.format("%02x", b));
            }
            hashResultante = constructorHex.toString();
        } catch (NoSuchAlgorithmException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al cifrar la contrasena", excepcion);
        }
        return hashResultante;
    }

    public static boolean verificarContrasena(String contrasenaSinCifrar, String contrasenaCifrada) {
        boolean esValida = false;
        String contrasenaCifradaInput = cifrarContrasena(contrasenaSinCifrar);
        if (contrasenaCifradaInput.equals(contrasenaCifrada)) {
            esValida = true;
        }
        return esValida;
    }

}
