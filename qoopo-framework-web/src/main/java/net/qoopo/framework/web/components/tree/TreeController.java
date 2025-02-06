package net.qoopo.framework.web.components.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.jpa.core.interfaces.Agrupable;
import net.qoopo.framework.models.Columna;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.web.controller.entity.complete.AbstractEntityCrudCompleteController;

/**
 * Este bean maneja la vista de Graficos de todos los modulos
 *
 * @author ALBERTO
 */
// @Named
// @SessionScoped
public class TreeController implements Serializable {

    public static final Logger log = Logger.getLogger("Qoopo");

    // protected List<SelectItem> gruposSeleccionados;
    protected List<OpcionBase> opcionesGrupo = new ArrayList<>();
    protected OpcionBase opcionGrupo = new OpcionBase(-100, 0, "'");
    protected OpcionBase opcionGrupo2 = new OpcionBase(-100, 0, "'");
    protected OpcionBase opcionGrupo3 = new OpcionBase(-100, 0, "'");
    protected TreeNode nodo = new DefaultTreeNode("Root", null, null);
    private List<Columna> columnas = new ArrayList<>();
    private Iterable datos;
    private boolean opcionesCargadas = false;
    protected AbstractEntityCrudCompleteController bean;// el bean que llama al metodo

    public TreeController() {
        // constructor
    }

    public Iterable getDatos() {
        return datos;
    }

    public void setDatos(Iterable datos) {
        this.datos = datos;
    }

    public AbstractEntityCrudCompleteController getBean() {
        return bean;
    }

    public void setBean(AbstractEntityCrudCompleteController bean) {
        this.bean = bean;
    }

    public void seleccionarEditar(AbstractEntity objeto) {
        try {
            if (bean != null) {
                bean.edit(objeto);
            }
        } catch (Exception e) {
            //
        }
    }

    public void eliminar(AbstractEntity objeto) {
        try {
            if (bean != null) {
                bean.delete(objeto);
            }
        } catch (Exception e) {
            //
        }
    }

    protected void cargarOpciones(Object entidad) {
        if (!opcionesCargadas) {
            opcionesGrupo.clear();
            opcionesGrupo.add(OpcionBase.SIN_GRUPO);
            if (entidad instanceof Agrupable) {
                Agrupable g = (Agrupable) entidad;
                if (g.getOpcionesGrupos() != null) {
                    opcionesGrupo.addAll(g.getOpcionesGrupos());
                }
                opcionesCargadas = true;
            }

            columnas.clear();
            try {
                if (entidad instanceof Agrupable) {
                    Agrupable g = (Agrupable) entidad;
                    columnas.addAll(g.getColumnas());
                }

            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    public void recargar() {
        Map mapa = new HashMap<>();
        opcionesCargadas = false;
        // System.out.println("Seleccionados = " +
        // Arrays.toString(this.gruposSeleccionados.toArray()));
        nodo = new DefaultTreeNode(new TreeGroup("Root"));
        if (datos != null /* && !datos.isEmpty() */) {
            // cargarOpciones(datos.get(0));
            datos.forEach(t -> {
                cargarOpciones(t);
                if (t instanceof Agrupable) {
                    Agrupable g = (Agrupable) t;
                    if (opcionGrupo.getCodigo() == OpcionBase.SIN_GRUPO.getCodigo()) {
                        TreeNode registro = new DefaultTreeNode(t, nodo);
                    } else {
                        for (String grupo : g.getGrupo(opcionGrupo)) {
                            // String grupo2 = g.getGrupo(opcionGrupo2);
                            // String grupo3 = g.getGrupo(opcionGrupo3);
                            if (mapa.containsKey(grupo)) {
                                TreeNode padre = (DefaultTreeNode) mapa.get(grupo);
                                TreeNode registro = new DefaultTreeNode(t, padre);
                            } else {
                                TreeNode padre = new DefaultTreeNode(new TreeGroup(grupo), nodo);
                                TreeNode hijo = new DefaultTreeNode(t, padre);
                                mapa.put(grupo, padre);
                            }
                        }
                    }
                }
            });
        }
    }

    public TreeNode getNodo() {
        return nodo;
    }

    public void setNodo(TreeNode nodo) {
        this.nodo = nodo;
    }

    public List<Columna> getColumnas() {
        return columnas;
    }

    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public OpcionBase getOpcionGrupo() {
        return opcionGrupo;
    }

    public void setOpcionGrupo(OpcionBase opcionGrupo) {
        this.opcionGrupo = opcionGrupo;
    }

    public OpcionBase getOpcionGrupo2() {
        return opcionGrupo2;
    }

    public void setOpcionGrupo2(OpcionBase opcionGrupo2) {
        this.opcionGrupo2 = opcionGrupo2;
    }

    public OpcionBase getOpcionGrupo3() {
        return opcionGrupo3;
    }

    public void setOpcionGrupo3(OpcionBase opcionGrupo3) {
        this.opcionGrupo3 = opcionGrupo3;
    }

    public List<OpcionBase> getOpcionesGrupo() {
        return opcionesGrupo;
    }

    public void setOpcionesGrupo(List<OpcionBase> opcionesGrupo) {
        this.opcionesGrupo = opcionesGrupo;
    }

    // public List<SelectItem> getGruposSeleccionados() {
    // return gruposSeleccionados;
    // }
    //
    // public void setGruposSeleccionados(List<SelectItem> gruposSeleccionados) {
    // this.gruposSeleccionados = gruposSeleccionados;
    // }
    // public List<SelectItem> completeGrupos(String query) {
    // query = query.toLowerCase();
    // List<SelectItem> suggestions = new ArrayList<>();
    // try {
    // for (OpcionBase p : opcionesGrupo) {
    // String texto = textoBean.getTextValue(p.getCodigoTexto());
    // if (texto.toLowerCase().contains(query)) {
    // suggestions.add(new SelectItem(p.getCodigo(), texto, texto));
    // }
    // }
    // } catch (Exception e) {
    // //
    // }
    // return suggestions;
    // }
}
