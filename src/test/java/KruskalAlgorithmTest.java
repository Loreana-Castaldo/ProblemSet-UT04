import java.util.HashMap;
import java.util.LinkedHashSet;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

public class KruskalAlgorithmTest extends TestCase {

    private IUndirectedGraphAlgorithm algoritmos;

    @Override
    protected void setUp() {
        algoritmos = new UndirectedGraphAlgorithm();
    }

    public void testKruskalGrafoConexoValidoDevuelveArbolMinimo() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona1", "Zona3", new WeightedEdge(4));
        grafo.agregarArista("Zona2", "Zona3", new WeightedEdge(2));
        grafo.agregarArista("Zona2", "Zona4", new WeightedEdge(5));
        grafo.agregarArista("Zona3", "Zona4", new WeightedEdge(3));

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.kruskal(grafo);

        assertNotNull(resultado);

        // Un árbol de expansión mínimo debe tener todos los vértices.
        assertEquals(grafo.cantidadDeVertices(), resultado.cantidadDeVertices());

        // Si hay V vértices, el árbol debe tener V - 1 aristas.
        assertEquals(grafo.cantidadDeVertices() - 1, resultado.cantidadDeAristas());

        // Costo mínimo esperado.
        assertEquals(6.0, sumarPesos(resultado), 0.001);

        // Estas son las aristas mínimas esperadas para este grafo.
        assertTrue(contieneArista(resultado, "Zona1", "Zona2"));
        assertTrue(contieneArista(resultado, "Zona2", "Zona3"));
        assertTrue(contieneArista(resultado, "Zona3", "Zona4"));

        // Si conecta todos los vértices y tiene V - 1 aristas, es un árbol.
        assertTrue(resultado.esConexo());
    }

    public void testKruskalGrafoCompletoParte1DevuelveCostoMinimo() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoParte1();

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.kruskal(grafo);

        assertNotNull(resultado);

        assertEquals(grafo.cantidadDeVertices(), resultado.cantidadDeVertices());
        assertEquals(grafo.cantidadDeVertices() - 1, resultado.cantidadDeAristas());

        // Costo mínimo obtenido en el Ejercicio 9.
        assertEquals(7.0, sumarPesos(resultado), 0.001);

        assertTrue(resultado.esConexo());
    }

    public void testKruskalGrafoConUnSoloVerticeDevuelveArbolSinAristas() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        grafo.agregarVertice("Zona1");

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.kruskal(grafo);

        assertNotNull(resultado);

        assertEquals(1, resultado.cantidadDeVertices());
        assertEquals(0, resultado.cantidadDeAristas());
        assertEquals(0.0, sumarPesos(resultado), 0.001);
    }

    public void testKruskalGrafoVacioDevuelveGrafoVacio() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.kruskal(grafo);

        assertNotNull(resultado);

        assertEquals(0, resultado.cantidadDeVertices());
        assertEquals(0, resultado.cantidadDeAristas());
        assertEquals(0.0, sumarPesos(resultado), 0.001);
    }

    public void testKruskalGrafoNoConexoLanzaError() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona3", "Zona4", new WeightedEdge(2));

        try {
            algoritmos.kruskal(grafo);
            fail("Se esperaba error porque el grafo no es conexo.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("conexo"));
        }
    }

    public void testKruskalGrafoNullLanzaError() {
        try {
            algoritmos.kruskal(null);
            fail("Se esperaba error porque el grafo es null.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("null"));
        }
    }

    public void testKruskalConEmpatesNoExigeAristasExactas() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona1", "Zona3", new WeightedEdge(1));
        grafo.agregarArista("Zona2", "Zona4", new WeightedEdge(2));
        grafo.agregarArista("Zona3", "Zona4", new WeightedEdge(2));

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.kruskal(grafo);

        assertNotNull(resultado);

        // No se exige una combinación exacta porque hay empates.
        // Se verifica que sea un árbol mínimo válido.
        assertEquals(grafo.cantidadDeVertices(), resultado.cantidadDeVertices());
        assertEquals(grafo.cantidadDeVertices() - 1, resultado.cantidadDeAristas());
        assertEquals(4.0, sumarPesos(resultado), 0.001);
        assertTrue(resultado.esConexo());
    }

    public void testKruskalNoModificaGrafoOriginal() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoParte1();

        int verticesAntes = grafo.cantidadDeVertices();
        int aristasAntes = grafo.cantidadDeAristas();

        algoritmos.kruskal(grafo);

        assertEquals(verticesAntes, grafo.cantidadDeVertices());
        assertEquals(aristasAntes, grafo.cantidadDeAristas());
    }

    private IUndirectedGraph<String, WeightedEdge> crearGrafoParte1() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4", "Zona5", "Zona6");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona1", "Zona3", new WeightedEdge(5));
        grafo.agregarArista("Zona1", "Zona4", new WeightedEdge(4));
        grafo.agregarArista("Zona1", "Zona5", new WeightedEdge(7));
        grafo.agregarArista("Zona1", "Zona6", new WeightedEdge(2));

        grafo.agregarArista("Zona2", "Zona3", new WeightedEdge(1));
        grafo.agregarArista("Zona2", "Zona4", new WeightedEdge(3));
        grafo.agregarArista("Zona2", "Zona5", new WeightedEdge(1));
        grafo.agregarArista("Zona2", "Zona6", new WeightedEdge(6));

        grafo.agregarArista("Zona3", "Zona4", new WeightedEdge(2));
        grafo.agregarArista("Zona3", "Zona5", new WeightedEdge(3));
        grafo.agregarArista("Zona3", "Zona6", new WeightedEdge(5));

        grafo.agregarArista("Zona4", "Zona5", new WeightedEdge(7));
        grafo.agregarArista("Zona4", "Zona6", new WeightedEdge(2));

        return grafo;
    }

    private IUndirectedGraph<String, WeightedEdge> crearGrafoVacio() {
        return new UndirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new HashMap<>()
        );
    }

    private void agregarVertices(
            IUndirectedGraph<String, WeightedEdge> grafo,
            String... vertices) {

        for (String vertice : vertices) {
            grafo.agregarVertice(vertice);
        }
    }

    private double sumarPesos(IUndirectedGraph<String, WeightedEdge> grafo) {
        double suma = 0;

        for (Edge<String, WeightedEdge> arista : grafo.aristas()) {
            suma += arista.dato().getWeight();
        }

        return suma;
    }

    private boolean contieneArista(
            IUndirectedGraph<String, WeightedEdge> grafo,
            String origen,
            String destino) {

        return grafo.obtenerArista(
                grafo.construirComparable(origen),
                grafo.construirComparable(destino)
        ) != null;
    }
}