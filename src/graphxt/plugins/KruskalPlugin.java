/**
 * KruskalPlugin.java
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import javax.swing.Timer;

/**
 * KruskalPlugin
 * 
 * Plugin que implementa o algoritmo de Kruskal para
 * achar a árvore de espalhamento mínima para um certo
 * grafo montado pelo usuário.
 * 
 * @author Giuliano Vilela
 */
public class KruskalPlugin extends BasePlugin implements ActionListener {
    /**
     * Construtor direto do BasePlugin.
     */
    public KruskalPlugin(Vertex<String> cur,Graph<String,Integer> graph,GraphView graph_view,GraphWindow window,Timer timer) {
        super(cur,graph,graph_view,window,timer);
    }

    /**
     * Inicializa as estruturas internas do plugin
     * @param cur Não faz uso deste parâmetro
     */
    @Override
    public void setInitialVertex(Vertex<String> cur) {
        disj_set = new HashMap<Vertex<String>,Integer>();
        heap = new PriorityQueue<Edge<String,Integer>>(graph.getNumEdges()*2,new Cmp());
        
        int i = 0;
        for (Vertex<String> v : graph.getVertexSet())
            disj_set.put(v,i++);
        
        for (Edge<String,Integer> ed : graph.getEdgeSet())
            heap.add(ed);
        
        window.postMessage("Kruskal initialized.");
        graph_view.deselectAll();
        graph_view.repaint();
    }

    /**
     * Atualiza o estado do funcionamento do
     * algoritmo interno.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (heap.isEmpty()) {
            window.postMessage("Kruskal finished.");
            stop();
            return;
        }

        Edge<String,Integer> cur_ed = heap.poll();
        EdgeView<String,Integer> cur_view = graph_view.getEdgeView(cur_ed);
        
        window.postMessage("Analizing " + cur_ed);
        
        int set1 = disj_set.get(cur_ed.getStart());
        int set2 = disj_set.get(cur_ed.getEnd());
        
        if (set1 == set2) {
            window.postMessage("- Discarded: Makes a cycle.");
            graph_view.removeEdge(cur_view);
            return;
        }
        
        cur_view.setSelected(true);
        
        window.postMessage("- Connecting " + cur_ed.getStart() + " and " + cur_ed.getEnd());
        
        graph_view.getVertexView(cur_ed.getStart()).setSelected(true);
        graph_view.getVertexView(cur_ed.getEnd()).setSelected(true);
        
        for (Vertex<String> v : graph.getVertexSet())
            if (disj_set.get(v) == set2)
                disj_set.put(v,set1);
        
        graph_view.repaint();
    }
    
    /**
     * Classe de comparação entre duas arestas,
     * utilizada para manter a ordenação da fila de prioridade.
     * As arestas são comparadas de acordo com os seus pesos.
     */
    public class Cmp implements Comparator<Edge<String,Integer>> {
        public int compare(Edge<String, Integer> e1, Edge<String, Integer> e2) {
            return (e1.getData() - e2.getData());
        }        
    }
    
    private HashMap<Vertex<String>,Integer> disj_set;
    private PriorityQueue<Edge<String,Integer>> heap;
}
