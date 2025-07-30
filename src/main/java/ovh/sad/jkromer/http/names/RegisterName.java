package ovh.sad.jkromer.http.names;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class RegisterName extends HttpEndpoint {

    private static class RegisterNameRequest {
        public String privatekey;

        public RegisterNameRequest(String privatekey) {
            this.privatekey = privatekey;
        }
    }

    public static class RegisterNameResponse extends ResponseBodyGeneric {}

    public static CompletableFuture<Result<RegisterNameResponse>> execute(String name, String privatekey) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            String url = jKromer.endpoint + "/names/" + encodedName;

            String json = gson.toJson(new RegisterNameRequest(privatekey));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            RegisterNameResponse body = gson.fromJson(response.body(), RegisterNameResponse.class);
                            if (body.ok == null || !body.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(body.error).toResponse(body.parameter);
                                return new Result.Err<RegisterNameResponse>(errorResponse);
                            }
                            return new Result.Ok<RegisterNameResponse>(body);
                        } catch (Exception e) {
                            return (Result<RegisterNameResponse>) new Result.Err<RegisterNameResponse>(Errors.internal_problem.toResponse("JSON parse failed: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<RegisterNameResponse>(Errors.internal_problem.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed building request: " + e.getMessage()))
            );
        }
    }
}
