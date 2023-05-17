package pro.sky.jd9.careercentertask.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.jd9.careercentertask.dto.WarehouseOperation;
import pro.sky.jd9.careercentertask.enums.Operation;
import pro.sky.jd9.careercentertask.exceptions.DatabaseError;
import pro.sky.jd9.careercentertask.exceptions.InvalidParameter;
import pro.sky.jd9.careercentertask.exceptions.ParameterMissing;
import pro.sky.jd9.careercentertask.model.Socks;
import pro.sky.jd9.careercentertask.model.StockRecord;
import pro.sky.jd9.careercentertask.repository.SocksRepository;
import pro.sky.jd9.careercentertask.repository.StockRepository;

@Service
public class StockServicePostgres implements StockService {
    private final SocksRepository socksRepository;
    private final StockRepository stockRepository;
    private final Logger logger = LoggerFactory.getLogger(StockServicePostgres.class);

    public StockServicePostgres(SocksRepository socksRepository, StockRepository stockRepository) {
        this.socksRepository = socksRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public int socksIncome(WarehouseOperation warehouseOperation) {
        verifyParameters(warehouseOperation);

        String color = warehouseOperation.getColor();
        int cottonPart = warehouseOperation.getCottonPart();
        int quantity = warehouseOperation.getQuantity();

        try {
            Socks socks = socksRepository.findSocksByColorAndCottonPart(color, cottonPart).orElse(null);
            if (socks == null) {
                socks = socksRepository.save(new Socks(color, cottonPart));
            }

            StockRecord stockRecord = stockRepository.findStockRecordBySocks(socks).orElse(null);
            if (stockRecord == null) {
                stockRepository.save(new StockRecord(quantity, socks));
            } else {
                stockRecord.setQuantity(stockRecord.getQuantity() + quantity);
                stockRepository.save(stockRecord);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DatabaseError();
        }
        return quantity;
    }

    @Override
    public int socksOutcome(WarehouseOperation warehouseOperation) {
        verifyParameters(warehouseOperation);

        String color = warehouseOperation.getColor();
        int cottonPart = warehouseOperation.getCottonPart();
        int quantity = warehouseOperation.getQuantity();

        try {
            Socks socks = socksRepository.findSocksByColorAndCottonPart(color, cottonPart).orElse(null);
            if (socks == null) {
                logger.error("Object 'Socks' having color = '%s' and cottonPart = '%d' not exist", color, cottonPart);
                throw new InvalidParameter("something from this: 'color' or 'cottonPart'");
            }
            StockRecord stockRecord = stockRepository.findStockRecordBySocks(socks).orElse(null);
            if (stockRecord == null) {
                logger.error("Socks is missing on stock (%s)", socks);
                throw new InvalidParameter("something from this: 'color' or 'cottonPart'");
            } else {
                if (stockRecord.getQuantity() < quantity) {
                    logger.error("Quantity of choosing socks is not enough on stock (%s)", socks);
                    throw new InvalidParameter("quantity");
                }
                int remainder = stockRecord.getQuantity() - quantity;

                if (remainder == 0) {
                    stockRepository.deleteStockRecord(stockRecord.getId());
                    socksRepository.deleteSocks(socks.getId());
                } else {
                    stockRecord.setQuantity(remainder);
                    stockRepository.save(stockRecord);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DatabaseError();
        }
        return quantity;
    }

    @Override
    public int getQuantityOfSocksByFilter(String color, String condition, int conditionValue) {
        if (color.isBlank()) { throw new ParameterMissing("color"); }
        if (condition.isBlank()) { throw new ParameterMissing("operation"); }
        if (conditionValue < 0 || conditionValue > 100) { throw new InvalidParameter("cottonPart"); }

        Operation operation = Operation.getOperationByCondition(condition);
        if (operation == null) {
            throw new InvalidParameter("operation");
        }
        int result = 0;

        try {
            switch (operation) {
                case EQUAL:
                    result = socksRepository.getRemainderByFilterIsEqual(color, conditionValue);
                    break;
                case MORE_THAN:
                    result = socksRepository.getRemainderByFilterIsMoreThan(color, conditionValue);
                    break;
                case LESS_THAN:
                    result = socksRepository.getRemainderByFilterIsLessThan(color, conditionValue);
                    break;
                default:
                    throw new InvalidParameter("operation");
            }

        } catch (InvalidParameter e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DatabaseError();
        }
        return result;
    }

    private void verifyParameters(WarehouseOperation warehouseOperation) {
        String color = warehouseOperation.getColor();
        int cottonPart = warehouseOperation.getCottonPart();
        int quantity = warehouseOperation.getQuantity();

        if (color.isBlank()) { throw new ParameterMissing("color"); }
        if (cottonPart == 0) { throw new ParameterMissing("cottonPart"); }
        if (quantity == 0) { throw new ParameterMissing("quantity"); }
        if (cottonPart < 0 || cottonPart > 100) { throw new InvalidParameter("cottonPart"); }
        if (quantity < 0) { throw new InvalidParameter("quantity"); }
    }
}
