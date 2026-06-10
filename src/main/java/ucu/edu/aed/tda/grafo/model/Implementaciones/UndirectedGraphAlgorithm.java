package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

public class UndirectedGraphAlgorithm implements IUndirectedGraphAlgorithm {

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> kruskal(IUndirectedGraph<V, D> graph) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'kruskal'");
    }

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph, Comparable<V> source) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prim'");
    }

    @Override
    public <V, D extends WeightedEdge> Edge<V, D> searchMinEdge(
            IUndirectedGraph<V, D> graph,
            Collection<V> U,
            Collection<V> V) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchMinEdge'");
    }

    @Override
    public <V, D> void bea(IUndirectedGraph<V, D> graph, Consumer<V> consumer) {
        if (graph == null || consumer == null) {
            return;
        }

        Set<V> visitados = new HashSet<>();

        for (V vertice : graph.vertices()) {
            if (!visitados.contains(vertice)) {
                bea(graph, vertice, consumer, visitados);
            }
        }
    }

    private <V, D> void bea(IUndirectedGraph<V, D> graph, V origen, Consumer<V> consumer, Set<V> visitados) {
        Queue<V> pendientes = new LinkedList<>();

        visitados.add(origen);
        pendientes.add(origen);

        while (!pendientes.isEmpty()) {
            V vertice = pendientes.poll();
            consumer.accept(vertice);

            for (Edge<V, D> adyacente : graph.adyacencias(graph.construirComparable(vertice))) {
                V vecino = adyacente.target();

                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    pendientes.add(vecino);
                }
            }
        }

    }


    @Override
    public <V, E> List<V> puntosDeArticulacion(IGraph<V, E> grafo) {
        List<V> resultado = new ArrayList<>();

        if (grafo == null || grafo.vertices().isEmpty()) {
            return resultado;
        }

        Set<V> puntos = new LinkedHashSet<>();

        Map<V, Boolean> visitado = new HashMap<>();
        Map<V, Integer> numBp = new HashMap<>();
        Map<V, Integer> numBajo = new HashMap<>();

        int[] cont = {0};

        for (V vertice : grafo.vertices()) {
            visitado.put(vertice, false);
            numBp.put(vertice, 0);
            numBajo.put(vertice, 0);
        }

        for (V vertice : grafo.vertices()) {
            if (!visitado.get(vertice)) {
                busquedaEnProfundidadArticulacion(
                        grafo,
                        vertice,
                        puntos,
                        cont,
                        visitado,
                        numBp,
                        numBajo
                );
            }
        }

        resultado.addAll(puntos);
        return resultado;
    }

    private <V, E> void busquedaEnProfundidadArticulacion(
            IGraph<V, E> grafo,
            V actual,
            Set<V> puntos,
            int[] cont,
            Map<V, Boolean> visitado,
            Map<V, Integer> numBp,
            Map<V, Integer> numBajo) {

        visitado.put(actual, true);

    cont[0]++;
    numBp.put(actual, cont[0]);
    numBajo.put(actual, cont[0]);
          
    List<V> hijos = new ArrayList<>();

    for (Edge<V, E> arista : grafo.adyacencias(grafo.construirComparable(actual))) {
        V adyacente = arista.target();

        if (!visitado.get(adyacente)) {
            busquedaEnProfundidadArticulacion(
                    grafo,
                    adyacente,
                    puntos,
                    cont,
                    visitado,
                    numBp,
                    numBajo
            );

            hijos.add(adyacente);

            numBajo.put(
                    actual,
                    Math.min(numBajo.get(actual), numBajo.get(adyacente))
            );
        } else {
            numBajo.put(
                    actual,
                    Math.min(numBajo.get(actual), numBp.get(adyacente))
            );
        }
    }

    if (numBp.get(actual) > 1) {
        for (V hijo : hijos) {
            if (numBajo.get(hijo) >= numBp.get(actual)) {
                puntos.add(actual);
            }
        }
    } else {
        if (hijos.size() > 1) {
            puntos.add(actual);
        }
    }
    }
    @Override
    public int numBacon(IGraph<String, String> grafo, String actor) {
        final String KEVIN_BACON = "Kevin_Bacon";

        if (grafo == null || actor == null) {
            return -1;
        }

        if (grafo.buscarVertice(grafo.construirComparable(actor)) == null) {
            return -1;
        }

        if (grafo.buscarVertice(grafo.construirComparable(KEVIN_BACON)) == null) {
            return -1;
        }

        if (actor.equals(KEVIN_BACON)) {
            return 0;
        }

        Set<String> visitados = new HashSet<>();
        Map<String, Integer> distancia = new HashMap<>();
        Queue<String> cola = new ArrayDeque<>();

        visitados.add(actor);
        distancia.put(actor, 0);
        cola.add(actor);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            int distanciaActual = distancia.get(actual);

            for (Edge<String, String> arista : grafo.adyacencias(grafo.construirComparable(actual))) {
                String vecino;

                if (actual.equals(arista.source())) {
                    vecino = arista.target();
                } else {
                    vecino = arista.source();
                }

                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    distancia.put(vecino, distanciaActual + 1);

                    if (vecino.equals(KEVIN_BACON)) {
                        return distancia.get(vecino);
                    }

                    cola.add(vecino);
                }
            }
        }

        return -1;
    }
}