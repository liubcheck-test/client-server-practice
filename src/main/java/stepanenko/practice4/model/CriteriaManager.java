package stepanenko.practice4.model;

public class CriteriaManager {
    private Criteria criteria;
    private Integer value1;
    private Integer value2;

    public CriteriaManager(Criteria criteria, Integer value1, Integer value2) {
        this.criteria = criteria;
        this.value1 = value1;
        this.value2 = value2;
    }

    public CriteriaManager(Criteria criteria, Integer value1) {
        this.criteria = criteria;
        this.value1 = value1;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Integer getValue1() {
        return value1;
    }

    public Integer getValue2() {
        return value2;
    }
}
