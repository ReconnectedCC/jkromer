package ovh.sad.jkromer.http.transactions;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;
import ovh.sad.jkromer.models.Transaction;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MakeTransaction extends HttpEndpoint {

    public static class MakeTransactionResponse extends ResponseBodyGeneric {
        public Transaction transaction;
    }

    public static CompletableFuture<Result<MakeTransactionResponse>> execute(
            String privateKey, String to, BigDecimal amount, String metadata
    ) {
        try {
            var requestJson = Map.of(
                    "privatekey", privateKey,
                    "to", to,
                    "amount", amount,
                    "metadata", metadata == null ? "" : metadata
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint + "/transactions/"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestJson)))
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            String data = response.body();
                            System.out.println(data);
                            MakeTransactionResponse json = gson.fromJson(data, MakeTransactionResponse.class);

                            if (json.ok == null || !json.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error).toResponse(json.parameter);
                                return new Result.Err<MakeTransactionResponse>(errorResponse);
                            }
                            return new Result.Ok<MakeTransactionResponse>(json);
                        } catch (Exception e) {
                            return (Result<MakeTransactionResponse>) new Result.Err<MakeTransactionResponse>(Errors.internal_problem.toResponse("Failed to parse JSON: " + e.getMessage()));
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