import java.util.HashMap;
import java.util.LinkedHashSet;

import junit.framework.TestCase;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraphAlgorithm;

public class KevinBaconTest extends TestCase {

    private IUndirectedGraphAlgorithm algoritmos;

    @Override
    protected void setUp() {
        algoritmos = new UndirectedGraphAlgorithm();
    }

    public void testKevinBaconTieneNumeroCero() {
        UndirectedGraph<String, String> grafo = crearGrafo();

        grafo.agregarVertice("Kevin_Bacon");

        assertEquals(0, algoritmos.numBacon(grafo, "Kevin_Bacon"));
    }

    public void testActorDirectamenteRelacionadoTieneNumeroUno() {
        UndirectedGraph<String, String> grafo = crearGrafo();

        agregarVertices(grafo, "Kevin_Bacon", "Tom_Hanks");

        grafo.agregarArista("Tom_Hanks", "Kevin_Bacon", "Apollo_13");

        assertEquals(1, algoritmos.numBacon(grafo, "Tom_Hanks"));
    }

    public void testActorRelacionadoIndirectamenteTieneNumeroDos() {
        UndirectedGraph<String, String> grafo = crearGrafo();

        agregarVertices(grafo, "Kevin_Bacon", "Tom_Hanks", "Sally_Field");

        grafo.agregarArista("Tom_Hanks", "Kevin_Bacon", "Apollo_13");
        grafo.agregarArista("Sally_Field", "Tom_Hanks", "Forrest_Gump");

        assertEquals(2, algoritmos.numBacon(grafo, "Sally_Field"));
    }

    public void testEligeElCaminoMasCorto() {
        UndirectedGraph<String, String> grafo = crearGrafo();

        agregarVertices(grafo,
                "Kevin_Bacon",
                "A",
                "B",
                "C",
                "D"
        );

        grafo.agregarArista("A", "B", "Pelicula_1");
        grafo.agregarArista("B", "C", "Pelicula_2");
        grafo.agregarArista("C", "Kevin_Bacon", "Pelicula_3");

        grafo.agregarArista("A", "D", "Pelicula_4");
        grafo.agregarArista("D", "Kevin_Bacon", "Pelicula_5");

        assertEquals(2, algoritmos.numBacon(grafo, "A"));
    }

    public void testActorNoConectadoConKevinBaconDevuelveMenosUno() {
        UndirectedGraph<String, String> grafo = crearGrafo();

        agregarVertices(grafo, "Kevin_Bacon", "Tom_Hanks", "Actor_Aislado");

        grafo.agregarArista("Tom_Hanks", "Kevin_Bacon", "Apollo_13");

        assertEquals(-1, algoritmos.numBacon(grafo, "Actor_Aislado"));
    }

    public void testActorInexistenteDevuelveMenosUno() {
        UndirectedGraph<String, String> grafo = crearGrafo();

        agregarVertices(grafo, "Kevin_Bacon", "Tom_Hanks");

        grafo.agregarArista("Tom_Hanks", "Kevin_Bacon", "Apollo_13");

        assertEquals(-1, algoritmos.numBacon(grafo, "No_Existe"));
    }

    public void testGrafoSinKevinBaconDevuelveMenosUno() {
        UndirectedGraph<String, String> grafo = crearGrafo();

        agregarVertices(grafo, "Tom_Hanks", "Sally_Field");

        grafo.agregarArista("Tom_Hanks", "Sally_Field", "Forrest_Gump");

        assertEquals(-1, algoritmos.numBacon(grafo, "Tom_Hanks"));
    }

    private UndirectedGraph<String, String> crearGrafo() {
        return new UndirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new HashMap<>()
        );
    }

    private void agregarVertices(UndirectedGraph<String, String> grafo, String... vertices) {
        for (String vertice : vertices) {
            grafo.agregarVertice(vertice);
        }
    }
}