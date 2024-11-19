package net.qoopo.framework.exporter.json;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
