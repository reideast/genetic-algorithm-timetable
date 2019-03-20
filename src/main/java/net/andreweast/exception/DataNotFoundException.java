package net.andreweast.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ResponseStatus means that throwing this exception will return a custom status rather than just 500 Server Error
// See: https://www.baeldung.com/exception-handling-for-rest-with-spring
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException() {
        super("Could not locate data within the database");
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
