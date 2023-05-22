package pro.sky.jd9.careercentertask.enums;

public enum Operation {
    MORE_THAN("moreThan"),
    LESS_THAN("lessThan"),
    EQUAL("equal");

    private String condition;

    Operation(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public static Operation getOperationByCondition(String condition) {
        Operation[] operations = Operation.values();
        for (Operation operation : operations) {
            if (operation.getCondition().equals(condition)) { return operation; }
        }
        return null;
    }
}
