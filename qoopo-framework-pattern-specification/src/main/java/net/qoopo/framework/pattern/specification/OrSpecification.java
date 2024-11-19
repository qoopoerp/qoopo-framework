package net.qoopo.framework.pattern.specification;

public class OrSpecification<T> extends CompositeSpecificacion<T> {

    private Specification<T> leftCondition;
    private Specification<T> rightCondition;

    public OrSpecification(Specification<T> leftCondition, Specification<T> rightCondition) {
        this.leftCondition = leftCondition;
        this.rightCondition = rightCondition;
    }

    public boolean isSatisfiedBy(T candidate) {
        return leftCondition.isSatisfiedBy(candidate) || rightCondition.isSatisfiedBy(candidate);
    }
}
