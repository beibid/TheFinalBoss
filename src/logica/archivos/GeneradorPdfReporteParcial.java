package logica.archivos;


import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.io.font.constants.StandardFonts;
import logica.dominio.Reporte;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GeneradorPdfReporteParcial {


    private static final Logger LOGGER = Logger.getLogger(GeneradorPdfReporteParcial.class.getName());
    private static final String CARPETA_REPORTES = "reportes/";

    public String generarPdf(Reporte reporte, String nombrePracticante, String nombreProyecto,
                             String nombreOrganizacion, String horasCubiertas) {
        crearCarpetaSiNoExiste();
        String nombreArchivo = generarNombreArchivo(reporte);
        String rutaCompleta = CARPETA_REPORTES + nombreArchivo;
        try {
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfWriter writer = new PdfWriter(rutaCompleta);
            PdfDocument documentoPdf = new PdfDocument(writer);
            Document documento = new Document(documentoPdf);

            agregarEncabezado(documento, fontNormal, fontBold, reporte);
            agregarDatosPracticante(documento, reporte, nombrePracticante, nombreProyecto, nombreOrganizacion, horasCubiertas, fontNormal, fontBold);
            agregarDescripcion(documento, reporte, fontNormal, fontBold);
            agregarTablaActividades(documento, reporte, fontNormal, fontBold, horasCubiertas);
            agregarPie(documento, fontNormal);
            documento.close();

            LOGGER.info("PDF generado correctamente: " + rutaCompleta);
        } catch (Exception excepcion) {
            LOGGER.log(Level.SEVERE, "Error al generar el PDF", excepcion);
            return null;
        }
        return rutaCompleta;
    }

    private void agregarEncabezado(Document documento, PdfFont fontNormal, PdfFont fontBold,
                                   Reporte reporte) throws IOException {
        documento.add(new Paragraph("FACULTAD DE ESTADÍSTICA E INFORMÁTICA")
                .setFont(fontBold)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("Licenciatura en Ingeniería de Software")
                .setFont(fontNormal)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("Prácticas de Ingeniería de Software")
                .setFont(fontNormal)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph("REPORTE PARCIAL — " + reporte.getTipoReporte().name().toUpperCase())
                .setFont(fontBold)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph(" "));
    }

    private void agregarDatosPracticante(Document documento, Reporte reporte,
                                         String nombrePracticante, String nombreProyecto,
                                         String nombreOrganizacion, String horasCubiertas,
                                         PdfFont fontNormal, PdfFont fontBold) throws IOException {
        documento.add(new Paragraph("INFORMACIÓN DEL REPORTE")
                .setFont(fontBold)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT));
        documento.add(new Paragraph(" "));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{40, 60}));
        tabla.setWidth(UnitValue.createPercentValue(100));

        agregarFilaTabla(tabla, "Matrícula:", reporte.getMatriculaPracticante(), fontNormal, fontBold);
        agregarFilaTabla(tabla, "Nombre del practicante:", nombrePracticante, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Organización vinculada:", nombreOrganizacion, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Nombre del proyecto:", nombreProyecto, fontNormal, fontBold);
        agregarFilaTabla(tabla, "Horas cubiertas:", horasCubiertas + " horas", fontNormal, fontBold);
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
                                    PdfFont fontNormal, PdfFont fontBold) throws IOException {
        documento.add(new Paragraph("DESCRIPCIÓN")
                .setFont(fontBold)
                .setFontSize(12));
        documento.add(new Paragraph(reporte.getDescripcion() != null ? reporte.getDescripcion() : "")
                .setFont(fontNormal)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.JUSTIFIED));
        documento.add(new Paragraph(" "));
    }

    private void agregarTablaActividades(Document documento, Reporte reporte,
                                         PdfFont fontNormal, PdfFont fontBold,
                                         String horasCubiertas) throws IOException {
        documento.add(new Paragraph("ACTIVIDADES REALIZADAS DURANTE LAS PRIMERAS " + horasCubiertas + " HORAS")
                .setFont(fontBold)
                .setFontSize(12));
        documento.add(new Paragraph(" "));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{25, 25, 15, 15, 10, 10}));
        tabla.setWidth(UnitValue.createPercentValue(100));

        agregarEncabezadoTabla(tabla, "Actividad", fontBold);
        agregarEncabezadoTabla(tabla, "Descripción", fontBold);
        agregarEncabezadoTabla(tabla, "Tiempo planeado", fontBold);
        agregarEncabezadoTabla(tabla, "Tiempo real", fontBold);
        agregarEncabezadoTabla(tabla, "Mes", fontBold);
        agregarEncabezadoTabla(tabla, "Semana", fontBold);

        String[] lineas = reporte.getActividades().split("\n");
        for (String linea : lineas) {
            String[] partes = linea.split("\\|", -1);
            String nombre = partes.length > 0 ? partes[0].trim() : "";
            String descripcion = partes.length > 1 ? partes[1].trim() : "";
            String tiempoPlaneado = partes.length > 2 ? partes[2].trim() : "";
            String tiempoReal = partes.length > 3 ? partes[3].trim() : "";
            String mes = partes.length > 4 ? partes[4].trim() : "";
            String semana = partes.length > 5 ? partes[5].trim() : "";

            agregarCeldaTabla(tabla, nombre, fontNormal);
            agregarCeldaTabla(tabla, descripcion, fontNormal);
            agregarCeldaTabla(tabla, tiempoPlaneado, fontNormal);
            agregarCeldaTabla(tabla, tiempoReal, fontNormal);
            agregarCeldaTabla(tabla, mes, fontNormal);
            agregarCeldaTabla(tabla, semana, fontNormal);
        }

        documento.add(tabla);
        documento.add(new Paragraph(" "));
    }

    private void agregarEncabezadoTabla(Table tabla, String texto, PdfFont fontBold) {
        tabla.addCell(new Cell()
                .add(new Paragraph(texto).setFont(fontBold).setFontSize(10))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER));
    }

    private void agregarCeldaTabla(Table tabla, String texto, PdfFont fontNormal) {
        tabla.addCell(new Cell()
                .add(new Paragraph(texto != null ? texto : "").setFont(fontNormal).setFontSize(10))
                .setTextAlignment(TextAlignment.CENTER));
    }

    private void agregarPie(Document documento, PdfFont fontNormal) throws IOException {
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph(" "));

        Table tablaFirmas = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        tablaFirmas.setWidth(UnitValue.createPercentValue(100));

        tablaFirmas.addCell(new Cell()
                .add(new Paragraph("_______________________________\nFirma del Practicante")
                        .setFont(fontNormal).setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));
        tablaFirmas.addCell(new Cell()
                .add(new Paragraph("_______________________________\nSello de la Organización Vinculada")
                        .setFont(fontNormal).setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));

        documento.add(tablaFirmas);
    }

    private String generarNombreArchivo(Reporte reporte) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "ReporteParcial_" + reporte.getMatriculaPracticante() + "_" + fecha + ".pdf";
    }

    private void crearCarpetaSiNoExiste() {
        File carpeta = new File(CARPETA_REPORTES);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }
}
