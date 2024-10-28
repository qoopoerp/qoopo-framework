package net.qoopo.framework.exporter.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.opencsv.CSVReader;

import net.qoopo.framework.exporter.Importer;


public class JsonImporter implements Importer {

    public static final Logger log = Logger.getLogger("Qoopo-JsonImporter");

    protected InputStream in;
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
        InputStreamReader csvStreamReader = new InputStreamReader(in);
        try (CSVReader csvReader = new CSVReader(csvStreamReader);) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                // lectura de cada registro
                // 1. Lectura de la cabecera
                if (campos.isEmpty()) {
                    for (String campo : line) {
                        campos.add(campo.trim());
                    }
                } else {
                    Map<String, String> registro = new HashMap<>();
                    int i = 0;
                    for (String valor : line) {
                        registro.put(campos.get(i), valor);
                        i++;
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
