package ovh.sad.jkromer.http.transactions;


import ovh.sad.jkromer.Errors;
import ovh.sad.jkromer.http.HttpEndpoint;
import ovh.sad.jkromer.http.ResponseBodyGeneric;
import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.jKromer;
import ovh.sad.jkromer.models.Name;
import ovh.sad.jkromer.models.Transaction;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ListTransactions extends HttpEndpoint  {
    public static class ListTransactionsBody extends ResponseBodyGeneric  {
        public Integer count;
        public Integer total;
        public List<Transaction> transactions;
    }

    public static CompletableFuture<Result<ListTransactionsBody>> execute(Integer limit, Integer offset) {
        try {
            int useLimit = (limit == null) ? 50 : Math.min(Math.max(limit, 1), 1000);
            int useOffset = (offset == null) ? 0 : Math.max(offset, 0);

            String url = String.format("%s/transactions?limit=%d&offset=%d",
                    jKromer.endpoint,
                    useLimit,
                    useOffset
            );

            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();

            return http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            ListTransactionsBody body = gson.fromJson(response.body(), ListTransactionsBody.class);
                            if (body.ok == null || !body.ok) {
                                Errors.ErrorResponse errorResponse = Errors.valueOf(body.error.error).toResponse(body.error.parameter);
                                return new Result.Err<ListTransactionsBody>(errorResponse);
                            }
                            return new Result.Ok<ListTransactionsBody>(body);
                        } catch (Exception e) {
                            return (Result<ListTransactionsBody>) new Result.Err<ListTransactionsBody>(Errors.INTERNAL_PROBLEM.toResponse("JSON parse failed: " + e.getMessage()));
                        }
                    })
                    .exceptionally(e -> new Result.Err<ListTransactionsBody>(Errors.INTERNAL_PROBLEM.toResponse("HTTP request failed: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new Result.Err<>(Errors.INTERNAL_PROBLEM.toResponse("Failed building request: " + e.getMessage()))
            );
        }
    }

    public static CompletableFuture<Result<ListTransactionsBody>> execute() {
        return execute(null, null);
    }
}