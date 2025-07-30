package ovh.sad.jkromer.http.addresses;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.jKromer;
import ovh.sad.jkromer.models.Address;
import ovh.sad.jkromer.http.Result;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GetAddress extends HttpEndpoint {
    public static class GetAddressBody extends ResponseBodyGeneric {
        public Address address;
    }

    public static CompletableFuture<Result<GetAddressBody>> execute(String address, boolean fetchNames) {
        try {
            String url = jKromer.endpoint + "/addresses/" + address;
            if (fetchNames) url += "?fetchNames=true";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetAddressBody json = gson.fromJson(response.body(), GetAddressBody.class);

                            if (json.ok == null || !json.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error).toResponse(json.parameter);
                                return new Result.Err<GetAddressBody>(errorResponse);
                            }
                            return new Result.Ok<GetAddressBody>(json);
                        } catch (Exception e) {
                            return (Result<GetAddressBody>) new Result.Err<GetAddressBody>(Errors.internal_problem.toResponse("Failed to parse JSON: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<>(Errors.internal_problem.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed to build HTTP request: " + e.getMessage()))
            );
        }
    }


    public static CompletableFuture<Result<GetAddressBody>> execute(String address) {
        return execute(address, false);
    }
}