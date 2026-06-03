
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.model.Implementaciones.DirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public class DirectedGraphTest extends TestCase {

    private DirectedGraph<String, Integer> grafo;

    @Override
    protected void setUp() {
        Set<String> vertices = new HashSet<>();
        Set<Edge<String, Integer>> aristas = new HashSet<>();
        Map<String, Set<Edge<String, Integer>>> adyacencias = new HashMap<>();

        grafo = new DirectedGraph<>(vertices, aristas, adyacencias);
    }

    public void testGrafoInicialmenteVacio() {
        assertEquals(0, grafo.cantidadDeVertices());
        assertEquals(0, grafo.cantidadDeAristas());
        assertTrue(grafo.vertices().isEmpty());
        assertTrue(grafo.aristas().isEmpty());
    }

    public void testAgregarVerticeCumplePostcondicion() {
        boolean agregado = grafo.agregarVertice("A");

        assertTrue(agregado);
        assertEquals(1, grafo.cantidadDeVertices());
        assertTrue(grafo.vertices().contains("A"));
        assertNotNull(grafo.adyacencias(grafo.construirComparable("A")));
        assertTrue(grafo.adyacencias(grafo.construirComparable("A")).isEmpty());
    }

    public void testAgregarVerticeDuplicadoNoModificaGrafo() {
        assertTrue(grafo.agregarVertice("A"));
        assertFalse(grafo.agregarVertice("A"));

        assertEquals(1, grafo.cantidadDeVertices());
        assertTrue(grafo.vertices().contains("A"));
    }

    public void testBuscarVerticeExistente() {
        grafo.agregarVertice("A");

        String encontrado = grafo.buscarVertice(grafo.construirComparable("A"));

        assertEquals("A", encontrado);
    }

    public void testBuscarVerticeInexistenteDevuelveNull() {
        grafo.agregarVertice("A");

        String encontrado = grafo.buscarVertice(grafo.construirComparable("B"));

        assertNull(encontrado);
    }

    public void testExisteVertice() {
        grafo.agregarVertice("A");

        assertTrue(grafo.existeVertice(grafo.construirComparable("A")));
        assertFalse(grafo.existeVertice(grafo.construirComparable("B")));
    }

    public void testAgregarAristaCumplePostcondiciones() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        boolean agregada = grafo.agregarArista("A", "B", 10);

        assertTrue(agregada);
        assertEquals(1, grafo.cantidadDeAristas());

        assertTrue(grafo.existeArista(
                grafo.construirComparable("A"),
                grafo.construirComparable("B")));

        assertFalse(grafo.existeArista(
                grafo.construirComparable("B"),
                grafo.construirComparable("A")));

        assertEquals(1, grafo.adyacencias(grafo.construirComparable("A")).size());
        assertEquals(0, grafo.adyacencias(grafo.construirComparable("B")).size());
    }

    public void testAgregarAristaEsDirigida() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        grafo.agregarArista("A", "B", 10);

        assertTrue(grafo.successors(grafo.construirComparable("A")).contains("B"));
        assertFalse(grafo.successors(grafo.construirComparable("B")).contains("A"));

        assertTrue(grafo.predecessors(grafo.construirComparable("B")).contains("A"));
        assertFalse(grafo.predecessors(grafo.construirComparable("A")).contains("B"));
    }

    public void testAgregarAristaConSourceInexistenteViolaPrecondicion() {
        grafo.agregarVertice("B");

        try {
            grafo.agregarArista("A", "B", 10);
            fail("Debia lanzar IllegalArgumentException porque el source no existe.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertEquals(1, grafo.cantidadDeVertices());
        assertEquals(0, grafo.cantidadDeAristas());
    }

    public void testAgregarAristaConTargetInexistenteViolaPrecondicion() {
        grafo.agregarVertice("A");

        try {
            grafo.agregarArista("A", "B", 10);
            fail("Debia lanzar IllegalArgumentException porque el target no existe.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertEquals(1, grafo.cantidadDeVertices());
        assertEquals(0, grafo.cantidadDeAristas());
    }

    public void testNoAgregaAristaDuplicada() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        assertTrue(grafo.agregarArista("A", "B", 10));
        assertFalse(grafo.agregarArista("A", "B", 10));

        assertEquals(1, grafo.cantidadDeAristas());
        assertEquals(1, grafo.adyacencias(grafo.construirComparable("A")).size());
    }

    public void testObtenerAristaExistente() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        Edge<String, Integer> arista = grafo.obtenerArista(
                grafo.construirComparable("A"),
                grafo.construirComparable("B"));

        assertNotNull(arista);
        assertEquals("A", arista.source());
        assertEquals("B", arista.target());
        assertEquals(Integer.valueOf(10), arista.dato());
    }

    public void testObtenerAristaInexistenteDevuelveNull() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        Edge<String, Integer> arista = grafo.obtenerArista(
                grafo.construirComparable("A"),
                grafo.construirComparable("B"));

        assertNull(arista);
    }

    public void testEliminarAristaExistenteCumplePostcondiciones() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        boolean eliminada = grafo.eliminarArista(
                grafo.construirComparable("A"),
                grafo.construirComparable("B"));

        assertTrue(eliminada);
        assertEquals(0, grafo.cantidadDeAristas());
        assertFalse(grafo.existeArista(
                grafo.construirComparable("A"),
                grafo.construirComparable("B")));
        assertTrue(grafo.adyacencias(grafo.construirComparable("A")).isEmpty());
    }

    public void testEliminarAristaInexistenteNoModificaGrafo() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        boolean eliminada = grafo.eliminarArista(
                grafo.construirComparable("A"),
                grafo.construirComparable("B"));

        assertFalse(eliminada);
        assertEquals(0, grafo.cantidadDeAristas());
    }

    public void testRemoverVerticeEliminaAristasEntrantesYSalientes() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "C", 20);
        grafo.agregarArista("C", "B", 30);

        boolean removido = grafo.removerVertice(grafo.construirComparable("B"));

        assertTrue(removido);
        assertFalse(grafo.vertices().contains("B"));
        assertEquals(2, grafo.cantidadDeVertices());
        assertEquals(0, grafo.cantidadDeAristas());

        assertTrue(grafo.adyacencias(grafo.construirComparable("A")).isEmpty());
        assertTrue(grafo.adyacencias(grafo.construirComparable("C")).isEmpty());
    }

    public void testRemoverVerticeInexistenteNoModificaGrafo() {
        grafo.agregarVertice("A");

        boolean removido = grafo.removerVertice(grafo.construirComparable("B"));

        assertFalse(removido);
        assertEquals(1, grafo.cantidadDeVertices());
        assertTrue(grafo.vertices().contains("A"));
    }

    public void testSuccessorsConVariosVecinos() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("A", "C", 20);

        Set<String> sucesores = grafo.successors(grafo.construirComparable("A"));

        assertEquals(2, sucesores.size());
        assertTrue(sucesores.contains("B"));
        assertTrue(sucesores.contains("C"));
    }

    public void testSuccessorsSinAristasSalientesDevuelveVacio() {
        grafo.agregarVertice("A");

        Set<String> sucesores = grafo.successors(grafo.construirComparable("A"));

        assertNotNull(sucesores);
        assertTrue(sucesores.isEmpty());
    }

    public void testSuccessorsDeVerticeInexistenteDevuelveVacio() {
        Set<String> sucesores = grafo.successors(grafo.construirComparable("X"));

        assertNotNull(sucesores);
        assertTrue(sucesores.isEmpty());
    }

    public void testPredecessorsConVariosVecinos() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "C", 10);
        grafo.agregarArista("B", "C", 20);

        Set<String> predecesores = grafo.predecessors(grafo.construirComparable("C"));

        assertEquals(2, predecesores.size());
        assertTrue(predecesores.contains("A"));
        assertTrue(predecesores.contains("B"));
    }

    public void testPredecessorsSinAristasEntrantesDevuelveVacio() {
        grafo.agregarVertice("A");

        Set<String> predecesores = grafo.predecessors(grafo.construirComparable("A"));

        assertNotNull(predecesores);
        assertTrue(predecesores.isEmpty());
    }

    public void testPredecessorsDeVerticeInexistenteDevuelveVacio() {
        Set<String> predecesores = grafo.predecessors(grafo.construirComparable("X"));

        assertNotNull(predecesores);
        assertTrue(predecesores.isEmpty());
    }

    public void testGradoDeSalida() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("A", "C", 20);

        assertEquals(2, grafo.gradoDeSalida(grafo.construirComparable("A")));
        assertEquals(0, grafo.gradoDeSalida(grafo.construirComparable("B")));
    }

    public void testGradoDeEntrada() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "C", 10);
        grafo.agregarArista("B", "C", 20);

        assertEquals(2, grafo.gradoDeEntrada(grafo.construirComparable("C")));
        assertEquals(0, grafo.gradoDeEntrada(grafo.construirComparable("A")));
    }

    public void testAdyacenciasDevuelveAristasSalientes() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("A", "C", 20);

        List<Edge<String, Integer>> adyacentes = grafo.adyacencias(grafo.construirComparable("A"));

        assertEquals(2, adyacentes.size());

        for (Edge<String, Integer> edge : adyacentes) {
            assertEquals("A", edge.source());
        }
    }

    public void testAdyacenciasDeVerticeInexistenteDevuelveVacio() {
        List<Edge<String, Integer>> adyacentes = grafo.adyacencias(grafo.construirComparable("X"));

        assertNotNull(adyacentes);
        assertTrue(adyacentes.isEmpty());
    }

    public void testModificarListaDevueltaPorAdyacenciasNoModificaGrafo() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        List<Edge<String, Integer>> adyacentes = grafo.adyacencias(grafo.construirComparable("A"));
        adyacentes.clear();

        assertEquals(1, grafo.adyacencias(grafo.construirComparable("A")).size());
        assertEquals(1, grafo.cantidadDeAristas());
    }

    public void testVerticesDevuelveVistaInmodificable() {
        grafo.agregarVertice("A");

        try {
            grafo.vertices().add("B");
            fail("vertices() debe devolver una vista inmodificable.");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }

        assertEquals(1, grafo.cantidadDeVertices());
    }

    public void testAristasDevuelveVistaInmodificable() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        try {
            grafo.aristas().clear();
            fail("aristas() debe devolver una vista inmodificable.");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }

        assertEquals(1, grafo.cantidadDeAristas());
    }

    public void testVaciarCumplePostcondiciones() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        grafo.vaciar();

        assertEquals(0, grafo.cantidadDeVertices());
        assertEquals(0, grafo.cantidadDeAristas());
        assertTrue(grafo.vertices().isEmpty());
        assertTrue(grafo.aristas().isEmpty());
        assertTrue(grafo.adyacencias(grafo.construirComparable("A")).isEmpty());
    }

    public void testTieneCiclosDevuelveFalseSiNoHayCiclo() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "C", 20);

        assertFalse(grafo.tieneCiclos());
    }

    public void testTieneCiclosDevuelveTrueSiHayCiclo() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "C", 20);
        grafo.agregarArista("C", "A", 30);

        assertTrue(grafo.tieneCiclos());
    }

    public void testEsConexoConGrafoVacio() {
        assertTrue(grafo.esConexo());
    }

    public void testEsConexoConGrafoConectadoFuerte() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "A", 10);
        grafo.agregarArista("B", "C", 20);
        grafo.agregarArista("C", "B", 20);

        assertTrue(grafo.esConexo());
    }

    public void testEsConexoConGrafoDesconectado() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);

        assertFalse(grafo.esConexo());
    }

    public void testConstructorConVerticesInicialesCreaAdyacencias() {
        Set<String> vertices = new HashSet<>();
        vertices.add("A");
        vertices.add("B");

        Set<Edge<String, Integer>> aristas = new HashSet<>();
        Map<String, Set<Edge<String, Integer>>> adyacencias = new HashMap<>();

        DirectedGraph<String, Integer> otroGrafo = new DirectedGraph<>(vertices, aristas, adyacencias);

        assertNotNull(otroGrafo.adyacencias(otroGrafo.construirComparable("A")));
        assertNotNull(otroGrafo.adyacencias(otroGrafo.construirComparable("B")));
        assertTrue(otroGrafo.adyacencias(otroGrafo.construirComparable("A")).isEmpty());
        assertTrue(otroGrafo.adyacencias(otroGrafo.construirComparable("B")).isEmpty());
    }

    public void testConstructorConAristasInicialesMantieneAdyacencias() {
        Set<String> vertices = new HashSet<>();
        vertices.add("A");
        vertices.add("B");

        Set<Edge<String, Integer>> aristas = new HashSet<>();
        aristas.add(new DirectedEdge<>("A", "B", 10));

        Map<String, Set<Edge<String, Integer>>> adyacencias = new HashMap<>();

        DirectedGraph<String, Integer> otroGrafo = new DirectedGraph<>(vertices, aristas, adyacencias);

        assertEquals(1, otroGrafo.cantidadDeAristas());
        assertEquals(1, otroGrafo.adyacencias(otroGrafo.construirComparable("A")).size());
        assertTrue(otroGrafo.successors(otroGrafo.construirComparable("A")).contains("B"));
    }
}