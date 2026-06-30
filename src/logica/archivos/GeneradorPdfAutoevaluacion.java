package logica.archivos;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import logica.dominio.AutoevaluacionPracticante;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneradorPdfAutoevaluacion {

    private static final Logger LOGGER = Logger.getLogger(GeneradorPdfAutoevaluacion.class.getName());
    private static final String CARPETA_AUTOEVALUACIONES = "autoevaluaciones/";

    private static final String[] AFIRMACIONES = {
            "Mi participación en la Organización Vinculada fue productiva.",
            "Logré la aplicación de los conocimientos teórico-prácticos adquiridos en la Licenciatura en Ingeniería de Software.",
            "Me sentí seguro al realizar las actividades encomendadas.",
            "Las actividades encomendadas despertaron mi interés.",
            "La Organización Vinculada me proporcionó la información y facilidades adecuados durante el desarrollo de las prácticas.",
            "La Organización Vinculada me dio a conocer las reglas internas que debía seguir al conducirme durante el desarrollo de las prácticas.",
            "El Responsable del Proyecto me orientó correctamente para el desarrollo de mis actividades.",
            "El Responsable del Proyecto realizó un seguimiento efectivo de mis actividades.",
            "El proyecto es congruente con la formación de mi carrera.",
            "Considero que las prácticas son importantes para mi formación profesional."
    };

    public String generarPdf(AutoevaluacionPracticante autoevaluacion, String nombrePracticante,
                             String nombreProyecto, String nombreOrganizacion,
                             String responsable) {
        crearCarpetaSiNoExiste();
        String nombreArchivo = generarNombreArchivo(autoevaluacion);
        String rutaCompleta = CARPETA_AUTOEVALUACIONES + nombreArchivo;
        Document documento = null;
        try {
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            documento = new Document(new PdfDocument(new PdfWriter(rutaCompleta)));
            agregarEncabezado(documento, fontNormal, fontBold);
            agregarDatosPracticante(documento, autoevaluacion, nombrePracticante,
                    nombreProyecto, nombreOrganizacion, responsable, fontNormal, fontBold);
            agregarTablaAfirmaciones(documento, autoevaluacion, fontNormal, fontBold);
            agregarPie(documento, fontNormal);
            LOGGER.info("PDF de autoevaluación generado: " + rutaCompleta);
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al generar PDF de autoevaluación", excepcion);
            return null;
        } finally {
            if (documento != null) {
                documento.close();
            }
        }
        return rutaCompleta;
    }

    private void agregarEncabezado(Document documento, PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("FACULTAD DE ESTADÍSTICA E INFORMÁTICA")
                .setFont(fontBold).setFontSize(13).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("Licenciatura en Ingeniería de Software")
                .setFont(fontBold).setFontSize(11).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("Formato: EVALUACIÓN DEL ALUMNO. EE Prácticas de Ingeniería Software")
                .setFont(fontBold).setFontSize(11).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph(" "));
    }

    private void agregarDatosPracticante(Document documento, AutoevaluacionPracticante autoevaluacion,
                                         String nombrePracticante, String nombreProyecto,
                                         String nombreOrganizacion, String responsable,
                                         PdfFont fontNormal, PdfFont fontBold) {
        Table tabla = new Table(UnitValue.createPercentArray(new float[]{40, 60}));
        tabla.setWidth(UnitValue.createPercentValue(100));
        agregarFilaTabla(tabla, "Nombre del alumno", nombrePracticante, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Matrícula", autoevaluacion.getMatriculaPracticante(), fontNormal, fontBold);
        agregarFilaTabla(tabla, "Organización vinculada", nombreOrganizacion, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Responsable del proyecto", responsable, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Nombre del proyecto", nombreProyecto, fontNormal, fontBold);
        documento.add(tabla);
        documento.add(new Paragraph(" "));
    }

    private void agregarFilaTabla(Table tabla, String etiqueta, String valor,
                                  PdfFont fontNormal, PdfFont fontBold) {
        tabla.addCell(new Cell()
                .add(new Paragraph(etiqueta).setFont(fontBold))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addCell(new Cell()
                .add(new Paragraph(valor != null ? valor : "").setFont(fontNormal))
                .setBorder(Border.NO_BORDER));
    }

    private void agregarTablaAfirmaciones(Document documento, AutoevaluacionPracticante autoevaluacion,
                                          PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("CRITERIOS")
                .setFont(fontBold).setFontSize(11).setTextAlignment(TextAlignment.CENTER));

        Table tablaCriterios = new Table(UnitValue.createPercentArray(new float[]{70, 10, 10, 10, 10, 10}));
        tablaCriterios.setWidth(UnitValue.createPercentValue(100));

        tablaCriterios.addCell(new Cell()
                .add(new Paragraph("AFIRMACIONES").setFont(fontBold))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        for (int i = 1; i <= 5; i++) {
            tablaCriterios.addCell(new Cell()
                    .add(new Paragraph(String.valueOf(i)).setFont(fontBold).setTextAlignment(TextAlignment.CENTER))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        }

        int[] respuestas = {
                autoevaluacion.getRespuesta1(), autoevaluacion.getRespuesta2(),
                autoevaluacion.getRespuesta3(), autoevaluacion.getRespuesta4(),
                autoevaluacion.getRespuesta5(), autoevaluacion.getRespuesta6(),
                autoevaluacion.getRespuesta7(), autoevaluacion.getRespuesta8(),
                autoevaluacion.getRespuesta9(), autoevaluacion.getRespuesta10()
        };

        for (int i = 0; i < AFIRMACIONES.length; i++) {
            tablaCriterios.addCell(new Cell()
                    .add(new Paragraph((i + 1) + ". " + AFIRMACIONES[i]).setFont(fontNormal).setFontSize(10)));
            for (int j = 1; j <= 5; j++) {
                String marca = respuestas[i] == j ? "X" : "";
                tablaCriterios.addCell(new Cell()
                        .add(new Paragraph(marca).setFont(fontNormal).setTextAlignment(TextAlignment.CENTER)));
            }
        }

        int puntuacionFinal = 0;
        for (int respuesta : respuestas) {
            puntuacionFinal += respuesta;
        }
        tablaCriterios.addCell(new Cell(1, 5)
                .add(new Paragraph("PUNTUACIÓN FINAL").setFont(fontBold).setTextAlignment(TextAlignment.RIGHT)));
        tablaCriterios.addCell(new Cell()
                .add(new Paragraph(String.valueOf(puntuacionFinal)).setFont(fontBold).setTextAlignment(TextAlignment.CENTER)));

        documento.add(tablaCriterios);
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("LUGAR Y FECHA: _______________________________________________")
                .setFont(fontNormal).setFontSize(10));
    }

    private void agregarPie(Document documento, PdfFont fontNormal) {
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("___________________________________")
                .setFont(fontNormal).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("NOMBRE Y FIRMA DEL ALUMNO")
                .setFont(fontNormal).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
    }

    private String generarNombreArchivo(AutoevaluacionPracticante autoevaluacion) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "Autoevaluacion_" + autoevaluacion.getMatriculaPracticante() + "_" + fecha + ".pdf";
    }

    private void crearCarpetaSiNoExiste() {
        File carpeta = new File(CARPETA_AUTOEVALUACIONES);
        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();
            if (!creada) {
                LOGGER.warning("No se pudo crear la carpeta de autoevaluaciones: " + CARPETA_AUTOEVALUACIONES);
            }
        }
    }
}