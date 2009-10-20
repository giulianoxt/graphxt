/**
 * PrimPlugin.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.plugins;

import graphxt.gui.GraphWindow;
import graphxt.model.Edge;
import graphxt.model.Graph;
import graphxt.model.Vertex;
import graphxt.view.EdgeView;
import graphxt.view.GraphView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.Timer;

/**
 * PrimPlugin
 * 
 * Plugin que implementa o algoritmo de Prim-Jarnik para
 * achar a árvore de espalhamento mínima para um certo
 * grafo montado pelo usuário.
 * 
 * @author Giuliano Vilela
 */
public class PrimPlugin extends BasePlugin implements ActionListener {
    /**
     * Construtor direto de BasePlugin
     */
    public PrimPlugin(Vertex<String> cur,Graph<String,Integer> graph,GraphView graph_view,GraphWindow window,Timer timer) {
        super(cur,graph,graph_view,window,timer);
    }

    /**
     * Inicializa as estruturas internas do plugin
     * @param cur Primeiro vértice à ser inserido na árvore
     */
    @Override
    public void setInitialVertex(Vertex<String> cur) {
        used = new HashSet<Vertex<String>>();
        used.add(cur);
        window.postMessage("Prim-Jarnik initialized.");
        graph_view.deselectAll();
        graph_view.getVertexView(cur).setSelected(true);
        graph_view.repaint();
    }

    /**
     * Atualiza o estado do funcionamento do
     * algoritmo interno.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (used.size() == graph.getNumVertex()) {
            window.postMessage("Removing unused edges.");
            
            ArrayList<EdgeView<String,Integer>> to_del = new ArrayList<EdgeView<String,Integer>>();
            
            for (EdgeView<String,Integer> ed : graph_view.getEdgeViewSet())
                if (!ed.isSelected())
                    to_del.add(ed);
            
            for (EdgeView<String,Integer> ed : to_del)
                graph_view.removeEdge(ed);
            
            window.postMessage("Prim-Jarnik finished.");
            stop();
            return;
        }

        int min = INF;
        Edge<String,Integer> min_ed = null;
        
        for (Edge<String,Integer> ed : graph.getEdgeSet()) {
            if (used.contains(ed.getStart()) == used.contains(ed.getEnd())) continue;
            
            if (ed.getData() < min) {
                min = ed.getData();
                min_ed = ed;
            }
        }
        
        window.postMessage("Analizing " + min_ed);
        
        used.add(min_ed.getStart());
        used.add(min_ed.getEnd());
        
        graph_view.getVertexView(min_ed.getStart()).setSelected(true);
        graph_view.getVertexView(min_ed.getEnd()).setSelected(true);
        graph_view.getEdgeView(min_ed).setSelected(true);
        
        graph_view.repaint();
    }

    private HashSet<Vertex<String>> used;
    private final int INF = 1073741824; // 2^30
}
