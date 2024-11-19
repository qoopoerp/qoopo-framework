package net.qoopo.framework.pattern.specification;

public abstract class CompositeSpecificacion<T> implements ICompositeSpecification<T> {

    public abstract boolean isSatisfiedBy(T candidate);

    @Override
    public ICompositeSpecification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    @Override
    public ICompositeSpecification<T> andNot(Specification<T> other) {
        return new AndNotSpecification<>(this, other);
    }

    @Override
    public ICompositeSpecification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    @Override
    public ICompositeSpecification<T> orNot(Specification<T> other) {
        return new OrNotSpecification<>(this, other);
    }

    @Override
    public ICompositeSpecification<T> not() {
        return new NotSpecification<>(this);
    }

}
