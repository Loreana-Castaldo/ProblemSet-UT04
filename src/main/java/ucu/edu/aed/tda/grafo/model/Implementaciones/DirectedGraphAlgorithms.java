package ucu.edu.aed.tda.grafo.model.Implementaciones;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.CriticalPathResult;
import ucu.edu.aed.tda.grafo.model.result.ICriticalPathResult;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import ucu.edu.aed.tda.grafo.model.result.Path;

public class DirectedGraphAlgorithms implements IDirectedGraphAlgorithms {

    @Override
    public <V, D extends WeightedEdge> IDijkstraResult<V> dijkstra(
            Comparable<V> source,
            IDirectedIGraph<V, D> grafo) {

        V origen = grafo.buscarVertice(source);

        Map<V, Double> distancias = new HashMap<>();
        Map<V, V> anteriores = new HashMap<>();

        for (V vertice : grafo.vertices()) {
            distancias.put(vertice, Double.POSITIVE_INFINITY);
            anteriores.put(vertice, null);
        }

        if (origen == null) {
            return new DijkstraResult<>(null, distancias, anteriores);
        }

        for (Edge<V, D> arista : grafo.aristas()) {
            if (arista.dato().getWeight() < 0) {
                throw new IllegalArgumentException("Dijkstra no admite pesos negativos.");
            }
        }

        distancias.put(origen, 0.0);

        PriorityQueue<Map.Entry<V, Double>> cola = new PriorityQueue<>(
                (a, b) -> Double.compare(a.getValue(), b.getValue())
        );

        cola.add(new AbstractMap.SimpleEntry<>(origen, 0.0));

        while (!cola.isEmpty()) {
            Map.Entry<V, Double> entrada = cola.poll();

            V actual = entrada.getKey();
            double costoActual = entrada.getValue();

            if (costoActual > distancias.get(actual)) {
                continue;
            }

            for (Edge<V, D> arista : grafo.adyacencias(grafo.construirComparable(actual))) {
                V vecino = arista.target();
                double nuevoCosto = distancias.get(actual) + arista.dato().getWeight();

                if (nuevoCosto < distancias.getOrDefault(vecino, Double.POSITIVE_INFINITY)) {
                    distancias.put(vecino, nuevoCosto);
                    anteriores.put(vecino, actual);
                    cola.add(new AbstractMap.SimpleEntry<>(vecino, nuevoCosto));
                }
            }
        }

        return new DijkstraResult<>(origen, distancias, anteriores);
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> floyd(IDirectedIGraph<V, D> grafo) {
        List<V> vertices = new ArrayList<>(grafo.vertices());
        Map<V, Integer> indices = new HashMap<>();

        for (int i = 0; i < vertices.size(); i++) {
            indices.put(vertices.get(i), i);
        }

        int n = vertices.size();

        double[][] costos = new double[n][n];
        Integer[][] next = new Integer[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                costos[i][j] = Double.POSITIVE_INFINITY;
            }

            costos[i][i] = 0.0;
            next[i][i] = i;
        }

        for (Edge<V, D> arista : grafo.aristas()) {
            Integer i = indices.get(arista.source());
            Integer j = indices.get(arista.target());

            if (i != null && j != null) {
                double peso = arista.dato().getWeight();

                if (peso < costos[i][j]) {
                    costos[i][j] = peso;
                    next[i][j] = j;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (Double.isInfinite(costos[i][k])) {
                    continue;
                }

                for (int j = 0; j < n; j++) {
                    if (Double.isInfinite(costos[k][j])) {
                        continue;
                    }

                    double nuevoCosto = costos[i][k] + costos[k][j];

                    if (nuevoCosto < costos[i][j]) {
                        costos[i][j] = nuevoCosto;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        return new FloydWarshallResult<>(vertices, indices, costos, next);
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> warshall(IDirectedIGraph<V, D> grafo) {
        List<V> vertices = new ArrayList<>(grafo.vertices());
        Map<V, Integer> indices = new HashMap<>();

        for (int i = 0; i < vertices.size(); i++) {
            indices.put(vertices.get(i), i);
        }

        int n = vertices.size();

        boolean[][] alcanzable = new boolean[n][n];
        Integer[][] next = new Integer[n][n];

        for (int i = 0; i < n; i++) {
            alcanzable[i][i] = true;
            next[i][i] = i;
        }

        for (Edge<V, D> arista : grafo.aristas()) {
            Integer i = indices.get(arista.source());
            Integer j = indices.get(arista.target());

            if (i != null && j != null) {
                alcanzable[i][j] = true;
                next[i][j] = j;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (!alcanzable[i][k]) {
                    continue;
                }

                for (int j = 0; j < n; j++) {
                    if (!alcanzable[i][j] && alcanzable[k][j]) {
                        alcanzable[i][j] = true;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        double[][] costos = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                costos[i][j] = alcanzable[i][j] ? 0.0 : Double.POSITIVE_INFINITY;
            }
        }

        return new FloydWarshallResult<>(vertices, indices, costos, next);
    }

    @Override
    public <V, D extends WeightedEdge> V obtenerCentroGrafo(IDirectedIGraph<V, D> grafo) {
        IFloydWarshallResult<V> resultado = floyd(grafo);

        V centro = null;
        double menorExcentricidad = Double.POSITIVE_INFINITY;

        for (V origen : grafo.vertices()) {
            double excentricidad = 0.0;

            for (V destino : grafo.vertices()) {
                if (!origen.equals(destino)) {
                    double costo = resultado.getCost(origen, destino);

                    if (costo > excentricidad) {
                        excentricidad = costo;
                    }
                }
            }

            if (centro == null || excentricidad < menorExcentricidad) {
                menorExcentricidad = excentricidad;
                centro = origen;
            }
        }

        return centro;
    }

    @Override
    public <V, D extends WeightedEdge> double obtenerExcentricidad(
            IDirectedIGraph<V, D> grafo,
            Comparable<V> vertexCriteria) {

        V origen = grafo.buscarVertice(vertexCriteria);

        if (origen == null) {
            return Double.POSITIVE_INFINITY;
        }

        IFloydWarshallResult<V> resultado = floyd(grafo);

        double excentricidad = 0.0;

        for (V destino : grafo.vertices()) {
            if (!origen.equals(destino)) {
                double costo = resultado.getCost(origen, destino);

                if (costo > excentricidad) {
                    excentricidad = costo;
                }
            }
        }

        return excentricidad;
    }

    @Override
    public <V, D extends WeightedEdge> List<Path<V>> obtenerTodosLosCaminos(
            Comparable<V> source,
            Comparable<V> target,
            IGraph<V, D> grafo) {

        V origen = grafo.buscarVertice(source);
        V destino = grafo.buscarVertice(target);

        List<Path<V>> caminos = new ArrayList<>();

        if (origen == null || destino == null) {
            return caminos;
        }

        ArrayDeque<List<V>> pilaCaminos = new ArrayDeque<>();
        ArrayDeque<Double> pilaCostos = new ArrayDeque<>();

        List<V> caminoInicial = new ArrayList<>();
        caminoInicial.add(origen);

        pilaCaminos.push(caminoInicial);
        pilaCostos.push(0.0);

        while (!pilaCaminos.isEmpty()) {
            List<V> caminoActual = pilaCaminos.pop();
            double costoActual = pilaCostos.pop();

            V ultimo = caminoActual.get(caminoActual.size() - 1);

            if (ultimo.equals(destino)) {
                caminos.add(new Path<>(new ArrayList<>(caminoActual), costoActual));
            } else {
                for (Edge<V, D> arista : grafo.adyacencias(grafo.construirComparable(ultimo))) {
                    V vecino = arista.target();

                    if (!caminoActual.contains(vecino)) {
                        List<V> nuevoCamino = new ArrayList<>(caminoActual);
                        nuevoCamino.add(vecino);

                        pilaCaminos.push(nuevoCamino);
                        pilaCostos.push(costoActual + arista.dato().getWeight());
                    }
                }
            }
        }

        return caminos;
    }

    @Override
    public <V, D> void recorridoEnProfundidad(
            IGraph<V, D> grafo,
            Comparable<V> sourceCriteria,
            Consumer<V> consumer) {

        V origen = grafo.buscarVertice(sourceCriteria);

        if (origen == null) {
            return;
        }

        Set<V> visitados = new HashSet<>();
        ArrayDeque<V> pila = new ArrayDeque<>();

        pila.push(origen);

        while (!pila.isEmpty()) {
            V actual = pila.pop();

            if (visitados.contains(actual)) {
                continue;
            }

            visitados.add(actual);
            consumer.accept(actual);

            List<Edge<V, D>> adyacentes = grafo.adyacencias(grafo.construirComparable(actual));

            for (int i = adyacentes.size() - 1; i >= 0; i--) {
                V vecino = adyacentes.get(i).target();

                if (!visitados.contains(vecino)) {
                    pila.push(vecino);
                }
            }
        }
    }

    @Override
    public <V, D> void recorridoEnAmplitud(
            IGraph<V, D> grafo,
            Comparable<V> sourceCriteria,
            Consumer<V> consumer) {

        V origen = grafo.buscarVertice(sourceCriteria);

        if (origen == null) {
            return;
        }

        Set<V> visitados = new HashSet<>();
        Queue<V> cola = new ArrayDeque<>();

        visitados.add(origen);
        cola.add(origen);

        while (!cola.isEmpty()) {
            V actual = cola.poll();
            consumer.accept(actual);

            for (Edge<V, D> arista : grafo.adyacencias(grafo.construirComparable(actual))) {
                V vecino = arista.target();

                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
    }

    @Override
    public <V, D> List<V> calcularClasificacionTopologica(IDirectedIGraph<V, D> grafo) {
        /**
         * ALGORITMO DE KAHN - Clasificación Topológica
         * 
         * Calcula un orden topológico de los vértices de un DAG (Directed Acyclic Graph).
         * Útil para representar:
         * - Sistemas de previaturas (requisitos previos de asignaturas)
         * - Órdenes de ejecución de tareas en un proyecto
         * - Dependencias de compilación
         * 
         * Complejidad: O(V + E) donde V = vértices, E = aristas
         * 
         * Pasos:
         * 1. Calcular el grado de entrada de cada vértice
         * 2. Encolar vértices sin dependencias (grado entrada = 0)
         * 3. Procesar cola: 
         *    - Sacar vértice, agregarlo al resultado
         *    - Disminuir grado de entrada de sucesores
         *    - Encolar sucesores que quedan sin dependencias
         * 4. Si no se procesaron todos los vértices → hay ciclos
         */
        
        // Paso 1: Inicializar grados de entrada
        Map<V, Integer> gradoEntrada = new HashMap<>();

        for (V vertice : grafo.vertices()) {
            gradoEntrada.put(vertice, 0);
        }

        for (Edge<V, D> arista : grafo.aristas()) {
            V destino = arista.target();
            gradoEntrada.put(destino, gradoEntrada.getOrDefault(destino, 0) + 1);
        }

        // Paso 2: Encolar vértices sin dependencias
        Queue<V> cola = new ArrayDeque<>();

        for (V vertice : gradoEntrada.keySet()) {
            if (gradoEntrada.get(vertice) == 0) {
                cola.add(vertice);
            }
        }

        // Paso 3: Procesar la cola
        List<V> resultado = new ArrayList<>();

        while (!cola.isEmpty()) {
            V actual = cola.poll();
            resultado.add(actual);

            // Para cada sucesor del vértice actual
            for (V sucesor : grafo.successors(grafo.construirComparable(actual))) {
                int nuevoGrado = gradoEntrada.get(sucesor) - 1;
                gradoEntrada.put(sucesor, nuevoGrado);

                // Si el sucesor queda sin dependencias, lo encolamos
                if (nuevoGrado == 0) {
                    cola.add(sucesor);
                }
            }
        }

        // Paso 4: Verificar si hay ciclos
        if (resultado.size() != grafo.vertices().size()) {
            throw new IllegalStateException("El grafo tiene ciclos, no existe clasificación topológica.");
        }

        return resultado;
    }

    @Override
    public <V, D extends WeightedEdge> ICriticalPathResult<V> calcularRutaCritica(
            Comparable<V> source,
            Comparable<V> target,
            IDirectedIGraph<V, D> grafo) {

        V origen = grafo.buscarVertice(source);
        V destino = grafo.buscarVertice(target);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Origen o destino no encontrados en el grafo");
        }

        // Obtener todos los caminos posibles
        List<Path<V>> todosCaminos = obtenerTodosLosCaminos(source, target, grafo);

        if (todosCaminos.isEmpty()) {
            throw new IllegalStateException("No hay caminos entre " + origen + " y " + destino);
        }

        // Encontrar el camino crítico (el de mayor costo)
        Path<V> caminoCritico = todosCaminos.get(0);
        for (Path<V> camino : todosCaminos) {
            if (camino.getCost() > caminoCritico.getCost()) {
                caminoCritico = camino;
            }
        }

        // Retornar resultado con todos los caminos y el crítico
        return new CriticalPathResult<>(caminoCritico, todosCaminos, origen, destino);
    }
}