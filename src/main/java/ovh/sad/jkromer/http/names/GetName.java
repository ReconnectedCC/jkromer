package ovh.sad.jkromer.http.names;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.models.Name;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GetName extends HttpEndpoint {
    public static class GetNameBody extends ResponseBodyGeneric {
        public Name name;
    }

    public static CompletableFuture<Result<GetNameBody>> execute(String name) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint + "/names/" + name))
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetNameBody json = gson.fromJson(response.body(), GetNameBody.class);

                            if (json.ok == null || !json.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error.error).toResponse(json.error.parameter);
                                return new Result.Err<GetNameBody>(errorResponse);
                            }
                            return new Result.Ok<GetNameBody>(json);
                        } catch (Exception e) {
                            return (Result<GetNameBody>) new Result.Err<GetNameBody>(Errors.INTERNAL_PROBLEM.toResponse("Failed to parse JSON: " + e.getMessage()));
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