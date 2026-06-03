package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;

public class DijkstraResult<V> implements IDijkstraResult<V> {

    private final V source;
    private final Map<V, Double> costos;
    private final Map<V, V> anteriores;

    public DijkstraResult(V source, Map<V, Double> costos, Map<V, V> anteriores) {
        this.source = source;
        this.costos = new HashMap<>(costos);
        this.anteriores = new HashMap<>(anteriores);
    }

    @Override
    public double getCost(V otherVertex) {
        return costos.getOrDefault(otherVertex, Double.POSITIVE_INFINITY);
    }

    @Override
    public List<V> getPath(V otherVertex) {
        if (source == null || otherVertex == null) {
            return Collections.emptyList();
        }

        if (Double.isInfinite(getCost(otherVertex))) {
            return Collections.emptyList();
        }

        List<V> camino = new ArrayList<>();

        V actual = otherVertex;

        while (actual != null) {
            camino.add(actual);

            if (actual.equals(source)) {
                break;
            }

            actual = anteriores.get(actual);
        }

        Collections.reverse(camino);

        if (camino.isEmpty() || !camino.get(0).equals(source)) {
            return Collections.emptyList();
        }

        return camino;
    }
}