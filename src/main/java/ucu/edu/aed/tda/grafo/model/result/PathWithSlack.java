package ucu.edu.aed.tda.grafo.model.result;

import java.util.List;

/**
 * Representa un camino con su costo total y la holgura (slack) calculada.
 * La holgura es la diferencia entre el camino crítico y el costo de este camino.
 */
public class PathWithSlack<V> {
    private final List<V> path;
    private final double cost;
    private final double slack;
    private final boolean isCritical;

    public PathWithSlack(List<V> path, double cost, double slack, boolean isCritical) {
        this.path = path;
        this.cost = cost;
        this.slack = slack;
        this.isCritical = isCritical;
    }

    public List<V> getPath() {
        return path;
    }

    public double getCost() {
        return cost;
    }

    public double getSlack() {
        return slack;
    }

    public boolean isCritical() {
        return isCritical;
    }

    @Override
    public String toString() {
        return String.format(
            "Camino: %s | Costo: %.2f | Holgura: %.2f | Crítico: %s",
            path, cost, slack, isCritical
        );
    }
}
