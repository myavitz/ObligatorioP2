package uy.edu.um;

import lombok.Data;

import java.util.Date;


@Data
public class Calificacion {
    private int userId;
    private int movieId;
    private double puntuacion;
    private Date fecha;


    public String toString(){
        return "Usuario: " + userId + " | Id Pelicula: " + movieId + " | Puntuación: " + puntuacion + " | Fecha: " + fecha;
    }

}
