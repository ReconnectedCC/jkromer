package ovh.sad.jkromer;


public enum Errors {
    INVALID_PARAMETER("invalid_parameter", true),
    ADDRESS_NOT_FOUND("address_not_found"),
    NAME_TAKEN("name_taken"),
    INSUFFICIENT_FUNDS("insufficient_funds"),
    NOT_NAME_OWNER("not_name_owner"),
    NAME_NOT_FOUND("name_not_found"),
    INTERNAL_PROBLEM("internal_error", true);

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