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

public class GeneradorPdfReporteFinal {

    private static final Logger LOGGER = Logger.getLogger(GeneradorPdfReporteFinal.class.getName());
    private static final String CARPETA_REPORTES = "reportes/";

    public String generarPdf(Reporte reporte, String nombrePracticante, String nombreProyecto,
                             String nombreOrganizacion, String horasCubiertas) {
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
                    nombreOrganizacion, horasCubiertas, fontNormal, fontBold);
            agregarTablaActividades(documento, reporte, fontNormal, fontBold);
            agregarObservaciones(documento, reporte, fontNormal, fontBold);
            agregarPie(documento, fontNormal);
            LOGGER.info("PDF reporte final generado: " + rutaCompleta);
        } catch (IOException excepcion) {
            LOGGER.log(Level.SEVERE, "Error al generar el PDF final", excepcion);
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
        documento.add(new Paragraph("REPORTE FINAL")
                .setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        documento.add(new Paragraph(" "));
    }

    private void agregarDatosPracticante(Document documento, Reporte reporte,
                                         String nombrePracticante, String nombreProyecto,
                                         String nombreOrganizacion, String horasCubiertas,
                                         PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("INFORMACIÓN DEL REPORTE")
                .setFont(fontBold).setFontSize(12));
        documento.add(new Paragraph(" "));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{40, 60}));
        tabla.setWidth(UnitValue.createPercentValue(100));

        agregarFila(tabla, "Matrícula:", reporte.getMatriculaPracticante(), fontNormal, fontBold);
        agregarFila(tabla, "Nombre del practicante:", nombrePracticante, fontNormal, fontBold);
        agregarFila(tabla, "Organización vinculada:", nombreOrganizacion, fontNormal, fontBold);
        agregarFila(tabla, "Nombre del proyecto:", nombreProyecto, fontNormal, fontBold);
        agregarFila(tabla, "Horas totales cubiertas:", horasCubiertas + " horas", fontNormal, fontBold);
        agregarFila(tabla, "Fecha de generación:",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), fontNormal, fontBold);

        documento.add(tabla);
        documento.add(new Paragraph(" "));
    }

    private void agregarFila(Table tabla, String etiqueta, String valor,
                             PdfFont fontNormal, PdfFont fontBold) {
        tabla.addCell(new Cell()
                .add(new Paragraph(etiqueta).setFont(fontBold))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addCell(new Cell()
                .add(new Paragraph(valor != null ? valor : "").setFont(fontNormal))
                .setBorder(Border.NO_BORDER));
    }

    private void agregarTablaActividades(Document documento, Reporte reporte,
                                         PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("ACTIVIDADES Y ENTREGABLES")
                .setFont(fontBold).setFontSize(12));
        documento.add(new Paragraph(" "));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{35, 20, 45}));
        tabla.setWidth(UnitValue.createPercentValue(100));

        agregarEncabezadoTabla(tabla, "Actividad / Entregable", fontBold);
        agregarEncabezadoTabla(tabla, "% Avance", fontBold);
        agregarEncabezadoTabla(tabla, "Observaciones", fontBold);

        String[] lineas = reporte.getActividades().split("\n");
        for (String linea : lineas) {
            String[] partes = linea.split("\\|", -1);
            String nombre = partes.length > 0 ? partes[0].replace("ENTREGABLE:", "Entregable: ").trim() : "";
            String avance = partes.length > 1 ? partes[1].trim() + "%" : "";
            String observacion = partes.length > 2 ? partes[2].trim() : "";
            agregarCelda(tabla, nombre, fontNormal);
            agregarCelda(tabla, avance, fontNormal);
            agregarCelda(tabla, observacion, fontNormal);
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

    private void agregarCelda(Table tabla, String texto, PdfFont fontNormal) {
        tabla.addCell(new Cell()
                .add(new Paragraph(texto != null ? texto : "").setFont(fontNormal).setFontSize(10))
                .setTextAlignment(TextAlignment.LEFT));
    }

    private void agregarObservaciones(Document documento, Reporte reporte,
                                      PdfFont fontNormal, PdfFont fontBold) {
        documento.add(new Paragraph("OBSERVACIONES GENERALES")
                .setFont(fontBold).setFontSize(12));
        documento.add(new Paragraph(reporte.getDescripcion() != null ? reporte.getDescripcion() : "")
                .setFont(fontNormal).setFontSize(11).setTextAlignment(TextAlignment.JUSTIFIED));
        documento.add(new Paragraph(" "));
    }

    private void agregarPie(Document documento, PdfFont fontNormal) {
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
        return "ReporteFinal_" + reporte.getMatriculaPracticante() + "_" + fecha + ".pdf";
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