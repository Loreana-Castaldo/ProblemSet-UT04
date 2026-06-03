package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;

public class FloydWarshallResult<V> implements IFloydWarshallResult<V> {

    private final List<V> vertices;
    private final Map<V, Integer> indices;
    private final double[][] costs;
    private final Integer[][] next;

    public FloydWarshallResult(List<V> vertices, Map<V, Integer> indices, double[][] costs, Integer[][] next) {
        this.vertices = vertices != null ? new ArrayList<>(vertices) : new ArrayList<>();
        this.costs = costs;
        this.next = next;

        this.indices = new HashMap<>();

        if (indices != null) {
            this.indices.putAll(indices);
        }

        for (int i = 0; i < this.vertices.size(); i++) {
            this.indices.putIfAbsent(this.vertices.get(i), i);
        }
    }

    @Override
    public List<V> getPath(V source, V target) {
        Integer i = indices.get(source);
        Integer j = indices.get(target);

        if (i == null || j == null) {
            return Collections.emptyList();
        }

        if (!connected(source, target)) {
            return Collections.emptyList();
        }

        List<V> path = new ArrayList<>();

        if (i.equals(j)) {
            path.add(vertices.get(i));
            return path;
        }

        if (next == null || next[i][j] == null) {
            return Collections.emptyList();
        }

        int actual = i;
        path.add(vertices.get(actual));

        while (actual != j) {
            Integer siguiente = next[actual][j];

            if (siguiente == null || siguiente < 0 || siguiente >= vertices.size()) {
                return Collections.emptyList();
            }

            actual = siguiente;
            path.add(vertices.get(actual));
        }

        return path;
    }

    @Override
    public double getCost(V source, V target) {
        Integer i = indices.get(source);
        Integer j = indices.get(target);

        if (i == null || j == null || costs == null) {
            return Double.POSITIVE_INFINITY;
        }

        return costs[i][j];
    }

    @Override
    public boolean connected(V source, V target) {
        return !Double.isInfinite(getCost(source, target));
    }
}