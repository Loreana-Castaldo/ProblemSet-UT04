

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.model.Implementaciones.DijkstraResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DijkstraResultTest extends TestCase {

    public void testGetCostDeberiaRetornarElCostoCuandoElVerticeExiste() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);
        costos.put("B", 4.5);

        Map<String, String> anteriores = new HashMap<String, String>();

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        assertEquals(0.0, resultado.getCost("A"), 0.0001);
        assertEquals(4.5, resultado.getCost("B"), 0.0001);
    }

    public void testGetCostDeberiaRetornarInfinitoCuandoElVerticeNoExiste() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);

        Map<String, String> anteriores = new HashMap<String, String>();

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        assertTrue(Double.isInfinite(resultado.getCost("X")));
    }

    public void testGetPathDeberiaRetornarCaminoCompletoDesdeElOrigenHastaElDestino() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);
        costos.put("B", 2.0);
        costos.put("C", 5.0);
        costos.put("D", 8.0);

        Map<String, String> anteriores = new HashMap<String, String>();
        anteriores.put("B", "A");
        anteriores.put("C", "B");
        anteriores.put("D", "C");

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        List<String> caminoEsperado = Arrays.asList("A", "B", "C", "D");

        assertEquals(caminoEsperado, resultado.getPath("D"));
    }

    public void testGetPathDeberiaRetornarSoloElOrigenCuandoElDestinoEsElOrigen() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);

        Map<String, String> anteriores = new HashMap<String, String>();

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        List<String> caminoEsperado = Collections.singletonList("A");

        assertEquals(caminoEsperado, resultado.getPath("A"));
    }

    public void testGetPathDeberiaRetornarListaVaciaCuandoElDestinoEsInalcanzable() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);
        costos.put("B", Double.POSITIVE_INFINITY);

        Map<String, String> anteriores = new HashMap<String, String>();
        anteriores.put("B", "A");

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        assertTrue(resultado.getPath("B").isEmpty());
    }

    public void testGetPathDeberiaRetornarListaVaciaCuandoElDestinoNoTieneCostoRegistrado() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);

        Map<String, String> anteriores = new HashMap<String, String>();

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        assertTrue(resultado.getPath("X").isEmpty());
    }

    public void testGetPathDeberiaRetornarListaVaciaCuandoElDestinoEsNull() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);

        Map<String, String> anteriores = new HashMap<String, String>();

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        assertTrue(resultado.getPath(null).isEmpty());
    }

    public void testGetPathDeberiaRetornarListaVaciaCuandoElOrigenEsNull() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);

        Map<String, String> anteriores = new HashMap<String, String>();

        DijkstraResult<String> resultado = new DijkstraResult<String>(null, costos, anteriores);

        assertTrue(resultado.getPath("A").isEmpty());
    }

    public void testGetPathDeberiaRetornarListaVaciaCuandoNoSePuedeReconstruirHastaElOrigen() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);
        costos.put("B", 3.0);

        Map<String, String> anteriores = new HashMap<String, String>();

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        assertTrue(resultado.getPath("B").isEmpty());
    }

    public void testConstructorDeberiaHacerCopiaDefensivaDeLosMapas() {
        Map<String, Double> costos = new HashMap<String, Double>();
        costos.put("A", 0.0);
        costos.put("B", 2.0);
        costos.put("C", 5.0);

        Map<String, String> anteriores = new HashMap<String, String>();
        anteriores.put("B", "A");
        anteriores.put("C", "B");

        DijkstraResult<String> resultado = new DijkstraResult<String>("A", costos, anteriores);

        costos.put("C", 100.0);
        anteriores.put("C", "A");

        assertEquals(5.0, resultado.getCost("C"), 0.0001);

        List<String> caminoEsperado = Arrays.asList("A", "B", "C");

        assertEquals(caminoEsperado, resultado.getPath("C"));
    }
}