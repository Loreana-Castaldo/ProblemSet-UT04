import java.util.HashMap;
import java.util.LinkedHashSet;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

public class PrimAlgorithmTest extends TestCase {

    private IUndirectedGraphAlgorithm algoritmos;

    @Override
    protected void setUp() {
        algoritmos = new UndirectedGraphAlgorithm();
    }

    public void testPrimGrafoConexoValidoDevuelveArbolMinimo() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona1", "Zona3", new WeightedEdge(4));
        grafo.agregarArista("Zona2", "Zona3", new WeightedEdge(2));
        grafo.agregarArista("Zona2", "Zona4", new WeightedEdge(5));
        grafo.agregarArista("Zona3", "Zona4", new WeightedEdge(3));

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.prim(grafo, grafo.construirComparable("Zona1"));

        assertNotNull(resultado);

        // Si hay V vértices, el árbol debe tener V - 1 aristas.
        assertEquals(grafo.cantidadDeVertices(), resultado.cantidadDeVertices());
        assertEquals(grafo.cantidadDeVertices() - 1, resultado.cantidadDeAristas());

        // Costo mínimo esperado.
        assertEquals(6.0, sumarPesos(resultado));

        // Aristas esperadas del árbol mínimo.
        assertTrue(contieneArista(resultado, "Zona1", "Zona2"));
        assertTrue(contieneArista(resultado, "Zona2", "Zona3"));
        assertTrue(contieneArista(resultado, "Zona3", "Zona4"));

        // El resultado debe conectar todos los vértices.
        assertTrue(resultado.esConexo());
    }

    public void testPrimGrafoCompletoParte1DevuelveCostoMinimo() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoParte1();

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.prim(grafo, grafo.construirComparable("Zona1"));

        assertNotNull(resultado);

        assertEquals(grafo.cantidadDeVertices(), resultado.cantidadDeVertices());
        assertEquals(grafo.cantidadDeVertices() - 1, resultado.cantidadDeAristas());

        // Costo mínimo de la Parte 1.
        assertEquals(7.0, sumarPesos(resultado));

        assertTrue(resultado.esConexo());
    }

    public void testPrimGrafoConUnSoloVerticeDevuelveArbolSinAristas() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        grafo.agregarVertice("Zona1");

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.prim(grafo, grafo.construirComparable("Zona1"));

        assertNotNull(resultado);

        assertEquals(1, resultado.cantidadDeVertices());
        assertEquals(0, resultado.cantidadDeAristas());
        assertEquals(0.0, sumarPesos(resultado));
    }

    public void testPrimGrafoNoConexoLanzaError() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona3", "Zona4", new WeightedEdge(2));

        try {
            algoritmos.prim(grafo, grafo.construirComparable("Zona1"));
            fail("Se esperaba error porque el grafo no es conexo.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("conexo"));
        }
    }

    public void testPrimOrigenInexistenteLanzaError() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona2", "Zona3", new WeightedEdge(2));

        try {
            algoritmos.prim(grafo, grafo.construirComparable("Zona9"));
            fail("Se esperaba error porque el origen no pertenece al grafo.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("origen"));
        }
    }

    public void testPrimGrafoNullLanzaError() {
        try {
            algoritmos.prim(null, null);
            fail("Se esperaba error por parámetros inválidos.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Parámetros"));
        }
    }

    public void testPrimOrigenNullLanzaError() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        grafo.agregarVertice("Zona1");

        try {
            algoritmos.prim(grafo, null);
            fail("Se esperaba error por origen null.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Parámetros"));
        }
    }

    public void testPrimConEmpatesNoExigeAristasExactas() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4");

        grafo.agregarArista("Zona1", "Zona2", new WeightedEdge(1));
        grafo.agregarArista("Zona1", "Zona3", new WeightedEdge(1));
        grafo.agregarArista("Zona2", "Zona4", new WeightedEdge(2));
        grafo.agregarArista("Zona3", "Zona4", new WeightedEdge(2));

        IUndirectedGraph<String, WeightedEdge> resultado =
                algoritmos.prim(grafo, grafo.construirComparable("Zona1"));

        assertNotNull(resultado);

        // No importa exactamente cuál arista empatada eligió.
        // Importa que el resultado sea un árbol mínimo válido.
        assertEquals(grafo.cantidadDeVertices(), resultado.cantidadDeVertices());
        assertEquals(grafo.cantidadDeVertices() - 1, resultado.cantidadDeAristas());
        assertEquals(4.0, sumarPesos(resultado));
        assertTrue(resultado.esConexo());
    }

    public void testSearchMinEdgeDevuelveMenorAristaEntreVisitadosYNoVisitados() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoVacio();

        agregarVertices(grafo, "Zona1", "Zona2", "Zona3", "Zona4");

        grafo.agregarArista("Zona1", "Zona3", new WeightedEdge(5));
        grafo.agregarArista("Zona1", "Zona4", new WeightedEdge(3));
        grafo.agregarArista("Zona2", "Zona3", new WeightedEdge(2));
        grafo.agregarArista("Zona2", "Zona4", new WeightedEdge(4));

        LinkedHashSet<String> visitados = new LinkedHashSet<>();
        visitados.add("Zona1");
        visitados.add("Zona2");

        LinkedHashSet<String> noVisitados = new LinkedHashSet<>();
        noVisitados.add("Zona3");
        noVisitados.add("Zona4");

        Edge<String, WeightedEdge> menor =
                algoritmos.searchMinEdge(grafo, visitados, noVisitados);

        assertNotNull(menor);
        assertEquals(2.0, menor.dato().getWeight());
        assertTrue(contieneExtremos(menor, "Zona2", "Zona3"));
    }

    public void testPrimNoModificaGrafoOriginal() {
        IUndirectedGraph<String, WeightedEdge> grafo = crearGrafoParte1();

        int verticesAntes = grafo.cantidadDeVertices();
        int aristasAntes = grafo.cantidadDeAristas();

        algoritmos.prim(grafo, grafo.construirComparable("Zona1"));

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

    private boolean contieneExtremos(
            Edge<String, WeightedEdge> arista,
            String extremoA,
            String extremoB) {

        return (arista.source().equals(extremoA) && arista.target().equals(extremoB))
                || (arista.source().equals(extremoB) && arista.target().equals(extremoA));
    }
}