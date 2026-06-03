
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.model.Implementaciones.Graph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

public class GraphTest extends TestCase {

    private ConcreteGraph<String, Integer> grafo;

    private static class ConcreteGraph<V, D> extends Graph<V, D> {
        public ConcreteGraph(
                Set<V> vertices,
                Set<Edge<V, D>> aristas,
                Map<V, Set<Edge<V, D>>> adyacencias) {

            super(vertices, aristas, adyacencias);
        }
    }

    @Override
    protected void setUp() {
        Set<String> vertices = new LinkedHashSet<>();
        Set<Edge<String, Integer>> aristas = new HashSet<>();
        Map<String, Set<Edge<String, Integer>>> adyacencias = new HashMap<>();

        grafo = new ConcreteGraph<>(vertices, aristas, adyacencias);
    }

    public void testConstructorConEstructurasVacias() {
        assertEquals(0, grafo.cantidadDeVertices());
        assertEquals(0, grafo.cantidadDeAristas());
        assertTrue(grafo.vertices().isEmpty());
        assertTrue(grafo.aristas().isEmpty());
    }

    public void testConstructorConNullInicializaEstructuras() {
        ConcreteGraph<String, Integer> otroGrafo = new ConcreteGraph<>(null, null, null);

        assertNotNull(otroGrafo.vertices());
        assertNotNull(otroGrafo.aristas());
        assertEquals(0, otroGrafo.cantidadDeVertices());
        assertEquals(0, otroGrafo.cantidadDeAristas());
    }

    public void testConstructorCreaAdyacenciasParaVerticesIniciales() {
        Set<String> vertices = new LinkedHashSet<>();
        vertices.add("A");
        vertices.add("B");

        ConcreteGraph<String, Integer> otroGrafo = new ConcreteGraph<>(
                vertices,
                new HashSet<>(),
                new HashMap<>());

        assertEquals(2, otroGrafo.cantidadDeVertices());
        assertNotNull(otroGrafo.adyacencias(otroGrafo.construirComparable("A")));
        assertNotNull(otroGrafo.adyacencias(otroGrafo.construirComparable("B")));
        assertTrue(otroGrafo.adyacencias(otroGrafo.construirComparable("A")).isEmpty());
        assertTrue(otroGrafo.adyacencias(otroGrafo.construirComparable("B")).isEmpty());
    }

    public void testConstructorCargaAristasInicialesEnAdyacencias() {
        Set<String> vertices = new LinkedHashSet<>();
        vertices.add("A");
        vertices.add("B");

        Set<Edge<String, Integer>> aristas = new HashSet<>();
        aristas.add(new DirectedEdge<>("A", "B", 10));

        ConcreteGraph<String, Integer> otroGrafo = new ConcreteGraph<>(
                vertices,
                aristas,
                new HashMap<>());

        assertEquals(2, otroGrafo.cantidadDeVertices());
        assertEquals(1, otroGrafo.cantidadDeAristas());
        assertEquals(1, otroGrafo.adyacencias(otroGrafo.construirComparable("A")).size());
        assertEquals(0, otroGrafo.adyacencias(otroGrafo.construirComparable("B")).size());
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

    public void testBuscarVerticeConCriterioNullDevuelveNull() {
        grafo.agregarVertice("A");

        assertNull(grafo.buscarVertice(null));
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

    public void testAgregarAristaConSourceInexistenteLanzaExcepcion() {
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

    public void testAgregarAristaConTargetInexistenteLanzaExcepcion() {
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

    public void testObtenerAristaConCriterioNullDevuelveNull() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        assertNull(grafo.obtenerArista(null, grafo.construirComparable("B")));
        assertNull(grafo.obtenerArista(grafo.construirComparable("A"), null));
    }

    public void testExisteArista() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        assertTrue(grafo.existeArista(
                grafo.construirComparable("A"),
                grafo.construirComparable("B")));

        assertFalse(grafo.existeArista(
                grafo.construirComparable("B"),
                grafo.construirComparable("A")));
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

    public void testEliminarAristaConCriterioNullNoModificaGrafo() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        assertFalse(grafo.eliminarArista(null, grafo.construirComparable("B")));
        assertFalse(grafo.eliminarArista(grafo.construirComparable("A"), null));

        assertEquals(1, grafo.cantidadDeAristas());
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

    public void testRemoverVerticeConCriterioNullNoModificaGrafo() {
        grafo.agregarVertice("A");

        boolean removido = grafo.removerVertice(null);

        assertFalse(removido);
        assertEquals(1, grafo.cantidadDeVertices());
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

    public void testAdyacenciasDeVerticeSinAristasDevuelveVacio() {
        grafo.agregarVertice("A");

        List<Edge<String, Integer>> adyacentes = grafo.adyacencias(grafo.construirComparable("A"));

        assertNotNull(adyacentes);
        assertTrue(adyacentes.isEmpty());
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

    public void testTieneCiclosConGrafoVacioDevuelveFalse() {
        assertFalse(grafo.tieneCiclos());
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

    public void testTieneCiclosDevuelveTrueConAutoBucle() {
        grafo.agregarVertice("A");

        grafo.agregarArista("A", "A", 10);

        assertTrue(grafo.tieneCiclos());
    }

    public void testEsConexoConGrafoVacio() {
        assertTrue(grafo.esConexo());
    }

    public void testEsConexoConUnSoloVertice() {
        grafo.agregarVertice("A");

        assertTrue(grafo.esConexo());
    }

    public void testEsConexoConCaminoDesdePrimerVerticeATodos() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "C", 20);

        assertTrue(grafo.esConexo());
    }

    public void testEsConexoConGrafoDesconectado() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        grafo.agregarArista("A", "B", 10);

        assertFalse(grafo.esConexo());
    }

    public void testConstruirComparableFuncionaConEquals() {
        grafo.agregarVertice("A");

        Comparable<String> comparable = grafo.construirComparable("A");

        assertEquals(0, comparable.compareTo("A"));
        assertEquals(1, comparable.compareTo("B"));
    }

    public void testCantidadDeVerticesYCantidadDeAristas() {
        assertEquals(0, grafo.cantidadDeVertices());
        assertEquals(0, grafo.cantidadDeAristas());

        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);

        assertEquals(2, grafo.cantidadDeVertices());
        assertEquals(1, grafo.cantidadDeAristas());
    }
}