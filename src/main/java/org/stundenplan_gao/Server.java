package org.stundenplan_gao;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public static void main(String[] args) {
        final String URL = "http://localhost:8080/Stundenplan_Server/stundenplan/schueler/login";

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        Map<Object, Object> data = new HashMap<>();
        data.put("username", "ysprenger");
        data.put("password", "abc123");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(data))
                .uri(URI.create(URL))
                .setHeader("User-Agent", "Stundenplan Client")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(response.headers());
        System.out.println(response.body());

        /*Client client;
        Response response;
        String URL;

        URL = ServerURL + ":" + port + "/Stundenplan_Server/stundenplan/schueler/";
        client = ClientBuilder.newClient();

        response = client.target(URL + "login").queryParam("username", "ysprenger").queryParam("password", "abc123").request().header(HttpHeaders.AUTHORIZATION, "").get();
        String token = response.readEntity(String.class);

        response = client.target(URL + "faecherauswahl").request().header(HttpHeaders.AUTHORIZATION, token).get();

        System.out.println(response.getStatus());
        System.out.println(Arrays.toString(response.readEntity(Fach[].class)));

        client.close();*/
    }

}
