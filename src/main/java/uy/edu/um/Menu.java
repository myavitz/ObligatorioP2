package uy.edu.um;

import java.util.Scanner;

public class Menu {

    public void mostrarMenu(){
        Scanner sc = new Scanner(System.in);
        int opcion;

        do{
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion){

                case 1 ->{

                }

                case 2 -> System.exit(1);

                case 3 -> System.out.println("Saliendo..");

                default -> System.out.println("Opción inválida(como vos).");
            }
        }while(opcion != 3);
    }
}