package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public abstract class Graph<V, D> implements IGraph<V, D> {

    protected Set<V> vertices;
    protected Set<Edge<V, D>> aristas;
    protected Map<V, Set<Edge<V, D>>> adyacencias;

    public Graph(Set<V> vertices, Set<Edge<V, D>> aristas, Map<V, Set<Edge<V, D>>> adyacencias) {
        this.vertices = vertices != null ? vertices : new LinkedHashSet<>();
        this.aristas = aristas != null ? aristas : new LinkedHashSet<>();
        this.adyacencias = adyacencias != null ? adyacencias : new HashMap<>();

        for (V v : this.vertices) {
            this.adyacencias.putIfAbsent(v, new LinkedHashSet<>());
        }

        for (Edge<V, D> edge : this.aristas) {
            this.adyacencias.putIfAbsent(edge.source(), new LinkedHashSet<>());
            this.adyacencias.get(edge.source()).add(edge);
        }
    }

    @Override
    public boolean agregarVertice(V vertex) {
        boolean agregado = vertices.add(vertex);

        if (agregado) {
            adyacencias.putIfAbsent(vertex, new LinkedHashSet<>());
        }

        return agregado;
    }

    @Override
    public V buscarVertice(Comparable<V> criterio) {
        if (criterio == null) {
            return null;
        }

        for (V v : vertices) {
            if (criterio.compareTo(v) == 0) {
                return v;
            }
        }

        return null;
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        if (!vertices.contains(source) || !vertices.contains(target)) {
            throw new IllegalArgumentException("Source and target vertices must be in the graph.");
        }

        Edge<V, D> nuevaArista = new DirectedEdge<>(source, target, dato);

        if (aristas.add(nuevaArista)) {
            adyacencias.putIfAbsent(source, new LinkedHashSet<>());
            adyacencias.get(source).add(nuevaArista);
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

        Set<Edge<V, D>> conjunto = adyacencias.get(edge.source());

        if (conjunto != null) {
            conjunto.remove(edge);
        }

        return true;
    }

    @Override
    public boolean removerVertice(Comparable<V> criteria) {
        V v = buscarVertice(criteria);

        if (v == null) {
            return false;
        }

        vertices.remove(v);

        aristas.removeIf(e -> e.source().equals(v) || e.target().equals(v));

        adyacencias.remove(v);

        for (Set<Edge<V, D>> conjunto : adyacencias.values()) {
            conjunto.removeIf(e -> e.source().equals(v) || e.target().equals(v));
        }

        return true;
    }

    @Override
    public Set<V> vertices() {
        return Collections.unmodifiableSet(vertices);
    }

    @Override
    public Set<Edge<V, D>> aristas() {
        return Collections.unmodifiableSet(aristas);
    }

    @Override
    public boolean existeArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        return obtenerArista(sourceCriteria, targetCriteria) != null;
    }

    @Override
    public Edge<V, D> obtenerArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        if (sourceCriteria == null || targetCriteria == null) {
            return null;
        }

        for (Edge<V, D> edge : aristas) {
            if (sourceCriteria.compareTo(edge.source()) == 0
                    && targetCriteria.compareTo(edge.target()) == 0) {
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

        return new ArrayList<>(conjunto);
    }

    @Override
    public boolean esConexo() {
        if (vertices.isEmpty()) {
            return true;
        }

        Set<V> visitados = new HashSet<>();
        Queue<V> cola = new ArrayDeque<>();

        V inicio = vertices.iterator().next();

        visitados.add(inicio);
        cola.add(inicio);

        while (!cola.isEmpty()) {
            V actual = cola.poll();

            for (Edge<V, D> edge : adyacencias(construirComparable(actual))) {
                V vecino = edge.target();

                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }

        return visitados.size() == vertices.size();
    }

    @Override
    public void vaciar() {
        vertices.clear();
        aristas.clear();
        adyacencias.clear();
    }

    @Override
    public boolean tieneCiclos() {
        Set<V> visitados = new HashSet<>();
        Set<V> enRecursion = new HashSet<>();

        for (V v : vertices) {
            if (!visitados.contains(v)) {
                if (dfsCiclo(v, visitados, enRecursion)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfsCiclo(V vertex, Set<V> visitados, Set<V> enRecursion) {
        if (enRecursion.contains(vertex)) {
            return true;
        }

        if (visitados.contains(vertex)) {
            return false;
        }

        visitados.add(vertex);
        enRecursion.add(vertex);

        for (Edge<V, D> edge : adyacencias(construirComparable(vertex))) {
            if (dfsCiclo(edge.target(), visitados, enRecursion)) {
                return true;
            }
        }

        enRecursion.remove(vertex);

        return false;
    }
}