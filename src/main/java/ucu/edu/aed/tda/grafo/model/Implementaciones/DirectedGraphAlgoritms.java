package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Consumer;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import ucu.edu.aed.tda.grafo.model.result.Path;

public class DirectedGraphAlgoritms<D, V> extends DirectedGraph<V, D> implements IDirectedGraphAlgorithms {

    public  IDijkstraResult<V> dijkstra(DirectedGraph<V,D> graph, V source) {
       Map<V, Double> dist = new HashMap<>();
       Map<V, V> prev = new HashMap<>();
       PriorityQueue<V> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        
       for (V v : graph.vertices) { {
              dist.put(v, Double.POSITIVE_INFINITY);
              prev.put(v, null);
              pq.add(v);
         }
         dist.put(source, 0.0); 
            while (!pq.isEmpty()) {
                V u = pq.poll();

                for (V v1 : graph.successors(graph.construirComparable(u))) {
                    
                  //  Edge<V, Double> edge = graph.aristas(graph.construirComparable(u), graph.construirComparable(v1));  
            Edge<V,D> edge =    graph.obtenerArista(        graph.construirComparable(u),        graph.construirComparable(v1)
    );
                if(edge == null) continue; // Si no hay arista, saltar
                  double peso = edge.dato;
                  double nuevaDist = dist.get(u) + peso;
                    if (nuevaDist < dist.get(v1)) {
                        dist.put(v1, nuevaDist  );
                        prev.put(v1, u);
                        pq.remove(v1); // Actualizar prioridad
                        pq.add(v1);
                      }
                }
                return new DijkstraResult<>(dist, prev);
            }
    }

    @Override
    public <V, D extends WeightedEdge> IDijkstraResult<V> dijkstra(Comparable<V> source, IDirectedIGraph<V, D> grafo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dijkstra'");
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> floyd(IDirectedIGraph<V, D> grafo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'floyd'");
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> warshall(IDirectedIGraph<V, D> grafo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'warshall'");
    }

    @Override
    public <V, D extends WeightedEdge> V obtenerCentroGrafo(IDirectedIGraph<V, D> grafo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerCentroGrafo'");
    }

    @Override
    public <V, D extends WeightedEdge> double obtenerExcentricidad(IDirectedIGraph<V, D> grafo,
            Comparable<V> vertexCriteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerExcentricidad'");
    }

    @Override
    public <V, D extends WeightedEdge> List<Path<V>> obtenerTodosLosCaminos(Comparable<V> source, Comparable<V> target,
            IGraph<V, D> grafo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodosLosCaminos'");
    }

    @Override
    public <V, D> void recorridoEnProfundidad(IGraph<V, D> grafo, Comparable<V> sourceCriteria, Consumer<V> consumer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recorridoEnProfundidad'");
    }

    @Override
    public <V, D> void recorridoEnAmplitud(IGraph<V, D> grafo, Comparable<V> sourceCriteria, Consumer<V> consumer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recorridoEnAmplitud'");
    }

    @Override
    public <V, D> List<V> calcularClasificacionTopologica(IDirectedIGraph<V, D> grafo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcularClasificacionTopologica'");
    }

}
