package ucu.edu.aed;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.Implementaciones.DirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import ucu.edu.aed.tda.grafo.model.result.Path;

public class Main  {

    public static void main(String[] args) {

        System.out.println("CREAR GRAFO Y PROBAR OPERACIONES BASICAS");

        DirectedGraph<String, WeightedEdge> grafo = new DirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new HashMap<>()
        );

        grafo.agregarVertice("Montevideo");
        grafo.agregarVertice("Canelones");
        grafo.agregarVertice("Punta del Este");
        grafo.agregarVertice("Durazno");
        grafo.agregarVertice("Rocha");
        grafo.agregarVertice("Colonia");
        grafo.agregarVertice("Artigas");
        grafo.agregarVertice("Florida");

        grafo.agregarArista("Montevideo", "Canelones", new WeightedEdge(30));
        grafo.agregarArista("Montevideo", "Punta del Este", new WeightedEdge(130));
        grafo.agregarArista("Montevideo", "Artigas", new WeightedEdge(700));

        grafo.agregarArista("Canelones", "Punta del Este", new WeightedEdge(90));
        grafo.agregarArista("Canelones", "Durazno", new WeightedEdge(170));
        grafo.agregarArista("Canelones", "Colonia", new WeightedEdge(200));
        grafo.agregarArista("Canelones", "Artigas", new WeightedEdge(500));

        grafo.agregarArista("Punta del Este", "Rocha", new WeightedEdge(90));

        System.out.println("Vertices:");
        System.out.println(grafo.vertices());


        //Por el toString de arista de los Sets de la coleccion de java, los valores de las aristas se imprimen con un guión antes de su valor.

        System.out.println("\nAristas:");
        for (Edge<String, WeightedEdge> arista : grafo.aristas()) {
            System.out.println(arista);
        }

        System.out.println("\nSucesores de Montevideo:");
        System.out.println(grafo.successors(grafo.construirComparable("Montevideo")));

        System.out.println("\nPredecesores de Punta del Este:");
        System.out.println(grafo.predecessors(grafo.construirComparable("Punta del Este")));

        System.out.println("\nGrado de salida de Canelones:");
        System.out.println(grafo.gradoDeSalida(grafo.construirComparable("Canelones")));

        System.out.println("\nGrado de entrada de Punta del Este:");
        System.out.println(grafo.gradoDeEntrada(grafo.construirComparable("Punta del Este")));

        System.out.println("\nExiste arista Montevideo -> Canelones:");
        System.out.println(grafo.existeArista(
                grafo.construirComparable("Montevideo"),
                grafo.construirComparable("Canelones")
        ));

        System.out.println("\nExiste arista Canelones -> Montevideo:");
        System.out.println(grafo.existeArista(
                grafo.construirComparable("Canelones"),
                grafo.construirComparable("Montevideo")
        ));

        System.out.println("\nDIJKSTRA DESDE MONTEVIDEO");

        IDirectedGraphAlgorithms algoritmos = new DirectedGraphAlgorithms();

        IDijkstraResult<String> dijkstra = algoritmos.dijkstra(
                grafo.construirComparable("Montevideo"),
                grafo
        );

        for (String destino : grafo.vertices()) {
            double costo = dijkstra.getCost(destino);
            List<String> camino = dijkstra.getPath(destino);

            System.out.println("Destino: " + destino);
            System.out.println("Costo: " + costo);
            System.out.println("Camino: " + camino);
            System.out.println();
        }

        System.out.println("RECORRIDOS");

        System.out.println("Recorrido en profundidad desde Montevideo:");
        algoritmos.recorridoEnProfundidad(
                grafo,
                grafo.construirComparable("Montevideo"),
                v -> System.out.print(v + " ")
        );

        System.out.println("\n\nRecorrido en amplitud desde Montevideo:");
        algoritmos.recorridoEnAmplitud(
                grafo,
                grafo.construirComparable("Montevideo"),
                v -> System.out.print(v + " ")
        );

        System.out.println("\n\nClasificacion topologica:");
        System.out.println(algoritmos.calcularClasificacionTopologica(grafo));

        System.out.println("\nTodos los caminos de Montevideo a Rocha:");
        List<Path<String>> caminos = algoritmos.obtenerTodosLosCaminos(
                grafo.construirComparable("Montevideo"),
                grafo.construirComparable("Rocha"),
                grafo
        );

        for (Path<String> path : caminos) {
            System.out.println("Camino: " + path.getPath() + " | Costo: " + path.getCost());
        }

        System.out.println("\nFLOYD, WARSHALL Y CENTRO DEL GRAFO");

        DirectedGraph<String, WeightedEdge> grafoPuertos = new DirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new HashMap<>()
        );

        grafoPuertos.agregarVertice("Montevideo");
        grafoPuertos.agregarVertice("Colonia");
        grafoPuertos.agregarVertice("Buenos Aires");
        grafoPuertos.agregarVertice("Punta del Este");

        grafoPuertos.agregarArista("Montevideo", "Punta del Este", new WeightedEdge(150));

        grafoPuertos.agregarArista("Colonia", "Montevideo", new WeightedEdge(300));
        grafoPuertos.agregarArista("Colonia", "Punta del Este", new WeightedEdge(390));

        grafoPuertos.agregarArista("Buenos Aires", "Montevideo", new WeightedEdge(400));
        grafoPuertos.agregarArista("Buenos Aires", "Colonia", new WeightedEdge(200));

        grafoPuertos.agregarArista("Punta del Este", "Buenos Aires", new WeightedEdge(410));

        IFloydWarshallResult<String> floyd = algoritmos.floyd(grafoPuertos);

        System.out.println("Costo minimo Montevideo -> Colonia:");
        System.out.println(floyd.getCost("Montevideo", "Colonia"));

        System.out.println("Camino minimo Montevideo -> Colonia:");
        System.out.println(floyd.getPath("Montevideo", "Colonia"));

        System.out.println("\nCosto minimo Buenos Aires -> Punta del Este:");
        System.out.println(floyd.getCost("Buenos Aires", "Punta del Este"));

        System.out.println("Camino minimo Buenos Aires -> Punta del Este:");
        System.out.println(floyd.getPath("Buenos Aires", "Punta del Este"));

        IFloydWarshallResult<String> warshall = algoritmos.warshall(grafoPuertos);

        System.out.println("\nWarshall: existe camino Montevideo -> Colonia:");
        System.out.println(warshall.connected("Montevideo", "Colonia"));

        System.out.println("Warshall: existe camino Colonia -> Buenos Aires:");
        System.out.println(warshall.connected("Colonia", "Buenos Aires"));

        System.out.println("\nExcentricidad de Buenos Aires:");
        System.out.println(algoritmos.obtenerExcentricidad(
                grafoPuertos,
                grafoPuertos.construirComparable("Buenos Aires")
        ));

        System.out.println("\nCentro del grafo:");
        System.out.println(algoritmos.obtenerCentroGrafo(grafoPuertos));
    }
}