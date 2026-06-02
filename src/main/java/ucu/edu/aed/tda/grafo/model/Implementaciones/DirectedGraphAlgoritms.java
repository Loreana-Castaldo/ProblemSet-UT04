package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
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

public class DirectedGraphAlgoritms implements IDirectedGraphAlgorithms {

    @Override
    public <V, D extends WeightedEdge> IDijkstraResult<V> dijkstra(Comparable<V> source, IDirectedIGraph<V, D> grafo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dijkstra'");
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> floyd(IDirectedIGraph<V, D> grafo) {

        List<V> vertices = new ArrayList<>(grafo.vertices());

        int n = vertices.size();

        double[][] A = new double[n][n];
        Integer[][] next = new Integer[n][n];

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                A[i][j] = Double.POSITIVE_INFINITY;

            }

            A[i][i] = 0;

        }

        for (Edge<V, D> edge : grafo.aristas()) {

            int i = vertices.indexOf(
                    edge.source());

            int j = vertices.indexOf(
                    edge.target());

            A[i][j] = edge.dato().getWeight();

            next[i][j] = j;

        }

        for (int k = 0; k < n; k++) {

            for (int i = 0; i < n; i++) {

                for (int j = 0; j < n; j++) {

                    if (A[i][k] + A[k][j] < A[i][j]) {

                        A[i][j] = A[i][k]
                                + A[k][j];

                        next[i][j] = next[i][k];

                    }

                }

            }

        }

        return new FloydWarshallResult<>(vertices, A, next);
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

        Set<V> visitados = new HashSet<>();

        V source = grafo.buscarVertice(
                sourceCriteria);

        if (source == null) {

            return;

        }

        dfs(
                grafo,
                source,
                visitados,
                consumer);
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

    private <V, D> void dfs(IGraph<V, D> grafo, V actual, Set<V> visitados, Consumer<V> consumer) {

        visitados.add(actual);

        consumer.accept(actual);

        for (Edge<V, D> edge : grafo.adyacencias(grafo.construirComparable(actual))) {

            V vecino = edge.target();

            if (!visitados.contains(vecino)) {

                dfs(grafo, vecino, visitados, consumer);

            }

        }

    }
}