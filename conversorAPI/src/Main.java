import com.google.gson.Gson;
import models.monedasConversion;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {
    public static List<String> monedas = new ArrayList<>();
    public static String accion = "";
    public static String moneda = "";
    //public static String url_str ="";
    public static String json;

    public static void main(String[] args) {
        monedas.add("MXN");
        monedas.add("ARS");
        monedas.add("COP");

        Scanner leer = new Scanner(System.in);
        int opcion = 0;
        int seleccion = 0;
        String url_str ="";
        while(true){
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
                    System.out.println("De que moneda quieres ver los valores: ");
                    System.out.println("1.- pesos mexicanos: MXN");
                    System.out.println("2.- pesos Argentinos: ARS");
                    System.out.println("3.- pesos colombianso: COP");
                    try{
                        seleccion = leer.nextInt();

                        int indice = Integer.parseInt(String.valueOf(seleccion)) - 1;

                        if (indice >= 0 && indice < monedas.size()) {
                            moneda = monedas.get(indice);
                            url_str = getApiUrl();
                        } else {
                            System.out.println("Opción inválida para recurso.");
                        }
                        getJsonEquivalent(url_str,moneda);
                    } catch (NumberFormatException e) {
                        System.out.println("INGRESA UN NUMERO!");
                    }
                    break;

                case 2:
                    String moneda1="";
                    String moneda2="";
                    double cantidad =0.0;
                    System.out.println("Que moneda quieres de origen: ");
                    System.out.println("1.- pesos mexicanos: MXN");
                    System.out.println("2.- pesos Argentinos: ARS");
                    System.out.println("3.- pesos colombianso: COP");
                    try{
                        seleccion = leer.nextInt();

                        int indice = Integer.parseInt(String.valueOf(seleccion)) - 1;

                        if (indice >= 0 && indice < monedas.size()) {
                            moneda1 = monedas.get(indice);
                        } else {
                            System.out.println("Opción inválida para recurso.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("INGRESA UN NUMERO!");
                    }

                    //moneda
                    System.out.println("Que moneda quieres de conversion: ");
                    System.out.println("1.- pesos mexicanos: MXN");
                    System.out.println("2.- pesos Argentinos: ARS");
                    System.out.println("3.- pesos colombianso: COP");
                    try{
                        seleccion = leer.nextInt();

                        int indice = Integer.parseInt(String.valueOf(seleccion)) - 1;

                        if (indice >= 0 && indice < monedas.size()) {
                            moneda2 = monedas.get(indice);
                            url_str = getApiUrl();

                        } else {
                            System.out.println("Opción inválida para recurso.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("INGRESA UN NUMERO!");
                    }

                    System.out.println("Ingresa la cantidad a convertir");

                    try{
                        cantidad = leer.nextDouble();

                        //mostrar el json o la infor recuperada
                    } catch (NumberFormatException e) {
                        System.out.println("INGRESA UN NUMERO!");
                    }

                    getJsonChange(url_str,moneda1,moneda2,cantidad);

                    break;

                    case 3:
                    System.out.println("SIONO ESTA BIEN CARO PATROON !");
                    break;

                default:
                    System.out.println("OPCION INCORRECTA NENE");
                    break;
            }
            if(opcion == 3){
                break;
            }
        }

    }

    //METODO SOLO PARA HACER la peticion de la URL
    public static void getJsonEquivalent(String url_str,String moneda){
        try {
           // System.out.println(": "+url_str);
            // Creando cliente HTTP
            //metodo abstracto para crear la peticion http
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    //crea la url donde se realiza la peticion
                    .uri(URI.create(url_str+"/latest/"+moneda))
                    .build();

            //creacion del string de la respuesta que se obtine de la peticion
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //vuelca el contenido de la respuesta en un json
            json = response.body();
            // System.out.println(json);.
            // Deserializa el JSON usando el record
            Gson gson = new Gson();
            monedasConversion data = gson.fromJson(json, monedasConversion.class);

            System.out.println("Estado de la API: " + data.result());
            System.out.println("Moneda Base: " + data.base_code());
            System.out.println("Ultima vez de consulta: "+data.time_last_update_utc());

            // Acceder a las tasas
            Map<String, Double> tasas = data.conversion_rates();
            System.out.println("Equivalencias de conversión:");

            for (int i = 0; i < monedas.size(); i++) {

                System.out.println(i+1+".- " + tasas.get(monedas.get(i))+" "+monedas.get(i));
            }
        }catch (IllegalArgumentException ex) {
            System.out.println("VERIFICA LA DIRECCION");

        } catch (Exception e) {
            System.out.println("ALGO MALO PASO: " + e);
        }
    }


    public static void getJsonChange(String url_str,String moneda1,String moneda2, double cantidad){
        //REGRESA LA URL DE LA ULTIMA ACTUALIZACION CON LA MONEDA QUE SE CONSULTA
        try {
            // Creando cliente HTTP
            //metodo abstracto para crear la peticion http
            String url = url_str+"/pair/"+moneda1+"/"+moneda2+"/"+cantidad;

            System.out.println(": "+url);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    //crea la url donde se realiza la peticion
                    .uri(URI.create(url))
                    .build();

            //creacion del string de la respuesta que se obtine de la peticion
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //vuelca el contenido de la respuesta en un json
            json = response.body();
            // System.out.println(json);.
            // Deserializa el JSON usando el record
            Gson gson = new Gson();
            monedasConversion data = gson.fromJson(json, monedasConversion.class);

            System.out.println("Estado de la API: " + data.result());
            System.out.println("Moneda Base: " + data.base_code());
            System.out.println("Ultima vez de consulta: "+data.time_last_update_utc());
            System.out.println("ORIGEN: "+ data.target_code());
            // Acceder a las tasas
            Map<String, Double> tasas = data.conversion_rates();
//tasas.get(monedas.get(i))
            System.out.println("Tasa de conversion: 1 "+data.base_code() +" equivale a "+ data.conversion_rate()+" "+data.target_code());
            System.out.println("Conversion! :");
            System.out.println(data.conversion_result());

        }catch (IllegalArgumentException ex) {

            System.out.println("VERIFICA LA DIRECCION");

        } catch (Exception e) {
            System.out.println("ALGO MALO PASO: " + e);
        }
    }

    public static String getApiUrl(){
        final Properties properties;
        properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            String urlBase = properties.getProperty("BASE_URL");
            String key = properties.getProperty("API_KEY");

            //SOLO REGRESA LA URL CON LA API LISTA PARA HACER LA PETICION
            return urlBase+key;

        } catch (IOException ex) {
            System.out.println("Error al cargar config.properties. Asegurate de que existe.");
            return null;
        }
    }

}