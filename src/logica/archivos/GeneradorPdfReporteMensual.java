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
import logica.dominio.Reporte;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneradorPdfReporteMensual {

    private static final Logger LOGGER = Logger.getLogger(GeneradorPdfReporteMensual.class.getName());
    private static final String CARPETA_REPORTES = "reportes/";

    public String generarPdf(Reporte reporte, String nombrePracticante, String nombreProyecto,
                             String nombreOrganizacion) {
        crearCarpetaSiNoExiste();
        String nombreArchivo = generarNombreArchivo(reporte);
        String rutaCompleta = CARPETA_REPORTES + nombreArchivo;
        Document documento = null;
        try {
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            documento = new Document(new PdfDocument(new PdfWriter(rutaCompleta)));
            agregarEncabezado(documento, fontNormal, fontBold);
            agregarDatosPracticante(documento, reporte, nombrePracticante, nombreProyecto,
                    nombreOrganizacion, fontNormal, fontBold);
            agregarDescripcion(documento, reporte, fontNormal, fontBold);
            agregarActividades(documento, reporte, fontNormal, fontBold);
            agregarPie(documento, fontNormal);
            LOGGER.info("PDF generado correctamente: " + rutaCompleta);
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al generar el PDF", excepcion);
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
                .setFont(fontBold).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("Licenciatura en Ingeniería de Software")
                .setFont(fontNormal).setFontSize(11).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("Prácticas de Ingeniería de Software")
                .setFont(fontNormal).setFontSize(11).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph(" "));
    }

    private void agregarDatosPracticante(Document documento, Reporte reporte,
                                         String nombrePracticante, String nombreProyecto,
                                         String nombreOrganizacion, PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("INFORMACIÓN DEL REPORTE")
                .setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        documento.add(new Paragraph(" "));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{40, 60}));
        tabla.setWidth(UnitValue.createPercentValue(100));

        agregarFilaTabla(tabla, "Matrícula:", reporte.getMatriculaPracticante(), fontNormal, fontBold);
        agregarFilaTabla(tabla, "Nombre del practicante:", nombrePracticante, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Organización vinculada:", nombreOrganizacion, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Nombre del proyecto:", nombreProyecto, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Tipo de reporte:", reporte.getTipoReporte().name(), fontNormal, fontBold);
        agregarFilaTabla(tabla, "Fecha de generación:",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), fontNormal, fontBold);

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

    private void agregarDescripcion(Document documento, Reporte reporte,
                                    PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("DESCRIPCIÓN").setFont(fontBold).setFontSize(12));
        documento.add(new Paragraph(reporte.getDescripcion() != null ? reporte.getDescripcion() : "")
                .setFont(fontNormal).setFontSize(11).setTextAlignment(TextAlignment.JUSTIFIED));
        documento.add(new Paragraph(" "));
    }

    private void agregarActividades(Document documento, Reporte reporte,
                                    PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("ACTIVIDADES REALIZADAS")
                .setFont(fontBold).setFontSize(12));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{20, 80}));
        tabla.setWidth(UnitValue.createPercentValue(100));

        String[] actividades = reporte.getActividades().split("\n");
        for (String actividad : actividades) {
            String[] partes = actividad.split(": ", 2);
            String semana = partes.length > 0 ? partes[0] : "";
            String descripcion = partes.length > 1 ? partes[1] : "";
            tabla.addCell(new Cell()
                    .add(new Paragraph(semana).setFont(fontBold))
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            tabla.addCell(new Cell()
                    .add(new Paragraph(descripcion).setFont(fontNormal))
                    .setBorder(Border.NO_BORDER));
        }
        documento.add(tabla);
        documento.add(new Paragraph(" "));
    }

    private void agregarPie(Document documento, PdfFont fontNormal) {
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("_______________________________")
                .setFont(fontNormal).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("Firma del Practicante")
                .setFont(fontNormal).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
    }

    private String generarNombreArchivo(Reporte reporte) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "Reporte_" + reporte.getTipoReporte().name() + "_" +
                reporte.getMatriculaPracticante() + "_" + fecha + ".pdf";
    }

    private void crearCarpetaSiNoExiste() {
        File carpeta = new File(CARPETA_REPORTES);
        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();
            if (!creada) {
                LOGGER.warning("No se pudo crear la carpeta de reportes: " + CARPETA_REPORTES);
            }
        }
    }
}