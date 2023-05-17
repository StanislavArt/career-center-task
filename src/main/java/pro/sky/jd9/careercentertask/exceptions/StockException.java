package pro.sky.jd9.careercentertask.exceptions;

public class StockException extends RuntimeException {
    private final String message;

    public StockException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
