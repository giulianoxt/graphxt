/*
 * BFSPlugin.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.plugins;

import graphxt.gui.GraphWindow;
import graphxt.model.Edge;
import graphxt.model.Graph;
import graphxt.model.Vertex;
import graphxt.view.GraphView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.Timer;

/**
 * BFSPlugin
 * 
 * É o plugin que implementa o percorrimento
 * em largura do grafo.
 * 
 * @author Giuliano Vilela
 */
public class BFSPlugin extends BasePlugin implements ActionListener {
    /**
     * Construtor direto do BasePlugin.
     */
    public BFSPlugin(Vertex<String> cur, Graph<String,Integer> graph, GraphView graph_view, GraphWindow window, Timer timer) {
        super(cur,graph,graph_view,window,timer);
    }
    
    /**
     * Inicializa as estruturas internas e
     * prepara para começar a buscar no vértice cur.
     */
    public void setInitialVertex(Vertex<String> cur) {
        visited = new HashSet<Vertex<String>>(graph.getNumVertex()*2);
        to_visit = new LinkedList<Vertex<String>>();
        
        if (cur == null) {
            cur = graph.getVertexSet().iterator().next();
        }
        
        to_visit.addLast(cur);
        window.postMessage("Plugin initialized.");
        
        graph_view.deselectAll();
        graph_view.repaint();
    }
    
    /**
     * Continua atualizando o grafo para mostrar o percorrimento
     * em largura. Caso não tenha mais para onde ir no grafo,
     * para o plugin.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (to_visit.isEmpty()) {
                window.postMessage("BFS finished.");
                stop();
                return;
        }
        
        Vertex<String> cur = to_visit.pop();
        
        window.postMessage("Current: Vertex<" + cur + ">.");
        
        visited.add(cur);
        
        graph_view.getVertexView(cur).setSelected(true);
        
        for (Edge<String,Integer> edge : graph.getEdgeSet()) {
            if (!edge.contains(cur)) continue;
            if (edge.isDirected() && !edge.getStart().getData().equals(cur.getData())) continue;
            
            Vertex<String> adj = edge.getOposite(cur);
            graph_view.getEdgeView(edge).setSelected(true);
            
            if (!visited.contains(adj) && !to_visit.contains(adj)) {
                window.postMessage("- Visiting: " + edge);
                to_visit.addLast(adj);
            }
        }
        
        graph_view.repaint();
    }

    protected LinkedList<Vertex<String>> to_visit;
    protected HashSet<Vertex<String>> visited;
}
