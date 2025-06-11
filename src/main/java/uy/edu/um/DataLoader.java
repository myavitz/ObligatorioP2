package uy.edu.um;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.text.ParseException;
import java.util.Date;

public class DataLoader {
    private MyHash<Integer, Pelicula> peliculas = new MyHashImpl();



    public void cargarDatos() {

        String csvFile = "tuArchivo.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine;

            // Leer la cabecera si querés saltarla

            Pelicula pelicula = new Pelicula();

            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {

                // Cada nextLine es un array con los campos bien separados

                String belongsToCollection = nextLine[1];
                int idColeccion;
                String nombreColeccion;

                if (belongsToCollection != null && !belongsToCollection.equals("null") && !){

                }else{

                }

                int budget = Integer.parseInt(nextLine[2]);
                pelicula.setPresupuesto(budget);

                String genres = nextLine[3].replace("'", "\"");
                JSONArray arrayGeneros = new JSONArray(genres);
                for (int i = 0; i < arrayGeneros.length(); i++) {
                    JSONObject generObj = arrayGeneros.getJSONObject(i);
                   String nombreGenero = generObj.getString("name");
                    pelicula.getGeneros().add(nombreGenero);
                }

                int id = Integer.parseInt(nextLine[5]);
                pelicula.setId(id);

                String original_language = nextLine[7];
                pelicula.setIdiomaOriginal(original_language);

                long release_date = Date.parse(nextLine[12]);
                pelicula.setFechaEstreno(release_date);

                int revenue = Integer.parseInt(nextLine[13]);
                pelicula.setGanancias(revenue);

                String title = nextLine[18];
                pelicula.setTitulo(title);


                peliculas.put(id, pelicula);

            }








        }catch(IOException | CsvValidationException e){
            e.printStackTrace();
        }
    }
}