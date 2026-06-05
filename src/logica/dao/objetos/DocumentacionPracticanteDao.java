package logica.dao.objetos;

import acceso.bd.ConexionBaseDeDatos;
import logica.dominio.DocumentacionPracticante;
import logica.dao.excepciones.UsuariosExcepcion;
import logica.dao.interfaces.DocumentacionPracticanteDaoInterfaz;
import logica.dominio.enums.EstadoRevision;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentacionPracticanteDao implements DocumentacionPracticanteDaoInterfaz {

    private static final Logger LOGGER = Logger.getLogger(DocumentacionPracticanteDao.class.getName());

    @Override
    public int agregarDocumentacion(DocumentacionPracticante documentacion) throws UsuariosExcepcion {
        if (documentacion.getEstadoRevision() == null) {
            throw new UsuariosExcepcion("El estado de revision no puede ser nulo");
        }
        if (documentacion.getRutaDeArchivo() == null) {
            throw new UsuariosExcepcion("La ruta del archivo no puede ser nula");
        }
        String consulta = "INSERT INTO DocumentacionPracticante (rutaDeArchivo, estadoRevision) VALUES (?, ?)";
        Connection conexion = null;
        PreparedStatement insercion = null;
        int idGenerado = -1;

        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            insercion = conexion.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            insercion.setString(1, documentacion.getRutaDeArchivo());
            insercion.setString(2, documentacion.getEstadoRevision().toString());
            insercion.executeUpdate();

            ResultSet tomarLlave = insercion.getGeneratedKeys();
            if (tomarLlave.next()) {
                idGenerado = tomarLlave.getInt(1);
                LOGGER.info("DocumentacionPracticante insertada correctamente con ID: " + idGenerado);
            }
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al insertar documentacion", excepcionSql);
            throw new UsuariosExcepcion("Error al agregar documentacion");
        } finally {
            try {
                if (insercion != null) {
                    insercion.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", excepcionSql);
            }
        }
        return idGenerado;
    }

    public List<DocumentacionPracticante> obtenerDocumentosPendientes(String matricula) throws UsuariosExcepcion {
        String consulta = "SELECT dp.idDocumentacionPracticante, dp.rutaDeArchivo, dp.estadoRevision " +
                "FROM documentacionpracticante dp " +
                "INNER JOIN entregadocumentacion ed ON dp.idDocumentacionPracticante = ed.entregaDocumentacion " +
                "WHERE ed.entregaMatricula = ? AND dp.estadoRevision = 'Pendiente'";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        List<DocumentacionPracticante> documentos = new ArrayList<>();
        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                DocumentacionPracticante documento = new DocumentacionPracticante(
                        resultado.getString("rutaDeArchivo"),
                        EstadoRevision.valueOf(resultado.getString("estadoRevision")),
                        null
                );
                documento.setIdDocumentacionPracticante(resultado.getInt("idDocumentacionPracticante"));
                documentos.add(documento);
            }
            LOGGER.info("Documentos pendientes obtenidos para: " + matricula);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al obtener documentos pendientes", excepcionSql);
            throw new UsuariosExcepcion("Error al obtener documentos pendientes");
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return documentos;
    }

    public int validarDocumento(int idDocumento, EstadoRevision estado, String motivoRechazo) throws UsuariosExcepcion {
        String consulta = "UPDATE documentacionpracticante SET estadoRevision = ?, motivoRechazado = ? " +
                "WHERE idDocumentacionPracticante = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexion = ConexionBaseDeDatos.getInstance().conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, estado.name());
            sentencia.setString(2, motivoRechazo);
            sentencia.setInt(3, idDocumento);
            filasAfectadas = sentencia.executeUpdate();
            LOGGER.info("Documento validado correctamente: " + idDocumento);
        } catch (SQLException excepcionSql) {
            LOGGER.log(Level.SEVERE, "Error al validar documento", excepcionSql);
            throw new UsuariosExcepcion("Error al validar documento");
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException excepcionSql) {
                LOGGER.log(Level.SEVERE, "Error al cerrar conexión", excepcionSql);
            }
        }
        return filasAfectadas;
    }
}