package uy.edu.um;

import uy.edu.um.tad.linkedlist.MyList;

import java.util.PriorityQueue;

public class Consultas {

    private MyList<Pelicula> peliculas;

    public Consultas(MyList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    // CONSULTA 1
    public void mostrarTop5PeliculasPorIdioma() {
        String[] idiomasObjetivo = {"en", "fr", "it", "es", "pt"};

        for (String idioma : idiomasObjetivo) {
            PriorityQueue<Pelicula> topPeliculas = new PriorityQueue<>(
                    (p1, p2) -> Integer.compare(p2.getCalificaciones().size(), p1.getCalificaciones().size())
            );

            for (int i = 0; i < peliculas.size(); i++) {
                Pelicula peli = peliculas.get(i);
                if (idioma.equalsIgnoreCase(peli.getIdiomaOriginal())) {
                    topPeliculas.add(peli);
                }
            }

            System.out.println("Top 5 películas en idioma: " + idioma.toUpperCase());

            int count = 0;
            while (!topPeliculas.isEmpty() && count < 5) {
                Pelicula top = topPeliculas.poll();
                System.out.println("ID: " + top.getId()
                        + " | Título: " + top.getTitulo()
                        + " | Evaluaciones: " + top.getCalificaciones().size()
                        + " | Idioma: " + top.getIdiomaOriginal());
                count++;
            }

            if (count == 0) {
                System.out.println("No se encontraron películas para este idioma.");
            }

            System.out.println();
        }
    }
    // TERMINA CONSULTA 1

    // CONSULTA 2
    public void mostrarTop10PeliculasMejorCalificacion() {
        PriorityQueue<Pelicula> topPeliculas = new PriorityQueue<>(
                (p1, p2) -> {
                    double prom2 = p2.promCalificaciones();
                    double prom1 = p1.promCalificaciones();
                    return Double.compare(prom2, prom1); // Orden descendente por promedio
                }
        );

        for (int i = 0; i < peliculas.size(); i++) {
            Pelicula peli = peliculas.get(i);
            if (!peli.getCalificaciones().isEmpty()) {
                topPeliculas.add(peli);
            }
        }

        System.out.println("Top 10 películas con mejor calificación media:");

        int count = 0;
        while (!topPeliculas.isEmpty() && count < 10) {
            Pelicula top = topPeliculas.poll();
            System.out.printf("ID: %d | Título: %s | Promedio: %.2f | Evaluaciones: %d%n",
                    top.getId(),
                    top.getTitulo(),
                    top.promCalificaciones(),
                    top.getCalificaciones().size()
            );
            count++;
        }

        if (count == 0) {
            System.out.println("No se encontraron películas con evaluaciones.");
        }

        System.out.println();
    }
    //TERMINA CONSULTA 2

    // CONSULTA 3
}
