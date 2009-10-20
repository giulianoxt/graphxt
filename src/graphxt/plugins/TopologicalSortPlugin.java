/*
 * TopologicalSortPlugin.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.plugins;

import graphxt.model.Vertex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import graphxt.gui.GraphWindow;
import graphxt.model.Edge;
import graphxt.model.Graph;
import graphxt.view.GraphView;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.Timer;

/**
 * TopologicalSortPlugin
 * 
 * Plugin que implementa o algoritmo
 * de ordenaçao topológica de um grafo.
 * 
 * @author Giuliano Vilela
 */
public class TopologicalSortPlugin extends BasePlugin implements ActionListener {
    /**
     * Construtor direto do BasePlugin.
     */
    public TopologicalSortPlugin(Vertex<String> c,Graph<String,Integer> g,GraphView gv,GraphWindow w,Timer t) {
        super(c,g,gv,w,t);
    }
    
    /**
     * Inicializa as estruturas internas do plugin.
     * @param cur Este parâmetro não é utilizado.
     */
    @Override
    public void setInitialVertex(Vertex<String> cur) {
        k = 0;
        queue = new LinkedList<Vertex<String>>();
        used_ed = new HashSet<Edge<String,Integer>>();
        used_vert = new HashSet<Vertex<String>>();
        
        window.postMessage("Topological Sort initialized.");
        
        for (Vertex<String> v : graph.getVertexSet()) {
            if (graph.getIncidentEdges(v).size() == 0) {
                window.postMessage("Putting " + v + " on the queue.");
                queue.addLast(v);
                
                for (Edge<String,Integer> ed : graph.getOutgoingEdges(v))
                    used_ed.add(ed);
            }
            clearVertexInfo(v);
        }
        
        graph_view.deselectAll();
    }

    /**
     * Responde à um ActionEvent enviado pelo Timer
     * referente à este plugin. Atualiza o estado interno
     * das estruturas e do funcionamento do algoritmo.
     * Caso o algoritmo já tenha terminado sua execução,
     * este método para o plugin e retorna.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (queue.isEmpty()) {
            window.postMessage("Topological Sort finished.");
            stop();
            return;
        }
        
        Vertex<String> cur = queue.poll();
        
        for (Edge<String,Integer> ed : graph.getIncidentEdges(cur)) {
            graph_view.getEdgeView(ed).setSelected(true);
        }
        
        window.postMessage("Current: Vertex<" + cur + ">.");
        
        used_vert.add(cur);
        graph_view.getVertexView(cur).setSelected(true);
        setVertexInfo(cur,String.valueOf(++k));
        
        for (Vertex<String> v : graph.getVertexSet()) {
            if (used_vert.contains(v) || queue.contains(v))
                continue;
            
            Set<Edge<String,Integer>> set_ed = graph.getIncidentEdges(v);
            int i = set_ed.size();
            
            for (Edge<String,Integer> ed : set_ed)
                if (used_ed.contains(ed))
                    --i;
            
            if (i != 0) continue;
            
            window.postMessage("Putting " + v + " on the queue.");
            
            queue.addLast(v);
                
            for (Edge<String,Integer> ed : graph.getOutgoingEdges(v))
                used_ed.add(ed);
        }
        
        graph_view.repaint();
    }

    private int k;
    private LinkedList<Vertex<String>> queue;
    private HashSet<Edge<String,Integer>> used_ed;
    private HashSet<Vertex<String>> used_vert;
}
