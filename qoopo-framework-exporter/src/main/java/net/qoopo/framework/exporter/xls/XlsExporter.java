package net.qoopo.framework.exporter.xls;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import net.qoopo.framework.exporter.Exporter;

public class XlsExporter implements Exporter {

    public static final Logger log = Logger.getLogger("Qoopo-XlsExporter");

    protected OutputStream out;
    private List<String> campos = new ArrayList<>();
    protected List<Map<String, String>> data = new ArrayList<>();
    private int index = 0;// el indice actual en el proceso de lectura

    @Override
    public void set(String campo, Object valor) {
        if (!campos.contains(campo)) {
            campos.add(campo);
        }
        if (valor != null) {
            data.get(this.index).put(campo, valor.toString());
        } else {
            data.get(this.index).put(campo, "");
        }
    }

    @Override
    public void set(Integer index, Object valor) {
        if (!campos.contains(index.toString())) { // ejecucion dudosa
            campos.add(index, index.toString());
        }
        data.get(this.index).put(index.toString(), valor.toString());
    }

    @Override
    public void startItem() {
        data.add(new HashMap<>());
    }

    @Override
    public void endItem() {
        this.index++;
    }

    @Override
    public void setOutputExporter(OutputStream out) {
        this.out = out;
    }

    @Override
    public void exportar() throws Exception {
        String[] entries;
        // se escribe la cabecera
        entries = new String[campos.size()];
        campos.toArray(entries);
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("data");
            // se escribe la cabecera
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < entries.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(entries[i]);
            }
            // se escribe la data
            int rowCount = 1;
            for (Map<String, String> registro : data) {
                for (int i = 0; i < campos.size(); i++) {
                    entries[i] = registro.get(campos.get(i));
                }
                Row row = sheet.createRow(rowCount++);
                for (int i = 0; i < entries.length; i++) {
                    Cell cell = row.createCell(i);
                    if (entries[i] != null) {
                        if (entries[i].length() < 32767) { // valor maximo de una celda
                            cell.setCellValue(entries[i]);
                        } else {
                            cell.setCellValue(entries[i].substring(0, 32767));
                        }
                    }
                }
            }
            workbook.write(this.out);
            // workbook.close();
        }
    }

    @Override
    public String getMimetype() {
        return " application/vnd.ms-excel"; // xls
    }

    @Override
    public String getExtension() {
        return ".xls";
    }

    @Override
    public void clear() {
        data.clear();
        index = 0;
    }

}
