package ovh.sad.jkromer.http;

import ovh.sad.jkromer.Errors;

public sealed interface Result<T> {
    record Ok<T>(T value) implements Result<T> {}
    record Err<T>(Errors.ErrorResponse error) implements Result<T> {}
}