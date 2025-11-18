import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLClassLoader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static List<String> monedas = new ArrayList<>();
    public static String accion = "";
    public static String moneda = "";
    public static String url_str ="";
    public static String json;

    public static void main(String[] args) {
        monedas.add("MXN");
        monedas.add("ARS");
        monedas.add("COP");

        Scanner leer = new Scanner(System.in);
        int opcion = 0;
        int seleccion = 0;

        while(true){

            System.out.println("VETE ALV ESTA BIEEEN CARO!!");
            System.out.println("INGRESA EL TIPO DE OPERACION QUE DESEAS HACER: ");
            System.out.println("-------------------------");
            System.out.println("1.- A cuanto esta el cambio? (EQUIVALENCIA)");
            System.out.println("2.- Conversion de moneda (X a Y)");
            System.out.println("3.- SALIR");
            try{
                opcion = leer.nextInt();
            } catch (NumberFormatException e) {
                System.out.println("INGRESA UN NUMERO!");
            }

            switch (opcion){
                case 1:
                    accion = "/latest/";
                    System.out.println("De que moneda quieres ver los valores: ");
                    System.out.println("1.- pesos mexicanos: MXN");
                    System.out.println("2.- pesos Argentinos: ARS");
                    System.out.println("3.- pesos colombianso: COP");
                    try{
                        seleccion = leer.nextInt();

                        int indice = Integer.parseInt(String.valueOf(seleccion)) - 1;

                        if (indice >= 0 && indice < monedas.size()) {
                            moneda = monedas.get(indice);
                            url_str = getApiKeyChange(moneda);
                        } else {
                            System.out.println("Opción inválida para recurso.");
                        }
                        getJson();
                    } catch (NumberFormatException e) {
                        System.out.println("INGRESA UN NUMERO!");
                    }
                    break;

                case 2:
                    break;

                default:
                    break;
            }


            //a que moneda se hace la conversion
           // url_str = url_str +"USD";
            //pedir la APIkey del archivo config


            if(opcion == 3){
                break;
            }
        }

    }

    public static void getJson(){

        try {
            System.out.println(": "+url_str);

            // Creando cliente HTTP
            //metodo abstracto para crear la peticion http
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    //crea la url donde se realiza la peticion
                    .uri(URI.create(url_str))
                    .build();

            //creacion del string de la respuesta que se obtine de la peticion
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //vuelca el contenido de la respuesta en un json
            json = response.body();
            System.out.println(json);

            //crea una instancia de la clase record para usarla como base para el json
            //starWarsAPI mitituloSW = gson.fromJson(json, starWarsAPI.class);

            // System.out.println(mitituloSW);
        }catch (IllegalArgumentException ex) {
            System.out.println("VERIFICA LA DIRECCION");

        } catch (Exception e) {
            System.out.println("ALGO MALO PASO: " + e);
        }
    }
    public static String getApiKeyChange(String moneda){
        final Properties properties;
        properties = new Properties();
            try (FileInputStream input = new FileInputStream("config.properties")) {
                properties.load(input);

                String URL_KEY = "";

                String urlBase = properties.getProperty("BASE_URL");
                String key = properties.getProperty("API_KEY");
                String arg = "/latest/";

                URL_KEY = urlBase+key+arg+moneda;
//                return properties.getProperty("BASE_URL"+"API_KEY"+"/latest/");
                return URL_KEY;

            } catch (IOException ex) {
                System.out.println("Error al cargar config.properties. Asegurate de que existe.");
                return null;
            }
    }

}