package net.qoopo.framework.proceso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.qoopo.framework.Accion;
import net.qoopo.framework.proceso.contador.ContadorBPS;

/**
 * Clase que permite la ejecucion de un proceso con control de avance y
 * monitoreo
 *
 * @author alberto
 */
public abstract class QProceso extends Thread {

    // tiempo predeterminado para dormir en el loop, y no sobresaturar el cpu
    protected long tiempoDormir = 100;
    /**
     * Para ser usada para control de ejecuion y detencion del proceso
     */
    protected boolean ejecutar;
    protected boolean detenido;
    protected JProgressBar progreso;
    protected JProgressBar progreso2;
    protected JLabel lblProgreso1;
    protected JLabel lblProgreso2;
    protected String[] args;
    protected Accion accionFinal;
    protected ContadorBPS contador;
    protected boolean seguirEjecutando;
    protected ExecutorService lanzador;

    protected JLabel lblEstadoGeneral;

    public abstract void detener();

    protected abstract void ejecutar();

    public abstract String getEstadoString();

    public Accion getAccionFinal() {
        return accionFinal;
    }

    public void setAccionFinal(Accion accionFinal) {
        this.accionFinal = accionFinal;
    }

    public JProgressBar getProgreso() {
        return progreso;
    }

    public void setProgreso(JProgressBar progreso) {
        this.progreso = progreso;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    // ejecuta la accionFinal
    public void terminarProceso() {
        if (accionFinal != null) {
            accionFinal.ejecutar();
        }
    }

    public ContadorBPS getContador() {
        return contador;
    }

    public void setContador(ContadorBPS contador) {
        this.contador = contador;
    }

    public static String tiempo(long milisegundos) {
        long hora = milisegundos / 3600000;
        long restohora = milisegundos % 3600000;
        long minuto = restohora / 60000;
        long restominuto = restohora % 60000;
        long segundo = restominuto / 1000;
        long restosegundo = restominuto % 1000;
        return hora + ":" + minuto + ":" + segundo + "." + Math.round(restosegundo);
    }

    public boolean isSeguirEjecutando() {
        return seguirEjecutando;
    }

    public void setSeguirEjecutando(boolean seguirEjecutando) {
        this.seguirEjecutando = seguirEjecutando;
    }

    public static String getStackTexto(Exception e) {
        StringWriter stack = new StringWriter();
        e.printStackTrace(new PrintWriter(stack));
        return stack.toString();
    }

    public JProgressBar getProgreso2() {
        return progreso2;
    }

    public void setProgreso2(JProgressBar progreso2) {
        this.progreso2 = progreso2;
    }

    public JLabel getLblProgreso1() {
        return lblProgreso1;
    }

    public void setLblProgreso1(JLabel lblProgreso1) {
        this.lblProgreso1 = lblProgreso1;
    }

    public JLabel getLblProgreso2() {
        return lblProgreso2;
    }

    public void setLblProgreso2(JLabel lblProgreso2) {
        this.lblProgreso2 = lblProgreso2;
    }

    public JLabel getLblEstadoGeneral() {
        return lblEstadoGeneral;
    }

    public void setLblEstadoGeneral(JLabel lblEstadoGeneral) {
        this.lblEstadoGeneral = lblEstadoGeneral;
    }

    public void iniciar() {
        start();
    }

    @Override
    public void run() {
        ejecutar = true;
        detenido = false;
        // super.run(); //To change body of generated methods, choose Tools | Templates.
        ejecutar();
    }

    public boolean isEjecutar() {
        return ejecutar;
    }

    public void setEjecutar(boolean ejecutar) {
        this.ejecutar = ejecutar;
    }

    public boolean isDetenido() {
        return detenido;
    }

    public void setDetenido(boolean detenido) {
        this.detenido = detenido;
    }

    public long getTiempoDormir() {
        return tiempoDormir;
    }

    /**
     * Tiempo predeterminado para dormir en el loop, y no sobresaturar el cpu
     *
     * @param tiempoDormir
     */
    public void setTiempoDormir(long tiempoDormir) {
        this.tiempoDormir = tiempoDormir;
    }

    public void esperarDetencion() {
        while (!detenido) {
            dormirEspera();
        }
    }

    public void dormir() {
        try {
            Thread.sleep(tiempoDormir);
        } catch (Exception ex) {

        }
    }

    public void dormirEspera() {
        try {
            Thread.sleep(100);
        } catch (Exception ex) {

        }
    }

}
