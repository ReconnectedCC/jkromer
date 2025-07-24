package ovh.sad.jkromer.http.names;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;
import ovh.sad.jkromer.models.Name;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class UpdateName extends HttpEndpoint {

    private static class UpdateNameRequest {
        String a;
        String privatekey;

        UpdateNameRequest(String a, String privatekey) {
            this.a = a;
            this.privatekey = privatekey;
        }
    }

    public static class UpdateNameResponse extends ResponseBodyGeneric {
        public Name name;
    }

    public static CompletableFuture<Result<UpdateNameResponse>> execute(String name, String data, String privatekey) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            String url = jKromer.endpoint + "/names/" + encodedName + "/update";

            String json = gson.toJson(new UpdateNameRequest(data, privatekey));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            UpdateNameResponse body = gson.fromJson(response.body(), UpdateNameResponse.class);
                            if (body.ok == null || !body.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(body.error.error).toResponse(body.error.parameter);
                                return new Result.Err<UpdateNameResponse>(errorResponse);
                            }
                            return new Result.Ok<UpdateNameResponse>(body);
                        } catch (Exception e) {
                            return (Result<UpdateNameResponse>) new Result.Err<UpdateNameResponse>(Errors.INTERNAL_PROBLEM.toResponse("JSON parse failed: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<UpdateNameResponse>(Errors.INTERNAL_PROBLEM.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed building request: " + e.getMessage()))
            );
        }
    }
}
