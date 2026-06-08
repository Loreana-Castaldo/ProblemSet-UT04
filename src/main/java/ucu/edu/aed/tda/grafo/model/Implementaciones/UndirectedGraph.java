package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public class UndirectedGraph<V, D> extends Graph<V, D> implements IUndirectedGraph<V, D> {

    public UndirectedGraph(Set<V> vertices, Set<Edge<V, D>> aristas, Map<V, Set<Edge<V, D>>> adyacencias) {
        super(vertices, aristas, adyacencias);
        //TODO Auto-generated constructor stub
    }

   
}
