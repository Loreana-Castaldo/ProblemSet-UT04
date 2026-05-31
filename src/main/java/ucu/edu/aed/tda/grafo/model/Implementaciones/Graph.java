package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public class Graph<V, D> implements IGraph<V, D> {

    private final Set<V> vertices = new HashSet<>();
    private final Set<Edge<V, D>> aristas = new HashSet<>();

    @Override
    public boolean agregarVertice(V vertex) {
        // TODO Auto-generated method stub
        return vertices.add(vertex);
    }

    @Override
    public V buscarVertice(Comparable<V> criterio) {
        // TODO Auto-generated method stub
        for (V v : vertices) {
            if (v.equals(criterio)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        // TODO Auto-generated method stub
        if (!vertices.contains(source) || !vertices.contains(target)) {
            throw new IllegalArgumentException("Source and target vertices must be in the graph.");
        }
        Edge<V, D> newEdge = new DirectedEdge<>(source, target, dato);
        return aristas.add(newEdge);
    }

    @Override
    public boolean eliminarArista(Comparable<V> source, Comparable<V> target) {
        // TODO Auto-generated method stub
        return aristas.removeIf(edge -> edge.source().equals(source) && edge.target().equals(target));

    }

    @Override
    public boolean removerVertice(Comparable<V> criteria) {
        // TODO Auto-generated method stub
        V v = buscarVertice(criteria);
        if (v == null)
            return false;

        vertices.remove(v);

        // eliminar aristas asociadas
        aristas.removeIf(e -> e.source().equals(v) || e.target().equals(v));

        return true;
    }

    @Override
    public Set<V> vertices() {
        return Collections.unmodifiableSet(vertices);

    }

    @Override
    public Set<Edge<V, D>> aristas() {
        // TODO Auto-generated method stub
        return Collections.unmodifiableSet(aristas);
    }

    @Override
    public boolean existeArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        // TODO Auto-generated method stub
        return obtenerArista(sourceCriteria, targetCriteria) != null;
    }

    @Override
    public Edge<V, D> obtenerArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        for (Edge<V, D> edge : aristas) {
            if (edge.source().equals(sourceCriteria) && edge.target().equals(targetCriteria)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public List<Edge<V, D>> adyacencias(Comparable<V> verticeCriteria) {
        List<Edge<V, D>> adyacentes = new java.util.ArrayList<>();
        for (Edge<V, D> edge : aristas) {
            if (edge.source().equals(verticeCriteria)) {
                adyacentes.add(edge);
            }
        }
        return adyacentes;
    }

    @Override
    public boolean esConexo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'esConexo'");
    }

    @Override
    public void vaciar() {
        vertices.clear();
        aristas.clear();
    }

    @Override
    public boolean tieneCiclos() {
        Set<V> visited = new HashSet<>();
        Set<V> recStack = new HashSet<>();
        for (V v : vertices) {
            if (!visited.contains(v)) {
                if (dfsCiclo(v, visited, recStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsCiclo(V vertex, Set<V> visited, Set<V> recStack) {
        if (recStack.contains(vertex)) {
            return true; // ciclo detectado
        }
        if (visited.contains(vertex)) {
            return false; // ya visitado, no ciclo por aquí
        }
        visited.add(vertex);
        recStack.add(vertex);
        for (Edge<V, D> edge : aristas) {
            if (edge.source().equals(vertex)) {
                if (dfsCiclo(edge.target(), visited, recStack)) {
                    return true;
                }
            }
        }
        recStack.remove(vertex);
        return false;
    }

}
