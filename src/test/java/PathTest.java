

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.model.result.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathTest extends TestCase {

    public void testGetPathRetornaElCaminoGuardado() {
        List<String> camino = Arrays.asList("A", "B", "C");

        Path<String> path = new Path<String>(camino, 10.5);

        assertEquals(camino, path.getPath());
    }

    public void testGetCostRetornaElCostoGuardado() {
        List<String> camino = Arrays.asList("A", "B", "C");

        Path<String> path = new Path<String>(camino, 10.5);

        assertEquals(10.5, path.getCost(), 0.0001);
    }

    public void testPathPermiteCaminoVacio() {
        List<String> camino = new ArrayList<String>();

        Path<String> path = new Path<String>(camino, 0.0);

        assertTrue(path.getPath().isEmpty());
        assertEquals(0.0, path.getCost(), 0.0001);
    }

    public void testPathPermiteCostoInfinito() {
        List<String> camino = Arrays.asList("A", "B");

        Path<String> path = new Path<String>(camino, Double.POSITIVE_INFINITY);

        assertTrue(Double.isInfinite(path.getCost()));
        assertEquals(camino, path.getPath());
    }

    public void testPathNoHaceCopiaDefensivaDelCamino() {
        List<String> camino = new ArrayList<String>();
        camino.add("A");
        camino.add("B");

        Path<String> path = new Path<String>(camino, 3.0);

        camino.add("C");

        assertEquals(Arrays.asList("A", "B", "C"), path.getPath());
    }

    public void testGetPathRetornaLaMismaReferenciaRecibidaEnConstructor() {
        List<String> camino = new ArrayList<String>();
        camino.add("A");
        camino.add("B");

        Path<String> path = new Path<String>(camino, 3.0);

        assertSame(camino, path.getPath());
    }
}