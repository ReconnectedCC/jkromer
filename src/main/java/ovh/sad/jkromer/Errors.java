package ovh.sad.jkromer;


public enum Errors {
    invalid_parameter("invalid_parameter", true),
    address_not_found("address_not_found"),
    name_taken("name_taken"),
    insufficient_funds("insufficient_funds"),
    not_name_owner("not_name_owner"),
    name_not_found("name_not_found"),
    internal_problem("internal_error", true);

    private final String errorCode;
    private final boolean acceptsParameter;

    Errors(String errorCode) {
        this(errorCode, false);
    }

    Errors(String errorCode, boolean acceptsParameter) {
        this.errorCode = errorCode;
        this.acceptsParameter = acceptsParameter;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean acceptsParameter() {
        return acceptsParameter;
    }

    public ErrorResponse toResponse(String parameter) {
        return new ErrorResponse(false, errorCode, parameter);
    }

    public record ErrorResponse(boolean ok, String error, String parameter) {}
}