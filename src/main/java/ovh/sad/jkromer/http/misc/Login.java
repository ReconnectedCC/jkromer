package ovh.sad.jkromer.http.misc;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Login extends HttpEndpoint {

    private static class LoginRequest {
        public String privatekey;

        public LoginRequest(String privatekey) {
            this.privatekey = privatekey;
        }
    }

    public static class LoginResponse extends ResponseBodyGeneric {
        public Boolean authed;
        public String address;
    }

    public static CompletableFuture<Result<LoginResponse>> execute(String privatekey) {
        try {
            String json = gson.toJson(new LoginRequest(privatekey));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jKromer.endpoint + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            LoginResponse resp = gson.fromJson(response.body(), LoginResponse.class);
                            if (resp.ok == null || !resp.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(resp.error).toResponse(resp.parameter);
                                return new Result.Err<LoginResponse>(errorResponse);
                            }
                            return new Result.Ok<LoginResponse>(resp);
                        } catch (Exception e) {
                            return (Result<LoginResponse>) new Result.Err<LoginResponse>(Errors.internal_problem.toResponse("JSON parse error: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<LoginResponse>(Errors.internal_problem.toResponse("HTTP error: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed to build request: " + e.getMessage()))
            );
        }
    }
}
