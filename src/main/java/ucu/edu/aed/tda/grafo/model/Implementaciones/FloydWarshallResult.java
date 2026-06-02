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
        this.vertices = vertices;

        this.costs = costs;
        this.next = next;

        this.indices = new HashMap<>();

        for (int i = 0; i < vertices.size(); i++) {

            indices.put(
                    vertices.get(i),
                    i);

        }
    }

    @Override
    public List getPath(V source, V target) {
        if (!connected(
                source,
                target)) {

            return Collections.emptyList();

        }

        int i = indices.get(source);

        int j = indices.get(target);

        List<V> path = new ArrayList<>();

        path.add(source);

        while (i != j) {

            i = next[i][j];

            path.add(vertices.get(i));

        }

        return path;
    }

    @Override
    public double getCost(Object source, Object target) {
        Integer i = indices.get(source);

        Integer j = indices.get(target);

        return costs[i][j];
    }

    @Override
    public boolean connected(Object source, Object target) {
        return getCost(source, target) != Double.POSITIVE_INFINITY;
    }

}
