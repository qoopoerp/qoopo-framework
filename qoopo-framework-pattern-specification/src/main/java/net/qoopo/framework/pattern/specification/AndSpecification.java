package net.qoopo.framework.pattern.specification;

public class AndSpecification<T> extends CompositeSpecificacion<T> {

    private Specification<T> leftCondition;
    private Specification<T> rightCondition;

    public AndSpecification(Specification<T> leftCondition, Specification<T> rightCondition) {
        this.leftCondition = leftCondition;
        this.rightCondition = rightCondition;
    }

    public boolean isSatisfiedBy(T candidate) {
        return leftCondition.isSatisfiedBy(candidate) && rightCondition.isSatisfiedBy(candidate);
    }
}
