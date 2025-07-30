package ovh.sad.jkromer.http.transactions;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;
import ovh.sad.jkromer.models.Transaction;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GetTransaction extends HttpEndpoint {
    public static class GetTransactionBody extends ResponseBodyGeneric {
        public Transaction transaction;
    }

    public static CompletableFuture<Result<GetTransactionBody>> execute(String id) {
        try {
            String url = jKromer.endpoint + "/transactions/" + id;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetTransactionBody json = gson.fromJson(response.body(), GetTransactionBody.class);

                            if (json.ok == null || !json.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error).toResponse(json.parameter);
                                return new Result.Err<GetTransactionBody>(errorResponse);
                            }
                            return new Result.Ok<GetTransactionBody>(json);
                        } catch (Exception e) {
                            return (Result<GetTransactionBody>) new Result.Err<GetTransactionBody>(Errors.internal_problem.toResponse("Failed to parse JSON: " + e.getMessage()));
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