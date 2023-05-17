package pro.sky.jd9.careercentertask.service;

public interface StockService {
    /**
     * Операция пополнения склада.
     *
     * @param color цвет
     * @param cottonPart процент хлопка. Возможные значения находятся в диапазоне [0,100]
     * @param quantity количество товара
     * @return количество оприходованного товара; либо нуль, если произошла ошибка в процессе оприходования
     */
    int socksIncome(String color, int cottonPart, int quantity);

    /**
     * Операция списания со склада.
     *
     * @param color цвет
     * @param cottonPart процент хлопка. Возможные значения находятся в диапазоне [0,100]
     * @param quantity количество товара
     * @return количество списанного товара; либо нуль, если произошла ошибка в процессе списания
     */
    int socksOutcome(String color, int cottonPart, int quantity);

    /**
     * Операция получения остатка товара по заданному фильтру.
     *
     * @param color цвет
     * @param condition тип операции (см. {@link pro.sky.jd9.careercentertask.enums.Operation})
     * @param conditionValue значение для отбора по cottonPart. Возможные значения находятся в диапазоне [0,100]
     * @return количество оставшегося товара по заданным условиям
     * @see pro.sky.jd9.careercentertask.enums.Operation
     */
    int getQuantityOfSocksByFilter(String color, String condition, int conditionValue);
}
