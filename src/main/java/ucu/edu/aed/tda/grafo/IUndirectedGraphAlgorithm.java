package ucu.edu.aed.tda.grafo;


import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

public interface IUndirectedGraphAlgorithm {
    /**
     * Implementa el algoritmo Kruskal
     */
    <V, D extends WeightedEdge> IUndirectedGraph<V, D> kruskal(IUndirectedGraph<V, D> graph);

    /**
     * ejecuta el algoritmo Prim sobre el grafo
     */
    <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph, Comparable<V> source);

    /**
     * Ejecuta prim sin un vértice origen
     */
    default <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph) {

        for (V vertex : graph.vertices()) {
            return prim(graph, graph.construirComparable(vertex));
        }

        return null;
    }


    /**
     * Retorna la mínima arista (u,v) del grafo "graph", tal que u está en U, y v está en V.
     * Este método es útil para implementar "Prim"
     */
    <V, D extends WeightedEdge> Edge<V, D> searchMinEdge(IUndirectedGraph<V, D> graph, Collection<V> U, Collection<V> V);

    /**
     * Implementa el algoritmo de búsqueda en amplitud
     */
    <V, D> void bea(IUndirectedGraph<V, D> graph, Consumer<V> consumer);

    /**
    * Retorna los puntos de articulación del grafo no dirigido.
    */
    <V, E> List<V> puntosDeArticulacion(IGraph<V, E> grafo);

    /**
    * Retorna el número de Bacon de un actor.
    * Si el actor no existe o no está conectado con Kevin_Bacon, retorna -1.
    */
    int numBacon(IGraph<String, String> grafo, String actor);
}
