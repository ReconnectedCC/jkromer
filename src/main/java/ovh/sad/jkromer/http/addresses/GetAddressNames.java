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

import ovh.sad.jkromer.jKromer;
import ovh.sad.jkromer.models.Name;

public class GetAddressNames extends HttpEndpoint {
    public static class GetAddressNamesBody extends ResponseBodyGeneric {
        public int count;
        public int total;
        public List<Name> names;
    }

    public static CompletableFuture<Result<GetAddressNamesBody>> execute(String address, Integer limit, Integer offset) {
        try {
            String encodedAddress = URLEncoder.encode(address, java.nio.charset.StandardCharsets.UTF_8);
            StringBuilder url = new StringBuilder(jKromer.endpoint + "/addresses/" + encodedAddress + "/names?");
            if (limit != null) url.append("limit=").append(Math.min(Math.max(limit, 1), 1000)).append("&");
            if (offset != null) url.append("offset=").append(Math.max(offset, 0)).append("&");

            // Trim trailing ampersand or question mark
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
                            GetAddressNamesBody resp = gson.fromJson(response.body(), GetAddressNamesBody.class);
                            if (resp.ok == null || !resp.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(resp.error).toResponse(resp.parameter);
                                return new Result.Err<GetAddressNamesBody>(errorResponse);
                            }
                            return new Result.Ok<GetAddressNamesBody>(resp);
                        } catch (Exception e) {
                            return (Result<GetAddressNamesBody>) new Result.Err<GetAddressNamesBody>(
                                    Errors.internal_problem.toResponse("JSON parse error: " + e.getMessage())
                            );
                        }
                    })
                    .exceptionally(e -> new Result.Err<GetAddressNamesBody>(
                            Errors.internal_problem.toResponse("HTTP error: " + e.getMessage()))
                    );

        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }

    public static CompletableFuture<Result<GetAddressNamesBody>> execute(String address) {
        return execute(address, null, null);
    }
}