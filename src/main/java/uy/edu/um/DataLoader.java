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
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataLoader {
    private MyHash<Integer, Pelicula> peliculas = new MyHashImpl();
    private MyHash<Integer, MyList<Calificacion>> ratingsPorPelicula = new MyHashImpl<>();
    private MyHash<Integer, Participante> participantesPorPelicula = new MyHashImpl<>();



    public void cargarDatos() {

        int peliculasCargadas = 0;
        int erroresParseo = 0;
        long inicio = System.currentTimeMillis();

        String csvmetadata = "C:\\Users\\franc\\OneDrive\\Escritorio\\DATASETS v2\\DATASETS v2\\movies_metadata.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvmetadata))) {
            String[] nextLine;

            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {

                Pelicula pelicula = new Pelicula();

                String belongsToCollection = nextLine[1];
                int idColeccion;
                String nombreColeccion;

                try {
                    if (belongsToCollection != null && !belongsToCollection.equals("null") && !belongsToCollection.isEmpty()) {
                        belongsToCollection = belongsToCollection.replace("'", "\"");
                        belongsToCollection = belongsToCollection.replace("\"{", "{");
                        belongsToCollection = belongsToCollection.replace("}\"", "}");
                        belongsToCollection = belongsToCollection.replace("\\\"", "\"");

                        // Corregir comillas internas mal puestas
                        belongsToCollection = belongsToCollection.replaceAll("(?<=\\w)\"(?=\\w)", "'");

                        JSONObject jsonColeccion = new JSONObject(belongsToCollection);
                        idColeccion = jsonColeccion.getInt("id");
                        nombreColeccion = jsonColeccion.getString("name");
                    } else {
                        idColeccion = Integer.parseInt(nextLine[5]);
                        nombreColeccion = nextLine[18];
                    }
                } catch (Exception e) {
                    System.out.println("Error parseando belongsToCollection en línea con id: " + nextLine[5]);
                    System.out.println("Valor problemático: " + belongsToCollection);
                    e.printStackTrace();
                    erroresParseo++;
                    continue; // Saltar esta película y continuar con la siguiente
                }

                int budget = 0;
                if (nextLine[2] != null && !nextLine[2].isEmpty() && !nextLine[2].equals("NaN")) {
                    try {
                        budget = Integer.parseInt(nextLine[2]);
                    } catch (NumberFormatException e) {
                        budget = 0; // Si falla, lo dejamos en 0
                    }
                }
                pelicula.setPresupuesto(budget);

                try {
                    String genres = nextLine[3].replace("'", "\"");
                    genres = genres.replace("\"[", "[");
                    genres = genres.replace("]\"", "]");
                    genres = genres.replace("\\\"", "\"");

                    JSONArray arrayGeneros = new JSONArray(genres);
                    for (int i = 0; i < arrayGeneros.length(); i++) {
                        JSONObject generObj = arrayGeneros.getJSONObject(i);
                        String nombreGenero = generObj.getString("name");
                        pelicula.getGeneros().add(nombreGenero);
                    }
                } catch (Exception e) {
                    System.out.println("Error parseando genres en línea con id: " + nextLine[5]);
                    System.out.println("Valor problemático: " + nextLine[3]);
                    e.printStackTrace();
                    erroresParseo++;
                    // Continuar aunque pierda géneros
                }

                try {
                    int id = Integer.parseInt(nextLine[5]);
                    pelicula.setId(id);

                    String original_language = nextLine[7];
                    pelicula.setIdiomaOriginal(original_language);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date r_date = null;

                    if (nextLine[12] != null && !nextLine[12].isEmpty()) {
                        try {
                            r_date = sdf.parse(nextLine[12]);
                        } catch (ParseException e) {
                            r_date = null;
                        }
                    }
                    pelicula.setFechaEstreno(r_date);

                    int revenue = 0;
                    if (nextLine[13] != null && !nextLine[13].isEmpty() && !nextLine[13].matches(".*[a-zA-Z]+.*")) {
                        try {
                            revenue = Integer.parseInt(nextLine[13]);
                        } catch (NumberFormatException e) {
                            revenue = 0;
                        }
                    }
                    pelicula.setGanancias(revenue);

                    String title = nextLine[18];
                    pelicula.setTitulo(title);

                    peliculas.put(id, pelicula);
                    peliculasCargadas++;

                    System.out.println("Película cargada: " + id + " - " + title);

                } catch (Exception e) {
                    System.out.println("Error procesando datos finales de la película con id: " + nextLine[5]);
                    e.printStackTrace();
                    erroresParseo++;
                }
            }

            long fin = System.currentTimeMillis();
            System.out.println("Carga finalizada.");
            System.out.println("Películas cargadas exitosamente: " + peliculasCargadas);
            System.out.println("Errores de parseo: " + erroresParseo);
            System.out.println("Tiempo total de carga: " + (fin - inicio) + " ms");

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        String csvcreditos = "C:\\Users\\franc\\OneDrive\\Escritorio\\DATASETS v2\\DATASETS v2\\credits.csv";
        int maserrores = 0;
        try (CSVReader reader2 = new CSVReader(new FileReader(csvcreditos))){
            String [] nextLine;
            reader2.readNext();
            while((nextLine = reader2.readNext()) != null){
                int idpeli = Integer.parseInt(nextLine[2]);
                try {
                    String mac = nextLine[0];
                    mac = mac.replaceAll("(?<=\\w)\"(?=\\w)", "'");
                    mac = mac.replace("\"[", "[");
                    mac = mac.replace("]\"", "]");
                    JSONArray macarray = new JSONArray(mac);
                    System.out.println(mac);
                    for (int i = 0; i < macarray.length(); i++) {
                        JSONObject actObj = macarray.getJSONObject(i);
                        String nombreactor = actObj.getString("name");
                        if(peliculas.get(idpeli)!=null)
                            peliculas.get(idpeli).getActores().add(nombreactor);

                    }

                }catch(Exception e){
                    System.out.println("error parseando en id " + idpeli);
                    maserrores++;
                }

            }
        }catch(IOException | CsvValidationException e){
            e.printStackTrace();
        }
        System.out.println("Hubieron "+ maserrores + " errores");

    }



}