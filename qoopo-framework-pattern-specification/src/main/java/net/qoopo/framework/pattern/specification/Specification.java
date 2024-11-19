package net.qoopo.framework.pattern.specification;

public interface Specification<T> {
    public boolean isSatisfiedBy(T candidate);

}
