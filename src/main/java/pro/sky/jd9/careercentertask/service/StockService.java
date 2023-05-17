package pro.sky.jd9.careercentertask.service;

import pro.sky.jd9.careercentertask.dto.WarehouseOperation;

public interface StockService {
    /**
     * Операция пополнения склада.
     *
     * @param warehouseOperation DTO (class {@link WarehouseOperation})
     * @return количество оприходованного товара; либо нуль, если произошла ошибка в процессе оприходования
     */
    int socksIncome(WarehouseOperation warehouseOperation);

    /**
     * Операция списания со склада. Если при списании остаток делается равным нулю,
     * то удаляется запись о количестве данных носков со склада (таблица "stock"),
     * а также удаляется запись об этих носках (таблица "socks").
     *
     * @param warehouseOperation DTO (class {@link WarehouseOperation})
     * @return количество списанного товара; либо нуль, если произошла ошибка в процессе списания
     */
    int socksOutcome(WarehouseOperation warehouseOperation);

    /**
     * Операция получения остатка товара по заданному фильтру.
     *
     * @param color цвет
     * @param condition тип операции (см. {@link pro.sky.jd9.careercentertask.enums.Operation})
     * @param conditionValue значение для отбора по {@code cottonPart}. Возможные значения находятся в диапазоне [0,100]
     * @return количество оставшегося товара по заданным условиям
     * @see pro.sky.jd9.careercentertask.enums.Operation
     */
    int getQuantityOfSocksByFilter(String color, String condition, int conditionValue);
}
