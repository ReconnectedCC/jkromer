package ovh.sad.jkromer.http.misc;

import com.google.gson.annotations.SerializedName;
import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GetMotd extends HttpEndpoint {
    public class GetMotdBody extends ResponseBodyGeneric {

        public static class Constants {

            public int wallet_version;
            public int nonce_max_size;
            public int name_cost;
            public int min_work;
            public int max_work;
            public double work_factor;
            public int seconds_per_block;
        }

        public static class Currency {

            public String address_prefix;
            public String name_suffix;
            public String currency_name;
            public String currency_symbol;
        }

        public static class Package {

            public String name;
            public String version;
            public String author;
            public String licence;
            public String repository;
        }

        public String server_time;
        public String motd;
        public Object set;
        public Object motd_set;
        public String public_url;
        public String public_ws_url;
        public boolean mining_enabled;
        public boolean transactions_enabled;
        public boolean debug_mode;
        public int work;
        public Object last_block;

        @SerializedName("package")
        public Package motdPackage;

        public Constants constants;
        public Currency currency;
        public String notice;
    }

    public static CompletableFuture<Result<GetMotdBody>> execute() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint + "/motd"))
                    .GET()
                    .build();

            return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            GetMotdBody json = gson.fromJson(response.body(), GetMotdBody.class);

                            if (json.ok == null || !json.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(json.error.error).toResponse(json.error.parameter);
                                return new Result.Err<GetMotdBody>(errorResponse);
                            }
                            return new Result.Ok<GetMotdBody>(json);
                        } catch (Exception e) {
                            return (Result<GetMotdBody>) new Result.Err<GetMotdBody>(Errors.INTERNAL_PROBLEM.toResponse("Failed to parse JSON: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed to build HTTP request: " + e.getMessage()))
            );
        }
    }
}