package ovh.sad.jkromer.http.misc;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GetSupply extends HttpEndpoint {
    public static class GetSupplyBody extends ResponseBodyGeneric {
        public BigDecimal money_supply;
    }

    public static CompletableFuture<Result<GetSupplyBody>> execute() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint + "/supply"))
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetSupplyBody json = gson.fromJson(response.body(), GetSupplyBody.class);

                            if (json.ok == null || !json.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error).toResponse(json.parameter);
                                return new Result.Err<GetSupplyBody>(errorResponse);
                            }
                            return new Result.Ok<GetSupplyBody>(json);
                        } catch (Exception e) {
                            return (Result<GetSupplyBody>) new Result.Err<GetSupplyBody>(Errors.internal_problem.toResponse("Failed to parse JSON: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<>(Errors.internal_problem.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed to build HTTP request: " + e.getMessage()))
            );
        }
    }
}