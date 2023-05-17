package pro.sky.jd9.careercentertask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParameterMissing extends StockException {
    public ParameterMissing(String parameterName) {
        super(String.format("Parameter '%s' missing", parameterName));
    }
}
