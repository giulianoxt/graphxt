/*
 * VertexView.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.view;

import graphxt.model.Vertex;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * VertexView<T>
 * 
 * Classe View relacionado à um Vertex<T>.
 * Esta classe representa uma extensão lógica da
 * classe Vertex<T>. Internamente, armazena um vértice
 * e contêm todas as informações necessárias para mapear
 * o modelo do vértice (Vertex<T>) para a tela e a iteração
 * com o usuário. 
 * Todas as opções referentes à visualização de vértices
 * estão contidas diretamente ou indiretamente aqui.
 * 
 * @author Giuliano Vilela
 */
public class VertexView<T> {
    /**
     * Construtor desabilitado.
     */
    private VertexView() { }
    
    /**
     * Cria um novo VertexView, relacionado ao vértice vert
     * e pegando informações do tema theme.
     * O vértice será vizualizado na posição (0,0).
     */
    public VertexView(Vertex<T> vert, GraphTheme theme) {
        this(0,0,vert,theme);
    }
    
    /**
     * Cria um novo VertexView, relacionado ao vértice vert
     * e pegando informações do tema theme.
     * O vértice será visualizado na posição (x,y).
     */
    public VertexView(double x, double y, Vertex<T> vert, GraphTheme theme) {
        setLocation(x,y);
        setVertex(vert);
        setGraphTheme(theme);
        setVisible(true);
    }
    
    /**
     * Modifica a posição do vértice na janela de visualização.
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        this.rad = 0.0;
    }
    
    /**
     * Modifica o vértice para o qual este
     * View pegará as informações.
     */
    public void setVertex(Vertex<T> vert) {
        vertex = vert;
        if (vert != null)
            view_info = vert.getData().toString();
    }

    /**
     * Modifica o tema utilizado pelo View.
     */
    public void setGraphTheme(GraphTheme theme) {
        gtheme = theme;
    }
    
    /**
     * Modifica o estado de seleção do vértice.
     */
    public void setSelected(boolean selected) {
        sel = selected;
    }
    
    /**
     * Modifica o estado de visibilidade deste vértice.
     * Este parâmetro indica se o vértice será desenhado ou não.
     */
    public void setVisible(boolean v) {
        vis = v;
    }
    
    /**
     * Indica se o vértice é visível.
     */
    public boolean isVisible() {
        return vis;
    }
    
    /**
     * Retorna a coordenada x do vértice.
     */
    public double getX() {
        return x;
    }
    
    /**
     * Retorna a coordenada y do vértice.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Retorna um Point2D representando a
     * posição atual do centro do vértice.
     */
    public Point2D.Double getLocation() {
        return (new Point2D.Double(x,y));
    }
    
    /**
     * Retorna o raio do círculo que representa o vértice.
     * 
     * Por questões de implementação, esta função só deve ser
     * chamada depois que o vértice tiver sido desenhado pelo
     * menos uma vez na tela. O valor retornado por essa função
     * depende do tema utilizado e da informação guardada pelo vértice.
     */
    public double getRadius() {
        return rad;
    }
    
    /**
     * Retorna o Vertex correspondente à este view.
     */
    public Vertex<T> getVertex() {
        return vertex;
    }
    
    /**
     * Retorna o tema utilizado pelo View.
     */
    public GraphTheme getGraphTheme() {
        return gtheme;
    }
    
    /**
     * Indica se o vértice está selecionado.
     */
    public boolean isSelected() {
        return sel;
    }
    
    /**
     * Modifica a informação que é de fato impressa na tela.
     */
    public void setViewInfo(String s) {
        view_info = s;
    }
    
    /**
     * Retorna a String que é impressa na tela, junto
     * à este vértice.
     */
    public String getViewInfo() {
        return view_info;
    }
    
    /**
     * Desenha o vértice na tela, na surface
     * determinada pelo Graphics2D g.
     */
    public void paint(Graphics g) {
        if (!vis) return;
        
        Graphics2D surface = (Graphics2D)g;
        
        Stroke old_stroke = surface.getStroke();
        Paint old_paint = surface.getPaint();
        Font old_font = surface.getFont();

        FontMetrics metrics = surface.getFontMetrics(gtheme.getVertexFont());
        Rectangle2D ret = metrics.getStringBounds(vertex.toString(), surface);
        double diameter = Math.max(ret.getHeight(),ret.getWidth())+3f;
        
        if (diameter < gtheme.getMinVertexDiameter())
            diameter = gtheme.getMinVertexDiameter();
        else if (diameter > gtheme.getMaxVertexDiameter())
            diameter = gtheme.getMaxVertexDiameter();
        
        if (sel) {
            double glow_diameter = diameter + gtheme.getSelectedVertexGlowSize();
            
            RadialGradientPaint paint = new RadialGradientPaint(
                    new Point2D.Double(x, y),
                    (float)diameter,
                    new float[] { 0.10f, 1f },
                    new Color[] {
                        gtheme.getSelectedVertexGlowColor(),
                        gtheme.getBackgroundColor()
                    }
            );
            
            surface.setStroke(new BasicStroke());
            surface.setPaint(paint);
            surface.fill(new Ellipse2D.Double(x-glow_diameter/2,y-glow_diameter/2,glow_diameter,glow_diameter));
        }
        
        if (sel)
            surface.setPaint(gtheme.getSelectedVertexFillColor());
        else
            surface.setPaint(gtheme.getVertexFillColor());
        surface.setStroke(new BasicStroke());
        surface.fill(new Ellipse2D.Double(x-diameter/2,y-diameter/2,diameter,diameter));

        diameter += gtheme.getVertexStrokeWidth();
        rad = diameter/2;
        
        surface.setPaint(gtheme.getVertexFontColor());
        surface.setFont(gtheme.getVertexFont());
       
        GlyphVector glyphs = gtheme.getVertexFont().createGlyphVector(
            surface.getFontRenderContext(), view_info
        );
        surface.drawGlyphVector(glyphs,
                (float)(x-ret.getWidth()/2f), (float)(y+ret.getHeight()/2f-2f)
        );
        
        surface.setStroke(
            new BasicStroke(
                (float)gtheme.getVertexStrokeWidth(),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
            )
        );
        
        surface.setPaint(gtheme.getVertexStrokeColor());
        surface.draw(new Ellipse2D.Double(x-diameter/2,y-diameter/2,diameter,diameter));
        
        surface.setStroke(old_stroke);
        surface.setPaint(old_paint);
        surface.setFont(old_font);
    }
    
    private double x, y, rad;
    private boolean sel, vis;
    private Vertex<T> vertex;
    private GraphTheme gtheme;
    private String view_info;
}
