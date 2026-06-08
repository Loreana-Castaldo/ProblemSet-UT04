import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.model.result.CriticalPathResult;
import ucu.edu.aed.tda.grafo.model.result.Path;
import ucu.edu.aed.tda.grafo.model.result.PathWithSlack;

public class CriticalPathResultTest extends TestCase {

    public void testConstructorGuardaCriticalPathYAllPaths() {
        Path<String> criticalPath = new Path<>(Arrays.asList("A", "B", "C"), 100.0);
        List<Path<String>> allPaths = new ArrayList<>();
        allPaths.add(criticalPath);
        allPaths.add(new Path<>(Arrays.asList("A", "C"), 80.0));

        CriticalPathResult<String> resultado = new CriticalPathResult<>(
                criticalPath,
                allPaths,
                "A",
                "C"
        );

        assertEquals(criticalPath, resultado.getCriticalPath());
        assertEquals(allPaths, resultado.getAllPaths());
    }

    public void testGetCriticalCostRetornaCostoDelCaminoCritico() {
        Path<String> criticalPath = new Path<>(Arrays.asList("A", "B", "C"), 100.0);
        List<Path<String>> allPaths = new ArrayList<>();
        allPaths.add(criticalPath);

        CriticalPathResult<String> resultado = new CriticalPathResult<>(
                criticalPath,
                allPaths,
                "A",
                "C"
        );

        assertEquals(100.0, resultado.getCriticalCost(), 0.0001);
    }

    public void testGetPathsWithSlackCalculaHolguraCorrectamente() {
        Path<String> criticalPath = new Path<>(Arrays.asList("A", "B", "C"), 100.0);
        List<Path<String>> allPaths = new ArrayList<>();
        allPaths.add(criticalPath);
        allPaths.add(new Path<>(Arrays.asList("A", "C"), 80.0));

        CriticalPathResult<String> resultado = new CriticalPathResult<>(
                criticalPath,
                allPaths,
                "A",
                "C"
        );

        List<PathWithSlack<String>> pathsWithSlack = resultado.getPathsWithSlack();

        assertEquals(2, pathsWithSlack.size());

        PathWithSlack<String> critico = pathsWithSlack.get(0);
        assertEquals(0.0, critico.getSlack(), 0.0001);
        assertTrue(critico.isCritical());

        PathWithSlack<String> noCritico = pathsWithSlack.get(1);
        assertEquals(20.0, noCritico.getSlack(), 0.0001);
        assertFalse(noCritico.isCritical());
    }

    public void testGetSlackCalculaHolguraParaUnCaminoEspecifico() {
        Path<String> criticalPath = new Path<>(Arrays.asList("A", "B", "C"), 100.0);
        List<Path<String>> allPaths = new ArrayList<>();
        Path<String> otherPath = new Path<>(Arrays.asList("A", "C"), 75.0);
        allPaths.add(criticalPath);
        allPaths.add(otherPath);

        CriticalPathResult<String> resultado = new CriticalPathResult<>(
                criticalPath,
                allPaths,
                "A",
                "C"
        );

        double slack = resultado.getSlack(otherPath);
        assertEquals(25.0, slack, 0.0001);
    }

    public void testGetSlackRetorna0ParaCaminoCritico() {
        Path<String> criticalPath = new Path<>(Arrays.asList("A", "B", "C"), 100.0);
        List<Path<String>> allPaths = new ArrayList<>();
        allPaths.add(criticalPath);

        CriticalPathResult<String> resultado = new CriticalPathResult<>(
                criticalPath,
                allPaths,
                "A",
                "C"
        );

        double slack = resultado.getSlack(criticalPath);
        assertEquals(0.0, slack, 0.0001);
    }

    public void testGetSourceYGetTargetRetornanVerticesCorrectos() {
        Path<String> criticalPath = new Path<>(Arrays.asList("A", "B", "C"), 100.0);
        List<Path<String>> allPaths = new ArrayList<>();
        allPaths.add(criticalPath);

        CriticalPathResult<String> resultado = new CriticalPathResult<>(
                criticalPath,
                allPaths,
                "A",
                "C"
        );

        assertEquals("A", resultado.getSource());
        assertEquals("C", resultado.getTarget());
    }

    public void testMultiplesCaminosConDiferentesHolguras() {
        Path<String> criticalPath = new Path<>(Arrays.asList("A", "B", "C", "D"), 200.0);
        List<Path<String>> allPaths = new ArrayList<>();
        allPaths.add(criticalPath);
        allPaths.add(new Path<>(Arrays.asList("A", "B", "D"), 150.0));
        allPaths.add(new Path<>(Arrays.asList("A", "D"), 100.0));

        CriticalPathResult<String> resultado = new CriticalPathResult<>(
                criticalPath,
                allPaths,
                "A",
                "D"
        );

        List<PathWithSlack<String>> pathsWithSlack = resultado.getPathsWithSlack();
        
        assertEquals(3, pathsWithSlack.size());
        assertEquals(0.0, pathsWithSlack.get(0).getSlack(), 0.0001);   // crítico
        assertEquals(50.0, pathsWithSlack.get(1).getSlack(), 0.0001);  // holgura 50
        assertEquals(100.0, pathsWithSlack.get(2).getSlack(), 0.0001); // holgura 100
    }
}
