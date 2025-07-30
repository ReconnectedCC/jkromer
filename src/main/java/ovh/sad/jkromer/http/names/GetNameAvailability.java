package ovh.sad.jkromer.http.names;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GetNameAvailability extends HttpEndpoint {

    public static class GetNameAvailabilityBody extends ResponseBodyGeneric {
        public Boolean available;
    }

    public static CompletableFuture<Result<GetNameAvailabilityBody>> execute(String name) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint + "/names/check/" + name))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetNameAvailabilityBody resp = gson.fromJson(response.body(), GetNameAvailabilityBody.class);
                            if (resp.ok == null || !resp.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(resp.error).toResponse(resp.parameter);
                                return new Result.Err<GetNameAvailabilityBody>(errorResponse);
                            }
                            return new Result.Ok<GetNameAvailabilityBody>(resp);
                        } catch (Exception e) {
                            return (Result<GetNameAvailabilityBody>) new Result.Err<GetNameAvailabilityBody>(Errors.internal_problem.toResponse("JSON parse error: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<GetNameAvailabilityBody>(Errors.internal_problem.toResponse("HTTP error: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }
}
