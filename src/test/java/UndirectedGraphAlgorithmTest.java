import java.util.HashMap;
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