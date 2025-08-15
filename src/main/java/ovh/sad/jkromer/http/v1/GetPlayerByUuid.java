package ovh.sad.jkromer.http.v1;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.http.transactions.GetTransaction;
import ovh.sad.jkromer.jKromer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class GetPlayerByUuid extends HttpEndpoint {

    public static CompletableFuture<Result<GetPlayerByName.GetPlayerByResponse>> execute(String uuid) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint_raw + "/api/v1/wallet/by-player/" + uuid))
                    .GET()
                    .build();

            return  http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetPlayerByName.GetPlayerByResponse json = gson.fromJson(response.body(), GetPlayerByName.GetPlayerByResponse.class);

                            if (json.error != null) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error).toResponse(json.message);
                                return new Result.Err<GetPlayerByName.GetPlayerByResponse>(errorResponse);
                            }
                            return new Result.Ok<GetPlayerByName.GetPlayerByResponse>(json);
                        } catch (Exception e) {
                            return (Result<GetPlayerByName.GetPlayerByResponse>) new Result.Err<GetPlayerByName.GetPlayerByResponse>(Errors.internal_problem.toResponse("Failed to parse JSON: " + e.getMessage()));
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

