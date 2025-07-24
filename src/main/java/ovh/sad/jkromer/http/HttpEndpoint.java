package ovh.sad.jkromer.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.net.http.HttpClient;
import java.time.Instant;

public class HttpEndpoint {
    protected static String endpoint = "https://kromer.sad.ovh/api/krist"; // This is never going 2 change probably
    protected static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type, ctx) ->
                    Instant.parse(json.getAsString()))
            .create();
    protected static final HttpClient http = HttpClient.newHttpClient(); // uses system default config

}
