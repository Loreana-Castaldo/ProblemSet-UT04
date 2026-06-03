package ucu.edu.aed.tda.grafo.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de ICriticalPathResult que almacena el análisis del camino crítico.
 */
public class CriticalPathResult<V> implements ICriticalPathResult<V> {
    
    private final Path<V> criticalPath;
    private final List<Path<V>> allPaths;
    private final V source;
    private final V target;

    public CriticalPathResult(Path<V> criticalPath, List<Path<V>> allPaths, V source, V target) {
        this.criticalPath = criticalPath;
        this.allPaths = allPaths;
        this.source = source;
        this.target = target;
    }

    @Override
    public Path<V> getCriticalPath() {
        return criticalPath;
    }

    @Override
    public double getCriticalCost() {
        return criticalPath.getCost();
    }

    @Override
    public List<Path<V>> getAllPaths() {
        return new ArrayList<>(allPaths);
    }

    @Override
    public List<PathWithSlack<V>> getPathsWithSlack() {
        List<PathWithSlack<V>> result = new ArrayList<>();
        double criticalCost = getCriticalCost();
        
        for (Path<V> path : allPaths) {
            double slack = criticalCost - path.getCost();
            boolean isCritical = slack == 0;
            result.add(new PathWithSlack<>(
                path.getPath(),
                path.getCost(),
                slack,
                isCritical
            ));
        }
        
        return result;
    }

    @Override
    public double getSlack(Path<V> path) {
        return getCriticalCost() - path.getCost();
    }

    @Override
    public V getSource() {
        return source;
    }

    @Override
    public V getTarget() {
        return target;
    }
}
