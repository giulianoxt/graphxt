/*
 * BasePlugin.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.plugins;

import graphxt.gui.GraphWindow;
import graphxt.model.Graph;
import graphxt.model.Vertex;
import graphxt.view.EdgeView;
import graphxt.view.GraphView;
import graphxt.view.VertexView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * BasePlugin.
 * 
 * Classe base para todos os plugins do GraphXT.
 * Os plugins são modelados como ActionListener's, que
 * respondem à Action's enviadas por um Timer geral do
 * programa. 
 * 
 * À cada vez que um Action é enviado, o algoritmo
 * interno que foi implementado pelo plugin atualiza o seu estado
 * e desenha alguma informação na tela, para o usuário.
 * 
 * @author Giuliano Vilela
 */
public abstract class BasePlugin implements ActionListener {
    /**
     * Inicializa o plugin.
     * 
     * @param cur Vértice inicial de onde o plugin irá começar a atuar.
     * @param graph Grafo ao qual o plugin estará rodando.
     * @param graph_view GraphView ao qual o plugin estará rodando.
     * @param window Janela principal onde o plugin está localizado.
     * @param timer Timer que gera os Actions que este plugin responde.
     */
    public BasePlugin(Vertex<String> cur, Graph<String,Integer> graph, GraphView graph_view, GraphWindow window, Timer timer) {
        setGraph(graph);
        setGraphView(graph_view);
        setGraphWindow(window);
        setTimer(timer);
        setInitialVertex(cur);
    }
    
    /**
     * Modifica o grafo para o qual este plugin se refere.
     */
    public void setGraph(Graph<String,Integer> g) {
        graph = g;
    }
    
    /**
     * Retorna o grafo tal que este plugin se refere.
     */
    public Graph<String,Integer> getGraph() {
        return graph;
    }
    
    /**
     * Modifica o GraphView para o qual este plugin se refere.
     */
    public void setGraphView(GraphView gview) {
        graph_view = gview;
        
        for (VertexView<String> v : gview.getVertexViewSet())
            v.setSelected(false);
        
        for (EdgeView<String,Integer> ed : gview.getEdgeViewSet())
            ed.setSelected(false);
    }
    
    /**
     * Retorna o GraphView para o qual este plugin se refere.
     */
    public GraphView getGraphView() {
        return graph_view;
    }
    
    /**
     * Modifica a janela em que este plugin foi chamado.
     */
    public void setGraphWindow(GraphWindow w) {
        window = w;
    }
    
    /**
     * Retorna a janela em que este plugin foi chamado.
     */
    public GraphWindow getGraphWindow() {
        return window;
    }
    
    /**
     * Modifica o timer que este plugin faz referência.
     */
    public void setTimer(Timer t) {
        timer = t;
    }
    
    /**
     * Retorna o timer que este plugin faz referência.
     */
    public Timer getTimer() {
        return timer;
    }
    
    /**
     * Cada plugin implementa de maneira diferente o modo
     * como lidam com o vértice inicial. Na prática, as
     * classes bases também utilizam este método para inicializar
     * o plugin pela primeira vez.
     */
    public abstract void setInitialVertex(Vertex<String> cur);
    
    /**
     * Faz com que este plugin pare de receber Action's emitidos
     * pelo seu timer interno.
     */
    public void stop() {
        timer.removeActionListener(this);
    }
    
    /**
     * Este método é implementado por cada uma das classes base
     * e indica como o plugin responde à um ActionEvent. Ou seja,
     * indica como ele progride à cada iteração do algoritmo interno.
     */
    @Override
    public abstract void actionPerformed(ActionEvent e);
 
    /**
     * Limpa a informação que é desenhada ao lado do Vertíce v.
     */
    public void clearVertexInfo(Vertex<String> v) {
        VertexView<String> view = graph_view.getVertexView(v);
        String inf = view.getViewInfo();
        if (inf.lastIndexOf('[') != -1) {
            view.setViewInfo(inf.substring(0, inf.lastIndexOf('[')).trim());
        }
    }
    
    /**
     * Modifica a informação que é desenhada ao lado do vértice v.
     */
    public void setVertexInfo(Vertex<String> v, String inf) {
        clearVertexInfo(v);
        VertexView<String> view = graph_view.getVertexView(v);
        view.setViewInfo(view.getViewInfo() + "  [" + inf + "]");
    }
    
    protected Graph<String,Integer> graph;
    protected GraphView graph_view;
    protected GraphWindow window;
    protected Timer timer;
}
