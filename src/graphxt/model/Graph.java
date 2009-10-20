/*
 * Graph.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Graph<T,V>
 * 
 * Classe principal do modelo interno da aplicação.
 * Representa um Grafo, contendo um conjunto de vértices
 * Vert, contendo elementos do tipo Vertex<T>, e um conjunto
 * de arestas Edge, contendo elementos do tipo Edge<T,V>.
 * 
 * @author Giuliano Vilela
 */
public class Graph<T,V> {
    /**
     * Cria um novo Grafo, com capacidade
     * interna para 36 vértices.
     */
    public Graph() {
        this(36);
    }
    
    /**
     * Cria um novo grafo com uma certa
     * capacidade interna.
     * 
     * @param capacity Capacidade interna do grafo.
     */
    public Graph(int capacity) {
        adj = new HashMap<Vertex<T>,HashSet<Edge<T,V>>>(capacity);
    }
    
    /**
     * Insere o vértice vert neste Grafo.
     * Caso vert já estiver no grafo, não haverá
     * nenhuma mudança.
     */
    public void insertVertex(Vertex<T> vert) {
        if (adj.containsKey(vert))
            return;
        
        adj.put(vert, new HashSet<Edge<T,V>>());
    }    

    /**
     * Insere a aresta edge no Grafo.
     * 
     * Caso algum dos vértices terminais de edge
     * não pertençam ao grafo, estes serão adicionados.
     */
    public void insertEdge(Edge<T,V> edge) {
        insertVertex(edge.getStart());
        insertVertex(edge.getEnd());
        
        adj.get(edge.getStart()).add(edge);
        adj.get(edge.getEnd()).add(edge);
    }
    
    /**
     * Conecta o vértice x ao vértice y,
     * com uma aresta não direcionada guardando
     * um dado null.
     */
    public void connect(Vertex<T> x, Vertex<T> y) {
        connect(x,y,false);
    }
    
    /**
     * Conecta o vértice x ao vértice y,
     * guardando um dado null.
     * 
     * @param x Vértice inicial
     * @param y Vértice final
     * @param directed Indica o direcionamento da nova aresta
     */
    public void connect(Vertex<T> x, Vertex<T> y, boolean directed) {
        connect(x,y,directed,null);
    }

    /**
     * Conecta o vértice x ao vértice y,
     * 
     * @param x Vértice inicial
     * @param y Vértice final
     * @param directed Indica o direcionamento da nova aresta
     * @param info Dado que será guardado na aresta que conecta os dois nós.
     */
    public void connect(Vertex<T> x, Vertex<T> y, boolean directed, V info) {
        insertEdge(new Edge<T,V>(x,y,directed,info));
    }
    
    /**
     * Remove o vértice vert deste grafo,
     * junto com todas as arestas que tinham
     * vert como um de seus vértice terminais.
     */
    public void removeVertex(Vertex<T> vert) {
        if (!contains(vert))
            return;
        
        adj.remove(vert);
        
        for (Vertex<T> vt : adj.keySet()) {
            Iterator<Edge<T,V>> it = adj.get(vt).iterator();
            
            while (it.hasNext()) {
                if (it.next().contains(vert))
                    it.remove();
            }
        }
    }
    
    /**
     * Remove a aresta edge do grafo.
     */
    public void removeEdge(Edge<T,V> edge) {
        if (!contains(edge))
            return;
        
        adj.get(edge.getStart()).remove(edge);
        adj.get(edge.getEnd()).remove(edge);
    }
    
    /**
     * Indica se o grafo contêm um vértice
     * guardando uma informação igual à vert.
     */
    public boolean contains(T vert) {
        for (Vertex<T> v : getVertexSet())
            if (v.getData().equals(vert))
                return true;
        return false;
    }
    
    /**
     * Indica se o grafo contêm o vértice vert.
     */
    public boolean contains(Vertex<T> vert) {
        return adj.containsKey(vert);
    }
            
    /**
     * Indica se o grafo contêm a aresta edge.
     */
    public boolean contains(Edge<T,V> edge) {
        if (!contains(edge.getStart()) || !contains(edge.getEnd()))
            return false;
        
        return (adj.get(edge.getStart()).contains(edge));
    }
    
    /**
     * Retorna o número de vértices no grafo.
     */
    public int getNumVertex() {
        return adj.size();
    }
    
    /**
     * Retorna o número de arestas no grafo.
     */
    public int getNumEdges() {
        int n = 0;
        
        for(Vertex<T> vert : adj.keySet()) {
            n += adj.get(vert).size();
        }
        
        return n;
    }
    
    /**
     * Retorna um conjunto contendo todos
     * os vértice do grafo.
     */
    public Set<Vertex<T>> getVertexSet() {
        return adj.keySet();
    }

    /**
     * Retorna um conjunto contendo todas
     * as arestas do grafo.
     */
    public Set<Edge<T,V>> getEdgeSet() {
        Set<Edge<T,V>> set = new HashSet<Edge<T,V>>();
        
        for (Vertex<T> vert : adj.keySet()) {
            set.addAll(adj.get(vert));
        }
        
        return set;
    }
    
    /**
     * Retorna um conjunto de vértices, contendo os vizinhos
     * do vértice vert.
     */
    public Set<Vertex<T>> getNeighbours(Vertex<T> vert) {
        Set<Vertex<T>> set = new HashSet<Vertex<T>>();
        
        if (!adj.containsKey(vert))
            return set;
        
        for (Edge<T,V> edge : adj.get(vert)) {
            if (edge.isDirected() && (edge.getStart().getData().equals(vert.getData()))) {
                set.add(edge.getEnd());
            }
            else if (!edge.isDirected()) {
                if (edge.getStart().getData().equals(vert.getData()))
                    set.add(edge.getEnd());
                else
                    set.add(edge.getStart());
            }
        }
        
        return set;
    }
    
    /**
     * Retorna todas as arestas que se ligam à vert.
     */
    public Set<Edge<T,V>> getConnectedEdges(Vertex<T> vert) {
        if (adj.containsKey(vert)) {
            return adj.get(vert);
        }
        else {
            return (new HashSet<Edge<T,V>>());
        }
    }
    
    /**
     * Retorna o conjunto das arestas incidentes à vert.
     */
    public Set<Edge<T,V>> getIncidentEdges(Vertex<T> vert) {
        HashSet<Edge<T,V>> set = new HashSet<Edge<T,V>>();
        
        for (Edge<T,V> edge : getEdgeSet()) {
            if (!edge.contains(vert)) continue;
            
            if (edge.isDirected() && edge.getEnd().getData().equals(vert.getData()))
                set.add(edge);
            else if (!edge.isDirected())
                set.add(edge);
        }

        return set;
    }
    
    /**
     * Retorna o conjunto das arestas que estão saindo de vert.
     */
    public Set<Edge<T,V>> getOutgoingEdges(Vertex<T> vert) {
        HashSet<Edge<T,V>> set = new HashSet<Edge<T,V>>();
        
        for (Edge<T,V> edge : getEdgeSet()) {
            if (!edge.contains(vert)) continue;
            
            if (edge.isDirected() && edge.getStart().getData().equals(vert.getData()))
                set.add(edge);
            else if (!edge.isDirected())
                set.add(edge);
        }
        
        return set;
    }
    
    /**
     * Retorna o vértice v pertencente à este grafo
     * tal que v.getData() seja info, caso exista.
     */
    public Vertex<T> getVertex(T info) {
        for (Vertex<T> v : getVertexSet())
            if (v.getData().equals(info))
                return v;
        return null;
    }
    
    /**
     * Indica se x e y estão conectados por uma aresta no grafo.
     */
    public boolean isConnected(Vertex<T> x, Vertex<T> y) {
        if (!adj.containsKey(x) || !adj.containsKey(y))
            return false;
        
        for (Edge<T,V> edge : adj.get(x)) {
            if (edge.getStart() == y || edge.getEnd() == y)
                return true;
        }
        
        return false;
    }
    
    /**
     * Estrutura de dados utilizada para modelar o grafo.
     * É um mapeamento baseado em código hash, entre
     * Vértices e Conjuntos de Arestas.
     * Cada vértice está associado à um conjunto de arestas
     * tais que este vértice é terminal em todas as arestas
     * do conjunto.
     * 
     * Esta estrutura permite adicionar facilmente novos
     * vértices e novas arestas e, principalmente, permite
     * iterar rapidamente sobre arestas vizinhas à um certo nó.
     */
    private HashMap<Vertex<T>,HashSet<Edge<T,V>>> adj;
}
