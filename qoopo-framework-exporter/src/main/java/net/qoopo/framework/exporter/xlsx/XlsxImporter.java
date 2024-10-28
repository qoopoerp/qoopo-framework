package net.qoopo.framework.exporter.xlsx;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.qoopo.framework.exporter.Importer;
import net.qoopo.framework.exporter.xls.XlsCellValueUtil;

public class XlsxImporter implements Importer {

    public static final Logger log = Logger.getLogger("Qoopo-XlsxImporter");

    protected InputStream in;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<String> campos = new ArrayList<>();
    protected List<Map<String, String>> data = null;
    private int index = 0;// el indice actual en el proceso de lectura

    @Override
    public Object get(String campo) {
        if (data != null && data.size() > this.index) {
            return data.get(this.index).get(campo);
        }
        return null;
    }

    @Override
    public Object get(Integer index) {
        if (data != null && data.size() > this.index) {
            return data.get(this.index).get(campos.get(index));
        }
        return null;
    }

    @Override
    public void startItem() {
        //
    }

    @Override
    public void endItem() {
        if (data != null && index < data.size()) {
            index++;
        }
    }

    @Override
    public boolean hasItems() {
        return data != null && index < data.size();
    }

    @Override
    public void setInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void importar() throws Exception {
        data = new ArrayList<>();
        campos.clear();
        // creating workbook instance that refers to .xls file
        try (XSSFWorkbook wb = new XSSFWorkbook(in);) {
            XSSFSheet sheet = wb.getSheetAt(0);
            // FormulaEvaluator formulaEvaluator =
            // wb.getCreationHelper().createFormulaEvaluator();
            for (Row row : sheet) {
                if (campos.isEmpty()) {
                    for (Cell cell : row) {
                        campos.add(cell.getStringCellValue());
                    }
                } else {
                    Map<String, String> registro = new HashMap<>();
                    // int i = 0;
                    for (Cell cell : row) {
                        registro.put(campos.get(cell.getColumnIndex()), XlsCellValueUtil.getValue(cell));
                        // i++;
                    }
                    data.add(registro);
                }
            }
        }
    }

    @Override
    public Integer getCurrent() {
        return index;
    }

    @Override
    public Integer getTotal() {
        return data != null ? data.size() : 0;
    }

}
