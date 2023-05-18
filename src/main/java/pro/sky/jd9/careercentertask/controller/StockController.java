package pro.sky.jd9.careercentertask.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.jd9.careercentertask.dto.WarehouseOperation;
import pro.sky.jd9.careercentertask.exceptions.StockException;
import pro.sky.jd9.careercentertask.service.StockService;

@RestController
@RequestMapping("api/socks")
public class StockController {
    private final StockService stockService;
    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("income")
    ResponseEntity<Void> socksIncome(@RequestBody WarehouseOperation warehouseOperation) {
        stockService.socksIncome(warehouseOperation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("outcome")
    ResponseEntity<Void> socksOutcome(@RequestBody WarehouseOperation warehouseOperation) {
        stockService.socksOutcome(warehouseOperation);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<String> getRemainder(@RequestParam("color") String color,
                                         @RequestParam("operation") String condition,
                                         @RequestParam("cottonPart") int cottonPart) {

        int remainder = stockService.getQuantityOfSocksByFilter(color, condition, cottonPart);
        return ResponseEntity.ok(String.valueOf(remainder));
    }

    @ExceptionHandler(StockException.class)
    ResponseEntity handleStockException(StockException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(exception.getClass().getAnnotation(ResponseStatus.class).value()).build();
    }
}
