package ovh.sad.jkromer.http.misc;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.http.internal.GiveMoney;
import ovh.sad.jkromer.jKromer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class StartWs extends HttpEndpoint {

    private static class StartWsRequest {
        public String privatekey;

        public StartWsRequest(String privatekey) {
            this.privatekey = privatekey;
        }
    }

    public static class StartWsResponse  {
        public String url;
    }

    public static CompletableFuture<Result<StartWsResponse>> execute() {
        return execute(null);
    }

    public static CompletableFuture<Result<StartWsResponse>> execute(String privatekey) {
        try {
            String json = gson.toJson(new StartWsRequest(privatekey));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint + "/ws/start"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            if (response.statusCode() != 200) {
                                return new Result.Err<StartWsResponse>(Errors.INTERNAL_PROBLEM.toResponse("Internal API request could not be sent. Status code: " + response.statusCode()));
                            }

                            StartWsResponse resp = gson.fromJson(response.body(), StartWsResponse.class);

                            return new Result.Ok<StartWsResponse>(resp);
                        } catch (Exception e) {
                            return (Result<StartWsResponse>) new Result.Err<StartWsResponse>(Errors.INTERNAL_PROBLEM.toResponse("JSON parse error: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<StartWsResponse>(Errors.INTERNAL_PROBLEM.toResponse("HTTP error: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }
}
