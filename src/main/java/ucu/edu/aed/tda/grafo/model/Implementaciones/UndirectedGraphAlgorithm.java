package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

public class UndirectedGraphAlgorithm implements IUndirectedGraphAlgorithm {

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> kruskal(IUndirectedGraph<V, D> graph) {
       
        //obtiene todas las aristas del grafo y las ordena por peso
        List<Edge<V, D>> aristas = new ArrayList<>(graph.aristas());
        aristas.sort(Comparator.comparingDouble(e -> e.dato().getWeight()));

        //arbol de expansion minima parcial
        Set<Edge<V, D>> mstEdges = new LinkedHashSet<>();
        Set<V> mstVertices = new LinkedHashSet<>(graph.vertices());

        for (Edge<V, D> edge : aristas) {

            //si agregar esta no forma un ciclo, la agrega 
            if (!hayCamino(edge.source(), edge.target(), mstEdges)) {
                mstEdges.add(edge);
            }
            //si ya tiene suficientes aristas para conectar todos los vertices, termina
            if (mstEdges.size() == mstVertices.size() - 1) {
                break;
            }
        }
        return new UndirectedGraph<>(mstVertices, mstEdges, null);  


    }

    private <V,D> boolean hayCamino(V source, V target, Set<Edge<V, D>> edges) {
        //busqueda en profundidad para verificar si existe un camino entre source y target
        Set<V> visited = new LinkedHashSet<>();
      
        return dfs(source, target, edges, visited);
    }

    //busqueda en profundidad
    private <V,D> boolean dfs(V source, V target, Set<Edge<V, D>> edges, Set<V> visited) {
        if (source.equals(target)) {      

            return true;
        }        
        
        visited.add(source);
        for (Edge<V, D> edge : edges) {

            V neighbor = edge.source().equals(source) ? edge.target() : edge.source();
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, target, edges, visited)) {
                    return true;
                }
            }
        }
        return false;
    
    
    }

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph, Comparable<V> source) {
       
       V start = (V) source;
        Set<V> vertices = new LinkedHashSet<>(graph.vertices()); //V
        Set<V> visitados = new LinkedHashSet<>(); //U
        vertices.remove(start);
        visitados.add(start);

        IUndirectedGraph<V,D> mst = new UndirectedGraph<>(new LinkedHashSet<>(), new LinkedHashSet<>(), null);
            while (!vertices.isEmpty()) {
                Edge<V, D> minEdge = searchMinEdge(graph, visitados, vertices);
                if (minEdge == null) {
                    break; // No hay más aristas para conectar los vértices restantes
                }
                
                mst.agregarVertice(minEdge.target());
                V newVertex ;
                if (vertices.contains(minEdge.source())) {
                    newVertex = minEdge.source();
                } else {
                    newVertex = minEdge.target();
                }
                vertices.remove(newVertex);
                visitados.add(newVertex);
            }
        return mst;
    }

    @Override
    public <V, D extends WeightedEdge> Edge<V, D> searchMinEdge(IUndirectedGraph<V, D> graph, Collection<V> U,
            Collection<V> V) {
        Double  min = Double.MAX_VALUE;
        Edge<V, D> minEdge = null;

        for(V u : U){
            for(V v: V){
                Edge<V, D> edge = graph.obtenerArista(u, v);
                if(edge != null ){
                    double weight = edge.dato().getWeight();
                    if(weight < min){
                        min = weight;
                        minEdge = edge;
                    }
                 
                }
            }
        }
        return minEdge;
    }

    @Override
    public <V, D> void bea(IUndirectedGraph<V, D> graph, Consumer<V> consumer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bea'");
    }
    
}
