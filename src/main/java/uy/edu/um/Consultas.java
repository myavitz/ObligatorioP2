package uy.edu.um;

import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.heap.MyHeap;
import uy.edu.um.tad.heap.MyHeapImpl;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.Collection;

public class Consultas {
    private final MyList<Pelicula> peliculas;

    public Consultas(MyList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    //CONSULTA 1
    public void mostrarTop5PeliculasPorIdioma() {
        String[] idiomasObjetivo = {"en", "fr", "it", "es", "pt"};

        for (String idioma : idiomasObjetivo) {
            // Heap de máximo (películas con más evaluaciones al tope)
            MyHeap<PeliculaConEvaluaciones> heap = new MyHeapImpl<>(false); // false → max heap

            for (int i = 0; i < peliculas.size(); i++) {
                Pelicula peli = peliculas.get(i);
                if (peli.getIdiomaOriginal() != null && peli.getIdiomaOriginal().equalsIgnoreCase(idioma)) {
                    int evaluaciones = peli.getCalificaciones().size();
                    heap.insert(new PeliculaConEvaluaciones(peli, evaluaciones));
                }
            }

            System.out.println("Top 5 películas en idioma: " + idioma.toUpperCase());

            int count = 0;
            while (heap.size() > 0 && count < 5) {
                PeliculaConEvaluaciones p = heap.delete();
                System.out.println("ID: " + p.pelicula.getId()
                        + " | Título: " + p.pelicula.getTitulo()
                        + " | Evaluaciones: " + p.evaluaciones
                        + " | Idioma: " + p.pelicula.getIdiomaOriginal());
                count++;
            }

            if (count == 0) {
                System.out.println("No se encontraron películas para este idioma.");
            }

            System.out.println();
        }
    }

    // Clase auxiliar interna para el heap
    private static class PeliculaConEvaluaciones implements Comparable<PeliculaConEvaluaciones> {
        Pelicula pelicula;
        int evaluaciones;

        public PeliculaConEvaluaciones(Pelicula pelicula, int evaluaciones) {
            this.pelicula = pelicula;
            this.evaluaciones = evaluaciones;
        }

        @Override
        public int compareTo(PeliculaConEvaluaciones o) {
            return Integer.compare(this.evaluaciones, o.evaluaciones); // usado en max heap
        }
    }
    //TERMINA CONSULTA 1

    //CONSULTA 2
    public void mostrarTop10PeliculasMejorCalificacion() {
        MyHeap<PeliculaConPromedio> heap = new MyHeapImpl<>(false); // false = max heap

        for (int i = 0; i < peliculas.size(); i++) {
            Pelicula peli = peliculas.get(i);
            int cantidad = peli.getCalificaciones().size();

            if (cantidad >= 100) {
                double promedio = peli.promCalificaciones();
                heap.insert(new PeliculaConPromedio(peli, promedio, cantidad));
            }
        }

        System.out.println("Top 10 películas con mejor calificación media (mínimo 100 evaluaciones):");

        int count = 0;
        while (heap.size() > 0 && count < 10) {
            PeliculaConPromedio p = heap.delete();
            System.out.printf("ID: %d | Título: %s | Promedio: %.2f | Evaluaciones: %d%n",
                    p.pelicula.getId(),
                    p.pelicula.getTitulo(),
                    p.promedio,
                    p.evaluaciones);
            count++;
        }

        if (count == 0) {
            System.out.println("No se encontraron películas con al menos 100 evaluaciones.");
        }

        System.out.println();
    }

    // Clase auxiliar para ordenar por promedio en el heap
    private static class PeliculaConPromedio implements Comparable<PeliculaConPromedio> {
        Pelicula pelicula;
        double promedio;
        int evaluaciones;

        public PeliculaConPromedio(Pelicula pelicula, double promedio, int evaluaciones) {
            this.pelicula = pelicula;
            this.promedio = promedio;
            this.evaluaciones = evaluaciones;
        }

        @Override
        public int compareTo(PeliculaConPromedio o) {
            return Double.compare(this.promedio, o.promedio); // max heap (mayores promedios primero)
        }

    }
    //TERMINA LA CONSULTA 2

    //CONSULTA 3
    public void mostrarTop5CollecionesPorIngresos(){
        //HASH para acumular ingresos por coleccion
        MyHash<Integer, ColeccionConIngresos> ingresosPorColeccion = new MyHashImpl<>();

        for (int i = 0; i < peliculas.size(); i++){
            Pelicula p = peliculas.get(i);

            if (p.getIdColeccion() != null && !p.getIdColeccion().isEmpty()){
                try {
                    int idColeccion = Integer.parseInt(p.getIdColeccion());
                    String nombreColeccion = p.getTituloColeccion();
                    int ingresos = p.getGanancias();

                    ColeccionConIngresos coleccion = ingresosPorColeccion.get(idColeccion);

                    if (coleccion == null){
                        coleccion = new ColeccionConIngresos(idColeccion, nombreColeccion, ingresos);
                        ingresosPorColeccion.put(idColeccion, coleccion);
                    } else {
                        coleccion.ingresosTotales += ingresos;
                    }

                } catch (NumberFormatException e) {
                    // ID de colección no válido (ignoramos)
                }
            }
        }

        // Pasamos las colecciones a un heap para obtener el top 5
        MyHeap<ColeccionConIngresos> heap = new MyHeapImpl<>(false); // false = max heap

        MyList<Integer> claves = ingresosPorColeccion.keys();
        for (int i = 0; i < claves.size(); i++) {
            int clave = claves.get(i);
            ColeccionConIngresos c = ingresosPorColeccion.get(clave);
            heap.insert(c);
        }

        System.out.println("Top 5 colecciones con mayores ingresos generados:");

        int count = 0;
        while (heap.size() > 0 && count < 5) {
            ColeccionConIngresos top = heap.delete();
            System.out.printf("ID: %d | Nombre: %s | Ingresos Totales: %d%n",
                    top.id,
                    top.nombre,
                    top.ingresosTotales);
            count++;
        }

        if (count == 0) {
            System.out.println("No se encontraron colecciones con datos suficientes.");
        }

        System.out.println();
    }

    // Clase auxiliar para agrupar datos por colección
    private static class ColeccionConIngresos implements Comparable<ColeccionConIngresos> {
        int id;
        String nombre;
        int ingresosTotales;

        public ColeccionConIngresos(int id, String nombre, int ingresosTotales) {
            this.id = id;
            this.nombre = nombre;
            this.ingresosTotales = ingresosTotales;
        }

        @Override
        public int compareTo(ColeccionConIngresos otra) {
            return Integer.compare(this.ingresosTotales, otra.ingresosTotales); // max heap
        }
    }
    //TERMINA CONSULTA 3

}
