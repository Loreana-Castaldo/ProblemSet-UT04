package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public class DirectedGraph<V, D> extends Graph<V, D> implements IDirectedIGraph<V, D> {

    public DirectedGraph() {
        super();
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

}
