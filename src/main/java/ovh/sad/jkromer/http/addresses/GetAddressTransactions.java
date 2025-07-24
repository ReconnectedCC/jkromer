package ovh.sad.jkromer.http.addresses;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import ovh.sad.jkromer.models.Transaction;

public class GetAddressTransactions extends HttpEndpoint  {
    public static class GetAddressTransactionsBody extends ResponseBodyGeneric {
        public int count;
        public int total;
        public List<Transaction> transactions;
    }

    public static CompletableFuture<Result<GetAddressTransactionsBody>> execute(String address, Boolean excludeMined, Integer limit, Integer offset) {
        try {
            String encodedAddress = URLEncoder.encode(address, java.nio.charset.StandardCharsets.UTF_8);
            StringBuilder url = new StringBuilder(endpoint + "/addresses/" + encodedAddress + "/transactions?");
            if (excludeMined != null) url.append("excludeMined=").append(excludeMined).append("&");
            if (limit != null) url.append("limit=").append(Math.min(Math.max(limit,1),1000)).append("&");
            if (offset != null) url.append("offset=").append(Math.max(offset,0)).append("&");

            // Remove trailing '&' or '?' if present
            if (url.charAt(url.length() - 1) == '&' || url.charAt(url.length() - 1) == '?') {
                url.deleteCharAt(url.length() - 1);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url.toString()))
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetAddressTransactionsBody resp = gson.fromJson(response.body(), GetAddressTransactionsBody.class);
                            System.out.println(resp.ok);
                            if (resp.ok == null || !resp.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(resp.error.error).toResponse(resp.error.parameter);
                                return new Result.Err<GetAddressTransactionsBody>(errorResponse);
                            }
                            return new Result.Ok<GetAddressTransactionsBody>(resp);
                        } catch (Exception e) {
                            return (Result<GetAddressTransactionsBody>) new Result.Err<GetAddressTransactionsBody>(Errors.INTERNAL_PROBLEM.toResponse("JSON parse error: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<GetAddressTransactionsBody>(Errors.INTERNAL_PROBLEM.toResponse("HTTP error: " + e.getMessage())));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }

    public static CompletableFuture<Result<GetAddressTransactionsBody>> execute(String address) {
        return execute(address, null, null, null);
    }
}