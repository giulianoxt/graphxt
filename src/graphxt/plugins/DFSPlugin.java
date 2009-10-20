/*
 * DFSPlugin.java
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
import javax.swing.Timer;

/**
 * DFSPlugin
 * 
 * Esta classe é uma especialização do BFSPlugin,
 * mudando o modo como ele escolhe o próximo vértice
 * na sequência da busca, implementando o algoritmo
 * de busca em profundidade.
 * 
 * @author Giuliano Vilela
 */
public class DFSPlugin extends BFSPlugin implements ActionListener {
    /**
     * Construtor direto do BFSPlugin.
     */
    public DFSPlugin(Vertex<String> cur, Graph<String,Integer> graph, GraphView graph_view, GraphWindow window, Timer timer) {
        super(cur,graph,graph_view,window,timer);
    }
    
    /**
     * Continua atualizando o grafo para mostrar o percorrimento
     * em profundidade. Caso não tenha mais para onde ir no grafo,
     * para o plugin.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (to_visit.isEmpty()) {
                window.postMessage("DFS finished.");
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
                to_visit.addFirst(adj);
            }
        }
        
        graph_view.repaint();
    }
}
