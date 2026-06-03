

import junit.framework.TestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import ucu.edu.aed.tda.grafo.model.result.Path;

public class DirectedGraphAlgorithmsTest extends TestCase {

    private DirectedGraphAlgorithms algoritmos;

    @Override
    protected void setUp() {
        algoritmos = new DirectedGraphAlgorithms();
    }

    public void testDijkstraCalculaCostoYCaminoMinimo() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C", "D"),
                Arrays.asList(
                        edge("A", "B", 2.0),
                        edge("A", "C", 5.0),
                        edge("B", "C", 1.0),
                        edge("C", "D", 3.0)
                )
        );

        IDijkstraResult<String> resultado = algoritmos.dijkstra(criterio("A"), grafo);

        assertEquals(0.0, resultado.getCost("A"), 0.0001);
        assertEquals(2.0, resultado.getCost("B"), 0.0001);
        assertEquals(3.0, resultado.getCost("C"), 0.0001);
        assertEquals(6.0, resultado.getCost("D"), 0.0001);

        assertEquals(Arrays.asList("A", "B", "C", "D"), resultado.getPath("D"));
    }

    public void testDijkstraConOrigenInexistenteRetornaCostosInfinitos() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B"),
                Arrays.asList(edge("A", "B", 2.0))
        );

        IDijkstraResult<String> resultado = algoritmos.dijkstra(criterio("X"), grafo);

        assertTrue(Double.isInfinite(resultado.getCost("A")));
        assertTrue(Double.isInfinite(resultado.getCost("B")));
        assertTrue(resultado.getPath("A").isEmpty());
    }

    public void testDijkstraConPesoNegativoLanzaExcepcion() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B"),
                Arrays.asList(edge("A", "B", -1.0))
        );

        try {
            algoritmos.dijkstra(criterio("A"), grafo);
            fail("Se esperaba IllegalArgumentException por peso negativo.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("pesos negativos"));
        }
    }

    public void testFloydCalculaElCaminoMinimoEntreTodosLosVertices() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C"),
                Arrays.asList(
                        edge("A", "B", 2.0),
                        edge("B", "C", 3.0),
                        edge("A", "C", 10.0)
                )
        );

        IFloydWarshallResult<String> resultado = algoritmos.floyd(grafo);

        assertEquals(0.0, resultado.getCost("A", "A"), 0.0001);
        assertEquals(2.0, resultado.getCost("A", "B"), 0.0001);
        assertEquals(5.0, resultado.getCost("A", "C"), 0.0001);
        assertEquals(3.0, resultado.getCost("B", "C"), 0.0001);
        assertTrue(Double.isInfinite(resultado.getCost("C", "A")));
    }

    public void testWarshallCalculaAlcanzabilidad() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C"),
                Arrays.asList(
                        edge("A", "B", 1.0),
                        edge("B", "C", 1.0)
                )
        );

        IFloydWarshallResult<String> resultado = algoritmos.warshall(grafo);

        assertEquals(0.0, resultado.getCost("A", "A"), 0.0001);
        assertEquals(0.0, resultado.getCost("A", "B"), 0.0001);
        assertEquals(0.0, resultado.getCost("A", "C"), 0.0001);

        assertTrue(Double.isInfinite(resultado.getCost("C", "A")));
        assertTrue(Double.isInfinite(resultado.getCost("C", "B")));
    }

    public void testObtenerExcentricidadRetornaMayorCostoDesdeUnVertice() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C"),
                Arrays.asList(
                        edge("A", "B", 2.0),
                        edge("B", "C", 3.0),
                        edge("A", "C", 10.0)
                )
        );

        double excentricidad = algoritmos.obtenerExcentricidad(grafo, criterio("A"));

        assertEquals(5.0, excentricidad, 0.0001);
    }

    public void testObtenerExcentricidadConVerticeInexistenteRetornaInfinito() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B"),
                Arrays.asList(edge("A", "B", 2.0))
        );

        double excentricidad = algoritmos.obtenerExcentricidad(grafo, criterio("X"));

        assertTrue(Double.isInfinite(excentricidad));
    }

    public void testObtenerCentroGrafoRetornaElVerticeConMenorExcentricidad() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C"),
                Arrays.asList(
                        edge("A", "B", 2.0),
                        edge("B", "C", 3.0),
                        edge("A", "C", 10.0)
                )
        );

        String centro = algoritmos.obtenerCentroGrafo(grafo);

        assertEquals("A", centro);
    }

    public void testObtenerTodosLosCaminosRetornaTodosLosCaminosSimples() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C"),
                Arrays.asList(
                        edge("A", "B", 1.0),
                        edge("B", "C", 2.0),
                        edge("A", "C", 5.0)
                )
        );

        List<Path<String>> caminos = algoritmos.obtenerTodosLosCaminos(
                criterio("A"),
                criterio("C"),
                grafo
        );

        assertEquals(2, caminos.size());
    }

    public void testObtenerTodosLosCaminosConOrigenInexistenteRetornaListaVacia() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B"),
                Arrays.asList(edge("A", "B", 1.0))
        );

        List<Path<String>> caminos = algoritmos.obtenerTodosLosCaminos(
                criterio("X"),
                criterio("B"),
                grafo
        );

        assertTrue(caminos.isEmpty());
    }

    public void testRecorridoEnProfundidadVisitaVerticesEnOrdenDFS() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C", "D"),
                Arrays.asList(
                        edge("A", "B", 1.0),
                        edge("A", "C", 1.0),
                        edge("B", "D", 1.0),
                        edge("C", "D", 1.0)
                )
        );

        final List<String> visitados = new ArrayList<String>();

        algoritmos.recorridoEnProfundidad(grafo, criterio("A"), visitados::add);

        assertEquals(Arrays.asList("A", "B", "D", "C"), visitados);
    }

    public void testRecorridoEnAmplitudVisitaVerticesEnOrdenBFS() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C", "D"),
                Arrays.asList(
                        edge("A", "B", 1.0),
                        edge("A", "C", 1.0),
                        edge("B", "D", 1.0),
                        edge("C", "D", 1.0)
                )
        );

        final List<String> visitados = new ArrayList<String>();

        algoritmos.recorridoEnAmplitud(grafo, criterio("A"), visitados::add);

        assertEquals(Arrays.asList("A", "B", "C", "D"), visitados);
    }

    public void testRecorridosConOrigenInexistenteNoVisitaNada() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B"),
                Arrays.asList(edge("A", "B", 1.0))
        );

        final List<String> visitadosDFS = new ArrayList<String>();
        final List<String> visitadosBFS = new ArrayList<String>();

        algoritmos.recorridoEnProfundidad(grafo, criterio("X"), visitadosDFS::add);
        algoritmos.recorridoEnAmplitud(grafo, criterio("X"), visitadosBFS::add);

        assertTrue(visitadosDFS.isEmpty());
        assertTrue(visitadosBFS.isEmpty());
    }

    public void testClasificacionTopologicaRespetaDependencias() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C", "D"),
                Arrays.asList(
                        edge("A", "B", 1.0),
                        edge("A", "C", 1.0),
                        edge("B", "D", 1.0),
                        edge("C", "D", 1.0)
                )
        );

        List<String> orden = algoritmos.calcularClasificacionTopologica(grafo);

        assertEquals(4, orden.size());

        assertTrue(orden.indexOf("A") < orden.indexOf("B"));
        assertTrue(orden.indexOf("A") < orden.indexOf("C"));
        assertTrue(orden.indexOf("B") < orden.indexOf("D"));
        assertTrue(orden.indexOf("C") < orden.indexOf("D"));
    }

    public void testClasificacionTopologicaConCicloLanzaExcepcion() {
        IDirectedIGraph<String, WeightedEdge> grafo = crearGrafo(
                Arrays.asList("A", "B", "C"),
                Arrays.asList(
                        edge("A", "B", 1.0),
                        edge("B", "C", 1.0),
                        edge("C", "A", 1.0)
                )
        );

        try {
            algoritmos.calcularClasificacionTopologica(grafo);
            fail("Se esperaba IllegalStateException porque el grafo tiene ciclos.");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("ciclos"));
        }
    }

    private static Comparable<String> criterio(String valor) {
        return new CriterioString(valor);
    }

    private static Edge<String, WeightedEdge> edge(
            final String source,
            final String target,
            double peso) {

        final WeightedEdge dato = crearWeightedEdge(peso);

        return new Edge<String, WeightedEdge>() {

            public String source() {
                return source;
            }

            public String target() {
                return target;
            }

            public WeightedEdge dato() {
                return dato;
            }

            public boolean directed() {
                return true;
            }
        };
    }

    private static WeightedEdge crearWeightedEdge(final double peso) {
        try {
            if (WeightedEdge.class.isInterface()) {
                return (WeightedEdge) Proxy.newProxyInstance(
                        WeightedEdge.class.getClassLoader(),
                        new Class[] { WeightedEdge.class },
                        new InvocationHandler() {
                            public Object invoke(Object proxy, Method method, Object[] args) {
                                if ("getWeight".equals(method.getName())) {
                                    return peso;
                                }

                                if ("toString".equals(method.getName())) {
                                    return "WeightedEdge(" + peso + ")";
                                }

                                if ("hashCode".equals(method.getName())) {
                                    return Double.valueOf(peso).hashCode();
                                }

                                if ("equals".equals(method.getName())) {
                                    return proxy == args[0];
                                }

                                throw new UnsupportedOperationException(method.getName());
                            }
                        }
                );
            }

            Constructor<WeightedEdge> constructor = WeightedEdge.class.getDeclaredConstructor(double.class);
            constructor.setAccessible(true);
            return constructor.newInstance(peso);

        } catch (Exception e) {
            throw new RuntimeException(
                    "No se pudo crear WeightedEdge. Revisá si WeightedEdge es interface o si tiene constructor con double.",
                    e
            );
        }
    }

    @SuppressWarnings("unchecked")
    private static IDirectedIGraph<String, WeightedEdge> crearGrafo(
            final List<String> vertices,
            final List<Edge<String, WeightedEdge>> aristas) {

        return (IDirectedIGraph<String, WeightedEdge>) Proxy.newProxyInstance(
                IDirectedIGraph.class.getClassLoader(),
                new Class[] { IDirectedIGraph.class },
                new InvocationHandler() {

                    public Object invoke(Object proxy, Method method, Object[] args) {
                        String nombreMetodo = method.getName();

                        if ("vertices".equals(nombreMetodo)) {
                            return new ListaSet<String>(vertices);
                        }

                        if ("aristas".equals(nombreMetodo)) {
                            return new ListaSet<Edge<String, WeightedEdge>>(aristas);
                        }

                        if ("buscarVertice".equals(nombreMetodo)) {
                            Comparable<String> criterio = (Comparable<String>) args[0];

                            for (String vertice : vertices) {
                                if (criterio.compareTo(vertice) == 0) {
                                    return vertice;
                                }
                            }

                            return null;
                        }

                        if ("construirComparable".equals(nombreMetodo)) {
                            return criterio((String) args[0]);
                        }

                        if ("adyacencias".equals(nombreMetodo)) {
                            Comparable<String> criterio = (Comparable<String>) args[0];
                            List<Edge<String, WeightedEdge>> adyacentes =
                                    new ArrayList<Edge<String, WeightedEdge>>();

                            for (Edge<String, WeightedEdge> arista : aristas) {
                                if (criterio.compareTo(arista.source()) == 0) {
                                    adyacentes.add(arista);
                                }
                            }

                            return adyacentes;
                        }

                        if ("successors".equals(nombreMetodo)) {
                            Comparable<String> criterio = (Comparable<String>) args[0];
                            ListaSet<String> sucesores = new ListaSet<String>();

                            for (Edge<String, WeightedEdge> arista : aristas) {
                                if (criterio.compareTo(arista.source()) == 0) {
                                    sucesores.add(arista.target());
                                }
                            }

                            return sucesores;
                        }

                        if ("toString".equals(nombreMetodo)) {
                            return "GrafoDePrueba";
                        }

                        if ("hashCode".equals(nombreMetodo)) {
                            return System.identityHashCode(proxy);
                        }

                        if ("equals".equals(nombreMetodo)) {
                            return proxy == args[0];
                        }

                        throw new UnsupportedOperationException(
                                "Método no implementado en el grafo de prueba: " + nombreMetodo
                        );
                    }
                }
        );
    }

    private static class CriterioString implements Comparable<String> {

        private final String valor;

        public CriterioString(String valor) {
            this.valor = valor;
        }

        public int compareTo(String otro) {
            return valor.compareTo(otro);
        }
    }

    private static class ListaSet<E> extends ArrayList<E> implements java.util.Set<E> {

        public ListaSet() {
            super();
        }

        public ListaSet(Collection<E> elementos) {
            super();

            for (E elemento : elementos) {
                add(elemento);
            }
        }

        @Override
        public boolean add(E elemento) {
            if (!contains(elemento)) {
                return super.add(elemento);
            }

            return false;
        }
    }
}