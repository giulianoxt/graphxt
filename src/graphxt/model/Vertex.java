/*
 * Vertex.java
 * 
 * @author Giuliano Vilela
 */

package graphxt.model;

/**
 * Vertex<T>
 * 
 * Classe que modela um vértice de um grafo.
 * Este vértice armazena um dado interno, de tipo
 * T, que é mostrado ao usuário através de T::toString.
 * 
 * @author Giuliano Vilela
 */
public class Vertex<T> {
    /**
     * Cria um novo vértice contendo uma informação nula.
     */
    public Vertex() {
        this(null);
    }
    
    /**
     * Cria um novo vértice.
     * @param d Informação que será guardada no vértice
     */
    public Vertex(T d) {
        data = d;
    }
    
    /**
     * Modifica o dado armazenado neste vértice.
     * @param d Novo dado à ser armazenado.
     */
    public void setData(T d) {
        data = d;
    }
    
    /**
     * Retorna a informação armazenada neste nó.
     */
    public T getData() {
        return data;
    }
    
    /**
     * Retorna a representação textual da informação
     * armazenada neste nó.
     */
    @Override
    public String toString() {
        return data.toString();
    }
    
    /**
     * Retorna um código hash referente à este nó.
     * Este código depende somente do dado armazenado
     * neste vértice.
     */
    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    /**
     * Indica se este nó guarda a mesma informação
     * que o nó v.
     */
    @Override
    public boolean equals(Object v) {
        return (data.equals(((Vertex<T>)v).data));
    }
    
    /**
     * Informação guardada por este nó.
     */
    private T data;
}
