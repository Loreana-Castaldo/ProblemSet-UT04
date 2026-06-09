package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
       
       //arbol de expansion minima
       //List<Edge<V, D>> mst = new LinkedList<>();

        //obtiene todas las aristas del grafo y las ordena por peso
        List<Edge<V, D>> aristas = new ArrayList<>(graph.aristas());
        aristas.sort(Comparator.comparingDouble(e -> e.dato().getWeight()));

        Set<Edge<V, D>> mstEdges = new LinkedHashSet<>();
        Set<V> mstVertices = new LinkedHashSet<>(graph.vertices());

        for (Edge<V, D> edge : aristas) {

            //construir grafo temporal
            Set<Edge<V, D>> tempEdges = new LinkedHashSet<>(mstEdges);
            tempEdges.add(edge);

            Graph<V, D> tempGraph = new UndirectedGraph<>(mstVertices, tempEdges, null);
            
            //si no genera ciclo se acepta la arista
             if (!tempGraph.tieneCiclos()    ) {
                mstEdges.add(edge);
            }

            //optimizacion basica, mst compelto
            if (mstEdges.size() == mstVertices.size() - 1) {
                break;
            }   
        }
        return new UndirectedGraph<>(mstVertices, mstEdges, null);  


    }

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph, Comparable<V> source) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prim'");
    }

    @Override
    public <V, D extends WeightedEdge> Edge<V, D> searchMinEdge(IUndirectedGraph<V, D> graph, Collection<V> U,
            Collection<V> V) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchMinEdge'");
    }

    @Override
    public <V, D> void bea(IUndirectedGraph<V, D> graph, Consumer<V> consumer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bea'");
    }
    
}
