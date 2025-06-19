package uy.edu.um;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataLoader {
    private MyHash<Integer, Pelicula> peliculas = new MyHashImpl();
    private MyHash<Integer, MyList<Calificacion>> ratingsPorPelicula = new MyHashImpl<>();
    private MyHash<Integer, Participante> participantesPorPelicula = new MyHashImpl<>();

    public MyList<Pelicula> peliculasComoLista() {
        MyList<Pelicula> lista = new MyLinkedListImpl<>();
        MyList<Integer> claves = peliculas.keys();

        for (int i = 0; i < claves.size(); i++) {
            Integer id = claves.get(i);
            Pelicula p = peliculas.get(id);
            if (p != null) {
                lista.add(p);
            }
        }

        return lista;
    }

    private String belongsToCollectionCrudo;
    private Integer idColeccionRecuperado;
    private String nombreColeccionRecuperado;


    public void cargarDatos() {
        int lineaActual = 1;
        int peliculasCargadas = 0;
        int erroresParseo = 0;
        long inicio = System.currentTimeMillis();

        String csvFile = "movies_metadata.csv";
        String csvcreditos = "credits.csv";

        String[] nextLine = null;

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withQuoteChar('"')
                .build();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile))
                .withCSVParser(parser)
                .build()) {

            reader.readNext(); // salto cabecera

            while ((nextLine = reader.readNext()) != null) {
                lineaActual++;

                if (nextLine.length < 19) {
                    System.out.println("Línea con columnas insuficientes en línea: " + lineaActual);
                    erroresParseo++;
                    continue;
                }

                Pelicula pelicula = new Pelicula();

                String belongsToCollection = nextLine[1];

                int idColeccion;
                String nombreColeccion;
                boolean lineaValida = true;

                try {
                    if (belongsToCollection != null && !belongsToCollection.equals("null") && !belongsToCollection.isEmpty() && belongsToCollection.trim().startsWith("{")) {
                        belongsToCollection = belongsToCollection.replaceAll("(?<=\\{|, )'(\\w+)':", "\"$1\":");  // claves
                        belongsToCollection = belongsToCollection.replaceAll(":\\s*'(.*?)'(?=[,}])", ": \"$1\""); // valores


                        // Corregir comillas internas mal puestas

                        belongsToCollection = belongsToCollection.replace("None", "null");


                        JSONObject jsonColeccion = new JSONObject(belongsToCollection);
                        idColeccion = jsonColeccion.getInt("id");
                        nombreColeccion = jsonColeccion.getString("name");
                    } else {
                        try {
                            idColeccion = Integer.parseInt(nextLine[5]); // id de la película
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido en línea: " + lineaActual);
                            lineaValida = false;
                        }
                        nombreColeccion = nextLine[18];
                    }
                } catch (Exception e) {
                    System.out.println("Película ignorada por error en belongsToCollection en línea: " + lineaActual);
                    System.out.println("Detalle: " + e.getMessage());
                    System.out.println("----------------------------------------------------");
                    erroresParseo++;
                    continue; // Saltar esta película y continuar con la siguiente
                }

                if (!lineaValida) {
                    System.out.println("Película ignorada por ID inválido en línea: " + lineaActual);
                    System.out.println("----------------------------------------------------");
                    erroresParseo++;
                    continue;
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
                    System.out.println("Error parseando en línea: " + lineaActual);
                    System.out.println("ID o dato clave: " + nextLine[5]);
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

                } catch (Exception e) {
                    System.out.println("Error procesando datos finales de la película con id: " + (nextLine != null && nextLine.length > 5 ? nextLine[5] : "desconocido"));
                    e.printStackTrace();
                    erroresParseo++;
                }
            }


        } catch (IOException | CsvValidationException e) {
            System.out.println("Error general leyendo el archivo CSV en línea: " + lineaActual);
            if (nextLine != null && nextLine.length > 5) {
                System.out.println("ID o dato clave: " + nextLine[5]);
            }
            e.printStackTrace();
        }

        int maserrores = 0;
        lineaActual = 0;
        int idpeli=0;

        try (CSVReader reader2 = new CSVReaderBuilder(new FileReader(csvcreditos))
                .withCSVParser(parser)
                .build()) {

            nextLine=null;
            reader2.readNext();

            while ((nextLine = reader2.readNext()) != null) {
                lineaActual++;

                try {
                     idpeli = Integer.parseInt(nextLine[2]);
                }catch(NumberFormatException e){
                    System.out.println("Error parseando el idpeli en linea: " + lineaActual);
                }

                try {
                    String mac = nextLine[0];

                    mac = mac.replaceAll("(?<=\\w)\"(?=\\w)", "'");
                    mac = mac.replace("\"[", "[");
                    mac = mac.replace("]\"", "]");
                    JSONArray macarray = new JSONArray(mac);

                    for (int i = 0; i < macarray.length(); i++) {
                        JSONObject actObj = macarray.getJSONObject(i);
                        String nombreactor = actObj.getString("name");
                        if (peliculas.get(idpeli) != null) {
                            peliculas.get(idpeli).getActores().add(nombreactor);
                        }
                    }

                } catch (Exception e) {
                    maserrores++;
                    System.out.println("Error parseando el nombre del actor en linea: " + lineaActual);
                }
                try {
                    String repo = nextLine[1];
                    repo = repo.replaceAll("(?<=\\w)\"(?=\\w)", "'");
                    repo = repo.replace("\"[", "[");
                    repo = repo.replace("]\"", "]");
                    JSONArray repoarray = new JSONArray(repo);

                    for (int i = 0; i < repoarray.length(); i++) {
                        JSONObject actObj = repoarray.getJSONObject(i);
                        String check = actObj.getString("job");
                        if (check.equals("Director")) {
                            String director = actObj.getString("name");
                            if (peliculas.get(idpeli) != null)
                                peliculas.get(idpeli).setDirector(director);
                        }
                    }
                } catch (Exception e) {
                    maserrores++;
                    System.out.println("Error parseando al director en linea: " + lineaActual);
                }
            }
        }catch (IOException e){
            System.out.println("PAPA");
        } catch (CsvValidationException e) {
            System.out.println("Soy gay");
        }

        long fin = System.currentTimeMillis();
        System.out.println("Carga finalizada.");
        System.out.println("Películas cargadas exitosamente: " + peliculasCargadas);
        System.out.println("Errores de parseo: " + erroresParseo);
        System.out.println("Tiempo total de carga: " + (fin - inicio) + " ms");

        System.out.println("Total de errores: Peliculas - " + erroresParseo + " Creditos - " + maserrores);
    }
}