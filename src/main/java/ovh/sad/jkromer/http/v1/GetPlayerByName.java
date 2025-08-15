package ovh.sad.jkromer.http.v1;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class GetPlayerByName extends HttpEndpoint {
    public static class Player {
        public int id;
        public String address;
        public double balance;
        public Date created_at;
        public boolean locked;
        public double total_in;
        public double total_out;
    }


    public static class GetPlayerByResponse {
        public String error;
        public String message;
        public Boolean ok;

        public ArrayList<Player> data;
    }

    public static CompletableFuture<Result<GetPlayerByResponse>> execute(String name) {
        System.out.println(jKromer.endpoint_raw + "/api/v1/wallet/by-name/" + name);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint_raw + "/api/v1/wallet/by-name/" + name))
                    .GET()
                    .build();

            return  http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            String a = response.body();
                            System.out.println(a);

                            GetPlayerByResponse json = gson.fromJson(a, GetPlayerByResponse.class);
                            if (json.error != null) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error).toResponse(json.message);
                                return new Result.Err<GetPlayerByResponse>(errorResponse);
                            }
                            return new Result.Ok<GetPlayerByResponse>(json);
                        } catch (Exception e) {
                            return (Result<GetPlayerByResponse>) new Result.Err<GetPlayerByResponse>(Errors.internal_problem.toResponse("Failed to parse JSON: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<>(Errors.internal_problem.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }
}

