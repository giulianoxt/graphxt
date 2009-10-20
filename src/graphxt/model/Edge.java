/*
 * Edge.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.model;

/**
 * Edge<T,V>
 * 
 * Classe que modela uma aresta de um grafo.
 * Esta aresta conecta nós do tipo Vertex<T>
 * contendo uma informação do tipo V.
 * 
 * @author Giuliano Vilela
 */
public class Edge<T,V> {
    /**
     * Cria uma aresta inválida.
     */
    public Edge() {
        this(null,null);
    }
    
    /**
     * Cria uma aresta não direcionada, conectando x e y.
     * Armazena como informação uma referência null.
     * 
     * @param x Primeiro vértice terminal
     * @param y Segundo vértice terminal
     */
    public Edge(Vertex<T> x, Vertex<T> y) {
        this(x,y,false);
    }
    
    /**
     * Cria uma aresta conectando x e y, com informação null.
     * 
     * @param x Primeiro vértice terminal
     * @param y Segundo vértice terminal
     * @param directed Indica se a aresta é direcionada
     */
    public Edge(Vertex<T> x, Vertex<T> y, boolean directed) {
        this(x,y,directed,null);
    }
    
    /**
     * Cria uma aresta conectando x e y, com direcionament
     * e informação indicados pelos parâmetros.
     * 
     * @param x Primeiro vértice terminal
     * @param y Segundo vértice terminal
     * @param directed Indica se a aresta é direcionada
     * @param data Indica a informação que será guardada na aresta
     */
    public Edge(Vertex<T> x, Vertex<T> y, boolean directed, V data) {
        setStart(x);
        setEnd(y);
        setDirected(directed);
        setData(data);
    }
    
    /**
     * Modifica o vértice de início desta aresta.
     * 
     * @param x Novo vértice de início
     */
    public void setStart(Vertex<T> x) {
        this.x = x;
    }
    
    /**
     * Modifica o vértice de término desta aresta.
     * 
     * @param y Novo vértice de término.
     */
    public void setEnd(Vertex<T> y) {
        this.y = y;
    }
    
    /**
     * Modifica o direcionamento desta aresta
     * 
     * @param dir Indica se a aresta é direcionada ou não
     */
    public void setDirected(boolean dir) {
        this.directed = dir;
    }
    
    /**
     * Modifica a informação guardada na aresta.
     * 
     * @param d Nova informação à ser guardada.
     */
    public void setData(V d) {
        data = d;
    }
    
    /**
     * Retorna o vértice de início desta aresta.
     */
    public Vertex<T> getStart() {
        return x;
    }
    
    /**
     * Retorna o vértice terminal desta aresta.
     */
    public Vertex<T> getEnd() {
        return y;
    }
    
    /**
     * Indica se esta aresta é direcionada ou não.
     */
    public boolean isDirected() {
        return directed;
    }
    
    /**
     * Retorna a informação guardada nesta aresta.
     */
    public V getData() {
        return data;
    }
    
    /**
     * Indica se um vértice é um nó terminal desta aresta.
     * @param nd Vértice à ser testado.
     * @return Booleano indicando se nd é um nó terminal desta aresta.
     */
    public boolean contains(Vertex<T> nd) {
        return (nd.getData().equals(x.getData()) || nd.getData().equals(y.getData()));
    }
    
    /**
     * Retorna um código hash, representando esta aresta.
     * Este código hash é tal que honra o contrato geral
     * imposto por Object.hashCode.
     * 
     * O código hash de uma Edge depende somente dos seus
     * dois vértices terminais e da informação que a aresta
     * guarda.
     */
    @Override
    public int hashCode() {
        return (x.getData().hashCode() + y.getData().hashCode() + data.hashCode());
    }
    
    /**
     * Retorna uma representação textual desta aresta, na forma:
     * "Edge<x,d,y>", onde x e y são os nós terminais desta aresta e
     * d é a informação guardada nesta aresta.
     */
    @Override
    public String toString() {
        return ("Edge<" + x.toString() + "," + data.toString() + "," + y.toString() + ">");
    }
    
    /**
     * Indica se esta aresta é a mesma que uma outra.
     */
    public boolean equals(Edge<T,V> edg) {
        return (x.getData().equals(edg.x.getData()) && y.getData().equals(edg.y.getData()) &&
                data.equals(edg.data) && directed == edg.directed
        );
    }
    
    /**
     * Inverte a ordem desta aresta,
     * trocando os nós terminais de lugar.
     * Em geral, só terá efeito em arestas direcionadas.
     */
    public void reverse() {
        Vertex<T> tmp = x;
        x = y;
        y = tmp;
    }
    
    /**
     * Retorna o vértice oposto à v nesta aresta.
     */
    public Vertex<T> getOposite(Vertex<T> v) {
        return ((v.getData().equals(x.getData())) ? y : x);
    }
    
    /**
     * Informação guardada pela aresta.
     */
    private V data;
    /**
     * Nós terminais da aresta.
     */
    private Vertex<T> x, y;
    /**
     * Indica se a aresta é direcionada.
     */
    private boolean directed;
}
