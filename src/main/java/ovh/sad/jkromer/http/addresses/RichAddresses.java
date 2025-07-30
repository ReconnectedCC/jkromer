package ovh.sad.jkromer.http.addresses;

import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;
import ovh.sad.jkromer.models.Address;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RichAddresses extends HttpEndpoint  {
    public static class RichAddressesBody extends ResponseBodyGeneric  {
        public Integer count;
        public Integer total;
        public List<Address> addresses;
    }

    public static CompletableFuture<Result<RichAddressesBody>> execute(Integer limit, Integer offset) {
        try {
            int useLimit = (limit == null) ? 50 : Math.min(Math.max(limit, 1), 1000);
            int useOffset = (offset == null) ? 0 : Math.max(offset, 0);

            String url = String.format("%s/addresses/rich?limit=%d&offset=%d",
                    jKromer.endpoint,
                    useLimit,
                    useOffset
            );

            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();

            return http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            RichAddressesBody body = gson.fromJson(response.body(), RichAddressesBody.class);
                            if (body.ok == null || !body.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(body.error).toResponse(body.parameter);
                                return new Result.Err<RichAddressesBody>(errorResponse);
                            }
                            return new Result.Ok<RichAddressesBody>(body);
                        } catch (Exception e) {
                            return (Result<RichAddressesBody>) new Result.Err<RichAddressesBody>(Errors.internal_problem.toResponse("JSON parse failed: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<RichAddressesBody>(Errors.internal_problem.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.internal_problem.toResponse("Failed building request: " + e.getMessage()))
            );
        }
    }

    public static CompletableFuture<Result<RichAddressesBody>> execute() {
        return execute(null, null);
    }
}