package net.qoopo.framework.pattern.specification;

public class NotSpecification<T> extends CompositeSpecificacion<T> {

    private Specification<T> specification;

    public NotSpecification(Specification<T> specification) {
        this.specification = specification;

    }

    public boolean isSatisfiedBy(T candidate) {
        return !specification.isSatisfiedBy(candidate);
    }
}
