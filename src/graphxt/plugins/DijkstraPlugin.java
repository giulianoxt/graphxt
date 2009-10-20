/*
 * DijkstraPlugin.java
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import javax.swing.Timer;

/**
 * DijkstraPlugin
 * 
 * Plugin que implementa o algoritmo de 
 * caminho mais curto, assim como descrito
 * por Dijkstra.
 * 
 * @author Giuliano Vilela
 */
public class DijkstraPlugin extends BasePlugin implements ActionListener {
    /**
     * Construtor direto do BasePlugin.
     */
    public DijkstraPlugin(Vertex<String> cur,Graph<String,Integer> graph,GraphView graph_view,GraphWindow window,Timer timer) {
        super(cur,graph,graph_view,window,timer);
    }
        
    /**
     * Inicializa o plugin e indica qual será
     * o vértice inicial para a aplicação do algoritmo.
     */
    public void setInitialVertex(Vertex<String> cur) {
        dist = new HashMap<Vertex<String>,Integer>();
        heap = new PriorityQueue<Vertex<String>>(graph.getNumVertex()*2,new Cmp());
        
        for(Vertex<String> v : graph.getVertexSet()) {
            dist.put(v, INF);
            if (!v.equals(cur)) heap.add(v);
            clearVertexInfo(v);
        }
        
        dist.put(cur, 0);
        heap.add(cur);
        
        graph_view.deselectAll();
        graph_view.repaint();
        window.postMessage("Dijkstra initialized.");
    }
    
    /**
     * Método que continua com o andamento do algoritmo,
     * em resposta à um ActionEvent enviado pelo Timer da
     * janela que inicializou este plugin.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (heap.isEmpty()) {
            window.postMessage("Dijkstra finished.");
            stop();
            return;
        }

        Vertex<String> cur = heap.poll();
        
        window.postMessage("Current: Vertex<" + cur + ">.");
        
        graph_view.getVertexView(cur).setSelected(true);
        
        for (Edge<String,Integer> edge : graph.getEdgeSet()) {
            if (!edge.contains(cur)) continue;
            if (edge.isDirected() && !edge.getStart().getData().equals(cur.getData())) continue;
            
            Vertex<String> adj = edge.getOposite(cur);
            
            int alt = dist.get(cur) + edge.getData();
            
            if (alt >= dist.get(adj)) continue;
            
            graph_view.getEdgeView(edge).setSelected(true);
            
            window.postMessage("- Found better path to Vertex<" + adj + ">");
            window.postMessage("- Visiting: " + edge);
            
            dist.put(adj,alt);
            heap.remove(adj);
            heap.add(adj);
            
            setVertexInfo(adj,cur+"/"+alt);
        }
        
        graph_view.repaint();
    }

    /**
     * Classe de comparação entre dois vértices.
     * Um vértice é considerado menor que outro
     * vértice caso ele possa ser acessado à uma distância
     * menor do vértice inicial que o segundo vértice.
     * 
     * Esta classe é utilizada para implementar a ordenação
     * da fila de prioridades interna do algoritmo.
     */
    public class Cmp implements Comparator<Vertex<String>> {
        public int compare(Vertex<String> v1, Vertex<String> v2) {
            return (dist.get(v1) - dist.get(v2));
        }
    }
    
    private HashMap<Vertex<String>,Integer> dist;
    private PriorityQueue<Vertex<String>> heap;
    
    private final int INF = 1073741824; // 2^30
}
