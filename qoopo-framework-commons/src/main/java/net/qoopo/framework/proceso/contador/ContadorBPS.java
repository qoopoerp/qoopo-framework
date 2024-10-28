
package net.qoopo.framework.proceso.contador;

import javax.swing.JLabel;

/**
 *
 * @author alberto
 */
public class ContadorBPS extends Thread {

    private double valorTotal = 0;
    private double valorActual = 0;
    private long tiempoInicio = -1L;
    private double tasa = 0.00;
    private String unidad = "";
    private String ps = "ps";
    private JLabel lblEstado = null;
    private int tipo;
    public static final int BYTES = 1;
    public static final int ENTERO = 2;
    public static final int FLOTANTE = 3;
    private int intervaloActualiza = 250;// ms (1000/4) 4 actualizaciones por segundo
    private boolean total = false;
    public String descripcion;

    public ContadorBPS() {
    }

    public ContadorBPS(String descripcion, String unidad, int tipo, boolean contadorTotal) {
        this.unidad = unidad;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.total = contadorTotal;
    }

    public ContadorBPS(String descripcion, String unidad, String ps, int tipo, boolean contadorTotal) {
        this.unidad = unidad;
        this.ps = ps;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.total = contadorTotal;
    }

    public ContadorBPS(String descripcion, String unidad, String ps, JLabel lblEstado, int tipo,
            boolean contadorTotal) {
        this.unidad = unidad;
        this.ps = ps;
        this.lblEstado = lblEstado;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.total = contadorTotal;
    }

    public ContadorBPS(String descripcion, String unidad, int tipo, int intervalo, boolean contadorTotal) {
        this.unidad = unidad;
        this.tipo = tipo;
        this.intervaloActualiza = intervalo;
        this.descripcion = descripcion;
        this.total = contadorTotal;
    }

    public ContadorBPS(String descripcion, String unidad, String ps, int tipo, int intervalo, boolean contadorTotal) {
        this.unidad = unidad;
        this.ps = ps;
        this.tipo = tipo;
        this.intervaloActualiza = intervalo;
        this.descripcion = descripcion;
        this.total = contadorTotal;
    }

    public ContadorBPS(String descripcion, String unidad, String ps, JLabel lblEstado, int tipo, int intervalo,
            boolean contadorTotal) {
        this.unidad = unidad;
        this.ps = ps;
        this.lblEstado = lblEstado;
        this.tipo = tipo;
        this.intervaloActualiza = intervalo;
        this.descripcion = descripcion;
        this.total = contadorTotal;
    }

    public void agregar(double valor) {
        synchronized (this) {
            this.valorActual += valor;
            this.valorTotal += valor;
            actualizarEstado();
        }
    }

    /**
     * Actualiza automaticamente un contro label
     */
    public void actualizarEstado() {
        try {
            if (lblEstado != null) {
                if (total) {
                    lblEstado.setText(this.getTotalFormated());
                } else {
                    lblEstado.setText(this.getTasaFormated());
                }
                lblEstado.repaint();
            }
        } catch (Exception e) {
        }
    }

    public void calcularTasa() {
        try {
            // hace el calclulo solo para el tipo tasa
            if (!total) {
                synchronized (this) {
                    long transcurrido = System.currentTimeMillis() - this.tiempoInicio;
                    if (transcurrido == 0) {
                        transcurrido = 1;
                    }
                    // if (transcurrido % intervaloActualiza == 0) {//hace el calculo
                    tasa = 1000.0D * this.valorActual / transcurrido;
                    if (transcurrido > 1000) {// reinicia el contador (1000 es un segundo )
                        this.tiempoInicio = System.currentTimeMillis();
                        this.valorActual = 0.0D;
                    }
                    // }
                }
            }
        } catch (Exception e) {
        }

        actualizarEstado();
    }

    @Override
    public void run() {
        this.tiempoInicio = System.currentTimeMillis();
        while (true) {
            try {
                calcularTasa();
                try {
                    sleep(intervaloActualiza);
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
        }
    }

    public void resetearValores() {
        valorActual = 0;
        valorTotal = 0;
    }

    public double getTasa() {
        return tasa;
    }

    public void setTasa(double rate) {
        this.tasa = rate;
    }

    public String getTasaFormated() {
        switch (tipo) {
            case ContadorBPS.BYTES:
                return this.getTasaBytesFormated();
            case ContadorBPS.ENTERO:
                return this.getTasaIntFormateado();
            default:
                return getRateDoubleFormateado();
        }
    }

    public String getTotalFormated() {
        switch (tipo) {
            case ContadorBPS.BYTES:
                return this.getTotalBitsFormated();
            case ContadorBPS.ENTERO:
                return this.getTotalIntFormateado();
            default:
                return getTotalDoubleFormateado();
        }
    }

    public String getTasaBytesFormated() {
        return String.valueOf(tasa).concat(ps);
    }

    public String getTasaIntFormateado() {
        return String.valueOf(Math.round(tasa)).concat(" ").concat(unidad).concat(ps);
    }

    public String getRateDoubleFormateado() {
        return String.format("%.2f", tasa).concat(" ").concat(unidad).concat(ps);
    }

    public String getTotalBitsFormated() {
        return String.valueOf(valorTotal);
    }

    public String getTotalIntFormateado() {
        return String.valueOf(Math.round(valorTotal)).concat(" ").concat(unidad);
    }

    public String getTotalDoubleFormateado() {
        return String.format("%.2f", valorTotal).concat(" ").concat(unidad);
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public JLabel getLblEstado() {
        return lblEstado;
    }

    public void setLblEstado(JLabel lblEstado) {
        this.lblEstado = lblEstado;
    }
}
