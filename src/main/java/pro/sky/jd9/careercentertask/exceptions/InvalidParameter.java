package pro.sky.jd9.careercentertask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidParameter extends StockException {
    public InvalidParameter(String parameterName) {
        super(String.format("Invalid parameter: '%s'", parameterName));
    }
}
