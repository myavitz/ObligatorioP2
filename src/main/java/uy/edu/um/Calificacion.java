package uy.edu.um;

import lombok.Data;

import java.util.Date;


@Data
public class Calificacion {
    private int userId;
    private double puntuacion;
    private Date fecha;
}
