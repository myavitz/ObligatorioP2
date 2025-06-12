package uy.edu.um;

import java.util.Scanner;

public class Menu {

    public void mostrarMenu(){

        Scanner sc = new Scanner(System.in);
        int opcion;

        do{
            System.out.println("Bienvenido al cine UMovie");
            System.out.println("Seleccione una de las siguientes opciones: ");
            System.out.println("1- Cargar los datos.");
            System.out.println("2- No hago nada. ");
            System.out.println("3- Salir. ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion){
                case 1 ->{
                    DataLoader loader = new DataLoader();
                    loader.cargarDatos();
                }
                case 2 -> {
                    System.out.println("No hago nada");
                }
                case 3 ->{
                    System.out.println("Saliendo..");
                    System.exit(1);
                }
                default -> System.out.println("Opción inválida(como vos).");
            }

        }while(opcion != 3);
    }
}