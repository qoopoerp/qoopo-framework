package net.qoopo.framework.pattern.specification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WrapperSpecification<T> extends CompositeSpecificacion<T> {
    private Specification<T> specification;

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return specification.isSatisfiedBy(candidate);
    }
}
