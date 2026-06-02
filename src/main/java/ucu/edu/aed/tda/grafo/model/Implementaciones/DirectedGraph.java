package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public class DirectedGraph<V, D> extends Graph<V, D> implements IDirectedIGraph<V, D> {

    public DirectedGraph(Set<Edge<V, D>> aristas, Map<V, Set<Edge<V, D>>> adyacencias) {
        super(aristas, adyacencias);
    }

    @Override
    public Set<V> successors(Comparable criteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'successors'");
    }

    @Override
    public Set<V> predecessors(Comparable criteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'predecessors'");
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {

        if (!adyacencias.containsKey(source) || !adyacencias.containsKey(target)) {
            throw new IllegalArgumentException("Source and target vertices must be in the graph.");
        }
        Edge<V, D> newEdge = new DirectedEdge<>(source, target, dato);

        if (aristas.add(newEdge)) {
            adyacencias.get(source).add(newEdge);

            return true;
        }
        return false;
    }

}
