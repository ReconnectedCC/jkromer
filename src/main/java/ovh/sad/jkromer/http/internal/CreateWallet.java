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

public class CreateWallet extends HttpEndpoint {
    private static class CreateWalletRequest {
        public String uuid;
        public String name;

        public CreateWalletRequest(String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
        }
    }

    public static class CreateWalletResponse {
        public String address;
        public String privatekey;
    }

    public static CompletableFuture<Result<CreateWalletResponse>> execute(String kromerKey, String name, String uuid) {
        try {
            String json = gson.toJson(new CreateWalletRequest(name, uuid));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint_raw + "/api/_internal/wallet/create"))
                    .header("Content-Type", "application/json")
                    .header("Kromer-Key", kromerKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            if (response.statusCode() != 200 ) {
                                return new Result.Err<CreateWalletResponse>(Errors.INTERNAL_PROBLEM.toResponse("Internal API request could not be sent. Status code: " + response.statusCode()));
                            }

                            CreateWalletResponse resp = gson.fromJson(response.body(), CreateWalletResponse.class);

                            return new Result.Ok<CreateWalletResponse>(resp);
                        } catch (Exception e) {
                            return (Result<CreateWalletResponse>) new Result.Err<CreateWalletResponse>(Errors.INTERNAL_PROBLEM.toResponse("JSON parse error: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<CreateWalletResponse>(Errors.INTERNAL_PROBLEM.toResponse("HTTP error: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }
}
