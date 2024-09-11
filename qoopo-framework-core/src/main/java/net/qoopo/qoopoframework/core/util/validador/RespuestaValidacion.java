package net.qoopo.qoopoframework.core.util.validador;

public class RespuestaValidacion {

    private boolean valido;
    private int tipoIdentificacion;
    private String tipoIdentificacionTexto;

    public RespuestaValidacion(boolean valido, int tipoIdentificacion) {
        this.valido = valido;
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public int getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(int tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getTipoIdentificacionTexto() {
        switch (tipoIdentificacion) {
            case TipoDocumento.RUC_PRIVADA:
                tipoIdentificacionTexto = "RUC PRIVADA";
                break;
            case TipoDocumento.RUC_PUBLICA:
                tipoIdentificacionTexto = "RUC PÚBLICA";
                break;
            case TipoDocumento.CEDULA:
                tipoIdentificacionTexto = "CÉDULA";
                break;
            case TipoDocumento.RUC_NATURAL:
                tipoIdentificacionTexto = "RUC NATURAL";
                break;
            case TipoDocumento.CONSUMIDOR_FINAL:
                tipoIdentificacionTexto = "CONSUMIDOR FINAL";
                break;
            default:
                tipoIdentificacionTexto = "N/A";
                break;
        }
        return tipoIdentificacionTexto;
    }

    @Override
    public String toString() {
        return "{" + "valido=" + valido + ", tipoIdentificacionTexto=" + getTipoIdentificacionTexto() + '}';
    }

}
