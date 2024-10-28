package net.qoopo.framework.printer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 * Clase que permite imprimir
 *
 * @author alberto
 */
public class Imprimir {

    // public static void main(String args[]) throws IOException {
    // FileInputStream inputStream = null;
    // try {
    //// inputStream = new
    // FileInputStream("/home/alberto/grive/griveTmp/testPdf.pdf");
    // inputStream = new
    // FileInputStream("/home/alberto/grive/griveTmp/FundioCarro.png");
    // imprimir(inputStream, "PDF2");
    // inputStream.close();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // }
    // }

    public static List<String> listarImpresorasNombres() {
        List<String> lst = new ArrayList<>();
        for (PrintService tmp : listarImpresoras()) {
            lst.add(tmp.getName());
        }
        return lst;
    }

    public static PrintService[] listarImpresoras() {
        return PrintServiceLookup.lookupPrintServices(null, null);
    }

    /**
     * Imprime un documento en la impresora definida. Si impresora es nulo o no
     * se encuentra la impresora definida se busca la impresora predeterminada
     *
     * @param inputStream
     * @param impresora
     */
    public static void imprimir(InputStream inputStream, String impresora) {

        if (inputStream == null) {
            System.out.println("No hay documento para imprimir");
            return;
        }

        DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc document = new SimpleDoc(inputStream, docFormat, null);
        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

        // ejemplo de atributos
        // attributeSet.add(new Copies(2));//2 copias
        // attributeSet.add(Sides.DUPLEX);//tamanio A4
        // attributeSet.add(OrientationRequested.LANDSCAPE);
        // attributeSet.add(MediaSizeName.NA_LETTER);
        // attributeSet.add(new NumberUp(2));
        // attributeSet.add(Finishings.STAPLE);
        // PrintService pService = PrintServiceLookup.lookupDefaultPrintService();
        PrintService pService = null;

        if (impresora == null) {
            pService = PrintServiceLookup.lookupDefaultPrintService();
        } else {
            PrintService services[] = PrintServiceLookup.lookupPrintServices(null, null);
            for (PrintService printService : services) {
                // System.out.println("preguntando impresora:" + printService.getName());
                if (printService.getName().equals(impresora)) {
                    pService = printService;
                    break;
                }
            }
            if (pService == null) {
                System.out.println("no se encontro la impresora buscada. Se busca impresora predeterminada");
                pService = PrintServiceLookup.lookupDefaultPrintService();
            }
        }

        if (pService != null) {
            // defaultPrintService.
            System.out.println("imprimiendo en " + pService.getName());
            DocPrintJob printJob = pService.createPrintJob();
            try {
                printJob.print(document, attributeSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("No existen impresoras instaladas");
        }

    }
}
