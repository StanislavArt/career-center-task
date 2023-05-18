package pro.sky.jd9.careercentertask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.jd9.careercentertask.dto.WarehouseOperation;
import pro.sky.jd9.careercentertask.exceptions.InvalidParameter;
import pro.sky.jd9.careercentertask.exceptions.ParameterMissing;
import pro.sky.jd9.careercentertask.model.Socks;
import pro.sky.jd9.careercentertask.repository.SocksRepository;
import pro.sky.jd9.careercentertask.repository.StockRepository;
import pro.sky.jd9.careercentertask.service.StockService;
import pro.sky.jd9.careercentertask.service.StockServicePostgres;

@SpringBootTest
public class StockServiceTest {
    private final StockService stockService;
    private final SocksRepository socksRepository;
    private final StockRepository stockRepository;

    public StockServiceTest(SocksRepository socksRepository, StockRepository stockRepository) {
        this.socksRepository = socksRepository;
        this.stockRepository = stockRepository;
        stockService = new StockServicePostgres(socksRepository, stockRepository);
    }

    @Test
    public void socksIncomeTest() {
        String color = "red";
        int cottonPart = 60;
        int quantity = 10;

        Socks socks = socksRepository.findSocksByColorAndCottonPart(color, cottonPart).orElse(null);
        Assertions.assertNull(socks);

        WarehouseOperation warehouseOperation = new WarehouseOperation();
        warehouseOperation.setColor(color);
        warehouseOperation.setCottonPart(cottonPart);
        warehouseOperation.setQuantity(quantity);

        int quantityActual = stockService.socksIncome(warehouseOperation);
        Assertions.assertEquals(quantity, quantityActual);

        socks = socksRepository.findSocksByColorAndCottonPart(color, cottonPart).orElse(null);
        Assertions.assertNotNull(socks);

        quantityActual = socksRepository.getRemainderByFilterIsEqual(color, cottonPart);
        Assertions.assertEquals(quantity, quantityActual);
    }

    @Test
    public void socksOutcomeTest() {
        String color = "blue";
        int cottonPart = 30;
        int quantity = 50;
        int sold = 30;

        WarehouseOperation warehouseOperation = new WarehouseOperation();
        warehouseOperation.setColor(color);
        warehouseOperation.setCottonPart(cottonPart);
        warehouseOperation.setQuantity(quantity);

        int quantityActual = stockService.socksIncome(warehouseOperation);
        Assertions.assertEquals(quantity, quantityActual);

        int quantityExpected = quantity - sold;
        warehouseOperation.setQuantity(sold);
        quantityActual = stockService.socksOutcome(warehouseOperation);
        Assertions.assertEquals(sold, quantityActual);

        quantityActual = socksRepository.getRemainderByFilterIsEqual(color, cottonPart);
        Assertions.assertEquals(quantityExpected, quantityActual);
    }

    @Test
    public void getQuantityOfSocksByFilter() {
        String color = "blue";
        int cottonPart1 = 20;
        int quantity1 = 30;
        int cottonPart2 = 70;
        int quantity2 = 20;
        int cottonPart3 = 100;
        int quantity3 = 60;

        WarehouseOperation warehouseOperation = new WarehouseOperation();
        warehouseOperation.setColor(color);
        warehouseOperation.setCottonPart(cottonPart1);
        warehouseOperation.setQuantity(quantity1);
        stockService.socksIncome(warehouseOperation);

        warehouseOperation.setCottonPart(cottonPart2);
        warehouseOperation.setQuantity(quantity2);
        stockService.socksIncome(warehouseOperation);

        warehouseOperation.setCottonPart(cottonPart3);
        warehouseOperation.setQuantity(quantity3);
        stockService.socksIncome(warehouseOperation);

        int quantityActual = stockService.getQuantityOfSocksByFilter(color, "equal", cottonPart3);
        Assertions.assertEquals(quantity3, quantityActual);

        quantityActual = stockService.getQuantityOfSocksByFilter(color, "lessThan", cottonPart2);
        Assertions.assertEquals(quantity1, quantityActual);

        quantityActual = stockService.getQuantityOfSocksByFilter(color, "moreThan", cottonPart1);
        Assertions.assertEquals(quantity2 + quantity3, quantityActual);
    }

    @Test
    public void socksIncomeOutcomeException() {
        String color = "red";
        int cottonPart = 0;
        int quantity = 10;

        WarehouseOperation warehouseOperation = new WarehouseOperation();
        warehouseOperation.setColor(color);
        warehouseOperation.setCottonPart(cottonPart);
        warehouseOperation.setQuantity(quantity);
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.socksIncome(warehouseOperation));
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.socksOutcome(warehouseOperation));

        warehouseOperation.setCottonPart(50);
        warehouseOperation.setQuantity(0);
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.socksIncome(warehouseOperation));
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.socksOutcome(warehouseOperation));

        warehouseOperation.setQuantity(10);
        warehouseOperation.setColor("");
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.socksIncome(warehouseOperation));
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.socksOutcome(warehouseOperation));

        warehouseOperation.setColor(color);
        warehouseOperation.setCottonPart(120);
        Assertions.assertThrows(InvalidParameter.class, () -> stockService.socksIncome(warehouseOperation));
        Assertions.assertThrows(InvalidParameter.class, () -> stockService.socksOutcome(warehouseOperation));

        warehouseOperation.setCottonPart(70);
        warehouseOperation.setQuantity(-5);
        Assertions.assertThrows(InvalidParameter.class, () -> stockService.socksIncome(warehouseOperation));
        Assertions.assertThrows(InvalidParameter.class, () -> stockService.socksOutcome(warehouseOperation));
    }

    @Test
    public void getQuantityOfSocksByFilterException() {
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.getQuantityOfSocksByFilter("", "equal", 0));
        Assertions.assertThrows(ParameterMissing.class, () -> stockService.getQuantityOfSocksByFilter("red", "", 0));
        Assertions.assertThrows(InvalidParameter.class, () -> stockService.getQuantityOfSocksByFilter("red", "reterterhyettrg", 0));
        Assertions.assertThrows(InvalidParameter.class, () -> stockService.getQuantityOfSocksByFilter("red", "equal", 110));
    }

}
