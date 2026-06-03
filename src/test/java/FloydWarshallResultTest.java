import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.model.Implementaciones.FloydWarshallResult;

public class FloydWarshallResultTest extends TestCase {

    private FloydWarshallResult<String> resultado;

    private List<String> vertices;
    private Map<String, Integer> indices;
    private double[][] costs;
    private Integer[][] next;

    @Override
    protected void setUp() {
        vertices = new ArrayList<>();
        vertices.add("A");
        vertices.add("B");
        vertices.add("C");
        vertices.add("D");

        indices = new HashMap<>();
        indices.put("A", 0);
        indices.put("B", 1);
        indices.put("C", 2);
        indices.put("D", 3);

        costs = new double[4][4];
        next = new Integer[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                costs[i][j] = Double.POSITIVE_INFINITY;
                next[i][j] = null;
            }

            costs[i][i] = 0.0;
            next[i][i] = i;
        }

        // Caminos:
        // A -> B costo 5
        // B -> C costo 7
        // A -> C costo 12 pasando por B
        // D queda desconectado del resto

        costs[0][1] = 5.0;
        next[0][1] = 1;

        costs[1][2] = 7.0;
        next[1][2] = 2;

        costs[0][2] = 12.0;
        next[0][2] = 1;

        resultado = new FloydWarshallResult<>(vertices, indices, costs, next);
    }

    public void testGetCostCaminoDirecto() {
        assertEquals(5.0, resultado.getCost("A", "B"));
    }

    public void testGetCostCaminoIndirecto() {
        assertEquals(12.0, resultado.getCost("A", "C"));
    }

    public void testGetCostMismoVertice() {
        assertEquals(0.0, resultado.getCost("A", "A"));
        assertEquals(0.0, resultado.getCost("B", "B"));
        assertEquals(0.0, resultado.getCost("C", "C"));
        assertEquals(0.0, resultado.getCost("D", "D"));
    }

    public void testGetCostSinCaminoDevuelveInfinito() {
        assertTrue(Double.isInfinite(resultado.getCost("C", "A")));
        assertTrue(Double.isInfinite(resultado.getCost("A", "D")));
        assertTrue(Double.isInfinite(resultado.getCost("D", "A")));
    }

    public void testGetCostConSourceInexistenteDevuelveInfinito() {
        assertTrue(Double.isInfinite(resultado.getCost("X", "A")));
    }

    public void testGetCostConTargetInexistenteDevuelveInfinito() {
        assertTrue(Double.isInfinite(resultado.getCost("A", "X")));
    }

    public void testGetCostConSourceNullDevuelveInfinito() {
        assertTrue(Double.isInfinite(resultado.getCost(null, "A")));
    }

    public void testGetCostConTargetNullDevuelveInfinito() {
        assertTrue(Double.isInfinite(resultado.getCost("A", null)));
    }

    public void testConnectedDevuelveTrueSiExisteCaminoDirecto() {
        assertTrue(resultado.connected("A", "B"));
    }

    public void testConnectedDevuelveTrueSiExisteCaminoIndirecto() {
        assertTrue(resultado.connected("A", "C"));
    }

    public void testConnectedDevuelveTrueParaMismoVertice() {
        assertTrue(resultado.connected("A", "A"));
    }

    public void testConnectedDevuelveFalseSiNoExisteCamino() {
        assertFalse(resultado.connected("C", "A"));
        assertFalse(resultado.connected("A", "D"));
        assertFalse(resultado.connected("D", "A"));
    }

    public void testConnectedConVerticeInexistenteDevuelveFalse() {
        assertFalse(resultado.connected("X", "A"));
        assertFalse(resultado.connected("A", "X"));
    }

    public void testGetPathCaminoDirecto() {
        List<String> path = resultado.getPath("A", "B");

        assertEquals(2, path.size());
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
    }

    public void testGetPathCaminoIndirecto() {
        List<String> path = resultado.getPath("A", "C");

        assertEquals(3, path.size());
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
        assertEquals("C", path.get(2));
    }

    public void testGetPathMismoVertice() {
        List<String> path = resultado.getPath("A", "A");

        assertEquals(1, path.size());
        assertEquals("A", path.get(0));
    }

    public void testGetPathSinCaminoDevuelveListaVacia() {
        assertTrue(resultado.getPath("C", "A").isEmpty());
        assertTrue(resultado.getPath("A", "D").isEmpty());
        assertTrue(resultado.getPath("D", "A").isEmpty());
    }

    public void testGetPathConSourceInexistenteDevuelveListaVacia() {
        assertTrue(resultado.getPath("X", "A").isEmpty());
    }

    public void testGetPathConTargetInexistenteDevuelveListaVacia() {
        assertTrue(resultado.getPath("A", "X").isEmpty());
    }

    public void testGetPathConSourceNullDevuelveListaVacia() {
        assertTrue(resultado.getPath(null, "A").isEmpty());
    }

    public void testGetPathConTargetNullDevuelveListaVacia() {
        assertTrue(resultado.getPath("A", null).isEmpty());
    }

    public void testModificarPathDevueltoNoRompeResultadoInterno() {
        List<String> path = resultado.getPath("A", "C");

        path.clear();

        List<String> nuevoPath = resultado.getPath("A", "C");

        assertEquals(3, nuevoPath.size());
        assertEquals("A", nuevoPath.get(0));
        assertEquals("B", nuevoPath.get(1));
        assertEquals("C", nuevoPath.get(2));
    }

    public void testConstructorCopiaListaDeVertices() {
        FloydWarshallResult<String> otroResultado =
                new FloydWarshallResult<>(vertices, indices, costs, next);

        vertices.clear();

        List<String> path = otroResultado.getPath("A", "C");

        assertEquals(3, path.size());
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
        assertEquals("C", path.get(2));
    }

    public void testConstructorConIndicesNullReconstruyeIndicesDesdeVertices() {
        FloydWarshallResult<String> otroResultado =
                new FloydWarshallResult<>(vertices, null, costs, next);

        assertEquals(12.0, otroResultado.getCost("A", "C"));

        List<String> path = otroResultado.getPath("A", "C");

        assertEquals(3, path.size());
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
        assertEquals("C", path.get(2));
    }

    public void testConstructorConVerticesNullNoRompe() {
        FloydWarshallResult<String> otroResultado =
                new FloydWarshallResult<>(null, null, costs, next);

        assertTrue(Double.isInfinite(otroResultado.getCost("A", "B")));
        assertFalse(otroResultado.connected("A", "B"));
        assertTrue(otroResultado.getPath("A", "B").isEmpty());
    }

    public void testCostsNullDevuelveInfinito() {
        FloydWarshallResult<String> otroResultado =
                new FloydWarshallResult<>(vertices, indices, null, next);

        assertTrue(Double.isInfinite(otroResultado.getCost("A", "B")));
        assertFalse(otroResultado.connected("A", "B"));
        assertTrue(otroResultado.getPath("A", "B").isEmpty());
    }

    public void testNextNullDevuelveListaVaciaParaCaminoNoTrivial() {
        FloydWarshallResult<String> otroResultado =
                new FloydWarshallResult<>(vertices, indices, costs, null);

        assertEquals(12.0, otroResultado.getCost("A", "C"));
        assertTrue(otroResultado.connected("A", "C"));
        assertTrue(otroResultado.getPath("A", "C").isEmpty());
    }

    public void testNextNullPermiteCaminoDeVerticeASiMismo() {
        FloydWarshallResult<String> otroResultado =
                new FloydWarshallResult<>(vertices, indices, costs, null);

        List<String> path = otroResultado.getPath("A", "A");

        assertEquals(1, path.size());
        assertEquals("A", path.get(0));
    }

    public void testNextConValorInvalidoDevuelveListaVacia() {
        next[0][2] = 99;

        FloydWarshallResult<String> otroResultado =
                new FloydWarshallResult<>(vertices, indices, costs, next);

        assertTrue(otroResultado.getPath("A", "C").isEmpty());
    }
}