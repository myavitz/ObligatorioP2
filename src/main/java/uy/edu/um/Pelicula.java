package uy.edu.um;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Pelicula {


    private int id;
    private int presupuesto;
    private int ganancias;
    private int cantidadEvaluaciones;
    private int duracion;

    private long fechaEstreno;

    private double sumaCalificaciones;

    private final MyList<String> generos = new MyLinkedListImpl<>();
    private final MyList<String> actores = new MyLinkedListImpl<>();
    private final MyList<Calificacion> calificaciones = new MyLinkedListImpl<>();

    private String titulo;
    private String idiomaOriginal;
    private String idColeccion;
    private String tituloColeccion;
    private String director;











}
