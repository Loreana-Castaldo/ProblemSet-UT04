package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public class DirectedGraph<V, D> extends Graph<V, D> implements IDirectedIGraph<V, D> {

    public DirectedGraph(
            Set<V> vertices,
            Set<Edge<V, D>> aristas,
            Map<V, List<Edge<V, D>>> adyacencias) {

        super(vertices, aristas, adyacencias);

        for (V vertice : vertices) {
            this.adyacencias.putIfAbsent(vertice, new ArrayList<>());
        }
    }

    @Override
    public boolean agregarVertice(V vertex) {
        boolean agregado = vertices.add(vertex);

        if (agregado) {
            adyacencias.putIfAbsent(vertex, new ArrayList<>());
        }

        return agregado;
    }

    @Override
    public Set<V> successors(Comparable<V> criteria) {
        Set<V> sucesores = new HashSet<>();

        V vertice = buscarVertice(criteria);

        if (vertice == null) {
            return sucesores;
        }

        List<Edge<V, D>> aristasSalientes = adyacencias.get(vertice);

        if (aristasSalientes == null) {
            return sucesores;
        }

        for (Edge<V, D> arista : aristasSalientes) {
            sucesores.add(arista.target());
        }

        return sucesores;
    }

    @Override
    public Set<V> predecessors(Comparable<V> criteria) {
        Set<V> predecesores = new HashSet<>();

        V vertice = buscarVertice(criteria);

        if (vertice == null) {
            return predecesores;
        }

        for (Edge<V, D> arista : aristas) {
            if (arista.target().equals(vertice)) {
                predecesores.add(arista.source());
            }
        }

        return predecesores;
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        if (!vertices.contains(source) || !vertices.contains(target)) {
            throw new IllegalArgumentException("Source and target vertices must be in the graph.");
        }

        adyacencias.putIfAbsent(source, new ArrayList<>());
        adyacencias.putIfAbsent(target, new ArrayList<>());

        Edge<V, D> nuevaArista = new DirectedEdge<>(source, target, dato);

        if (aristas.add(nuevaArista)) {
            adyacencias.get(source).add(nuevaArista);
            return true;
        }

        return false;
    }
}