package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.UndirectedEdge;

public class UndirectedGraph<V, D> extends Graph<V, D> implements IUndirectedGraph<V, D> {

    public UndirectedGraph(Set<V> vertices, Set<Edge<V, D>> aristas, Map<V, Set<Edge<V, D>>> adyacencias) {
        super(vertices, aristas, adyacencias);

        for (V vertice : this.vertices) {
            this.adyacencias.putIfAbsent(vertice, new LinkedHashSet<>());
        }

        for (Edge<V, D> edge : this.aristas) {
            this.adyacencias.putIfAbsent(edge.source(), new LinkedHashSet<>());
            this.adyacencias.putIfAbsent(edge.target(), new LinkedHashSet<>());

            this.adyacencias.get(edge.source()).add(edge);
            this.adyacencias.get(edge.target()).add(edge);
        }
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        if (!vertices.contains(source) || !vertices.contains(target)) {
            throw new IllegalArgumentException("Source and target vertices must be in the graph.");
        }

        adyacencias.putIfAbsent(source, new LinkedHashSet<>());
        adyacencias.putIfAbsent(target, new LinkedHashSet<>());

        Edge<V, D> nuevaArista = new UndirectedEdge<>(source, target, dato);

        if (aristas.add(nuevaArista)) {
            adyacencias.get(source).add(nuevaArista);
            adyacencias.get(target).add(nuevaArista);
            return true;
        }

        return false;
    }

    @Override
    public boolean eliminarArista(Comparable<V> source, Comparable<V> target) {
        Edge<V, D> edge = obtenerArista(source, target);

        if (edge == null) {
            return false;
        }

        aristas.remove(edge);

        for (Set<Edge<V, D>> conjunto : adyacencias.values()) {
            conjunto.remove(edge);
        }

        return true;
    }

    @Override
    public Edge<V, D> obtenerArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        if (sourceCriteria == null || targetCriteria == null) {
            return null;
        }

        for (Edge<V, D> edge : aristas) {
            boolean mismoSentido =
                    sourceCriteria.compareTo(edge.source()) == 0
                            && targetCriteria.compareTo(edge.target()) == 0;

            boolean sentidoInverso =
                    sourceCriteria.compareTo(edge.target()) == 0
                            && targetCriteria.compareTo(edge.source()) == 0;

            if (mismoSentido || sentidoInverso) {
                return edge;
            }
        }

        return null;
    }

    @Override
    public List<Edge<V, D>> adyacencias(Comparable<V> verticeCriteria) {
        V vertice = buscarVertice(verticeCriteria);

        if (vertice == null) {
            return new ArrayList<>();
        }

        Set<Edge<V, D>> conjunto = adyacencias.get(vertice);

        if (conjunto == null) {
            return new ArrayList<>();
        }

        List<Edge<V, D>> resultado = new ArrayList<>();

        for (Edge<V, D> edge : conjunto) {
            if (edge.source().equals(vertice)) {
                resultado.add(edge);
            } else {
                resultado.add(new UndirectedEdge<>(vertice, edge.source(), edge.dato()));
            }
        }

        return resultado;
    }
}