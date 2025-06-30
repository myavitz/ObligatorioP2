package uy.edu.um;

import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private DataLoader loader;

    public void mostrarMenu() {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("-                                                                                     -");
            System.out.println("-                                                                                     -");
            System.out.println("Bienvenido al cine UMovie");
            System.out.println("Seleccione una de las siguientes opciones: ");
            System.out.println("1- Cargar los datos.");
            System.out.println("2- Ejecutar consultas.");
            System.out.println("3- Salir.");

            try {
                opcion = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1 -> {
                        System.out.println("Iniciando la carga de Datos...");
                        loader = new DataLoader();
                        loader.cargarDatos();
                    }
                    case 2 -> mostrarSubMenu(sc);
                    case 3 -> {
                        System.out.println("Saliendo...");
                        System.exit(0);
                    }
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
                sc.nextLine(); // consumir entrada inválida
            }

        } while (true);
    }

    private void mostrarSubMenu(Scanner sc) {
        int opcionSubMenu = -1;

        do {
            System.out.println("Seleccione una de las siguientes opciones");
            System.out.println("1- Top 5 de las películas que más calificaciones por idioma");
            System.out.println("2- Top 10 de las películas que mejor calificación media tienen por parte de los usuarios");
            System.out.println("3- Top 5 de las colecciones que más ingresos generaron");
            System.out.println("4- Top 10 de los directores que mejor calificación tienen");
            System.out.println("5- Actor con más calificaciones recibidas en cada mes del año");
            System.out.println("6- Usuarios con más calificaciones por género");
            System.out.println("7- Volver al menú principal");

            try {
                opcionSubMenu = sc.nextInt();
                sc.nextLine();

                switch (opcionSubMenu) {
                    case 1 -> {
                        verificarCarga();
                        new Consultas(loader.peliculasComoLista()).mostrarTop5PeliculasPorIdioma();
                    }
                    case 2 -> {
                        verificarCarga();
                        new Consultas(loader.peliculasComoLista()).mostrarTop10PeliculasMejorCalificacion();
                    }
                    case 3 -> {
                        verificarCarga();
                        new Consultas(loader.peliculasComoLista()).mostrarTop5CollecionesPorIngresos();
                    }
                    case 4 -> {
                        verificarCarga();
                        new Consultas(loader.peliculasComoLista()).mostrarTop10DirectoresConMejorPromedio();
                    }
                    case 5 -> {
                        verificarCarga();
                        new Consultas(loader.peliculasComoLista()).actorConMasCalificacionesPorMes();
                    }
                    case 6 -> {
                        verificarCarga();
                        System.out.println("Ejecutando consulta...");
                        MyList<Pelicula> peliculas = loader.peliculasComoLista();
                        MyHash<Integer, MyHash<String, Integer>> evals = loader.getEvaluacionesUsuarioPorGenero();
                        MyList<Integer> clavesUsuarios = loader.getClavesUsuarios();
                        MyHash<String, Integer> cantidadEvaluacionesPorGenero = loader.getCantidadEvaluacionesPorGenero();

                        System.out.println("Cantidad de usuarios en evaluacionesUsuarioPorGenero: " + evals.size());
                        System.out.println("Cantidad de clavesUsuarios: " + clavesUsuarios.size());

                        Consultas consulta6 = new Consultas(peliculas, evals, clavesUsuarios, cantidadEvaluacionesPorGenero);

                        long start = System.currentTimeMillis();
                        consulta6.usuariosConMasEvaluaciones();
                        long end = System.currentTimeMillis();
                        System.out.println("Tiempo total ejecución consulta 6: " + (end - start) + " ms");
                    }
                    case 7 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
                sc.nextLine();
            }

        } while (opcionSubMenu != 7);
    }

    private void verificarCarga() {
        if (loader == null) {
            System.out.println("Los datos no están cargados.");
            throw new IllegalStateException("Datos no cargados.");
        }
    }
}