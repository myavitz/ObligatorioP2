package uy.edu.um;

import uy.edu.um.tad.binarytree.MySearchBinaryTree;
import uy.edu.um.tad.binarytree.MySearchBinaryTreeImpl;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.heap.MyHeap;
import uy.edu.um.tad.heap.MyHeapImpl;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.*;

public class Consultas {
    private final MyList<Pelicula> peliculas;

    public Consultas(MyList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    //CONSULTA 1
    public void mostrarTop5PeliculasPorIdioma() {
        long inicio = System.currentTimeMillis();

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

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms");
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
        long inicio = System.currentTimeMillis();

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

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms\n");
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
    public void mostrarTop5CollecionesPorIngresos() {
        long inicio = System.currentTimeMillis();

        // HASH para acumular ingresos por colección
        MyHash<Integer, ColeccionConIngresos> ingresosPorColeccion = new MyHashImpl<>();

        for (int i = 0; i < peliculas.size(); i++) {
            Pelicula p = peliculas.get(i);

            if (p.getIdColeccion() != null && !p.getIdColeccion().isEmpty()) {
                try {
                    int idColeccion = Integer.parseInt(p.getIdColeccion());
                    String nombreColeccion = p.getTituloColeccion();
                    int ingresos = p.getGanancias();

                    ColeccionConIngresos coleccion = ingresosPorColeccion.get(idColeccion);

                    if (coleccion == null) {
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

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms\n");
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

    //CONSULTA 4
    public void mostrarTop10DirectoresConMejorPromedio() {
        long inicio = System.currentTimeMillis();

        MyHash<String, DirectorStats> datosPorDirector = agruparDatosPorDirector();

        MyHeap<ResultadoDirector> top10 = construirHeapTop10(datosPorDirector);

        imprimirResultados(top10);

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms");
    }

    private MyHash<String, DirectorStats> agruparDatosPorDirector() {
        MyHash<String, DirectorStats> datos = new MyHashImpl<>();

        MyList<Pelicula> listaPeliculas = this.peliculas;
        for (int i = 0; i < listaPeliculas.size(); i++) {
            Pelicula p = listaPeliculas.get(i);
            if (p == null || p.getDirector() == null) continue;

            String director = p.getDirector();

            DirectorStats stats = datos.get(director);
            if (stats == null) {
                stats = new DirectorStats();
                datos.put(director, stats);
            }

            stats.cantidadPeliculas++;

            MyList<Calificacion> calis = p.getCalificaciones();
            for (int j = 0; j < calis.size(); j++) {
                stats.agregarCalificacion(calis.get(j).getPuntuacion());
            }
        }
        return datos;
    }

    private MyHeap<ResultadoDirector> construirHeapTop10(MyHash<String, DirectorStats> datosPorDirector) {
        MyHeap<ResultadoDirector> heap = new MyHeapImpl<>(true); // min heap

        MyList<String> directores = datosPorDirector.keys();
        for (int i = 0; i < directores.size(); i++) {
            String nombre = directores.get(i);
            DirectorStats ds = datosPorDirector.get(nombre);

            if (ds.cantidadPeliculas > 1 && ds.cantidadEvaluaciones > 100) {
                double mediana = ds.calcularMediana();
                ResultadoDirector res = new ResultadoDirector(nombre, ds.cantidadPeliculas, mediana);

                if (heap.size() < 10) {
                    heap.insert(res);
                } else if (res.compareTo(heap.get()) > 0) {
                    heap.delete();
                    heap.insert(res);
                }
            }
        }
        return heap;
    }

    private void imprimirResultados(MyHeap<ResultadoDirector> top10) {
        MyList<ResultadoDirector> resultadoFinal = new MyLinkedListImpl<>();
        while (top10.size() > 0) {
            resultadoFinal.add(top10.delete());
        }
        for (int i = resultadoFinal.size() - 1; i >= 0; i--) {
            System.out.println(resultadoFinal.get(i));
        }
    }


    //CLASE AUXILIAR 1 PARA CONSULTA 4
    public class DirectorStats {
        int cantidadPeliculas = 0;
        int cantidadEvaluaciones = 0;
        int contadorUnico = 0;

        // Usamos un árbol binario de búsqueda para mantener las calificaciones ordenadas
        MySearchBinaryTree<Double, Double> calificaciones = new MySearchBinaryTreeImpl<>();

        public void agregarCalificacion(double calificacion) {
            // Para evitar claves duplicadas (calificaciones iguales), sumamos un pequeño offset
            double clave = calificacion + contadorUnico * 0.00001;
            calificaciones.add(clave, calificacion);
            contadorUnico++;
            cantidadEvaluaciones++;
        }

        public double calcularMediana() {
            MyList<Double> ordenadas = calificaciones.inOrderValues();
            int n = ordenadas.size();
            if (n == 0) return 0;

            if (n % 2 == 1) {
                return ordenadas.get(n / 2);
            } else {
                return (ordenadas.get(n / 2 - 1) + ordenadas.get(n / 2)) / 2.0;
            }
        }
    }

    public class ResultadoDirector implements Comparable<ResultadoDirector> {
        private final String nombre;
        private final int cantidadPeliculas;
        private final double mediana;

        public ResultadoDirector(String nombre, int cantidadPeliculas, double mediana) {
            this.nombre = nombre;
            this.cantidadPeliculas = cantidadPeliculas;
            this.mediana = mediana;
        }

        public String getNombre() {
            return nombre;
        }

        public int getCantidadPeliculas() {
            return cantidadPeliculas;
        }

        public double getMediana() {
            return mediana;
        }

        @Override
        public int compareTo(ResultadoDirector otro) {
            // Ordenamos por mediana (menor mediana = menor prioridad en min heap)
            return Double.compare(this.mediana, otro.mediana);
        }

        @Override
        public String toString() {
            return nombre + "," + cantidadPeliculas + "," + String.format(Locale.US, "%.3f", mediana);
            //TRES NUMEROS DESPUES DE LA COMA PARA VER SI HACE BIEN LOS CALCULOS
        }
    }

    //TERMINA LA CONSULTA 4
    //Consulta 5
    public void actorConMasCalificacionesPorMes() {
        MyHash<Integer, MyHash<String, int[]>> datosPorMes = new MyHashImpl<>();

        for (int i = 0; i < peliculas.size(); i++) {
            Pelicula p = peliculas.get(i);
            MyList<String> actores = p.getActores();
            MyList<Calificacion> calificaciones = p.getCalificaciones();

            // Hash para saber en qué meses ya fue contada esta película
            MyHash<Integer, Boolean> mesesYaContados = new MyHashImpl<>();

            for (int j = 0; j < calificaciones.size(); j++) {
                Calificacion c = calificaciones.get(j);
                Date fecha = c.getFecha();
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTime(fecha);
                int mes = calendar.get(Calendar.MONTH) + 1;

                if (!datosPorMes.contains(mes)) {
                    datosPorMes.put(mes, new MyHashImpl<>());
                }
                MyHash<String, int[]> actoresDelMes = datosPorMes.get(mes);

            }
        }
    }
}