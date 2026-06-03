import java.util.Arrays;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.model.result.PathWithSlack;

public class PathWithSlackTest extends TestCase {

    public void testConstructorGuardaPathCostoYHolgura() {
        PathWithSlack<String> pathWithSlack = new PathWithSlack<>(
                Arrays.asList("A", "B", "C"),
                100.0, 0.0, true
        );

        assertEquals(Arrays.asList("A", "B", "C"), pathWithSlack.getPath());
        assertEquals(100.0, pathWithSlack.getCost(), 0.0001);
        assertEquals(0.0, pathWithSlack.getSlack(), 0.0001);
        assertTrue(pathWithSlack.isCritical());
    }

    public void testGetPathRetornaElCaminoAlmacenado() {
        PathWithSlack<String> pathWithSlack = new PathWithSlack<>(
                Arrays.asList("X", "Y", "Z"),
                50.0, 10.0, false
        );

        assertEquals(Arrays.asList("X", "Y", "Z"), pathWithSlack.getPath());
    }

    public void testGetCostRetornaElCostoDelCamino() {
        PathWithSlack<String> pathWithSlack = new PathWithSlack<>(
                Arrays.asList("A", "B"),
                75.5, 0.0, true
        );

        assertEquals(75.5, pathWithSlack.getCost(), 0.0001);
    }

    public void testGetSlackRetornaLaHolgura() {
        PathWithSlack<String> pathWithSlack = new PathWithSlack<>(
                Arrays.asList("A", "B", "C"),
                50.0, 25.0, false
        );

        assertEquals(25.0, pathWithSlack.getSlack(), 0.0001);
    }

    public void testIsCriticalRetornaTrueSiEsCamminoCritico() {
        PathWithSlack<String> pathWithSlack = new PathWithSlack<>(
                Arrays.asList("A", "B", "C"),
                100.0, 0.0, true
        );

        assertTrue(pathWithSlack.isCritical());
    }

    public void testIsCriticalRetornaFalseSiNoEsCaminoCritico() {
        PathWithSlack<String> pathWithSlack = new PathWithSlack<>(
                Arrays.asList("A", "B"),
                50.0, 10.0, false
        );

        assertFalse(pathWithSlack.isCritical());
    }

    public void testCaminoCriticalTieneCeroHolgura() {
        PathWithSlack<String> pathCritical = new PathWithSlack<>(
                Arrays.asList("A", "B", "C", "D"),
                200.0, 0.0, true
        );

        assertTrue(pathCritical.isCritical());
        assertEquals(0.0, pathCritical.getSlack(), 0.0001);
    }

    public void testCaminoNoCriticalTieneHolguraPositiva() {
        PathWithSlack<String> pathNonCritical = new PathWithSlack<>(
                Arrays.asList("A", "C"),
                100.0, 50.0, false
        );

        assertFalse(pathNonCritical.isCritical());
        assertTrue(pathNonCritical.getSlack() > 0);
        assertEquals(50.0, pathNonCritical.getSlack(), 0.0001);
    }

    public void testToStringFormateaCorrectamente() {
        PathWithSlack<String> pathWithSlack = new PathWithSlack<>(
                Arrays.asList("A", "B", "C"),
                100.0, 0.0, true
        );

        String formatted = pathWithSlack.toString();
        
        assertNotNull(formatted);
        assertTrue(formatted.length() > 0);
        assertTrue(formatted.contains("A"));
        assertTrue(formatted.contains("Costo"));
        assertTrue(formatted.contains("Holgura"));
    }

    public void testMultiplePathsWithDifferentSlacks() {
        PathWithSlack<String> critical = new PathWithSlack<>(
                Arrays.asList("A", "B", "C"),
                200.0, 0.0, true
        );
        PathWithSlack<String> medium = new PathWithSlack<>(
                Arrays.asList("A", "B", "C", "D"),
                150.0, 50.0, false
        );
        PathWithSlack<String> loose = new PathWithSlack<>(
                Arrays.asList("A", "D"),
                100.0, 100.0, false
        );

        assertTrue(critical.isCritical());
        assertFalse(medium.isCritical());
        assertFalse(loose.isCritical());

        assertEquals(0.0, critical.getSlack(), 0.0001);
        assertEquals(50.0, medium.getSlack(), 0.0001);
        assertEquals(100.0, loose.getSlack(), 0.0001);
    }
}
