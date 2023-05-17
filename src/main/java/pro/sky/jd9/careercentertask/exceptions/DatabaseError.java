package pro.sky.jd9.careercentertask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseError extends StockException {
    public DatabaseError() {
        super("Database error");
    }
}
