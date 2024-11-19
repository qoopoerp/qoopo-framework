package net.qoopo.framework.exporter.csv;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.opencsv.CSVWriter;

import net.qoopo.framework.exporter.Exporter;


public class CsvExporter implements Exporter {

    public static final Logger log = Logger.getLogger("Qoopo-CsvExporter");

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
        // try {
        String[] entries;
        try (var osw = new OutputStreamWriter(out, StandardCharsets.UTF_8); var writer = new CSVWriter(osw)) {
            // se escribe la cabecera
            entries = new String[campos.size()];
            campos.toArray(entries);
            writer.writeNext(entries);
            // se escribe la data
            for (Map<String, String> registro : data) {
                for (int i = 0; i < campos.size(); i++) {
                    entries[i] = registro.get(campos.get(i));
                }
                writer.writeNext(entries);
            }
        }
    }

    @Override
    public String getMimetype() {
        return "text/csv";
    }

    @Override
    public String getExtension() {
        return ".csv";
    }

    @Override
    public void clear() {
        data.clear();
        index = 0;
    }

}
