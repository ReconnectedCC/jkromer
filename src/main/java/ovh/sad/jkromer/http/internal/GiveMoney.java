package ovh.sad.jkromer.http.internal;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class GiveMoney extends HttpEndpoint {
    private static class GiveMoneyRequest {
        public float amount;
        public String address;

        public GiveMoneyRequest(float amount, String address) {
            this.address = address;
            this.amount = amount;
        }
    }

    public static class GiveMoneyResponse {
        public String address;
        public double balance;
        public Date created_at;
        public int id;
        public boolean locked;
        public String private_key;
        public double total_in;
        public double total_out;
    }

    public static CompletableFuture<Result<GiveMoneyResponse>> execute(String kromerKey, float amount, String address) {
        try {
            String json = gson.toJson(new GiveMoneyRequest(amount, address));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint_raw + "/api/_internal/wallet/give-money"))
                    .header("Content-Type", "application/json")
                    .header("Kromer-Key", kromerKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            if (response.statusCode() != 200 ) {
                                return new Result.Err<GiveMoneyResponse>(Errors.INTERNAL_PROBLEM.toResponse("Internal API request could not be sent. Status code: " + response.statusCode()));
                            }

                            GiveMoneyResponse resp = gson.fromJson(response.body(), GiveMoneyResponse.class);

                            return new Result.Ok<GiveMoneyResponse>(resp);
                        } catch (Exception e) {
                            return (Result<GiveMoneyResponse>) new Result.Err<GiveMoneyResponse>(Errors.INTERNAL_PROBLEM.toResponse("JSON parse error: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<GiveMoneyResponse>(Errors.INTERNAL_PROBLEM.toResponse("HTTP error: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }
}
