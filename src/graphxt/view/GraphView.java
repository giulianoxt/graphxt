/*
 * GraphView.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.view;

import graphxt.gui.GraphWindow;
import graphxt.model.Graph;
import graphxt.model.Vertex;
import graphxt.model.Edge;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JComponent;
import java.util.HashMap;
import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

/**
 * GraphView
 * 
 * Classe principal para a visualização dos Grafos.
 * Representa o mapeamento de um modelo (Graph<T,V>)
 * para uma tela onde este pode ser visto. Utiliza
 * internamente VertexView's e EdgeView's para delegar
 * as tarefas desenhar os vértices e as arestas.
 * 
 * Seu papel principal está no código que lida com os eventos
 * gerados pelo usuário através do mouse. Consiste em um conjunto
 * de métodos que respondem aos eventos gerados pelo Swing, atualizando
 * o estado geral do grafo como um todo.
 * 
 * O GraphView é um JComponent, e, portanto, pode ser
 * plugado em qualquer interface que o necessite. A única atenção
 * especial deve ser dada ao fato de que os eventos devem ser
 * conectados corretamente à este GraphView.
 * 
 * @author Giuliano Vilela
 */
public class GraphView extends JComponent {
    /**
     * Cria um View representado um Grafo vazio.
     */
    public GraphView() {
        this(new Graph());
    }
    
    /**
     * Cria um View representando o grafo G e utilizando o tema default.
     */
    public GraphView(Graph<String,Integer> g) {
        this(g,GraphTheme.Default);
    }
    
    /**
     * Cria um View representando o grafo g, de acordo
     * com as informações contidas no tema theme.
     */
    public GraphView(Graph<String,Integer> g, GraphTheme theme) {
        super();
        setTheme(theme);
        setGraph(g);
        setVisible(true);
        hoover_vert = null;
        hoover_edge = null;
        sel_vert = null;
        sel_edge = null;
        ad_ed_step = 0;
        drag_edge = null;
        drag_vert = null;
    }
    
    /**
     * Retorna o Grafo que este View representa.
     */
    public Graph<String,Integer> getGraph() {
        return graph;
    }
    
    /**
     * Retorna o tema com o qual este View pega
     * as informações para desenhar o grafo na tela.
     */
    public GraphTheme getTheme() {
        return theme;
    }
    
    /**
     * Retorna o VertexView associado ao vértice vertex.
     */
    public VertexView<String> getVertexView(Vertex<String> vertex) {
        return map_vert.get(vertex);
    }
    
    /**
     * Retorna a EdgeView associada à aresta edge.
     */
    public EdgeView<String,Integer> getEdgeView(Edge<String,Integer> edge) {
        return map_edg.get(edge);
    }
    
    /**
     * Retorna uma coleção iterável, contendo todos os
     * VertexView deste grafo.
     */
    public Collection<VertexView<String>> getVertexViewSet() {
        return map_vert.values();
    }
    
    /**
     * Retorna uma coleção iterável, contendo todos os
     * EdgeView deste grafo.
     */
    public Collection<EdgeView<String,Integer>> getEdgeViewSet() {
        return map_edg.values();
    }
    
    /**
     * Modifica o tema utilizado por este GraphView.
     */
    public void setTheme(GraphTheme t) {
        theme = t;
    }

    /**
     * Adiciona um novo vértice à este grafo, junto com o VertexView.
     * @param x Coordenada x do novo vértice
     * @param y Coordenada y do novo vértice
     * @param info Informação que será guardada pelo vértice
     */
    public void addVertex(double x, double y, String info) {
        Vertex<String> vert = new Vertex<String>(info);
        VertexView<String> view = new VertexView<String>(x,y,vert,theme);
        
        graph.insertVertex(vert);
        map_vert.put(vert,view);
        
        repaint();
    }
    
    /**
     * Remove o vértice que guarda a informação info.
     * Também são removidas todas as arestas relacionadas
     * com este vértice.
     */
    public void removeVertex(String info) {
        Vertex<String> vert = graph.getVertex(info);
        graph.removeVertex(vert);
        map_vert.remove(vert);
        
        ArrayList<Edge<String,Integer>> to_del = new ArrayList<Edge<String,Integer>>(20);
        
        for (Edge<String,Integer> ed : map_edg.keySet())
            if (ed.contains(vert))
                to_del.add(ed);
        
        for (Edge<String,Integer> ed : to_del)
            map_edg.remove(ed);
        
        repaint();
    }
    
    /**
     * Adiciona uma nova aresta ao Grafo.
     * @param a Informação que o vértice inicial desta aresta guarda.
     * @param b Informação que o vértice terminal desta aresta guarda.
     * @param directed Indica se esta aresta é direcionada ou não.
     * @param info A informação que será guardada pela aresta.
     */
    public void addEdge(String a, String b, boolean directed, Integer info) {
        Vertex<String> av = graph.getVertex(a);
        Vertex<String> bv = graph.getVertex(b);
        
        if (av == null || bv == null) return;
        
        VertexView<String> avi = map_vert.get(av);
        VertexView<String> bvi = map_vert.get(bv);
        
        Edge<String,Integer> edg = new Edge<String,Integer>(av,bv,directed,info);
        
        graph.insertEdge(edg);
        map_edg.put(edg,new EdgeView<String,Integer>(avi,bvi,edg,theme));
    }
    
    /**
     * Remove do Grafo a aresta representada por uma EdgeView.
     */
    public void removeEdge(EdgeView<String,Integer> edge_view) {
        graph.removeEdge(edge_view.getEdge());
        map_edg.remove(edge_view.getEdge());
        
        repaint();
    }
    
    /**
     * Deseleciona todos os vértices do grafo e limpa
     * a informação que é desenhada ao lado do vértice.
     */
    public void deselectAll() {
        for (VertexView<String> v : getVertexViewSet()) {
            v.setSelected(false);
            
            String inf = v.getViewInfo();
            if (inf.lastIndexOf('[') != -1) {
                v.setViewInfo(inf.substring(0, inf.lastIndexOf('[')).trim());
            }
        }
        
        for (EdgeView<String,Integer> ed : getEdgeViewSet())
            ed.setSelected(false);
    }
    
    /**
     * Otimiza um Graphics2D em questões de
     * qualidade de renderização.
     */
    private void setOptimized(Graphics2D surf) {
        surf.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        surf.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        surf.setRenderingHint(
                RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);
        surf.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        setDoubleBuffered(true);
        setOpaque(true);
    }
    
    /**
     * Modifica o grafo que este View representa.
     * Inicializa as estruturas internas e, caso o grafo
     * não esteja vazio, mostra seus elementos na tela
     * em uma forma circular em torno do centro da janela.
     */
    public void setGraph(Graph<String,Integer> g) {
        graph = g;
        map_vert = new HashMap<Vertex<String>,VertexView<String>>(g.getNumVertex()+25);
        map_edg = new HashMap<Edge<String,Integer>,EdgeView<String,Integer>>(g.getNumEdges()+25);
        
        if (g.getNumVertex() == 0)
            return;
        
        Rectangle2D.Double ret = new Rectangle2D.Double(
                0.0,0.0,getBounds().getWidth(),getBounds().getHeight()
        );

        double radius = Math.max(Math.min(ret.getWidth(),ret.getHeight())-50,MIN_GRAPH_RADIUS);
        Point2D.Double center = new Point2D.Double(ret.getCenterX(),ret.getCenterY());
        
        int i = 0, n = g.getNumVertex();

        for(Vertex<String> v : g.getVertexSet()) {
            VertexView<String> v_view = new VertexView<String>(
                (center.getX() + radius*Math.cos(i*2*Math.PI/n)),
                (center.getY() + radius*Math.sin(i*2*Math.PI/n)),
                v, theme
            );
            map_vert.put(v,v_view);
            ++i;
        }
        
        for(Edge<String,Integer> e : g.getEdgeSet()) {
            EdgeView<String,Integer> e_view = new EdgeView<String,Integer>(
                map_vert.get(e.getStart()),
                map_vert.get(e.getEnd()),
                e, theme
            );
            map_edg.put(e,e_view);
        }
    }
    
    /**
     * Desenha este grafo na superfície representada por g.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D surface = (Graphics2D)g;
        setOptimized(surface);
        
        Color old_background = surface.getBackground();
        surface.setBackground(theme.getBackgroundColor());
        surface.clearRect(0,0,getWidth(),getHeight());
        surface.setBackground(old_background);
        
        for (EdgeView<String,Integer> e : getEdgeViewSet())
            e.paint(surface);
        
        for (VertexView<String> v : getVertexViewSet())
            v.paint(surface);
    }
    
    /**
     * Responde à um evento do tipo mouseMoved.
     */
    public void mouseMoved(MouseEvent evt) {
        if (hoover_vert != null && hoover_vert != sel_vert) {
            hoover_vert.setSelected(false);
            hoover_vert = null;
            repaint();
        }
        else if (hoover_edge != null && hoover_edge != sel_edge) {
            hoover_edge.setSelected(false);
            hoover_edge = null;
            repaint();
        }
        
        VertexView<String> vert_view = onVertex(evt);
        EdgeView<String,Integer> edge_view = onEdge(evt);
        
        if (vert_view != null && !vert_view.isSelected() && vert_view != sel_vert) {
            hoover_vert = vert_view;
            hoover_vert.setSelected(true);
            repaint();
        }
        else if (edge_view != null && !edge_view.isSelected() && edge_view != sel_edge) {
            hoover_edge = edge_view;
            hoover_edge.setSelected(true);
            repaint();
        }
    }
    
    /**
     * Responde à um evento do tipo mousePressed.
     */
    public void mousePressed(MouseEvent evt) {
        VertexView<String> vert_view = onVertex(evt);
        EdgeView<String,Integer> edge_view = onEdge(evt);
        
        if (vert_view != null)
            drag_vert = vert_view;
        else if (edge_view != null)
            drag_edge = edge_view;
    }
    
    /**
     * Responde à um evento do tipo mouseReleased.
     */
    public void mouseReleased() {
        drag_vert = null;
        drag_edge = null;
    }
    
    /**
     * Responde à um evento do tipo mouseDragged.
     */
    public void mouseDragged(MouseEvent evt) {
        if (drag_vert != null) {
            double dx = evt.getX() - drag_vert.getX();
            double dy = evt.getY() - drag_vert.getY();
            drag_vert.setLocation(evt.getX(), evt.getY());
            
            for (EdgeView<String,Integer> edg : getEdgeViewSet())
                if (edg.getEdge().contains(drag_vert.getVertex()))
                    edg.setControlPoint(
                        edg.getControlX()+dx/3f,
                        edg.getControlY()+dy/3f);
            
            repaint();
        }
        else if (drag_edge != null) {
            drag_edge.setControlPoint(evt.getX(), evt.getY());
            repaint();
        }
    }
    
    /**
     * Recebe um MouseEvent e indica se o mouse, no momento
     * em que o evento foi gerado, estava sobre algum VertexView.
     * Caso sim, retorna este VertexView. Caso contrário, retorna
     * null.
     */
    private VertexView<String> onVertex(MouseEvent evt) {
        Point2D click = evt.getPoint();
        VertexView<String> vert_click = null;
        
        for (VertexView<String> vert_view : getVertexViewSet())
            if (click.distance(vert_view.getLocation()) <= vert_view.getRadius()) {
                vert_click = vert_view;
                break;
            }

        return vert_click;
    }

    /**
     * Recebe um MouseEvent e indica se o mouse, no momento
     * em que o evento foi gerado, estava sobre algum EdgeView.
     * Caso sim, retorna este EdgeView. Caso contrário, retorna
     * null.
     */
    public EdgeView<String,Integer> onEdge(MouseEvent evt) {
        Point2D click = evt.getPoint();
        EdgeView<String,Integer> edge_click = null;
        
        double ew = theme.getEdgeWidth();
        Rectangle2D.Double click_ret = new Rectangle2D.Double(
            click.getX()-ew/2f,click.getY()-ew/2f,ew,ew
        );
        
        for (EdgeView<String,Integer> ed_view : getEdgeViewSet())
            try {
                if (ed_view.getEdgeShape().intersects(click_ret)) {
                    edge_click = ed_view;
                    break;
                }
            } catch (NullPointerException e) {
                continue;
            }
        
        return edge_click;
    }
    
    /**
     * Responde à um evento do tipo mouseClicked e
     * lida com a opção atual selecionada pelo usuário
     * na interface do programa.
     */
    public void mouseClicked(MouseEvent evt, ArrayList<JToggleButton> buttons, GraphWindow window) {
        VertexView<String> vert_click = onVertex(evt), vert_hack = null;
        EdgeView<String,Integer> edge_click = onEdge(evt), edge_hack = null;
        boolean any = false, erase_sel_vert = false, erase_sel_edge = false;
        
        sel_vert = null;
        sel_edge = null;
        
        for(JToggleButton b : buttons) {
            if (!b.isSelected()) continue;
            any = true;
            
            if (b == window.selVeButton) {
                if (vert_click != null) {
                    vert_click.setSelected(true);
                    sel_vert = vert_click;
                    sel_edge = null;
                    hoover_edge = null;
                    hoover_vert = null;
                    if (evt.getButton() == MouseEvent.BUTTON1) {
                        vert_hack = vert_click;
                        erase_sel_vert = true;
                        erase_sel_edge = true;
                    }
                    
                    window.postMessage("Vertex " + vert_click.getVertex() + " was selected.");
                }
                else {
                    erase_sel_vert = true;
                    erase_sel_edge = true;
                }
            }
            else if (b == window.selEdButton) {
                if (edge_click != null) {
                    edge_click.setSelected(true);
                    sel_edge = edge_click;
                    sel_vert = null;
                    hoover_edge = null;
                    hoover_vert = null;                            
                    if (evt.getButton() == MouseEvent.BUTTON1) {
                        erase_sel_edge = true;
                        erase_sel_vert = true;
                        edge_hack = edge_click;
                    }
                    
                    window.postMessage(edge_click.getEdge() + " was selected.");
                }
                else {
                    erase_sel_edge = true;
                    erase_sel_vert = true;
                }
            }
            else if (b == window.selVeEdButton) {
                if (vert_click != null) {
                    vert_click.setSelected(true);
                    sel_vert = vert_click;
                    sel_edge = null;
                    hoover_edge = null;
                    hoover_vert = null;
                    if (evt.getButton() == MouseEvent.BUTTON1) {
                        erase_sel_vert = true;
                        erase_sel_edge = true;
                        vert_hack = vert_click;
                    }
                    
                    window.postMessage("Vertex<" + vert_click.getVertex() + "> was selected.");
                }
                else if (edge_click != null) {
                    edge_click.setSelected(true);
                    sel_edge = edge_click;
                    sel_vert = null;
                    hoover_edge = null;
                    hoover_vert = null;
                    if (evt.getButton() == MouseEvent.BUTTON1) {
                        erase_sel_edge = true;
                        erase_sel_vert = true;
                        edge_hack = edge_click;
                    }
                    
                    window.postMessage(edge_click.getEdge() + " was selected.");
                }
                else {
                    erase_sel_vert = true;
                    erase_sel_edge = true;
                }
            }
            else if (b == window.adVeButton) {
                if (vert_click == null) {
                    String info = JOptionPane.showInputDialog("Type the info of the new vertex:");
                    
                    if (info == null) break;
                    
                    if (graph.contains(info)) {
                        JOptionPane.showMessageDialog(
                            this,"There already exists a vertex with this same info.",
                            "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                        );
                    }
                    else {
                        addVertex(evt.getX(), evt.getY(), info);
                        erase_sel_vert = true;
                        erase_sel_edge = true;
                        
                        window.postMessage("Vertex<" + info + "> was added.");
                    }    
                }
                else {
                    JOptionPane.showMessageDialog(
                        this,"Can't create new vertex on top of another one.",
                        "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            else if (b == window.reVeButton) {
                if (vert_click != null) {
                    removeVertex(vert_click.getVertex().getData());
                    erase_sel_vert = true;
                    erase_sel_edge = true;
                    
                    window.postMessage("Vertex<" + vert_click.getVertex() + "> was removed.");
                }
                else {
                    JOptionPane.showMessageDialog(
                        this,"In order to remove, click on a valid vertex.",
                        "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                    );     
                }
            }
            else if (b == window.reEdButton) {
                if (edge_click != null) {
                    removeEdge(edge_click);
                    erase_sel_edge = true;
                    erase_sel_vert = true;
                    
                    window.postMessage(edge_click.getEdge() + " was removed.");
                }
                else {
                    JOptionPane.showMessageDialog(
                        this,"In order to remove, click on a valid edge.",
                        "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                    );                     
                }
            }
            else if (b == window.adEdButton) {
                if (ad_ed_step == 0) {
                    if (vert_click != null) {
                        ad_ed_step = 1;
                        ad_ed_tmp = vert_click;
                        erase_sel_vert = true;
                        erase_sel_edge = true;
                        
                        window.postMessage("Selected Vertex<" + vert_click.getVertex() + "> as first.");
                    }
                    else {
                        JOptionPane.showMessageDialog(
                            this,"In order to add an edge, click on a valid first vertex.",
                            "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                        );                      
                    }
                }
                else if (ad_ed_step == 1) {
                    if (vert_click != null && vert_click != ad_ed_tmp) {
                        window.postMessage("Selected Vertex<" + vert_click.getVertex() + "> as second.");
                        
                        String info = JOptionPane.showInputDialog("Type the info of the new edge:");
                        if (info == null || info.isEmpty()) { ad_ed_step = 0; break; }
                        
                        int opt = JOptionPane.showConfirmDialog(
                            this, "Is the edge directed?",
                            "GraphXT - " + window.getTitle(),JOptionPane.YES_NO_OPTION
                        );
                        
                        boolean dir = (opt == JOptionPane.YES_OPTION);
                        
                        addEdge(ad_ed_tmp.getVertex().getData(),vert_click.getVertex().getData(),dir,Integer.valueOf(info));
                        
                        ad_ed_step = 0;
                        erase_sel_vert = true;
                        erase_sel_edge = true;
                    }
                    else {
                        JOptionPane.showMessageDialog(
                            this,"In order to add an edge, click on a valid second vertex.",
                            "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                        );                      
                    }
                }
                break;
            }
            else if (b == window.lbVeButton) {
                if (vert_click != null) {
                   erase_sel_vert = true;
                   erase_sel_edge = true;
                   
                   String tmp = JOptionPane.showInputDialog(
                        "Type the new info of the vertex:",
                        vert_click.getVertex().toString()
                   );
                   
                   if (tmp == null || tmp.isEmpty()) break;
                   
                   vert_click.getVertex().setData(tmp);
                   vert_click.setViewInfo(tmp);
                }
                else {
                    JOptionPane.showMessageDialog(
                        this,"Click on a valid vertex to change it's info.",
                        "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                    );                       
                }
            }
            else if (b == window.lbEdButton) {
                if (edge_click != null) {
                    erase_sel_vert = true;
                    erase_sel_edge = true;
         
                    String tmp = JOptionPane.showInputDialog(
                        "Type the new info of the edge:",
                        edge_click.getEdge().getData()
                    );
                    
                    if (tmp == null || tmp.isEmpty()) break;
                    
                    edge_click.getEdge().setData(Integer.valueOf(tmp));
                }
                else {
                    JOptionPane.showMessageDialog(
                        this,"Click on a valid edge to change it's info.",
                        "GraphXT - " + window.getTitle(), JOptionPane.ERROR_MESSAGE
                    );                      
                }
            }
            
            ad_ed_step = 0;
            break;
        }
        
        if (!any) {
            // TODO: o quê acontece quando clica
            // sem nenhum botão selecionado?
            // GUESS: Nada. Lida no MouseDragged?
        }
        
        if (erase_sel_vert) {
            for (VertexView<String> v : getVertexViewSet())
                if (v != vert_hack) v.setSelected(false);
        }
        
        if (erase_sel_edge) {
            for (EdgeView<String,Integer> ed : getEdgeViewSet())
                if (ed != edge_hack) ed.setSelected(false);
        }
        
        repaint();
    }
    
    private int ad_ed_step;
    private Graph<String,Integer> graph;
    private GraphTheme theme;
    private VertexView<String> hoover_vert, sel_vert, ad_ed_tmp, drag_vert;
    private EdgeView<String,Integer> hoover_edge, sel_edge, drag_edge;
    private HashMap<Vertex<String>,VertexView<String>> map_vert;
    private HashMap<Edge<String,Integer>,EdgeView<String,Integer>> map_edg;
    
    private static final double MIN_GRAPH_RADIUS = 30.0;
}
