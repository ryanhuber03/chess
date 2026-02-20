package errorException;

import javax.management.remote.JMXServerErrorException;

public class ErrorException {
    private String message;
    private int errorNum;

    public ErrorResponse error403 () {
        errorNum = 403;
        message = "Error: username already taken";
        return new ErrorResponse(this.message);
    }

    public ErrorResponse error400 () {
        errorNum = 400;
        message = "Error: bad request";
        return new ErrorResponse(this.message);
    }

    public ErrorResponse error401 () {
        errorNum = 401;
        message = "Error: unauthorized";
        return new ErrorResponse(this.message);
    }

    public record ErrorResponse (String message) {}
}
