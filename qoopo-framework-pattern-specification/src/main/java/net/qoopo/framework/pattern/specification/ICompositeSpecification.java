package net.qoopo.framework.pattern.specification;

public interface ICompositeSpecification<T> extends Specification<T> {

    public ICompositeSpecification<T> and(Specification<T> other);

    public ICompositeSpecification<T> andNot(Specification<T> other);

    public ICompositeSpecification<T> or(Specification<T> other);

    public ICompositeSpecification<T> orNot(Specification<T> other);

    public ICompositeSpecification<T> not();
}
