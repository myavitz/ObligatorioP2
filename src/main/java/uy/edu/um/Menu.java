package uy.edu.um;

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

        }while(opcion != 3);
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
                    System.out.println("Consulta 3");
                case 4:
                    System.out.println("Consulta 4");
                case 5:
                    System.out.println("Consulta 5");
                case 6:
                    System.out.println("Consulta 6");
                case 7:
                    System.out.println("Volviendo al menu principal");
                    opcionSubMenu = 7;
            }
        }while (opcionSubMenu !=7);
    }
}