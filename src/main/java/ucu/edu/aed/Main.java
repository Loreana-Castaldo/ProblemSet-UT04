package ucu.edu.aed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.Implementaciones.DirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.ICriticalPathResult;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import ucu.edu.aed.tda.grafo.model.result.Path;
import ucu.edu.aed.tda.grafo.model.result.PathWithSlack;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== VERIFICACION DE CICLOS CON ARCHIVOS ===\n");

        // Cargar datos desde archivos
        DirectedGraph<String, WeightedEdge> grafo = cargarGrafoDesdeArchivos(
                "aeropuertos_2.txt",
                "conexiones_2.txt");

        if (grafo != null) {
            System.out.println("Vertices cargados:");
            System.out.println(grafo.vertices());

            System.out.println("\nAristas cargadas:");
            for (Edge<String, WeightedEdge> arista : grafo.aristas()) {
                System.out.println(arista);
            }

            System.out.println("\n¿El grafo tiene ciclos? " + grafo.tieneCiclos());
            System.out.println();

            // Demostración de ruta crítica
            demostrarRutaCritica(grafo);

            // Demostración de clasificación topológica
            demostrarClasificacionTopologica();
        }

        System.out.println("\n\nCREAR GRAFO Y PROBAR OPERACIONES BASICAS");

        Map<String, Set<Edge<String, WeightedEdge>>> adyacencias = new HashMap<>();

        DirectedGraph<String, WeightedEdge> grafo2 = new DirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                adyacencias);

        grafo2.agregarVertice("Montevideo");
        grafo2.agregarVertice("Canelones");
        grafo2.agregarVertice("Punta del Este");
        grafo2.agregarVertice("Durazno");
        grafo2.agregarVertice("Rocha");
        grafo2.agregarVertice("Colonia");
        grafo2.agregarVertice("Artigas");
        grafo2.agregarVertice("Florida");

        grafo2.agregarArista("Montevideo", "Canelones", new WeightedEdge(30));
        grafo2.agregarArista("Montevideo", "Punta del Este", new WeightedEdge(130));
        grafo2.agregarArista("Montevideo", "Artigas", new WeightedEdge(700));

        grafo2.agregarArista("Canelones", "Punta del Este", new WeightedEdge(90));
        grafo2.agregarArista("Canelones", "Durazno", new WeightedEdge(170));
        grafo2.agregarArista("Canelones", "Colonia", new WeightedEdge(200));
        grafo2.agregarArista("Canelones", "Artigas", new WeightedEdge(500));

        grafo2.agregarArista("Punta del Este", "Rocha", new WeightedEdge(90));

        System.out.println("Vertices:");
        System.out.println(grafo2.vertices());

        // Por el toString de arista de los Sets de la coleccion de java, los valores de
        // las aristas se imprimen con un guión antes de su valor.

        System.out.println("\nAristas:");
        for (Edge<String, WeightedEdge> arista : grafo2.aristas()) {
            System.out.println(arista);
        }

        System.out.println("\nSucesores de Montevideo:");
        System.out.println(grafo2.successors(grafo2.construirComparable("Montevideo")));

        System.out.println("\nPredecesores de Punta del Este:");
        System.out.println(grafo2.predecessors(grafo2.construirComparable("Punta del Este")));

        System.out.println("\nGrado de salida de Canelones:");
        System.out.println(grafo2.gradoDeSalida(grafo2.construirComparable("Canelones")));

        System.out.println("\nGrado de entrada de Punta del Este:");
        System.out.println(grafo2.gradoDeEntrada(grafo2.construirComparable("Punta del Este")));

        System.out.println("\nExiste arista Montevideo -> Canelones:");
        System.out.println(grafo2.existeArista(
                grafo2.construirComparable("Montevideo"),
                grafo2.construirComparable("Canelones")));

        System.out.println("\nExiste arista Canelones -> Montevideo:");
        System.out.println(grafo2.existeArista(
                grafo2.construirComparable("Canelones"),
                grafo2.construirComparable("Montevideo")));

        System.out.println("\nDIJKSTRA DESDE MONTEVIDEO");

        IDirectedGraphAlgorithms algoritmos = new DirectedGraphAlgorithms();

        IDijkstraResult<String> dijkstra = algoritmos.dijkstra(
                grafo2.construirComparable("Montevideo"),
                grafo2);

        for (String destino : grafo2.vertices()) {
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
                grafo2,
                grafo2.construirComparable("Montevideo"),
                v -> System.out.print(v + " "));

        System.out.println("\n\nRecorrido en amplitud desde Montevideo:");
        algoritmos.recorridoEnAmplitud(
                grafo2,
                grafo2.construirComparable("Montevideo"),
                v -> System.out.print(v + " "));

        System.out.println("\n\nClasificacion topologica:");
        System.out.println(algoritmos.calcularClasificacionTopologica(grafo2));

        System.out.println("\nTodos los caminos de Montevideo a Rocha:");
        List<Path<String>> caminos = algoritmos.obtenerTodosLosCaminos(
                grafo2.construirComparable("Montevideo"),
                grafo2.construirComparable("Rocha"),
                grafo2);

        for (Path<String> path : caminos) {
            System.out.println("Camino: " + path.getPath() + " | Costo: " + path.getCost());
        }

        System.out.println("\nFLOYD, WARSHALL Y CENTRO DEL GRAFO");

        Map<String, Set<Edge<String, WeightedEdge>>> adyacenciasPuertos = new HashMap<>();
        DirectedGraph<String, WeightedEdge> grafoPuertos = new DirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                adyacenciasPuertos);

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
                grafoPuertos.construirComparable("Buenos Aires")));

        System.out.println("\nCentro del grafo:");
        System.out.println(algoritmos.obtenerCentroGrafo(grafoPuertos));

        DirectedGraph<String, WeightedEdge> grafoEj3 = cargarGrafoDesdeArchivos(
                "aeropuertos.txt",
                "conexiones.txt");

        if (grafoEj3 == null) {
            System.out.println("No fue posible cargar el grafo.");
            return;
        }

        System.out.println("Grafo cargado correctamente.");

        System.out.println("Cantidad de aeropuertos: " + grafoEj3.vertices().size());

        System.out.println("Cantidad de conexiones: " + grafoEj3.aristas().size());

        for (Edge<String, WeightedEdge> arista : grafoEj3.aristas()) {

            DirectedEdge<String, WeightedEdge> a = (DirectedEdge<String, WeightedEdge>) arista;

            System.out.println(a.source() + " -> " + a.target() + " : " + a.dato().getWeight());
        }

        System.out.println("\nPrueba de Flujo de Grafo no Dirijido");

        UndirectedGraph<String, WeightedEdge> grafoNoDirigido = new UndirectedGraph<>(
            new LinkedHashSet<>(),
            new LinkedHashSet<>(),
            new HashMap<>()
        );

    grafoNoDirigido.agregarVertice("Nairobi");
    grafoNoDirigido.agregarVertice("Cairo");
    grafoNoDirigido.agregarVertice("Monrovia");
    grafoNoDirigido.agregarVertice("Garoua");
    grafoNoDirigido.agregarVertice("Mekele");
    grafoNoDirigido.agregarVertice("Praia");

    grafoNoDirigido.agregarArista("Nairobi", "Cairo", new WeightedEdge(1));
    grafoNoDirigido.agregarArista("Nairobi", "Monrovia", new WeightedEdge(1));
    grafoNoDirigido.agregarArista("Nairobi", "Garoua", new WeightedEdge(1));
    grafoNoDirigido.agregarArista("Monrovia", "Garoua", new WeightedEdge(1));
    grafoNoDirigido.agregarArista("Monrovia", "Mekele", new WeightedEdge(1));
    grafoNoDirigido.agregarArista("Garoua", "Mekele", new WeightedEdge(1));
    grafoNoDirigido.agregarArista("Mekele", "Praia", new WeightedEdge(1));

    IUndirectedGraphAlgorithm algoritmosNoDirigidos = new UndirectedGraphAlgorithm();

    System.out.println("Puntos de articulacion:");
    System.out.println(algoritmosNoDirigidos.puntosDeArticulacion(grafoNoDirigido));

    }

    private static DirectedGraph<String, WeightedEdge> cargarGrafoDesdeArchivos(
            String archivoAeropuertos, String archivoConexiones) {

        Map<String, Set<Edge<String, WeightedEdge>>> adyacencias = new HashMap<>();
        DirectedGraph<String, WeightedEdge> grafo = new DirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                adyacencias);

        // Cargar vértices (aeropuertos)
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAeropuertos))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    grafo.agregarVertice(linea);
                }
            }
            System.out.println("✓ Aeropuertos cargados desde " + archivoAeropuertos);
        } catch (IOException e) {
            System.err.println("✗ Error al leer " + archivoAeropuertos + ": " + e.getMessage());
            return null;
        }

        // Cargar aristas (conexiones)
        try (BufferedReader br = new BufferedReader(new FileReader(archivoConexiones))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(",");
                    if (partes.length == 3) {
                        String origen = partes[0].trim();
                        String destino = partes[1].trim();
                        double peso = Double.parseDouble(partes[2].trim());

                        grafo.agregarArista(origen, destino, new WeightedEdge(peso));
                    }
                }
            }
            System.out.println("✓ Conexiones cargadas desde " + archivoConexiones);
        } catch (IOException e) {
            System.err.println("✗ Error al leer " + archivoConexiones + ": " + e.getMessage());
            return null;
        }

        return grafo;
    }

    private static void demostrarRutaCritica(DirectedGraph<String, WeightedEdge> grafo) {
        System.out.println("=== ANALISIS DE RUTA CRITICA (CPM) ===\n");

        IDirectedGraphAlgorithms algoritmos = new DirectedGraphAlgorithms();

        // Encontrar vértices origen (sin entrada) y destino (sin salida)
        String origen = null;
        String destino = null;

        for (String v : grafo.vertices()) {
            int gradoEntrada = grafo.predecessors(grafo.construirComparable(v)).size();
            int gradoSalida = grafo.successors(grafo.construirComparable(v)).size();

            if (gradoEntrada == 0 && origen == null) {
                origen = v;
            }
            if (gradoSalida == 0 && destino == null) {
                destino = v;
            }
        }

        if (origen == null || destino == null) {
            System.out.println("No se encontraron vértices de origen o destino apropiados para la ruta crítica.");
            return;
        }

        System.out.println("Analizando ruta crítica desde: " + origen + " → " + destino);

        try {
            ICriticalPathResult<String> resultado = algoritmos.calcularRutaCritica(
                    grafo.construirComparable(origen),
                    grafo.construirComparable(destino),
                    grafo);

            System.out.println("\n CAMINO CRITICO:");
            System.out.println("Ruta: " + resultado.getCriticalPath().getPath());
            System.out.println("Costo Total: " + resultado.getCriticalCost() + " (MÁXIMO - Proyecto se atrasará)");

            System.out.println("\n TODOS LOS CAMINOS POSIBLES:");
            System.out.println("(Ordenados por holgura)");
            System.out.println("-".repeat(120));

            List<PathWithSlack<String>> pathsWithSlack = resultado.getPathsWithSlack();

            // Ordenar por holgura
            pathsWithSlack.sort((a, b) -> Double.compare(a.getSlack(), b.getSlack()));

            for (PathWithSlack<String> path : pathsWithSlack) {
                String marker = path.isCritical() ? " CRITICO" : "✓ Normal";
                System.out.printf(
                        "%s | %s | Costo: %.1f | Holgura: %.1f días%n",
                        marker,
                        path.getPath(),
                        path.getCost(),
                        path.getSlack());
            }

            System.out.println("-".repeat(120));
            System.out.println("\n INTERPRETACION:");
            System.out.println("• Camino Crítico: Cualquier retraso causa retraso en el proyecto");
            System.out.println("• Holgura: Días de tolerancia sin afectar la fecha de entrega");
            System.out.println("• Mayor holgura = Mayor flexibilidad en el cronograma");

        } catch (Exception e) {
            System.out.println("Error en análisis de ruta crítica: " + e.getMessage());
        }
    }

    private static void demostrarClasificacionTopologica() {
        System.out.println("\n\n=== CLASIFICACION TOPOLOGICA ===\n");
        System.out.println("La clasificación topológica es útil para:");
        System.out.println("1. SISTEMA DE PREVIATURAS: Requisitos previos de asignaturas en una carrera");
        System.out.println("2. PLAN DE PROYECTO: Orden de ejecución de tareas con dependencias\n");

        IDirectedGraphAlgorithms algoritmos = new DirectedGraphAlgorithms();

        // EJEMPLO 1: SISTEMA DE PREVIATURAS
        demostrarSistemaPreviaturas(algoritmos);

        // EJEMPLO 2: PLAN DE PROYECTO
        demostrarPlanProyecto(algoritmos);
    }

    private static void demostrarSistemaPreviaturas(IDirectedGraphAlgorithms algoritmos) {
        System.out.println("\n EJEMPLO 1: SISTEMA DE PREVIATURAS DE CARRERA");
        System.out.println("Arista A → B significa: B requiere haber cursado A\n");

        Map<String, Set<Edge<String, WeightedEdge>>> adyacencias = new HashMap<>();
        DirectedGraph<String, WeightedEdge> grafoAsignaturas = new DirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                adyacencias);

        // Agregar asignaturas
        String[] asignaturas = {
                "Programación I", "Programación II", "Estructuras de Datos",
                "Algoritmos", "Base de Datos", "Ingeniería de Software"
        };

        for (String asignatura : asignaturas) {
            grafoAsignaturas.agregarVertice(asignatura);
        }

        // Agregar relaciones de prerequisitos
        grafoAsignaturas.agregarArista("Programación I", "Programación II", new WeightedEdge(1));
        grafoAsignaturas.agregarArista("Programación I", "Estructuras de Datos", new WeightedEdge(1));
        grafoAsignaturas.agregarArista("Programación II", "Algoritmos", new WeightedEdge(1));
        grafoAsignaturas.agregarArista("Estructuras de Datos", "Algoritmos", new WeightedEdge(1));
        grafoAsignaturas.agregarArista("Programación II", "Base de Datos", new WeightedEdge(1));
        grafoAsignaturas.agregarArista("Base de Datos", "Ingeniería de Software", new WeightedEdge(1));
        grafoAsignaturas.agregarArista("Algoritmos", "Ingeniería de Software", new WeightedEdge(1));

        try {
            List<String> ordenTopologico = algoritmos.calcularClasificacionTopologica(grafoAsignaturas);

            System.out.println(" Orden recomendado de cursado:\n");
            int semestre = 1;
            for (int i = 0; i < ordenTopologico.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, ordenTopologico.get(i));
                if ((i + 1) % 2 == 0 && i < ordenTopologico.size() - 1) {
                    System.out.println();
                }
            }

        } catch (IllegalStateException e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private static void demostrarPlanProyecto(IDirectedGraphAlgorithms algoritmos) {
        System.out.println("\n\n  EJEMPLO 2: PLAN DE PROYECTO CON DEPENDENCIAS");
        System.out.println("Arista T1 → T2 significa: T2 depende de que T1 esté completada\n");

        Map<String, Set<Edge<String, WeightedEdge>>> adyacencias = new HashMap<>();
        DirectedGraph<String, WeightedEdge> grafoProyecto = new DirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                adyacencias);

        // Agregar tareas del proyecto
        String[] tareas = {
                "Planificación",
                "Diseño UI",
                "Diseño BD",
                "Desarrollo Backend",
                "Desarrollo Frontend",
                "Testing",
                "Deployment",
                "Documentación"
        };

        for (String tarea : tareas) {
            grafoProyecto.agregarVertice(tarea);
        }

        // Agregar dependencias del proyecto
        grafoProyecto.agregarArista("Planificación", "Diseño UI", new WeightedEdge(5));
        grafoProyecto.agregarArista("Planificación", "Diseño BD", new WeightedEdge(5));
        grafoProyecto.agregarArista("Diseño UI", "Desarrollo Frontend", new WeightedEdge(10));
        grafoProyecto.agregarArista("Diseño BD", "Desarrollo Backend", new WeightedEdge(8));
        grafoProyecto.agregarArista("Desarrollo Frontend", "Testing", new WeightedEdge(7));
        grafoProyecto.agregarArista("Desarrollo Backend", "Testing", new WeightedEdge(7));
        grafoProyecto.agregarArista("Testing", "Deployment", new WeightedEdge(3));
        grafoProyecto.agregarArista("Deployment", "Documentación", new WeightedEdge(4));

        try {
            List<String> ordenTopologico = algoritmos.calcularClasificacionTopologica(grafoProyecto);

            System.out.println(" Orden de ejecución de tareas:\n");
            for (int i = 0; i < ordenTopologico.size(); i++) {
                System.out.printf("  Fase %d: %s%n", i + 1, ordenTopologico.get(i));
            }

            System.out.println("\n Validación: El proyecto NO tiene ciclos → Es ejecutable");

        } catch (IllegalStateException e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }
}