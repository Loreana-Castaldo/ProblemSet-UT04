package ucu.edu.aed.tda.grafo.model.result;

import java.util.List;

/**
 * Interfaz que representa el resultado del análisis de camino crítico (Critical Path Method).
 * Proporciona información sobre el camino crítico y la holgura de otros caminos.
 */
public interface ICriticalPathResult<V> {
    
    /**
     * Retorna el camino crítico (ruta con costo máximo).
     */
    Path<V> getCriticalPath();
    
    /**
     * Retorna el costo del camino crítico.
     */
    double getCriticalCost();
    
    /**
     * Retorna todos los caminos posibles entre origen y destino.
     */
    List<Path<V>> getAllPaths();
    
    /**
     * Retorna todos los caminos con su holgura calculada.
     */
    List<PathWithSlack<V>> getPathsWithSlack();
    
    /**
     * Retorna la holgura de un camino específico.
     */
    double getSlack(Path<V> path);
    
    /**
     * Retorna el vértice origen del análisis.
     */
    V getSource();
    
    /**
     * Retorna el vértice destino del análisis.
     */
    V getTarget();
}
