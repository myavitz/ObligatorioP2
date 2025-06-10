package uy.edu.um;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataLoader {

    public void cargarDatos() {
        String csvFile = "tuArchivo.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))){
            String[] nextLine;
            // Leer la cabecera si querés saltarla
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                // Cada nextLine es un array con los campos bien separados
                String adult = nextLine[0];
                String belongsToCollection = nextLine[1];
                String budget = nextLine[2];
                String genres = nextLine[3];
                String homepage = nextLine[4];
                String id = nextLine[5];
                String imdb_id = nextLine[6];
                String original_language = nextLine[7];
                String original_title = nextLine[8];
                String overview = nextLine[9];
                String production_companies = nextLine[10];
                String production_countries = nextLine[11];
                String release_date = nextLine[12];
                String revenue = nextLine[13];
                String runtime = nextLine[14];
                String spoken_languages = nextLine[15];
                String status = nextLine[16];
                String tagline = nextLine[17];
                String title = nextLine[18];

            }

        }catch(IOException | CsvValidationException e){
            e.printStackTrace();
        }
    }
}