package net.qoopo.framework.chart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.ChartDataSet;
import org.primefaces.model.charts.ChartOptions;
import org.primefaces.model.charts.axes.AxesGridLines;
import org.primefaces.model.charts.axes.cartesian.CartesianAxes;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.bubble.BubbleChartDataSet;
import org.primefaces.model.charts.bubble.BubbleChartModel;
import org.primefaces.model.charts.bubble.BubbleChartOptions;
import org.primefaces.model.charts.data.BubblePoint;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;

public class QChartJS implements Serializable {
  public static final int ORDER_NOORDER = 0;

  public static final int ORDER_ASC = 1;

  public static final int ORDER_DES = 2;

  public void setBgColor(List<String> bgColor) {
    this.bgColor = bgColor;
  }

  public void setBorderColor(List<String> borderColor) {
    this.borderColor = borderColor;
  }

  public void setAcumulativa(boolean acumulativa) {
    this.acumulativa = acumulativa;
  }

  public void setMapa(HashMap<String, QChartSerie> mapa) {
    this.mapa = mapa;
  }

  public void setModeloLinea(LineChartModel modeloLinea) {
    this.modeloLinea = modeloLinea;
  }

  public void setModeloArea(LineChartModel modeloArea) {
    this.modeloArea = modeloArea;
  }

  public void setModeloBarra(BarChartModel modeloBarra) {
    this.modeloBarra = modeloBarra;
  }

  public void setModeloPie(PieChartModel modeloPie) {
    this.modeloPie = modeloPie;
  }

  public void setModeloDona(DonutChartModel modeloDona) {
    this.modeloDona = modeloDona;
  }

  public void setModeloBarraHorizontal(HorizontalBarChartModel modeloBarraHorizontal) {
    this.modeloBarraHorizontal = modeloBarraHorizontal;
  }

  public void setModeloBurbuja(BubbleChartModel modeloBurbuja) {
    this.modeloBurbuja = modeloBurbuja;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setLeyendaX(String leyendaX) {
    this.leyendaX = leyendaX;
  }

  public void setLeyendaY(String leyendaY) {
    this.leyendaY = leyendaY;
  }

  public void setContador(int contador) {
    this.contador = contador;
  }

  public void setContadorSerie(int contadorSerie) {
    this.contadorSerie = contadorSerie;
  }

  public void setAnguloRotacionY(int anguloRotacionY) {
    this.anguloRotacionY = anguloRotacionY;
  }

  public void setAnguloRotacionX(int anguloRotacionX) {
    this.anguloRotacionX = anguloRotacionX;
  }

  public void setMostrarLeyenda(boolean mostrarLeyenda) {
    this.mostrarLeyenda = mostrarLeyenda;
  }

  public void setMostrarTitulo(boolean mostrarTitulo) {
    this.mostrarTitulo = mostrarTitulo;
  }

  public void setMostrarLineas(boolean mostrarLineas) {
    this.mostrarLineas = mostrarLineas;
  }

  public void setSpanGaps(boolean spanGaps) {
    this.spanGaps = spanGaps;
  }

  public void setStacked(boolean stacked) {
    this.stacked = stacked;
  }

  public void setIndiceColor(int indiceColor) {
    this.indiceColor = indiceColor;
  }

  public void setIndiceColorLinea(int indiceColorLinea) {
    this.indiceColorLinea = indiceColorLinea;
  }

  public void setTipoOrden(int tipoOrden) {
    this.tipoOrden = tipoOrden;
  }

  public void setMaxLengthLabels(int maxLengthLabels) {
    this.maxLengthLabels = maxLengthLabels;
  }

  private List<String> bgColor = new ArrayList<>();

  public List<String> getBgColor() {
    return this.bgColor;
  }

  private List<String> borderColor = new ArrayList<>();

  public List<String> getBorderColor() {
    return this.borderColor;
  }

  private boolean acumulativa = false;

  private HashMap<String, QChartSerie> mapa;

  private LineChartModel modeloLinea;

  private LineChartModel modeloArea;

  private BarChartModel modeloBarra;

  private PieChartModel modeloPie;

  private DonutChartModel modeloDona;

  private HorizontalBarChartModel modeloBarraHorizontal;

  private BubbleChartModel modeloBurbuja;

  public boolean isAcumulativa() {
    return this.acumulativa;
  }

  public HashMap<String, QChartSerie> getMapa() {
    return this.mapa;
  }

  public LineChartModel getModeloLinea() {
    return this.modeloLinea;
  }

  public LineChartModel getModeloArea() {
    return this.modeloArea;
  }

  public BarChartModel getModeloBarra() {
    return this.modeloBarra;
  }

  public PieChartModel getModeloPie() {
    return this.modeloPie;
  }

  public DonutChartModel getModeloDona() {
    return this.modeloDona;
  }

  public HorizontalBarChartModel getModeloBarraHorizontal() {
    return this.modeloBarraHorizontal;
  }

  public BubbleChartModel getModeloBurbuja() {
    return this.modeloBurbuja;
  }

  private String titulo = "";

  public String getTitulo() {
    return this.titulo;
  }

  private String leyendaX = "";

  public String getLeyendaX() {
    return this.leyendaX;
  }

  private String leyendaY = "";

  public String getLeyendaY() {
    return this.leyendaY;
  }

  private int contador = 0;

  public int getContador() {
    return this.contador;
  }

  private int contadorSerie = 0;

  public int getContadorSerie() {
    return this.contadorSerie;
  }

  private int anguloRotacionY = 0;

  public int getAnguloRotacionY() {
    return this.anguloRotacionY;
  }

  private int anguloRotacionX = 0;

  public int getAnguloRotacionX() {
    return this.anguloRotacionX;
  }

  private boolean mostrarLeyenda = true;

  public boolean isMostrarLeyenda() {
    return this.mostrarLeyenda;
  }

  private boolean mostrarTitulo = false;

  public boolean isMostrarTitulo() {
    return this.mostrarTitulo;
  }

  private boolean mostrarLineas = true;

  public boolean isMostrarLineas() {
    return this.mostrarLineas;
  }

  private boolean spanGaps = true;

  public boolean isSpanGaps() {
    return this.spanGaps;
  }

  private boolean stacked = false;

  public boolean isStacked() {
    return this.stacked;
  }

  private int indiceColor = -1;

  public int getIndiceColor() {
    return this.indiceColor;
  }

  private int indiceColorLinea = -1;

  public int getIndiceColorLinea() {
    return this.indiceColorLinea;
  }

  private int tipoOrden = 0;

  public int getTipoOrden() {
    return this.tipoOrden;
  }

  private int maxLengthLabels = 20;

  public int getMaxLengthLabels() {
    return this.maxLengthLabels;
  }

  public QChartJS() {
    this.mapa = new HashMap<>();
    iniciarColores();
  }

  public QChartJS(String titulo) {
    this.titulo = titulo;
    this.mapa = new HashMap<>();
    iniciarColores();
  }

  public QChartJS(String titulo, String leyendaX, String leyendaY) {
    this.titulo = titulo;
    this.leyendaX = leyendaX;
    this.leyendaY = leyendaY;
    this.mapa = new HashMap<>();
    iniciarColores();
  }

  private void iniciarColores() {
    this.bgColor = new ArrayList<>();
    this.borderColor = new ArrayList<>();
    this.bgColor.add("rgba(153, 102, 255, 0.2)");
    this.bgColor.add("rgba(255, 99, 132, 0.2)");
    this.bgColor.add("rgba(255, 159, 64, 0.2)");
    this.bgColor.add("rgba(75, 192, 192, 0.2)");
    this.bgColor.add("rgba(54, 162, 235, 0.2)");
    this.bgColor.add("rgba(201, 203, 207, 0.2)");
    this.bgColor.add("rgba(255, 205, 86, 0.2)");
    this.borderColor.add("rgb(153, 102, 255)");
    this.borderColor.add("rgb(255, 99, 132)");
    this.borderColor.add("rgb(255, 159, 64)");
    this.borderColor.add("rgb(75, 192, 192)");
    this.borderColor.add("rgb(54, 162, 235)");
    this.borderColor.add("rgb(201, 203, 207)");
    this.borderColor.add("rgb(255, 205, 86)");
  }

  public void clear() {
    this.mapa.clear();
    this.indiceColor = -1;
    this.indiceColorLinea = -1;
  }

  public void addSerie(String nombre) {
    if (this.mapa == null)
      this.mapa = new HashMap<>();
    if (!this.mapa.containsKey(nombre))
      this.mapa.put(nombre, new QChartSerie(this.contadorSerie++, nombre, new ArrayList<>()));
  }

  public void addSerie(String nombre, int indice) {
    if (this.mapa == null)
      this.mapa = new HashMap<>();
    if (!this.mapa.containsKey(nombre))
      this.mapa.put(nombre, new QChartSerie(indice, nombre, new ArrayList<>()));
  }

  public void addElement(String clave, BigDecimal valor) {
    addElement(this.titulo, Integer.valueOf(this.contador++), clave, valor.doubleValue());
  }

  public void addElement(Integer indice, String clave, BigDecimal valor) {
    addElement(this.titulo, indice, clave, valor.doubleValue());
  }

  public void addElement(String serie, String clave, BigDecimal valor) {
    addElement(serie, Integer.valueOf(this.contador++), clave, valor.doubleValue());
  }

  public void addElement(String serie, Integer indice, String clave, BigDecimal valor) {
    addElement(serie, indice, clave, valor.doubleValue());
  }

  public void addElement(String clave, double valor) {
    addElement(this.titulo, Integer.valueOf(this.contador++), clave, valor);
  }

  public void addElement(Integer indice, String clave, double valor) {
    addElement(this.titulo, indice, clave, valor);
  }

  public void addElementXY(String clave, BigDecimal valorX, BigDecimal valorY, BigDecimal valorRadio) {
    addElementXY(this.titulo, Integer.valueOf(this.contador++), clave, valorX.doubleValue(), valorY.doubleValue(),
        valorRadio
            .doubleValue());
  }

  public void addElementXY(Integer indice, String clave, BigDecimal valorX, BigDecimal valorY, BigDecimal valorRadio) {
    addElementXY(this.titulo, indice, clave, valorX.doubleValue(), valorY.doubleValue(), valorRadio.doubleValue());
  }

  public void addElementXY(String clave, double valorX, double valorY, double valorRadio) {
    addElementXY(this.titulo, Integer.valueOf(this.contador++), clave, valorX, valorY, valorRadio);
  }

  public void addElementXY(Integer indice, String clave, double valorX, double valorY, double valorRadio) {
    addElementXY(this.titulo, indice, clave, valorX, valorY, valorRadio);
  }

  public void addElement(String serie, Integer indice, String clave, double valor) {
    if (this.mapa == null)
      this.mapa = new HashMap<>();
    if (clave != null && clave.length() > this.maxLengthLabels)
      clave = clave.substring(0, this.maxLengthLabels);
    if (!this.mapa.containsKey(serie))
      this.mapa.put(serie, new QChartSerie(this.contadorSerie++, serie, new ArrayList<>()));
    ((QChartSerie) this.mapa.get(serie)).agregarDato(new QChartElement(indice.intValue(), clave, valor));
  }

  public void addElementXY(String serie, Integer indice, String clave, double valorX, double valorY,
      double valorRadio) {
    if (this.mapa == null)
      this.mapa = new HashMap<>();
    if (clave != null && clave.length() > this.maxLengthLabels)
      clave = clave.substring(0, this.maxLengthLabels);
    if (!this.mapa.containsKey(serie))
      this.mapa.put(serie, new QChartSerie(this.contadorSerie++, serie, new ArrayList<>()));
    ((QChartSerie) this.mapa.get(serie))
        .agregarDato(new QChartElement(indice.intValue(), clave, valorX, valorY, valorRadio));
  }

  private List<QChartElement> getClaves() {
    Map<String, QChartElement> values = new HashMap<>();
    for (Map.Entry<String, QChartSerie> serie : this.mapa.entrySet()) {
      for (QChartElement e : ((QChartSerie) serie.getValue()).getDatos()) {
        if (!values.containsKey(e.getClave())) {
          values.put(e.getClave(), new QChartElement(e.getIndice(), e.getClave(), e.getValor()));
          continue;
        }
        ((QChartElement) values.get(e.getClave()))
            .setValor(((QChartElement) values.get(e.getClave())).getValor() + e.getValor());
      }
    }
    List<QChartElement> returnValue = new ArrayList<>(values.values());
    switch (this.tipoOrden) {
      case 1:
        Collections.sort(returnValue, (o1, o2) -> Double.compare(o1.getValor(), o2.getValor()));
        return returnValue;
      case 2:
        Collections.sort(returnValue, (o1, o2) -> Double.compare(o2.getValor(), o1.getValor()));
        return returnValue;
    }
    Collections.sort(returnValue, (o1, o2) -> Integer.compare(o1.getIndice(), o2.getIndice()));
    return returnValue;
  }

  private List<String> toLabels(List<QChartElement> list) {
    List<String> values = new ArrayList<>();
    list.forEach(c -> values.add(c.getClave()));
    return values;
  }

  private List<QChartElement> depurarValores(List<QChartElement> lista, Collection<QChartElement> claves) {
    List<QChartElement> nuevos = new ArrayList<>();
    for (QChartElement clave : claves) {
      boolean encontrado = false;
      for (QChartElement elemento : lista) {
        if (elemento.getClave().equals(clave.getClave())) {
          encontrado = true;
          break;
        }
      }
      if (!encontrado)
        nuevos.add(new QChartElement(clave.getIndice(), clave.getClave(), 0.0D));
    }
    lista.addAll(nuevos);
    return lista;
  }

  public void groupSum() {
    if (this.mapa != null) {
      List<QChartElement> claves = getClaves();
      for (Map.Entry<String, QChartSerie> serie : this.mapa.entrySet()) {
        Map<String, QChartElement> ocupados = new HashMap<>();
        for (QChartElement e : ((QChartSerie) serie.getValue()).getDatos()) {
          if (ocupados.containsKey(e.getClave())) {
            QChartElement elemento = ocupados.get(e.getClave());
            elemento.setValor(elemento.getValor() + e.getValor());
            ocupados.put(e.getClave(), elemento);
            continue;
          }
          ocupados.put(e.getClave(), e);
        }
        ((QChartSerie) serie.getValue()).getDatos().clear();
        List<QChartElement> tmp = new ArrayList<>();
        tmp.addAll(ocupados.values());
        tmp = depurarValores(tmp, claves);
        Collections.sort(tmp);
        if (this.acumulativa) {
          double suma = 0.0D;
          for (QChartElement item : tmp) {
            suma += item.getValor();
            item.setValor(suma);
          }
        }
        ((QChartSerie) serie.getValue()).getDatos().addAll(tmp);
      }
    }
  }

  public void groupAvg() {
    if (this.mapa != null) {
      List<QChartElement> claves = getClaves();
      for (Map.Entry<String, QChartSerie> entry : this.mapa.entrySet()) {
        Map<String, QChartElement> ocupados = new HashMap<>();
        for (QChartElement e : ((QChartSerie) entry.getValue()).getDatos()) {
          if (ocupados.containsKey(e.getClave())) {
            ((QChartElement) ocupados.get(e.getClave()))
                .setValor((((QChartElement) ocupados.get(e.getClave())).getValor() + e.getValor()) / 2.0D);
            continue;
          }
          ocupados.put(e.getClave(), e);
        }
        ((QChartSerie) entry.getValue()).getDatos().clear();
        List<QChartElement> tmp = new ArrayList<>();
        tmp.addAll(ocupados.values());
        tmp = depurarValores(tmp, claves);
        Collections.sort(tmp);
        ((QChartSerie) entry.getValue()).getDatos().addAll(tmp);
      }
    }
  }

  public void createBarModel() {
    this.modeloBarra = new BarChartModel();
    this.modeloBarraHorizontal = new HorizontalBarChartModel();
    List<QChartSerie> series = new ArrayList<>(this.mapa.values());
    Collections.sort(series);
    ChartData data = new ChartData();
    List<QChartElement> labels = getClaves();
    for (QChartSerie serie : series) {
      BarChartDataSet datosSerie = new BarChartDataSet();
      datosSerie.setLabel(serie.getNombre());
      List<Number> values = new ArrayList<>();
      List<QChartElement> ordenada = serie.getDatos();
      for (QChartElement label : labels) {
        for (QChartElement eg : ordenada) {
          if (eg.getClave().equals(label.getClave()))
            values.add(Double.valueOf(eg.getValor()));
        }
      }
      datosSerie.setBackgroundColor(getColor());
      datosSerie.setBorderColor(getColorLinea());
      datosSerie.setBorderWidth(Integer.valueOf(1));
      datosSerie.setData(values);
      data.addChartDataSet((ChartDataSet) datosSerie);
    }
    data.setLabels(toLabels(labels));
    this.modeloBarra.setData(data);
    this.modeloBarraHorizontal.setData(data);
    this.modeloBarra.setOptions((BarChartOptions) aplicarOpciones((ChartOptions) new BarChartOptions()));
    this.modeloBarraHorizontal.setOptions((BarChartOptions) aplicarOpciones((ChartOptions) new BarChartOptions()));
  }

  public void createLineModel() {
    this.modeloLinea = new LineChartModel();
    List<QChartSerie> series = new ArrayList<>(this.mapa.values());
    Collections.sort(series);
    List<QChartElement> labels = getClaves();
    ChartData data = new ChartData();
    for (QChartSerie serie : series) {
      LineChartDataSet datosSerie = new LineChartDataSet();
      datosSerie.setLabel(serie.getNombre());
      datosSerie.setFill(Boolean.valueOf(false));
      List<Object> values = new ArrayList();
      List<QChartElement> ordenada = serie.getDatos();
      for (QChartElement label : labels) {
        for (QChartElement eg : ordenada) {
          if (eg.getClave().equals(label.getClave()))
            values.add(Double.valueOf(eg.getValor()));
        }
      }
      datosSerie.setBackgroundColor(getColor());
      datosSerie.setBorderColor(getColorLinea());
      datosSerie.setBorderWidth(Integer.valueOf(1));
      datosSerie.setData(values);
      data.addChartDataSet((ChartDataSet) datosSerie);
    }
    data.setLabels(toLabels(labels));
    this.modeloLinea.setData(data);
    this.modeloLinea.setOptions((LineChartOptions) aplicarOpciones((ChartOptions) new LineChartOptions()));
  }

  public void createBarLineModel() {
    createBarLineModel(true);
  }

  public void createBarLineModel(boolean alternar) {
    this.modeloBarra = new BarChartModel();
    boolean linea = false;
    List<QChartSerie> series = new ArrayList<>(this.mapa.values());
    Collections.sort(series);
    ChartData data = new ChartData();
    List<QChartElement> labels = getClaves();
    if (this.stacked && series.size() > 1) {
      int lastIndice = series.size() + 1;
      QChartSerie sumSerie = new QChartSerie(lastIndice, "Sum", true, labels);
      series.add(sumSerie);
    }
    for (QChartSerie serie : series) {
      if (!alternar)
        linea = serie.isLineal();
      if (linea) {
        LineChartDataSet datosSerie = new LineChartDataSet();
        datosSerie.setLabel(serie.getNombre());
        datosSerie.setFill(Boolean.valueOf(false));
        List<Object> values = new ArrayList();
        List<QChartElement> ordenada = serie.getDatos();
        for (QChartElement label : labels) {
          for (QChartElement eg : ordenada) {
            if (eg.getClave().equals(label.getClave()))
              values.add(Double.valueOf(eg.getValor()));
          }
        }
        datosSerie.setBackgroundColor(getColor());
        datosSerie.setBorderColor(getColorLinea());
        datosSerie.setBorderWidth(Integer.valueOf(1));
        if (alternar)
          datosSerie.setYaxisID("right-y-axis");
        datosSerie.setData(values);
        data.addChartDataSet((ChartDataSet) datosSerie);
      } else {
        BarChartDataSet datosSerie = new BarChartDataSet();
        datosSerie.setLabel(serie.getNombre());
        List<Number> values = new ArrayList<>();
        List<QChartElement> ordenada = serie.getDatos();
        for (QChartElement label : labels) {
          for (QChartElement eg : ordenada) {
            if (eg.getClave().equals(label.getClave()))
              values.add(Double.valueOf(eg.getValor()));
          }
        }
        if (alternar)
          datosSerie.setYaxisID("left-y-axis");
        datosSerie.setBackgroundColor(getColor());
        datosSerie.setBorderColor(getColorLinea());
        datosSerie.setBorderWidth(Integer.valueOf(1));
        datosSerie.setData(values);
        data.addChartDataSet((ChartDataSet) datosSerie);
      }
      if (alternar)
        linea = !linea;
    }
    data.setLabels(toLabels(labels));
    this.modeloBarra.setData(data);
    BarChartOptions options = (BarChartOptions) aplicarOpciones((ChartOptions) new BarChartOptions());
    if (alternar) {
      CartesianScales cScales = new CartesianScales();
      CartesianLinearAxes linearAxes = new CartesianLinearAxes();
      linearAxes.setId("left-y-axis");
      linearAxes.setPosition("left");
      linearAxes.setStacked(this.stacked);
      linearAxes.setOffset(true);
      linearAxes.setBeginAtZero(true);
      linearAxes.setGrid(new AxesGridLines());
      linearAxes.getGrid().setDisplay(this.mostrarLineas);
      CartesianLinearAxes linearAxes2 = new CartesianLinearAxes();
      linearAxes2.setId("right-y-axis");
      linearAxes2.setPosition("right");
      linearAxes2.setStacked(this.stacked);
      linearAxes2.setOffset(true);
      linearAxes2.setBeginAtZero(true);
      linearAxes2.setGrid(new AxesGridLines());
      linearAxes2.getGrid().setDisplay(this.mostrarLineas);
      cScales.addYAxesData((CartesianAxes) linearAxes);
      cScales.addYAxesData((CartesianAxes) linearAxes2);
      options.setScales(cScales);
    }
    this.modeloBarra.setOptions(options);
  }

  public void crearModeloArea() {
    this.modeloArea = new LineChartModel();
    List<QChartSerie> series = new ArrayList<>(this.mapa.values());
    Collections.sort(series);
    List<QChartElement> labels = getClaves();
    ChartData data = new ChartData();
    for (QChartSerie serie : series) {
      LineChartDataSet datosSerie = new LineChartDataSet();
      datosSerie.setLabel(serie.getNombre());
      List<Object> values = new ArrayList();
      List<QChartElement> ordenada = serie.getDatos();
      for (QChartElement label : labels) {
        for (QChartElement eg : ordenada) {
          if (eg.getClave().equals(label.getClave()))
            values.add(Double.valueOf(eg.getValor()));
        }
      }
      datosSerie.setBackgroundColor(getColor());
      datosSerie.setBorderColor(getColorLinea());
      datosSerie.setBorderWidth(Integer.valueOf(1));
      datosSerie.setData(values);
      datosSerie.setFill(Boolean.valueOf(true));
      datosSerie.setTension(Double.valueOf(0.5D));
      data.addChartDataSet((ChartDataSet) datosSerie);
    }
    data.setLabels(toLabels(labels));
    this.modeloArea.setData(data);
    this.modeloArea.setOptions((LineChartOptions) aplicarOpciones((ChartOptions) new LineChartOptions()));
    this.modeloArea.getOptions().setShowLines(this.mostrarLineas);
    this.modeloArea.getOptions().setSpanGaps(this.spanGaps);
  }

  public void crearModeloPie() {
    this.modeloPie = new PieChartModel();
    List<QChartSerie> series = new ArrayList<>(this.mapa.values());
    Collections.sort(series);
    List<QChartElement> labels = getClaves();
    ChartData data = new ChartData();
    for (QChartSerie serie : series) {
      PieChartDataSet datosSerie = new PieChartDataSet();
      List<Number> values = new ArrayList<>();
      List<QChartElement> ordenada = serie.getDatos();
      for (QChartElement label : labels) {
        for (QChartElement eg : ordenada) {
          if (eg.getClave().equals(label.getClave()))
            values.add(Double.valueOf(eg.getValor()));
        }
      }
      datosSerie.setBackgroundColor(this.bgColor);
      datosSerie.setBorderColor(this.borderColor);
      datosSerie.setData(values);
      data.addChartDataSet((ChartDataSet) datosSerie);
    }
    data.setLabels(toLabels(labels));
    this.modeloPie.setData(data);
    this.modeloPie.setOptions((PieChartOptions) aplicarOpciones((ChartOptions) new PieChartOptions()));
  }

  public void crearModeloDona() {
    this.modeloDona = new DonutChartModel();
    List<QChartSerie> series = new ArrayList<>(this.mapa.values());
    Collections.sort(series);
    List<QChartElement> labels = getClaves();
    ChartData data = new ChartData();
    for (QChartSerie serie : series) {
      DonutChartDataSet datos = new DonutChartDataSet();
      List<Number> values = new ArrayList<>();
      List<QChartElement> ordenada = serie.getDatos();
      for (QChartElement label : labels) {
        for (QChartElement eg : ordenada) {
          if (eg.getClave().equals(label.getClave()))
            values.add(Double.valueOf(eg.getValor()));
        }
      }
      datos.setBackgroundColor(this.bgColor);
      datos.setBorderColor(this.borderColor);
      datos.setData(values);
      data.addChartDataSet((ChartDataSet) datos);
    }
    data.setLabels(toLabels(labels));
    this.modeloDona.setData(data);
    this.modeloDona.setOptions((DonutChartOptions) aplicarOpciones((ChartOptions) new DonutChartOptions()));
  }

  public void crearModeloBurbuja() {
    this.modeloBurbuja = new BubbleChartModel();
    List<QChartSerie> series = new ArrayList<>(this.mapa.values());
    Collections.sort(series);
    ChartData data = new ChartData();
    List<QChartElement> labels = getClaves();
    for (QChartSerie serie : series) {
      BubbleChartDataSet datos = new BubbleChartDataSet();
      List<BubblePoint> values = new ArrayList<>();
      List<QChartElement> ordenada = serie.getDatos();
      for (QChartElement label : labels) {
        for (QChartElement eg : ordenada) {
          if (eg.getClave().equals(label.getClave())) {
            values.add(new BubblePoint(Double.valueOf(eg.getValorX()), Double.valueOf(eg.getValorY()),
                Double.valueOf(eg.getValor())));
            // labels.add(new QChartElement(0, eg.getClave(), eg.getValor()));
          }
        }
      }
      datos.setData(values);
      data.addChartDataSet((ChartDataSet) datos);
      datos.setBackgroundColor(getColor());
      datos.setBorderColor(getColorLinea());
    }
    data.setLabels(toLabels(labels));
    this.modeloBurbuja.setData(data);
    this.modeloBurbuja.setOptions((BubbleChartOptions) aplicarOpciones((ChartOptions) new BubbleChartOptions()));
  }

  private ChartOptions aplicarOpciones(ChartOptions opciones) {
    Title tit = new Title();
    tit.setText(this.titulo);
    tit.setDisplay(this.mostrarTitulo);
    opciones.setTitle(tit);
    Legend legend = new Legend();
    legend.setDisplay(this.mostrarLeyenda);
    opciones.setLegend(legend);
    if (opciones instanceof BarChartOptions) {
      CartesianScales cScales = new CartesianScales();
      CartesianLinearAxes linearAxes = new CartesianLinearAxes();
      linearAxes.setStacked(this.stacked);
      linearAxes.setOffset(true);
      linearAxes.setBeginAtZero(true);
      linearAxes.setGrid(new AxesGridLines());
      linearAxes.getGrid().setDisplay(this.mostrarLineas);
      cScales.addXAxesData((CartesianAxes) linearAxes);
      cScales.addYAxesData((CartesianAxes) linearAxes);
      ((BarChartOptions) opciones).setScales(cScales);
    }
    if (opciones instanceof LineChartOptions) {
      CartesianScales cScales = new CartesianScales();
      CartesianLinearAxes linearAxes = new CartesianLinearAxes();
      linearAxes.setGrid(new AxesGridLines());
      linearAxes.getGrid().setDisplay(this.mostrarLineas);
      cScales.addXAxesData((CartesianAxes) linearAxes);
      cScales.addYAxesData((CartesianAxes) linearAxes);
      ((LineChartOptions) opciones).setScales(cScales);
    }
    return opciones;
  }

  private String getColor() {
    if (this.indiceColor < this.bgColor.size() - 1) {
      this.indiceColor++;
    } else {
      this.indiceColor = 0;
    }
    return this.bgColor.get(this.indiceColor);
  }

  private String getColorLinea() {
    if (this.indiceColorLinea < this.borderColor.size() - 1) {
      this.indiceColorLinea++;
    } else {
      this.indiceColorLinea = 0;
    }
    return this.borderColor.get(this.indiceColorLinea);
  }
}
