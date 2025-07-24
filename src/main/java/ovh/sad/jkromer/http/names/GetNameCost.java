package ovh.sad.jkromer.http.names;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GetNameCost extends HttpEndpoint {
    public static class GetNameCostBody extends ResponseBodyGeneric {
        public Float name_cost;
    }

    public static CompletableFuture<Result<GetNameCostBody>> execute() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint + "/names/cost"))
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetNameCostBody json = gson.fromJson(response.body(), GetNameCostBody.class);

                            if (json.ok == null || !json.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error.error).toResponse(json.error.parameter);
                                return new Result.Err<GetNameCostBody>(errorResponse);
                            }
                            return new Result.Ok<GetNameCostBody>(json);
                        } catch (Exception e) {
                            return (Result<GetNameCostBody>) new Result.Err<GetNameCostBody>(Errors.INTERNAL_PROBLEM.toResponse("Failed to parse JSON: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed to build HTTP request: " + e.getMessage()))
            );
        }
    }
}