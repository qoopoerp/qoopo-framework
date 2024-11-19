package net.qoopo.framework.pattern.specification.testcase;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.specification.Specification;
import net.qoopo.framework.pattern.specification.testcase.model.Product;

@AllArgsConstructor
public class PriceSpecification implements Specification<Product> {

    private Double minValue = 0.0d;

    @Override
    public boolean isSatisfiedBy(Product candidate) {
        return candidate.getPrice() >= minValue;
    }

}
