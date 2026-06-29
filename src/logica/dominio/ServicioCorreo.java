package logica.dominio;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ServicioCorreo {

    private static final String CORREO_REMITENTE = "bichitoespimo@gmail.com";
    private static final String CONTRASENA_APP = "msew hrnd pvqw nxsf";
    private static final String HOST_SMTP = "smtp.gmail.com";
    private static final String PUERTO_SMTP = "587";
    private static final Logger LOGGER = Logger.getLogger(ServicioCorreo.class.getName());

    public static void enviarContrasenaInicial(String correoDestino, String nombre, String contrasenaTemporal) {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", HOST_SMTP);
        propiedades.put("mail.smtp.port", PUERTO_SMTP);

        Session sesion = Session.getInstance(propiedades, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CORREO_REMITENTE, CONTRASENA_APP);
            }
        });

        Transport transporte = null;
        try {
            MimeMessage mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(CORREO_REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            mensaje.setSubject("Bienvenido al Sistema de Prácticas Profesionales");
            mensaje.setText("Hola " + nombre + ",\n\n" +
                    "Tu cuenta ha sido creada exitosamente.\n" +
                    "Tu contraseña temporal es: " + contrasenaTemporal + "\n\n" +
                    "Al iniciar sesión por primera vez deberás cambiarla.\n\n" +
                    "Saludos,\nSistema de Prácticas Profesionales");
            transporte = sesion.getTransport("smtp");
            transporte.connect(HOST_SMTP, CORREO_REMITENTE, CONTRASENA_APP);
            transporte.sendMessage(mensaje, mensaje.getAllRecipients());
            LOGGER.info("Correo enviado correctamente a: " + correoDestino);
        } catch (MessagingException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al enviar correo", excepcion);
            excepcion.printStackTrace();
        } finally {
            if (transporte != null) {
                try {
                    transporte.close();
                } catch (MessagingException excepcion) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar la conexion de correo", excepcion);
                }
            }
        }
    }
}
