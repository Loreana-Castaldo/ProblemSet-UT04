package ucu.edu.aed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashSet;

import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraph;
import ucu.edu.aed.tda.grafo.model.Implementaciones.UndirectedGraphAlgorithm;

public class KevinBacon {

    public static void main(String[] args) {
        UndirectedGraph<String, String> grafo = cargarGrafoKevinBacon(
                "actores.txt",
                "en_pelicula.txt"
        );

        if (grafo == null) {
            System.out.println("No se pudo cargar el grafo.");
            return;
        }

        IUndirectedGraphAlgorithm algoritmos = new UndirectedGraphAlgorithm();

        String[] actores = {
                "John_Goodman",
                "Tom_Cruise",
                "Jason_Statham",
                "Lukas_Haas",
                "Djimon_Hounsou"
        };

        try (PrintWriter salida = new PrintWriter(new FileWriter("salida.txt"))) {
            for (String actor : actores) {
                int numero = algoritmos.numBacon(grafo, actor);

                String linea = actor + ": " + numero;

                System.out.println(linea);
                salida.println(linea);
            }

            System.out.println("Resultados guardados en salida.txt");

        } catch (IOException e) {
            System.err.println("Error al escribir salida.txt: " + e.getMessage());
        }
    }

    private static UndirectedGraph<String, String> cargarGrafoKevinBacon(
            String archivoActores,
            String archivoPeliculas) {

        UndirectedGraph<String, String> grafo = new UndirectedGraph<>(
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new HashMap<>()
        );

        try (BufferedReader br = new BufferedReader(new FileReader(archivoActores))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

                if (primeraLinea && esEncabezado(linea)) {
                    primeraLinea = false;
                    continue;
                }

                primeraLinea = false;
                grafo.agregarVertice(linea);
            }

        } catch (IOException e) {
            System.err.println("Error al leer " + archivoActores + ": " + e.getMessage());
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivoPeliculas))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

                if (primeraLinea && esEncabezado(linea)) {
                    primeraLinea = false;
                    continue;
                }

                primeraLinea = false;

                String[] partes = separarLinea(linea, 3);

                if (partes.length < 3) {
                    System.err.println("Línea inválida en " + archivoPeliculas + ": " + linea);
                    continue;
                }

                String actor1 = partes[0].trim();
                String actor2 = partes[1].trim();
                String pelicula = partes[2].trim();

                if (grafo.buscarVertice(grafo.construirComparable(actor1)) == null) {
                    grafo.agregarVertice(actor1);
                }

                if (grafo.buscarVertice(grafo.construirComparable(actor2)) == null) {
                    grafo.agregarVertice(actor2);
                }

                grafo.agregarArista(actor1, actor2, pelicula);
            }

        } catch (IOException e) {
            System.err.println("Error al leer " + archivoPeliculas + ": " + e.getMessage());
            return null;
        }

        return grafo;
    }

    private static boolean esEncabezado(String linea) {
        String normalizada = linea.toLowerCase();

        return normalizada.contains("actor")
                || normalizada.contains("pelicula")
                || normalizada.contains("película");
    }

    private static String[] separarLinea(String linea, int limite) {
        if (linea.contains(";")) {
            return linea.split(";", limite);
        }

        return linea.split(",", limite);
    }
}