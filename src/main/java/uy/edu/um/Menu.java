package uy.edu.um;

import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.util.Scanner;

public class Menu {

    private DataLoader loader;

    public void mostrarMenu(){

        Scanner sc = new Scanner(System.in);
        int opcion;

        do{
            System.out.println("-                                                                                     -");
            System.out.println("-                                                                                     -");
            System.out.println("Bienvenido al cine UMovie");
            System.out.println("Seleccione una de las siguientes opciones: ");
            System.out.println("1- Cargar los datos.");
            System.out.println("2- Ejecutar consultas.");
            System.out.println("3- Salir. ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion){
                case 1 ->{
                    loader = new DataLoader();
                    loader.cargarDatos();
                }
                case 2 -> {
                    mostrarSubMenu(sc);
                }
                case 3 ->{
                    System.out.println("Saliendo..");
                    System.exit(1);
                }
                default -> System.out.println("Opción inválida(como vos).");
            }

        }while(true);
    }

    private void mostrarSubMenu(Scanner sc){
        int opcionSubMenu;

        do {
            System.out.println("Seleccione una de las siguientes opciones");
            System.out.println("1- Top 5 de las peliculas que mas calificaciones por idioma");
            System.out.println("2- Top 10 de las peliculas que mejor calificacion media tienen por parte de los usuarios");
            System.out.println("3- Top 5 de las colecciones que mas ingresos generaron");
            System.out.println("4- Top 10 de los directores que mejor calificacion tienen");
            System.out.println("5- Actor con mas calificaciones recibidas en cada mes del año");
            System.out.println("6- Usuarios con mas calificaciones por genero");
            System.out.println("7- Volver al menu principal");
            opcionSubMenu = sc.nextInt();
            sc.nextLine();

            switch (opcionSubMenu) {
                case 1:
                    if (loader == null) {
                        System.out.println("Los datos no estan cargados");
                    } else {
                        System.out.println("Ejecutando consulta...");
                        Consultas consulta1 = new Consultas(loader.peliculasComoLista());
                        consulta1.mostrarTop5PeliculasPorIdioma();
                    }break;

                case 2:
                    if (loader == null){
                        System.out.println("Los datos no estan cargados");
                    } else {
                        System.out.println("Ejecutando consulta...");
                        Consultas consulta2  = new Consultas(loader.peliculasComoLista());
                        consulta2.mostrarTop10PeliculasMejorCalificacion();
                    }break;

                case 3:
                    if (loader == null){
                        System.out.println("Los datos no estan cargados");
                    } else {
                        System.out.println("Ejecutando consulta...");
                        Consultas consulta3 = new Consultas(loader.peliculasComoLista());
                        consulta3.mostrarTop5CollecionesPorIngresos();
                    }break;
                case 4:
                    if (loader == null){
                        System.out.println("Los datos no estan cargados");
                    } else {
                        System.out.println("Ejecutando consulta...");
                        Consultas consulta4 = new Consultas(loader.peliculasComoLista());
                        consulta4.mostrarTop10DirectoresConMejorPromedio();
                    }break;
                case 5:
                    if (loader == null){
                        System.out.println("Los datos no estan cargados");
                    } else {
                        System.out.println("Ejecutando consulta...");
                        Consultas consulta5 = new Consultas(loader.peliculasComoLista());
                        consulta5.actorConMasCalificacionesPorMes();
                    }break;
                case 6:
                    System.out.println("Consulta 6");
                    if (loader == null){
                        System.out.println("Los datos no están cargados");
                    } else {
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
                    break;

                case 7:
                    System.out.println("Volviendo al menu principal");
                    opcionSubMenu = 7;
            }
        }while (opcionSubMenu !=7);
    }
}