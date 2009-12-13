/*
 * EdgeView.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.view;

import graphxt.model.Edge;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * EdgeView<T,V>
 * 
 * Classe View relacionado à um Edge<T,V>.
 * Esta classe representa uma extensão lógica da
 * classe Edge<T,V>. Internamente, armazena uma aresta
 * e contêm todas as informações necessárias para mapear
 * o modelo da aresta (Edge<T,V>) para a tela e a iteração
 * com o usuário. 
 * Todas as opções referentes à visualização de arestas
 * estão contidas diretamente ou indiretamente aqui.
 * 
 * @author Giuliano Vilela
 */
public class EdgeView<T,V> {
    /**
     * Construtor bloqueado.
     */
    private EdgeView() { }
    
    /**
     * Cria um novo View para uma aresta edge.
     * O view fica relacionado à dois VertexView start e end,
     * que contêm informações sobre aonde os vértices ligados
     * por esta aresta estão. Além disso, também pega informações
     * de um GraphTheme (informações visuais, usadas no método paint).
     */
    public EdgeView(VertexView<T> st, VertexView<T> end, Edge<T,V> edge, GraphTheme theme) {
        setStartVertexView(st);
        setEndVertexView(end);
        setEdge(edge);
        setGraphTheme(theme);
        setSelected(false);
        setControlPoint(st.getX(),st.getY());
        setVisible(true);
        setEdgeInfo(null);
    }
    
    /**
     * Modifica o VertexView de início desta aresta.
     */
    public void setStartVertexView(VertexView<T> st) {
        st_view = st;
    }
    
    /**
     * Modifica o VertexView de término desta aresta.
     */
    public void setEndVertexView(VertexView<T> end) {
        end_view = end;
    }
    
    /**
     * Modifica a aresta que este View representa.
     */
    public void setEdge(Edge<T,V> ed) {
        edge = ed;
    }
    
    /**
     * Modifica o GraphTheme utilizado por este View.
     */
    public void setGraphTheme(GraphTheme theme) {
        gtheme = theme;
    }
    
    /**
     * Modifica o estado de seleção desta aresta.
     */
    public void setSelected(boolean s) {
        sel = s;
    }
    
    /**
     * Modifica o estado de visibilidade desta aresta.
     */
    public void setVisible(boolean v) {
        vis = v;
    }
    
    /**
     * Indica se esta aresta é visível, i.e.,
     * está sendo desenhada no método paint.
     */
    public boolean isVisible() {
        return vis;
    }
    
    /**
     * Modifica o ponto de controle desta aresta.
     * O ponto de controle é um ponto no user-space,
     * de referência, que indica como a curva que 
     * representa esta aresta será desenhada.
     */
    public void setControlPoint(double x, double y) {
        ctr_x = x;
        ctr_y = y;
    }
    
    /**
     * Retorna o VertexView que representa o
     * vértice de início desta aresta.
     */
    public VertexView<T> getStartVertexView() {
        return st_view;
    }

    /**
     * Retorna o VertexView que representa o
     * vértice de término desta aresta.
     */
    public VertexView<T> getEndVertexView() {
        return end_view;
    }
    
    /**
     * Retorna a Edge<T,V> que modela este View.
     */
    public Edge<T,V> getEdge() {
        return edge;
    }
    
    /**
     * Retorna o tema utilizado por este View.
     */
    public GraphTheme getGraphTheme() {
        return gtheme;
    }
    
    /**
     * Indica se esta aresta está selecionada.
     */
    public boolean isSelected() {
        return sel;
    }
    
    /**
     * Retorna a coordenada x do ponto de controle da aresta.
     */
    public double getControlX() {
        return ctr_x;
    }
    
    /**
     * Retorna a coordenada y do ponto de controle da aresta.
     */
    public double getControlY() {
        return ctr_y;
    }
    
    /**
     * Retorna um Shape que representa a área que seria
     * obtida caso fechássemos a curva da aresta e tomassemos
     * um polígono convexo para extrair a área. Serve para
     * detectar colisões com esta aresta.
     * Este método não deve ser chamado antes que a aresta tenha sido
     * desenhada pelo menos uma vez na tela.
     */
    public Shape getEdgeShape() {
        return edge_shape;
    }

    public void setEdgeInfo(String info) {
      edge_info = info;
    }

    public String getEdgeInfo() {
      return edge_info;
    }

    /**
     * Desenha a aresta na superfície representada por g.
     */
    public void paint(Graphics g) {
        if (!vis) return;
        
        Graphics2D surface = (Graphics2D)g;
        
        Stroke old_stroke = surface.getStroke();
        Paint old_paint = surface.getPaint();
        Font old_font = surface.getFont();
        
        surface.setStroke(
            new BasicStroke(
                (float)gtheme.getEdgeWidth(),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
            )
        );
        
        if (sel)
            surface.setPaint(gtheme.getSelectedEdgeColor());
        else
            surface.setPaint(gtheme.getEdgeColor());
        
        Shape s = edge_shape = new QuadCurve2D.Double(
            st_view.getX(), st_view.getY(),
            ctr_x, ctr_y,
            end_view.getX(), end_view.getY()
        );
        surface.draw(s);
        
        surface.setFont(gtheme.getEdgeFont());
        surface.setPaint(gtheme.getEdgeFontColor());

        String str = edge_info != null ? edge_info : edge.getData().toString();
        
        Rectangle2D ret1, ret2, ret3, ret4, result = null, orig = null;
        
        FontMetrics metrics = surface.getFontMetrics(gtheme.getEdgeFont());
        orig = ret1 = metrics.getStringBounds(str, surface);
        ret1.setRect(s.getBounds2D().getCenterX(),
                    s.getBounds2D().getCenterY(),
                    ret1.getWidth()+EDGE_DATA_INC,
                    ret1.getHeight()+EDGE_DATA_INC);
        ret2 = (Rectangle2D)ret1.clone();
        ret3 = (Rectangle2D)ret1.clone();
        ret4 = (Rectangle2D)ret1.clone();
        
        ArrayList<Rectangle2D> res = new ArrayList<Rectangle2D>(10);
        
        while (true) {
            res.clear();
            
            if (!s.intersects(ret1)) {
                res.add(ret1);
            }
            if (!s.intersects(ret2)) {
                res.add(ret2);
            }
            if (!s.intersects(ret3)) {
                res.add(ret3);
            }
            if (!s.intersects(ret4)) {
                res.add(ret4);
            }
            
            if (res.size() > 0) {
                double min = 10000f;
                Point2D.Double edg_ctr = new Point2D.Double(ctr_x, ctr_y);
                
                for (Rectangle2D ret : res) {
                    Point2D.Double ret_center = new Point2D.Double(ret.getCenterX(),ret.getCenterY());
                    
                    if (edg_ctr.distance(ret_center) < min) {
                        min = edg_ctr.distance(ret_center);
                        result = ret;
                    }
                }
                
                break;
            }
            
            ret1.setRect(
                ret1.getX()-EDGE_DATA_INC,
                ret1.getY()-EDGE_DATA_INC,
                ret1.getWidth(),ret1.getHeight()
            );
                
            ret2.setRect(
                ret2.getX()+EDGE_DATA_INC,
                ret2.getY()+EDGE_DATA_INC,
                ret2.getWidth(),ret2.getHeight()
            );
            
            ret3.setRect(
                ret3.getX()+EDGE_DATA_INC,
                ret3.getY()-EDGE_DATA_INC,
                ret3.getWidth(),ret3.getHeight()
            );
            
            ret4.setRect(
                ret4.getX()-EDGE_DATA_INC,
                ret4.getY()+EDGE_DATA_INC,
                ret4.getWidth(),ret4.getHeight()
            );
        }
        
        surface.drawString(str,
                (float)(result.getCenterX()-orig.getWidth()/2),
                (float)result.getCenterY()
        );
        
        try {
            if (edge.isDirected()) {
                double x1 = ctr_x, y1 = ctr_y;
                double x2 = end_view.getX(), y2 = end_view.getY();
                double w1 = end_view.getRadius()+gtheme.getSelectedVertexGlowSize()+6f;
                double dist = (new Point2D.Double(x1,y1)).distance(new Point2D.Double(x2,y2));
                
                double x = x2 - (w1 * (x2 - x1)) / dist;
                double y = y2 - (w1 * (y2 - y1)) / dist;
                
                surface.setPaint(Color.WHITE); // TODO: Botar essa informação no theme
                surface.draw(new Line2D.Double(x,y,x2,y2));
            }
        } catch (NullPointerException e) {
            // pode cair aqui no começo, sem problemas!
        }
        
        surface.setPaint(old_paint);
        
        surface.setStroke(old_stroke);
        surface.setPaint(old_paint);
        surface.setFont(old_font);
    }
    
    private boolean sel, vis;
    private double ctr_x, ctr_y;
    private Shape edge_shape;
    private Edge<T,V> edge;
    private GraphTheme gtheme;
    private VertexView<T> st_view, end_view;

    private String edge_info;
    
    private static final double EDGE_DATA_INC = 10f;
}
