package logica.dominio;

import logica.dominio.enums.Rol;

public class SesionUsuario {
    private static SesionUsuario instancia;
    private UsuarioSesion usuarioActivo;

    private SesionUsuario() {}

    public static SesionUsuario getInstance() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public void iniciarSesion(UsuarioSesion usuario) {
        this.usuarioActivo = usuario;
    }

    public void cerrarSesion() {
        this.usuarioActivo = null;
    }

    public UsuarioSesion getUsuarioActivo() {
        return usuarioActivo;
    }

    public String getNombre() {
        return usuarioActivo != null ? usuarioActivo.getNombre() : null;
    }

    public String getApellidos() {
        return usuarioActivo != null ? usuarioActivo.getApellidos() : null;
    }

    public boolean haySesionActiva() {
        return usuarioActivo != null;
    }
    public boolean tieneRol(Rol rolEsperado) {
        return usuarioActivo != null && usuarioActivo.getRol() == rolEsperado;
    }
    public String getMatricula() {
        return usuarioActivo != null ? usuarioActivo.getMatricula() : null;
    }
    public int getIdUsuario() {
        return usuarioActivo != null ? usuarioActivo.getIdUsuario() : 0;
    }
}