package uy.edu.um;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.ls.LSOutput;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.heap.MyHeap;
import uy.edu.um.tad.heap.MyHeapImpl;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

@Data
@Builder


public class Consultas {

    private final MyList<Pelicula> peliculas;

    public Consultas(MyList<Pelicula> peliculas){
        this.peliculas = peliculas;
    }

    // ### Clase auxilia, dejarlo asi o crear una nueva ### //
    private static class peliculaCalificada implements Comparable<peliculaCalificada>{
        Pelicula pelicula;
        int calificaciones;

        public peliculaCalificada(Pelicula pelicula, int cantidad){
            this.pelicula = pelicula;
            this.calificaciones = cantidad;
        }

        @Override
        public int compareTo(peliculaCalificada otra){
            return Integer.compare(this.calificaciones, otra.calificaciones);
        }
    // ### Termina la clase auxiliar ### //
    }
    public void mostrarTop5PeliculasPorIdioma(){
        long startTime = System.currentTimeMillis();

        String[] idiomasElegir = {"en", "fr", "it", "es", "pt"};
        MyHash<String, MyHeap<peliculaCalificada>> topPorIdioma = new MyHashImpl<>(10);

        for (String idioma : idiomasElegir){
            topPorIdioma.put(idioma, new MyHeapImpl<>(5, false));
        }
        for (int i = 0; i < peliculas.size(); i++){
            Pelicula p = peliculas.get(i);
            if (p == null) continue;

            String idioma = p.getIdiomaOriginal();
            if (topPorIdioma.contains(idioma)){
                int cantidad = p.getCalificaciones().size();
                peliculaCalificada entrada = new peliculaCalificada(p, cantidad);

                MyHeap<peliculaCalificada> heap = topPorIdioma.get(idioma);
                heap.insert(entrada);
                if (heap.size() > 5){
                    heap.delete();
                }
            }
        }
        for (String idioma : idiomasElegir){
            System.out.println("Top 5 por idioma: " + idioma);
            MyHeap<peliculaCalificada> heap = topPorIdioma.get(idioma);

            MyList<peliculaCalificada> resultado = new MyLinkedListImpl<>();
            while (heap.size() > 0){
                resultado.add(heap.delete());
            }
            for (int i = resultado.size() - 1; i >= 0; i--){
                peliculaCalificada pc = resultado.get(i);
                System.out.println(pc.pelicula.getId() + "," +  pc.pelicula.getTitulo() + "," + pc.calificaciones + "," + idioma);
            }
            System.out.println();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion: " + (endTime -startTime) + "ms");
    }
}
