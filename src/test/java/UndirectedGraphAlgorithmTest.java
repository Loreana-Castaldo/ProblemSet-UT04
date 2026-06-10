import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

public class UndirectedGraphAlgorithmTest extends TestCase {

    private IUndirectedGraphAlgorithm algoritmos;

    @Override
    protected void setUp() {
        algoritmos = new UndirectedGraphAlgorithm();
    }

    public void testPuntosDeArticulacionGrafoDelEjercicio() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo,
                "Nairobi",
                "Cairo",
                "Monrovia",
                "Garoua",
                "Mekele",
                "Praia"
        );

        grafo.agregarArista("Nairobi", "Cairo", new WeightedEdge(1));
        grafo.agregarArista("Nairobi", "Monrovia", new WeightedEdge(1));
        grafo.agregarArista("Nairobi", "Garoua", new WeightedEdge(1));
        grafo.agregarArista("Monrovia", "Garoua", new WeightedEdge(1));
        grafo.agregarArista("Monrovia", "Mekele", new WeightedEdge(1));
        grafo.agregarArista("Garoua", "Mekele", new WeightedEdge(1));
        grafo.agregarArista("Mekele", "Praia", new WeightedEdge(1));

        List<String> puntos = algoritmos.puntosDeArticulacion(grafo);

        assertEquals(2, puntos.size());
        assertTrue(puntos.contains("Nairobi"));
        assertTrue(puntos.contains("Mekele"));

        assertFalse(puntos.contains("Cairo"));
        assertFalse(puntos.contains("Monrovia"));
        assertFalse(puntos.contains("Garoua"));
        assertFalse(puntos.contains("Praia"));
    }

    public void testGrafoEnCadenaTienePuntosIntermedios() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C", "D");

        grafo.agregarArista("A", "B", new WeightedEdge(1));
        grafo.agregarArista("B", "C", new WeightedEdge(1));
        grafo.agregarArista("C", "D", new WeightedEdge(1));

        List<String> puntos = algoritmos.puntosDeArticulacion(grafo);

        assertEquals(2, puntos.size());
        assertTrue(puntos.contains("B"));
        assertTrue(puntos.contains("C"));

        assertFalse(puntos.contains("A"));
        assertFalse(puntos.contains("D"));
    }

    public void testGrafoEnCicloNoTienePuntosDeArticulacion() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C", "D");

        grafo.agregarArista("A", "B", new WeightedEdge(1));
        grafo.agregarArista("B", "C", new WeightedEdge(1));
        grafo.agregarArista("C", "D", new WeightedEdge(1));
        grafo.agregarArista("D", "A", new WeightedEdge(1));

        List<String> puntos = algoritmos.puntosDeArticulacion(grafo);

        assertTrue(puntos.isEmpty());
    }

    public void testGrafoEstrellaTieneCentroComoPuntoDeArticulacion() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Centro", "A", "B", "C");

        grafo.agregarArista("Centro", "A", new WeightedEdge(1));
        grafo.agregarArista("Centro", "B", new WeightedEdge(1));
        grafo.agregarArista("Centro", "C", new WeightedEdge(1));

        List<String> puntos = algoritmos.puntosDeArticulacion(grafo);

        assertEquals(1, puntos.size());
        assertTrue(puntos.contains("Centro"));

        assertFalse(puntos.contains("A"));
        assertFalse(puntos.contains("B"));
        assertFalse(puntos.contains("C"));
    }

    public void testGrafoVacioDevuelveListaVacia() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        List<String> puntos = algoritmos.puntosDeArticulacion(grafo);

        assertNotNull(puntos);
        assertTrue(puntos.isEmpty());
    }

    public void testGrafoConUnSoloVerticeNoTienePuntosDeArticulacion() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        grafo.agregarVertice("A");

        List<String> puntos = algoritmos.puntosDeArticulacion(grafo);

        assertTrue(puntos.isEmpty());
    }

    public void testMetodoNoModificaCantidadDeVerticesNiAristas() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C");

        grafo.agregarArista("A", "B", new WeightedEdge(1));
        grafo.agregarArista("B", "C", new WeightedEdge(1));

        int cantidadVerticesAntes = grafo.cantidadDeVertices();
        int cantidadAristasAntes = grafo.cantidadDeAristas();

        algoritmos.puntosDeArticulacion(grafo);

        assertEquals(cantidadVerticesAntes, grafo.cantidadDeVertices());
        assertEquals(cantidadAristasAntes, grafo.cantidadDeAristas());
    }

    public void testBeaConGrafoNullNoHaceNada() {
        List<String> visitados = new ArrayList<>();

        try {
            algoritmos.bea(null, x -> visitados.add((String) x));
        } catch (Exception e) {
            fail("bea no debería lanzar excepción si el grafo es null");
        }

        assertTrue(visitados.isEmpty());
    }

    public void testBeaConConsumerNullNoHaceNada() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B");
        grafo.agregarArista("A", "B", new WeightedEdge(1));

        try {
            algoritmos.bea(grafo, null);
        } catch (Exception e) {
            fail("bea no debería lanzar excepción si el consumer es null");
        }
    }

    public void testBeaConGrafoVacioNoVisitaNada() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        List<String> visitados = new ArrayList<>();

        algoritmos.bea(grafo, visitados::add);

        assertTrue(visitados.isEmpty());
    }

    public void testBeaConUnSoloVerticeLoVisita() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        grafo.agregarVertice("A");

        List<String> visitados = new ArrayList<>();

        algoritmos.bea(grafo, visitados::add);

        assertEquals(1, visitados.size());
        assertEquals("A", visitados.get(0));
    }

    public void testBeaRecorreGrafoConexoEnAnchura() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C", "D");

        /*
              A
             / \
            B   C
            |
            D

            BEA esperado desde A:
            A, B, C, D
        */

        grafo.agregarArista("A", "B", new WeightedEdge(1));
        grafo.agregarArista("A", "C", new WeightedEdge(1));
        grafo.agregarArista("B", "D", new WeightedEdge(1));

        List<String> visitados = new ArrayList<>();

        algoritmos.bea(grafo, visitados::add);

        assertEquals(4, visitados.size());
        assertEquals("A", visitados.get(0));
        assertEquals("B", visitados.get(1));
        assertEquals("C", visitados.get(2));
        assertEquals("D", visitados.get(3));
    }

    public void testBeaNoRepiteVerticesEnGrafoConCiclo() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C");

        /*
            A -- B
             \  /
              C
        */

        grafo.agregarArista("A", "B", new WeightedEdge(1));
        grafo.agregarArista("B", "C", new WeightedEdge(1));
        grafo.agregarArista("A", "C", new WeightedEdge(1));

        List<String> visitados = new ArrayList<>();

        algoritmos.bea(grafo, visitados::add);

        assertEquals(3, visitados.size());
        assertTrue(visitados.contains("A"));
        assertTrue(visitados.contains("B"));
        assertTrue(visitados.contains("C"));

        assertEquals(visitados.size(), new HashSet<>(visitados).size());
    }

    public void testBeaRecorreTodasLasComponentesConexas() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C", "D", "E");

        /*
            Componente 1:
            A -- B -- C

            Componente 2:
            D -- E
        */

        grafo.agregarArista("A", "B", new WeightedEdge(1));
        grafo.agregarArista("B", "C", new WeightedEdge(1));
        grafo.agregarArista("D", "E", new WeightedEdge(1));

        List<String> visitados = new ArrayList<>();

        algoritmos.bea(grafo, visitados::add);

        assertEquals(5, visitados.size());

        assertTrue(visitados.contains("A"));
        assertTrue(visitados.contains("B"));
        assertTrue(visitados.contains("C"));
        assertTrue(visitados.contains("D"));
        assertTrue(visitados.contains("E"));

        assertEquals(visitados.size(), new HashSet<>(visitados).size());
    }

    public void testBeaConVerticesAisladosLosVisitaTodos() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C");

        List<String> visitados = new ArrayList<>();

        algoritmos.bea(grafo, visitados::add);

        assertEquals(3, visitados.size());

        assertTrue(visitados.contains("A"));
        assertTrue(visitados.contains("B"));
        assertTrue(visitados.contains("C"));

        assertEquals(visitados.size(), new HashSet<>(visitados).size());
    }

    public void testBeaNoModificaCantidadDeVerticesNiAristas() {
        UndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "A", "B", "C");

        grafo.agregarArista("A", "B", new WeightedEdge(1));
        grafo.agregarArista("B", "C", new WeightedEdge(1));

        int cantidadVerticesAntes = grafo.cantidadDeVertices();
        int cantidadAristasAntes = grafo.cantidadDeAristas();

        algoritmos.bea(grafo, vertice -> { });

        assertEquals(cantidadVerticesAntes, grafo.cantidadDeVertices());
        assertEquals(cantidadAristasAntes, grafo.cantidadDeAristas());
    }

    private UndirectedGraph<String, WeightedEdge> crearGrafoVacio() {
        return new UndirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new HashMap<>()
        );
    }

    private void agregarVertices(UndirectedGraph<String, WeightedEdge> grafo, String... vertices) {
        for (String vertice : vertices) {
            grafo.agregarVertice(vertice);
        }
    }
}